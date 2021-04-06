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
import yapion.utils.ReflectionsUtils;

import java.nio.charset.StandardCharsets;
import java.util.*;

@Slf4j
class YAPIONInternalParser {

    // Parse steps done
    private int count = 0;

    // last char
    private char lastChar = '\u0000';

    // Result object and current
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
    private StringBuilder unicode = null;

    // Key and Value variables
    private StringBuilder current = new StringBuilder();
    private String key = "";

    // All Objects and Pointers
    private final List<YAPIONObject> yapionObjectList = new ArrayList<>();
    private final List<YAPIONPointer> yapionPointerList = new ArrayList<>();

    // YAPIONValue type specifications
    private final List<ValueHandler<?>> valueHandlerSet = new ArrayList<>();

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
        log.debug(typeStack.toString() + " ->  0x" + String.format("%04X", (int) c) + " " + c);
        if (typeStack.isEmpty()) {
            initialType(c);
            return;
        }

        switch (typeStack.peek()) {
            case POINTER:
                parsePointer(c);
                return;
            case ARRAY:
                parseArray(c, lastChar);
                return;
            case VALUE:
                parseValue(c);
                return;
            case MAP:
                parseMap(c, lastChar);
                return;
            default:
                break;
        }

        if (everyType(c, lastChar)) {
            return;
        }
        if (!escaped && c == '}') {
            pop(YAPIONType.OBJECT);
            reset();
            // ((YAPIONObject) currentObject).
            currentObject = currentObject.getParent();
            if (typeStack.isEmpty()) {
                finished = true;
            }
            return;
        }
        if (parseSpecialEscape(c)) {
            return;
        }
        if (c == '\\' && !escaped) {
            escaped = true;
            return;
        }
        if (current.length() == 0 && c == ' ' && escaped) {
            current.append(c);
            escaped = false;
            return;
        }
        if (current.length() != 0 || (c != ' ' && c != '\n')) {
            current.append(c);
        }
        if (escaped) {
            escaped = false;
        }
    }

    private void parseFinish() {
        if (typeStack.isNotEmpty()) {
            throw new YAPIONParserException("Object is not closed correctly");
        }
        if (count == 0) {
            throw new YAPIONParserException("No parse steps were done");
        }

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
            ReflectionsUtils.invokeMethod("setYAPIONObject", yapionPointer, yapionObject);
        }
        log.debug("pFinish  [done]");
    }

    private void push(YAPIONType yapionType) {
        log.debug("push     [" + yapionType + "]");
        typeStack.push(yapionType);
        key = stringBuilderToUTF8String(current);
        current = new StringBuilder();

        if (yapionType == YAPIONType.VALUE) {
            valueHandlerSet.clear();
            valueHandlerSet.addAll(YAPIONValue.allValueHandlers());
        }
    }

    private void pop(YAPIONType yapionType) {
        log.debug("pop      [" + yapionType + "]");
        ReflectionsUtils.invokeMethod("setParseTime", currentObject, new ReflectionsUtils.Parameter(long.class, typeStack.peekTime()));
        typeStack.pop(yapionType);
    }

    private void reset() {
        log.debug("reset    [" + current + ", " + key + "]");
        current = new StringBuilder();
        key = "";
    }

    private void initialType(char c) {
        if (c == '{' && typeStack.isEmpty() && result == null) {
            log.debug("initial  [Create]");
            typeStack.push(YAPIONType.OBJECT);
            result = new YAPIONObject();
            yapionObjectList.add(result);
            currentObject = result;
            return;
        }
        log.debug("initial  [EXCEPTION] -> 0x" + String.format("%04X", (int) c));
        throw new YAPIONParserException("Initial char is not '{'");
    }

    private void add(@NonNull String key, @NonNull YAPIONAnyType value) {
        log.debug("add      ['" + key + "'='" + value + "']");
        if (currentObject instanceof YAPIONObject) {
            ((YAPIONObject) currentObject).add(key, value);
        } else if (currentObject instanceof YAPIONMap) {
            ReflectionsUtils.invokeMethod("add", (YAPIONMap) currentObject, new ReflectionsUtils.Parameter(YAPIONParserMapValue.class, new YAPIONParserMapValue(value)));
        } else if (currentObject instanceof YAPIONArray) {
            ((YAPIONArray) currentObject).add(value);
        }
    }

    private boolean everyType(char c, char lastChar) {
        if (escaped) {
            return false;
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
        return false;
    }

    private boolean parseSpecialEscape(char c) {
        if (parseUTF8Escape(c)) return true;
        if (!escaped) return false;
        switch (c) {
            case 'n':
                current.append("\n");
                break;
            case 't':
                current.append("\t");
                break;
            case 'r':
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
            log.debug("UTF8     [" + unicode + "]");
            if (unicode.length() == 4) {
                current.append((char) Integer.parseInt(unicode.toString(), 16));
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
        if (!escaped && c == ')') {
            log.debug("ValueHandler to use -> " + valueHandlerSet);
            pop(YAPIONType.VALUE);
            add(key, YAPIONValue.parseValue(stringBuilderToUTF8String(current), valueHandlerSet));
            reset();
        } else {
            if (c == '\\' && !escaped) {
                escaped = true;
                return;
            }
            if (escaped) {
                if (c != '(' && c != ')') {
                    valueHandlerSet.removeIf(valueHandler -> !valueHandler.allowed('\\', stringBuilderToUTF8String(current).length()));
                    current.append('\\');
                }
                escaped = false;
            }
            valueHandlerSet.removeIf(valueHandler -> !valueHandler.allowed(c, stringBuilderToUTF8String(current).length()));
            current.append(c);
        }
    }

    private boolean isHex(char c) {
        return (c >= '0' && c <= '9') || (c >= 'A' && c <= 'F') || (c >= 'a' && c <= 'f');
    }

    private void parsePointer(char c) {
        if (isHex(c)) {
            current.append(c);
        }
        if (current.length() == 16) {
            pop(YAPIONType.POINTER);
            YAPIONPointer yapionPointer = new YAPIONPointer(stringBuilderToUTF8String(current));
            yapionPointerList.add(yapionPointer);
            add(key, yapionPointer);
            reset();
        }
    }

    private void parseMap(char c, char lastChar) {
        if (everyType(c, lastChar)) {
            return;
        }
        if (c == '>') {
            pop(YAPIONType.MAP);
            ReflectionsUtils.invokeMethod("finishMapping", (YAPIONMap) currentObject);
            currentObject = currentObject.getParent();
            reset();
        }
    }

    private void parseArray(char c, char lastChar) {
        key = "";
        if (!escaped) {
            if (c == ',') {
                if (current.length() != 0) {
                    add("", YAPIONValue.parseValue(stringBuilderToUTF8String(current)));
                }
                current = new StringBuilder();
                return;
            }
            if (c == ']') {
                if (current.length() != 0) {
                    add("", YAPIONValue.parseValue(stringBuilderToUTF8String(current)));
                }
                pop(YAPIONType.ARRAY);
                currentObject = currentObject.getParent();
                reset();
                return;
            }
        }
        if (current.length() == 0 && everyType(c, lastChar)) {
            return;
        }
        if (current.length() == 0 && (c == ' ' || c == '\n') && !escaped) {
            return;
        }
        current.append(c);
    }

    private String stringBuilderToUTF8String(StringBuilder st) {
        return new String(st.toString().getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8);
    }

}
