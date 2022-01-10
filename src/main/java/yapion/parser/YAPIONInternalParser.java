/*
 * Copyright 2019,2020,2021 yoyosource
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package yapion.parser;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import yapion.exceptions.parser.YAPIONParserException;
import yapion.hierarchy.api.groups.YAPIONAnyType;
import yapion.hierarchy.api.groups.YAPIONDataType;
import yapion.hierarchy.types.*;
import yapion.hierarchy.types.value.ValueHandler;
import yapion.parser.callbacks.CallbackResult;
import yapion.parser.callbacks.CallbackType;
import yapion.parser.callbacks.ParseCallback;
import yapion.utils.ReferenceFunction;
import yapion.utils.ReferenceIDUtils;

import java.nio.charset.StandardCharsets;
import java.util.*;

@Slf4j
final class YAPIONInternalParser {

    // Parse steps done
    private long count = 0;

    // last char
    private char lastChar = '\u0000';

    // Result object and current
    YAPIONType initialType = null;
    private MightValue mightValue = MightValue.FALSE;
    private YAPIONObject result = null;
    private YAPIONAnyType currentObject = null;

    // Parsing has finished one object
    private boolean finished = false;

    // The TypeStack
    private final TypeStack typeStack = new TypeStack();

    // How the ReferenceValue should be computed
    private ReferenceFunction referenceFunction = ReferenceIDUtils.REFERENCE_FUNCTION;

    // Unicode and Escaping variables
    private boolean escaped = false;
    private boolean lastCharEscaped = false;
    private StringBuilder unicode = null;

    // Key and Value variables
    private StringBuilder current = new StringBuilder();
    private String key = "";

    // All Objects and Pointers
    private final List<YAPIONDataType<?, ?>> yapionDataTypes = new ArrayList<>();
    private final List<YAPIONPointer> yapionPointerList = new LinkedList<>();

    // YAPIONValue type specifications
    private final List<ValueHandler<?>> valueHandlerList = new LinkedList<>();

    // Comments
    CommentParsing comments = CommentParsing.IGNORE;
    private List<String> currentComments = new ArrayList<>();

    // lazy parsing
    boolean lazy = false;

    // json/yapion parsing
    boolean forceOnlyJSON = false;
    boolean forceOnlyYAPION = false;

    boolean disabledObject = false;
    boolean disabledArray = false;
    boolean disabledMap = false;
    boolean disabledValue = false;
    boolean disabledPointer = false;

    Map<CallbackType<?>, ParseCallback<?>> parseCallbackMap = new HashMap<>();

    void setReferenceFunction(ReferenceFunction referenceFunction) {
        this.referenceFunction = referenceFunction;
    }

    boolean isFinished() {
        return finished;
    }

    YAPIONObject finish() {
        parseFinish();
        return result;
    }

    long count() {
        return count;
    }

    void advance(char c) {
        log.debug("{} -> {}{}", typeStack, (int) c, (c >= ' ' && c <= '~' ? " '" + c + "'" : ""));
        if (typeStack.isEmpty()) {
            initialType(c);
            count++;
            lastChar = c;
            return;
        }

        switch (typeStack.peek()) {
            case POINTER:
                parsePointer(c);
                break;
            case ARRAY:
                parseArray(c, lastChar);
                break;
            case VALUE:
                parseValue(c, lastChar);
                break;
            case MAP:
                parseMap(c, lastChar);
                break;
            case COMMENT:
                parseComment(c, lastChar);
                break;
            default:
                parseObject(c, lastChar);
                break;
        }
        count++;
        lastChar = c;
    }

    private void parseFinish() {
        List<String> strings = new ArrayList<>();
        if (!key.isEmpty()) {
            strings.add("A key was specified without a follow up value '" + key + "'");
        }
        if (current.length() != 0) {
            strings.add("A value was specified without an end '" + current + "'");
        }
        if (mightValue == MightValue.TRUE) {
            parseValueJSONEnd('\u0000');
        }
        if (count == 0) {
            result = new YAPIONObject();
            log.debug("finish   [done]");
            return;
        }
        if (typeStack.isNotEmpty()) {
            while (typeStack.isNotEmpty()) {
                try {
                    typeStack.pop(YAPIONType.ANY);
                } catch (YAPIONParserException e) {
                    strings.add(e.getMessage());
                }
            }
            throw new YAPIONParserException("Exception while finishing the current parsed Object\n- " + String.join("\n- ", strings));
        }
        addComments(result, true);

        if (!yapionPointerList.isEmpty()) {
            log.debug("pFinish  [init]");
            Map<Long, YAPIONDataType<?, ?>> yapionDataTypeHashMap = new HashMap<>();
            for (YAPIONDataType<?, ?> yapionDataType : yapionDataTypes) {
                yapionDataTypeHashMap.put(new YAPIONPointer(yapionDataType).getPointerID(), yapionDataType);
            }
            log.debug("pFinish  [setPointer]");
            for (YAPIONPointer yapionPointer : yapionPointerList) {
                long id = yapionPointer.getPointerID();
                YAPIONDataType<?, ?> yapionDataType = yapionDataTypeHashMap.get(id);
                if (yapionDataType == null) continue;
                yapionPointer.set(yapionDataType);
            }
            log.debug("pFinish  [done]");
        }
        log.debug("finish   [done]");
    }

    private void push(YAPIONType yapionType) {
        log.debug("push     [{}]", yapionType);
        typeStack.push(yapionType, count);
        if (lazy) {
            while (current.length() > 0 && isWhiteSpace(current.charAt(current.length() - 1))) {
                current.deleteCharAt(current.length() - 1);
            }
        }
        key = stringBuilderToUTF8String(current);
        current = new StringBuilder();

        valueHandlerList.clear();
        valueHandlerList.addAll(YAPIONValue.allValueHandlers());
    }

    private void pop(YAPIONType yapionType) {
        log.debug("pop      [{}]", yapionType);
        typeStack.pop(yapionType);
        if (yapionType == YAPIONType.OBJECT || yapionType == YAPIONType.ARRAY || yapionType == YAPIONType.MAP) {
            addComments(currentObject, true);
        }
    }

    private void reset() {
        log.debug("reset    ['{}'='{}']", current.toString().replace("\r", "\\r").replace("\n", "\\n").replace("\t", "\\t"), key.replace("\r", "\\r").replace("\n", "\\n").replace("\t", "\\t"));
        current = new StringBuilder();
        key = "";
    }

    private void initialType(char c) {
        if (result != null) {
            throw new YAPIONParserException("A DataType was already parsed");
        }
        log.debug("initial  [CREATE]");
        typeStack.push(YAPIONType.OBJECT, count);
        result = new YAPIONObject();
        yapionDataTypes.add(result);
        currentObject = result;
        if (c == '{') {
            initialType = YAPIONType.OBJECT;
        } else if (c == '[') {
            initialType = YAPIONType.ARRAY;
            typeStack.push(YAPIONType.ARRAY, count);
            YAPIONArray yapionArray = new YAPIONArray();
            yapionDataTypes.add(yapionArray);
            result.add("", yapionArray);
            currentObject = yapionArray;
        } else if (c == '<') {
            initialType = YAPIONType.MAP;
            typeStack.push(YAPIONType.MAP, count);
            YAPIONMap yapionMap = new YAPIONMap();
            yapionDataTypes.add(yapionMap);
            result.add("", yapionMap);
            currentObject = yapionMap;
        } else {
            throw new YAPIONParserException("The first character needs to be of a DataType which are '{' or '[' or '<'");
        }
    }

    private void add(@NonNull String key, @NonNull YAPIONAnyType value) {
        log.debug("add      ['{}'='{}']", key.replace("\r", "\\r").replace("\n", "\\n").replace("\t", "\\t"), value);
        currentObject.getType().getAddConsumer().accept(currentObject, key, value);
        addComments(value, false);
    }

    private void addComments(YAPIONAnyType value, boolean ending) {
        log.debug("COMMENT: add {} to {}", currentComments, value);
        if (ending) {
            ((YAPIONDataType) value).getEndingComments().addAll(currentComments);
        } else {
            value.getComments().addAll(currentComments);
        }
        currentComments.clear();
    }

    private boolean everyType(char c, char lastChar) {
        if (escaped) {
            return false;
        }
        if (!disabledValue && !forceOnlyYAPION && mightValue != MightValue.FALSE) {
            if (c == ' ') {
                return true;
            }
            mightValue = MightValue.FALSE;

            if (c == '{' || c == '[' || c == '"' || (c >= '0' && c <= '9') || c == '-' || c == 't' || c == 'n' || c == 'f') {
                current.delete(current.length() - 2, current.length());
                current.deleteCharAt(0);
                if (c != '{' && c != '[') {
                    log.debug("type     [JSON]");
                    push(YAPIONType.VALUE);
                    mightValue = MightValue.TRUE;
                    parseValue(c, lastChar);
                    return true;
                }
            } else {
                throw new YAPIONParserException("Invalid JSON");
            }
        }
        if (!disabledObject && c == '{') {
            log.debug("type     [OBJECT]");
            push(YAPIONType.OBJECT);
            YAPIONObject yapionObject = new YAPIONObject();
            yapionDataTypes.add(yapionObject);
            add(key, yapionObject);
            currentObject = yapionObject;
            key = "";
            return true;
        }
        if (!disabledArray && c == '[') {
            log.debug("type     [ARRAY]");
            push(YAPIONType.ARRAY);
            YAPIONArray yapionArray = new YAPIONArray();
            yapionDataTypes.add(yapionArray);
            add(key, yapionArray);
            currentObject = yapionArray;
            key = "";
            return true;
        }
        if (!disabledValue && !forceOnlyJSON && c == '(') {
            log.debug("type     [VALUE]");
            push(YAPIONType.VALUE);
            return true;
        }
        if (!disabledPointer && !forceOnlyJSON && lastChar == '-' && c == '>') {
            log.debug("type     [POINTER]");
            if (typeStack.peek() != YAPIONType.MAP) {
                current.deleteCharAt(current.length() - 1);
            }
            push(YAPIONType.POINTER);
            return true;
        }
        if (!forceOnlyJSON && lastChar == '/' && c == '*' && current.length() <= 1) {
            if (comments.isParse()) {
                log.debug("type    [COMMENT]");
                push(YAPIONType.COMMENT);
                key = "";
                return true;
            } else {
                log.info("Enabling comment support could benefit the parsing?");
            }
        }
        if (!disabledMap && !forceOnlyJSON && c == '<') {
            log.debug("type     [MAP]");
            push(YAPIONType.MAP);
            YAPIONMap yapionMap = new YAPIONMap();
            yapionDataTypes.add(yapionMap);
            add(key, yapionMap);
            currentObject = yapionMap;
            key = "";
            return true;
        }
        if (!disabledValue && !forceOnlyYAPION && c == ':' && typeStack.peek() == YAPIONType.OBJECT && current.length() > 2 && current.charAt(0) == '"' && current.charAt(current.length() - 1) == '"') {
            log.debug("type     [JSON ?]");
            mightValue = MightValue.MIGHT;
            return false;
        }
        return false;
    }

    private void parseObject(char c, char lastChar) {
        if (everyType(c, lastChar)) {
            return;
        }
        if (!escaped) {
            if (c == '}') {
                parseEndObject();
                return;
            }
            if (c == '\\') {
                escaped = true;
                return;
            }
        }
        if (parseSpecialEscape(c)) {
            return;
        }
        if (escaped && current.length() == 0 && isWhiteSpace(c)) {
            current.append(c);
            escaped = false;
            return;
        }
        if (current.length() != 0 || !isWhiteSpace(c)) {
            current.append(c);
        }
        escaped = false;
    }

    private void parseEndObject() {
        pop(YAPIONType.OBJECT);
        ParseCallback<YAPIONObject> callback = (ParseCallback<YAPIONObject>) parseCallbackMap.get(CallbackType.OBJECT);
        if (callback != null) {
            CallbackResult callbackResult = callback.onParse(key, (YAPIONObject) currentObject);
            if (callbackResult == CallbackResult.STOP || callbackResult == CallbackResult.IGNORE_AND_STOP) {
                typeStack.clear();
                finished = true;
            }
            if (callbackResult == CallbackResult.IGNORE || callbackResult == CallbackResult.IGNORE_AND_STOP) {
                YAPIONAnyType tempParent = currentObject.getParent();
                if (tempParent != null) {
                    ((YAPIONDataType) tempParent).removeIf(yapionAnyType -> yapionAnyType == currentObject);
                }
                currentObject = tempParent;
                reset();
                return;
            }
            if (callbackResult == CallbackResult.STOP) {
                reset();
                return;
            }
        }
        reset();
        currentObject = currentObject.getParent();
        finished = typeStack.isEmpty();
    }

    private boolean parseSpecialEscape(char c) {
        if (parseUTF8Escape(c)) return true;
        if (!escaped) return false;
        switch (c) {
            case 'n':
                sortValueHandler('\n', current.length());
                current.append("\n");
                break;
            case 't':
                sortValueHandler('\t', current.length());
                current.append("\t");
                break;
            case 'r':
                sortValueHandler('\r', current.length());
                current.append("\r");
                break;
            case '\\':
                sortValueHandler('\\', current.length());
                current.append("\\");
                break;
            default:
                return false;
        }
        escaped = false;
        return true;
    }

    private boolean parseUTF8Escape(char c) {
        if (escaped && c == 'u') {
            log.debug("UTF8     [initial]");
            unicode = new StringBuilder();
            escaped = false;
            return true;
        }
        if (unicode != null && unicode.length() < 4) {
            if (!((c >= '0' && c <= '9') || (c >= 'A' && c <= 'Z'))) {
                throw new YAPIONParserException("Invalid UTF8 escape sequence");
            }
            unicode.append(c);
            log.debug("UTF8     [{}]", unicode);
            if (unicode.length() == 4) {
                char unicodeChar = (char) Integer.parseInt(unicode.toString(), 16);
                sortValueHandler(unicodeChar, current.length());
                current.append(unicodeChar);
                unicode = null;
            }
            return true;
        }
        return false;
    }

    private int valueIndex = 0;

    private void parseValue(char c, char lastChar) {
        if (parseSpecialEscape(c)) {
            return;
        }
        if (mightValue == MightValue.FALSE && !escaped && c == ')' && valueIndex == 0) {
            log.debug("ValueHandler to use -> {}", valueHandlerList);
            pop(YAPIONType.VALUE);
            YAPIONValue yapionValue = YAPIONValue.parseValue(stringBuilderToUTF8String(current), valueHandlerList);
            ParseCallback<YAPIONValue> callback = (ParseCallback<YAPIONValue>) parseCallbackMap.get(CallbackType.VALUE);
            if (callback != null) {
                CallbackResult callbackResult = callback.onParse(key, yapionValue);
                if (callbackResult == CallbackResult.STOP || callbackResult == CallbackResult.IGNORE_AND_STOP) {
                    typeStack.clear();
                    finished = true;
                }
                if (callbackResult == CallbackResult.IGNORE || callbackResult == CallbackResult.IGNORE_AND_STOP) {
                    currentObject = currentObject.getParent();
                    reset();
                    return;
                }
                if (callbackResult == CallbackResult.STOP) {
                    add(key, yapionValue);
                    reset();
                    return;
                }
            }
            add(key, yapionValue);
            reset();
            return;
        }
        if (!escaped) {
            if (c == ')') {
                valueIndex--;
            }
            if (c == '(') {
                valueIndex++;
            }
        }
        if (mightValue == MightValue.TRUE && !escaped && (c == ',' || c == '}' || c == ']' || c == '>') && !(lastCharEscaped && lastChar == '"') && tryParseValueJSONEnd(c, lastChar)) {
            return;
        }
        if (c == '\\' && !escaped) {
            escaped = true;
            return;
        }
        lastCharEscaped = escaped;
        if (escaped) {
            boolean b = mightValue == MightValue.TRUE && c == '"';
            b |= typeStack.peek() == YAPIONType.ARRAY && (c == ',' || c == '-' || c == '[' || c == ']');
            b |= typeStack.peek() == YAPIONType.ARRAY && current.length() == 0 && c == ' ';
            if (!b) {
                sortValueHandler('\\', current.length());
                current.append('\\');
            }
            escaped = false;
        }
        sortValueHandler(c, current.length());
        current.append(c);
    }

    private boolean tryParseValueJSONEnd(char c, char lastChar) {
        if (forceOnlyYAPION) {
            return false;
        }
        log.debug("TryParse '{}' '{}'", c, lastChar);
        StringBuilder now = new StringBuilder(current);
        shortenJSON(now);
        log.debug("shortened StringBuilder '{}'", now);

        if (now.length() > 0 && now.charAt(0) == '"' && now.charAt(now.length() - 1) == '"') {
            parseValueJSONEnd(c);
            return true;
        }
        String st = now.toString();
        valueHandlerList.clear();
        valueHandlerList.addAll(YAPIONValue.allValueHandlers());
        if (st.equals("false")) {
            parseValueJSONEnd(c);
            return true;
        }
        if (st.equals("true")) {
            parseValueJSONEnd(c);
            return true;
        }
        if (st.equals("null")) {
            parseValueJSONEnd(c);
            return true;
        }
        if (st.matches("-?\\d+(\\.\\d+)?([eE][+-]?\\d+)?")) {
            parseValueJSONEnd(c);
            return true;
        }
        return false;
    }

    private void shortenJSON(StringBuilder now) {
        while (true) {
            char temp = now.charAt(now.length() - 1);
            if (!(temp == ' ' || temp == '\t' || temp == '\n')) {
                break;
            }
            now.deleteCharAt(now.length() - 1);
        }
    }

    private void parseValueJSONEnd(char c) {
        log.debug("ValueHandler to use -> {}", valueHandlerList);
        log.debug("END char -> {}", c);
        pop(YAPIONType.VALUE);
        shortenJSON(current);
        add(key, YAPIONValue.parseValue(stringBuilderToUTF8String(current), valueHandlerList));
        reset();
        mightValue = MightValue.FALSE;
        switch (c) {
            case '}':
                parseEndObject();
                break;
            case ']':
                parseEndArray();
                break;
            case '>':
                parseEndMap();
                break;
            default:
                break;
        }
    }

    private void parsePointer(char c) {
        if (!((c >= '0' && c <= '9') || (c >= 'a' && c <= 'f') || (c >= 'A' && c <= 'F'))) {
            throw new YAPIONParserException("Invalid pointer: " + c);
        }
        current.append(c);
        if (current.length() == 16) {
            pop(YAPIONType.POINTER);
            YAPIONPointer yapionPointer = new YAPIONPointer(stringBuilderToUTF8String(current));
            ParseCallback<YAPIONPointer> callback = (ParseCallback<YAPIONPointer>) parseCallbackMap.get(CallbackType.POINTER);
            if (callback != null) {
                CallbackResult callbackResult = callback.onParse(key, yapionPointer);
                if (callbackResult == CallbackResult.STOP || callbackResult == CallbackResult.IGNORE_AND_STOP) {
                    typeStack.clear();
                    finished = true;
                }
                if (callbackResult == CallbackResult.IGNORE || callbackResult == CallbackResult.IGNORE_AND_STOP) {
                    YAPIONAnyType tempParent = currentObject.getParent();
                    ((YAPIONDataType) tempParent).removeIf(yapionAnyType -> yapionAnyType == currentObject);
                    currentObject = tempParent;
                    reset();
                    return;
                }
                if (callbackResult == CallbackResult.STOP) {
                    yapionPointerList.add(yapionPointer);
                    add(key, yapionPointer);
                    reset();
                    return;
                }
            }
            yapionPointerList.add(yapionPointer);
            add(key, yapionPointer);
            reset();
        }
    }

    private void parseMap(char c, char lastChar) {
        boolean allowed = c == ':' || isWhiteSpace(c);
        if (!disabledValue) allowed |= c == '(';
        if (!disabledObject) allowed |= c == '{';
        if (!disabledArray) allowed |= c == '[';
        if (!disabledMap) allowed |= c == '<' || c == '>';
        if (!disabledObject) allowed |= c == '-' || c == '>';
        if (comments.isParse()) allowed |= c == '/' || c == '*';
        if (!allowed) {
            throw new YAPIONParserException("Invalid map char used: " + c);
        }
        if (!everyType(c, lastChar) && c == '>') {
            parseEndMap();
        }
    }

    private void parseEndMap() {
        pop(YAPIONType.MAP);
        ParseCallback<YAPIONMap> callback = (ParseCallback<YAPIONMap>) parseCallbackMap.get(CallbackType.MAP);
        if (callback != null) {
            CallbackResult callbackResult = callback.onParse(key, (YAPIONMap) currentObject);
            if (callbackResult == CallbackResult.STOP || callbackResult == CallbackResult.IGNORE_AND_STOP) {
                typeStack.clear();
                finished = true;
            }
            if (callbackResult == CallbackResult.IGNORE || callbackResult == CallbackResult.IGNORE_AND_STOP) {
                YAPIONAnyType tempParent = currentObject.getParent();
                ((YAPIONDataType) tempParent).removeIf(yapionAnyType -> yapionAnyType == currentObject);
                currentObject = tempParent;
                reset();
                return;
            }
            if (callbackResult == CallbackResult.STOP) {
                ((YAPIONMap) currentObject).finishMapping();
                currentObject = currentObject.getParent();
                reset();
                return;
            }
        }
        ((YAPIONMap) currentObject).finishMapping();
        currentObject = currentObject.getParent();
        reset();
        if (initialType == YAPIONType.MAP && typeStack.getStack().size() == 1) {
            typeStack.pop(YAPIONType.OBJECT);
            finished = true;
        }
    }

    private void parseArray(char c, char lastChar) {
        // TODO: Enforce syntax (Commas between elements!!!)
        key = "";
        if (!escaped && (c == ',' || c == ']')) {
            if (current.length() != 0) {
                add("", YAPIONValue.parseValue(stringBuilderToUTF8String(current), valueHandlerList));
            }
            if (c == ',') {
                current = new StringBuilder();
                valueHandlerList.clear();
                valueHandlerList.addAll(YAPIONValue.allValueHandlers());
                return;
            }
            parseEndArray();
            return;
        }
        if (!lastCharEscaped && current.length() == 0 && everyType(c, lastChar)) {
            return;
        }
        if (!lastCharEscaped && current.length() == 1 && (lastChar == '-' || lastChar == '/') && everyType(c, lastChar)) {
            return;
        }
        if (current.length() == 0 && isWhiteSpace(c) && !escaped) {
            return;
        }
        parseValue(c, lastChar);
    }

    private void parseEndArray() {
        pop(YAPIONType.ARRAY);
        ParseCallback<YAPIONArray> callback = (ParseCallback<YAPIONArray>) parseCallbackMap.get(CallbackType.ARRAY);
        if (callback != null) {
            CallbackResult callbackResult = callback.onParse(key, (YAPIONArray) currentObject);
            if (callbackResult == CallbackResult.STOP || callbackResult == CallbackResult.IGNORE_AND_STOP) {
                typeStack.clear();
                finished = true;
            }
            if (callbackResult == CallbackResult.IGNORE || callbackResult == CallbackResult.IGNORE_AND_STOP) {
                YAPIONAnyType tempParent = currentObject.getParent();
                ((YAPIONDataType) tempParent).removeIf(yapionAnyType -> yapionAnyType == currentObject);
                currentObject = tempParent;
                reset();
                return;
            }
            if (callbackResult == CallbackResult.STOP) {
                reset();
                return;
            }
        }
        currentObject = currentObject.getParent();
        reset();
        if (initialType == YAPIONType.ARRAY && typeStack.getStack().size() == 1) {
            typeStack.pop(YAPIONType.OBJECT);
            finished = true;
        }
    }

    private void parseComment(char c, char lastChar) {
        if (lastChar == '*' && c == '/') {
            if (current.length() > 0) {
                current.deleteCharAt(current.length() - 1);
            }
            pop(YAPIONType.COMMENT);
            if (comments.isAdd()) {
                ParseCallback<String> callback = (ParseCallback<String>) parseCallbackMap.get(CallbackType.COMMENT);
                if (callback != null) {
                    CallbackResult callbackResult = callback.onParse(null, current.toString());
                    if (callbackResult == CallbackResult.STOP || callbackResult == CallbackResult.IGNORE_AND_STOP) {
                        typeStack.clear();
                        finished = true;
                    }
                    if (callbackResult == CallbackResult.IGNORE || callbackResult == CallbackResult.IGNORE_AND_STOP) {
                        reset();
                        return;
                    }
                    if (callbackResult == CallbackResult.STOP) {
                        currentComments.add(current.toString());
                        log.debug("COMMENT: {}", current);
                        reset();
                        return;
                    }
                }
                currentComments.add(current.toString());
                log.debug("COMMENT: {}", current);
            }
            reset();
            return;
        }
        current.append(c);
    }

    public void sortValueHandler(char c, int length) {
        valueHandlerList.removeIf(valueHandler -> !valueHandler.allowed(c, length));
    }

    private String stringBuilderToUTF8String(StringBuilder st) {
        return new String(st.toString().getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8);
    }

    private boolean isWhiteSpace(char c) {
        return c == ' ' || c == '\n' || c == '\r' || c == '\t' || c == ',';
    }
}
