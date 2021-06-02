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
import yapion.hierarchy.types.*;
import yapion.hierarchy.types.value.ValueHandler;
import yapion.utils.ReferenceFunction;
import yapion.utils.ReferenceIDUtils;

import java.nio.charset.StandardCharsets;
import java.util.*;

@Slf4j
final class YAPIONInternalParser {

    // Parse steps done
    private int count = 0;

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
    private final List<YAPIONObject> yapionObjectList = new ArrayList<>();
    private final List<YAPIONPointer> yapionPointerList = new LinkedList<>();

    // YAPIONValue type specifications
    private final List<ValueHandler<?>> valueHandlerList = new LinkedList<>();

    void setReferenceFunction(ReferenceFunction referenceFunction) {
        this.referenceFunction = referenceFunction;
    }

    void advance(char c) {
        step(c);
        count++;
        lastChar = c;
    }

    boolean isFinished() {
        return finished;
    }

    YAPIONObject finish() {
        parseFinish();
        return result;
    }

    int count() {
        return count;
    }

    private void step(char c) {
        log.debug("{} -> {}", typeStack, (int) c);
        if (typeStack.isEmpty() && initialType(c)) {
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
                parseValue(c);
                break;
            case MAP:
                parseMap(c, lastChar);
                break;
            default:
                parseObject(c, lastChar);
        }
    }

    private void parseFinish() {
        if (mightValue == MightValue.TRUE) {
            parseValueJSONEnd('\u0000');
        }
        if (count == 0) {
            throw new YAPIONParserException("No parse steps were done");
        }
        if (typeStack.isEmpty() && !hadInitial && typeStack.pop(YAPIONType.OBJECT) == YAPIONType.OBJECT) {
            throw new YAPIONParserException("Object is closed too often");
        }
        if (typeStack.isNotEmpty() && hadInitial) {
            throw new YAPIONParserException("Object is not closed correctly");
        }

        if (!yapionPointerList.isEmpty()) {
            log.debug("pFinish  [init]");
            Map<Long, YAPIONObject> yapionObjectMap = new HashMap<>();
            for (YAPIONObject yapionObject : yapionObjectList) {
                yapionObjectMap.put(yapionObject.referenceValue(referenceFunction), yapionObject);
            }
            log.debug("pFinish  [setPointer]");
            for (YAPIONPointer yapionPointer : yapionPointerList) {
                long id = yapionPointer.getPointerID();
                YAPIONObject yapionObject = yapionObjectMap.get(id);
                if (yapionObject == null) continue;
                yapionPointer.setYAPIONObject(yapionObject);
            }
            log.debug("pFinish  [done]");
        }
        log.debug("finish   [done]");
    }

    private void push(YAPIONType yapionType) {
        log.debug("push     [{}]", yapionType);
        typeStack.push(yapionType);
        key = stringBuilderToUTF8String(current);
        current = new StringBuilder();

        valueHandlerList.clear();
        YAPIONValue.allValueHandlers().toArrayList(valueHandlerList);
    }

    private void pop(YAPIONType yapionType) {
        log.debug("pop      [{}]", yapionType);
        typeStack.pop(yapionType);
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
        yapionObjectList.add(result);
        currentObject = result;
        return hadInitial;
    }

    private void add(@NonNull String key, @NonNull YAPIONAnyType value) {
        log.debug("add      ['{}'='{}']", key.replace("\r", "\\r").replace("\n", "\\n").replace("\t", "\\t"), value);
        currentObject.getType().getAddConsumer().accept(currentObject, key, value);
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

            if (c == '{' || c == '[' || c == '<' || c == '"' || (c >= '0' && c <= '9') || c == 't' || c == 'n' || c == 'f') {
                current.delete(current.length() - 2, current.length());
                current.deleteCharAt(0);
                if (c != '{' && c != '[' && c != '<') {
                    log.debug("type     [JSON]");
                    push(YAPIONType.VALUE);
                    mightValue = MightValue.TRUE;
                    parseValue(c);
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
            yapionObjectList.add(yapionObject);
            add(key, yapionObject);
            currentObject = yapionObject;
            key = "";
            return true;
        }
        if (c == '[') {
            log.debug("type     [ARRAY]");
            push(YAPIONType.ARRAY);
            YAPIONArray yapionArray = new YAPIONArray();
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
            current.deleteCharAt(current.length() - 1);
            push(YAPIONType.POINTER);
            return true;
        }
        if (c == '<') {
            log.debug("type     [MAP]");
            push(YAPIONType.MAP);
            YAPIONMap yapionMap = new YAPIONMap();
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

    private void parseValue(char c) {
        if (parseSpecialEscape(c)) {
            return;
        }
        if (mightValue == MightValue.FALSE && !escaped && c == ')') {
            log.debug("ValueHandler to use -> {}", valueHandlerList);
            pop(YAPIONType.VALUE);
            add(key, YAPIONValue.parseValue(stringBuilderToUTF8String(current), valueHandlerList));
            reset();
            return;
        }
        if (mightValue == MightValue.TRUE && !escaped && (c == ',' || c == '}' || c == ']' || c == '>')) {
            parseValueJSONEnd(c);
            return;
        }
        if (c == '\\' && !escaped) {
            escaped = true;
            return;
        }
        lastCharEscaped = escaped;
        if (escaped) {
            if (typeStack.peek() == YAPIONType.ARRAY && (c == ',' || c == '-')) {
                // Ignored
            } else if (typeStack.peek() == YAPIONType.ARRAY && current.length() == 0 && c == ' ') {
                // Ignored
            } else if (c != '(' && c != ')') {
                sortValueHandler('\\', current.length());
                current.append('\\');
            }
            escaped = false;
        }
        sortValueHandler(c, current.length());
        current.append(c);
    }

    private void parseValueJSONEnd(char c) {
        log.debug("ValueHandler to use -> {}", valueHandlerList);
        pop(YAPIONType.VALUE);
        while (true) {
            char temp = current.charAt(current.length() - 1);
            if (!(temp == ' ' || temp == '\t' || temp == '\n')) {
                break;
            }
            current.deleteCharAt(current.length() - 1);
        }
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
                YAPIONValue.allValueHandlers().toArrayList(valueHandlerList);
                return;
            }
            parseEndArray();
            return;
        }
        if (!lastCharEscaped && current.length() == 0 && everyType(c, lastChar)) {
            return;
        }
        if (!lastCharEscaped && current.length() == 1 && lastChar == '-' && everyType(c, lastChar)) {
            return;
        }
        if (current.length() == 0 && isWhiteSpace(c) && !escaped) {
            return;
        }
        parseValue(c);
    }

    private void parseEndArray() {
        pop(YAPIONType.ARRAY);
        currentObject = currentObject.getParent();
        reset();
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
