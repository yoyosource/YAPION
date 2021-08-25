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
    private boolean hadInitial = true;
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
        if (typeStack.isEmpty() && initialType(c)) {
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
        if (mightValue == MightValue.TRUE) {
            parseValueJSONEnd('\u0000');
        }
        if (count == 0) {
            result = new YAPIONObject();
            log.debug("finish   [done]");
            return;
        }
        if (!hadInitial) {
            if (typeStack.isEmpty()) {
                throw new YAPIONParserException("Object is closed too often");
            }
            if (typeStack.pop(YAPIONType.OBJECT) != YAPIONType.OBJECT) {
                throw new YAPIONParserException("Some types are open");
            }
        } else {
            if (typeStack.isNotEmpty()) {
                throw new YAPIONParserException("Object is not closed correctly");
            }
        }
        addComments(result, true);

        if (!yapionPointerList.isEmpty()) {
            log.debug("pFinish  [init]");
            Map<Long, YAPIONDataType<?, ?>> yapionDataTypeHashMap = new HashMap<>();
            for (YAPIONDataType<?, ?> yapionDataType : yapionDataTypes) {
                yapionDataTypeHashMap.put(yapionDataType.referenceValue(referenceFunction), yapionDataType);
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
        typeStack.push(yapionType);
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

    private boolean initialType(char c) {
        if (c == '{' && typeStack.isEmpty() && result == null) {
            log.debug("initial  [CREATE]");
        } else {
            log.debug("initial  [CREATE] -> {}", (int) c);
            hadInitial = false;
        }
        typeStack.push(YAPIONType.OBJECT);
        result = new YAPIONObject();
        yapionDataTypes.add(result);
        currentObject = result;
        return hadInitial;
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
        if (mightValue != MightValue.FALSE) {
            if (c == ' ') {
                return true;
            }
            mightValue = MightValue.FALSE;

            if (c == '{' || c == '[' || c == '<' || c == '"' || (c >= '0' && c <= '9') || c == '-' || c == 't' || c == 'n' || c == 'f') {
                current.delete(current.length() - 2, current.length());
                current.deleteCharAt(0);
                if (c != '{' && c != '[' && c != '<') {
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
        if (c == '{') {
            log.debug("type     [OBJECT]");
            push(YAPIONType.OBJECT);
            YAPIONObject yapionObject = new YAPIONObject();
            yapionDataTypes.add(yapionObject);
            add(key, yapionObject);
            currentObject = yapionObject;
            key = "";
            return true;
        }
        if (c == '[') {
            log.debug("type     [ARRAY]");
            push(YAPIONType.ARRAY);
            YAPIONArray yapionArray = new YAPIONArray();
            yapionDataTypes.add(yapionArray);
            add(key, yapionArray);
            currentObject = yapionArray;
            key = "";
            return true;
        }
        if (c == '(') {
            log.debug("type     [VALUE]");
            push(YAPIONType.VALUE);
            return true;
        }
        if (lastChar == '-' && c == '>') {
            log.debug("type     [POINTER]");
            if (typeStack.peek() != YAPIONType.MAP) {
                current.deleteCharAt(current.length() - 1);
            }
            push(YAPIONType.POINTER);
            return true;
        }
        if (lastChar == '/' && c == '*' && current.length() <= 1) {
            if (comments.isParse()) {
                log.debug("type    [COMMENT]");
                push(YAPIONType.COMMENT);
                key = "";
                return true;
            } else {
                log.info("Enabling comment support could benefit the parsing?");
            }
        }
        if (c == '<') {
            log.debug("type     [MAP]");
            push(YAPIONType.MAP);
            YAPIONMap yapionMap = new YAPIONMap();
            yapionDataTypes.add(yapionMap);
            add(key, yapionMap);
            currentObject = yapionMap;
            key = "";
            return true;
        }
        if (c == ':' && typeStack.peek() == YAPIONType.OBJECT && current.length() > 2 && current.charAt(0) == '"' && current.charAt(current.length() - 1) == '"') {
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
            add(key, YAPIONValue.parseValue(stringBuilderToUTF8String(current), valueHandlerList));
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
        current.append(c);
        if (current.length() == 16) {
            pop(YAPIONType.POINTER);
            YAPIONPointer yapionPointer = new YAPIONPointer(stringBuilderToUTF8String(current));
            yapionPointerList.add(yapionPointer);
            add(key, yapionPointer);
            reset();
        }
    }

    private void parseMap(char c, char lastChar) {
        if (!everyType(c, lastChar) && c == '>') {
            parseEndMap();
        }
    }

    private void parseEndMap() {
        pop(YAPIONType.MAP);
        ((YAPIONMap) currentObject).finishMapping();
        currentObject = currentObject.getParent();
        reset();
    }

    private void parseArray(char c, char lastChar) {
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
        currentObject = currentObject.getParent();
        reset();
    }

    private void parseComment(char c, char lastChar) {
        if (lastChar == '*' && c == '/') {
            if (current.length() > 0) {
                current.deleteCharAt(current.length() - 1);
            }
            if (comments.isAdd()) {
                currentComments.add(current.toString());
                log.debug("COMMENT: {}", current);
            }
            pop(YAPIONType.COMMENT);
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
