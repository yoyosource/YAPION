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
import yapion.exceptions.utils.YAPIONIOException;
import yapion.hierarchy.api.ObjectOutput;
import yapion.hierarchy.api.groups.YAPIONAnyType;
import yapion.hierarchy.output.AbstractOutput;
import yapion.hierarchy.types.*;
import yapion.utils.ReferenceFunction;
import yapion.utils.ReferenceIDUtils;
import yapion.utils.ReflectionsUtils;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public final class YAPIONParser {

    /**
     * Parses the String to an YAPIONObject.
     *
     * @param s the string to parse
     * @return YAPIONObject parsed out of the string
     */
    public static YAPIONObject parse(String s) {
        return new YAPIONParser(s).parse().result();
    }

    /**
     * Parses the String to an YAPIONObject.
     *
     * @param s the string to parse
     * @return YAPIONObject parsed out of the string
     *
     * @deprecated since 0.23.0
     */
    @Deprecated
    public static YAPIONObject parseOld(String s) {
        return new YAPIONParser(s).setReferenceFunction(ReferenceIDUtils.REFERENCE_FUNCTION_OLD).parse().result();
    }

    /**
     * Parses the JSON String to an YAPIONObject.
     *
     * @param s the string to parse
     * @return YAPIONObject parsed out of the string
     */
    public static YAPIONObject parseJSON(String s) {
        return JSONParser.parse(s);
    }

    /**
     * This method maps every corrupted YAPIONPointer and YAPIONMap
     * back to their corresponding YAPION representations. Useful
     * to serialize a java object with JSON and create the underlying
     * Java object back out of this JSON representation.
     *
     * <br><br>{"@pointer":"0000000000000000"} will be interpreted as a pointer.
     * <br>{"@mapping":[]} will be interpreted as a map.
     *
     * <br><br>Some specialties for non Lossy JSON {@link ObjectOutput#toJSON(AbstractOutput)}
     * instead of {@link ObjectOutput#toJSONLossy(AbstractOutput)} will be
     * the primitive types that are represented by an YAPIONObject
     * instead.
     * This is to ensure that Data loss will not happen.
     * <br>{"@byte":0} will be interpreted as a {@link Byte}
     * <br>{"@short":0} will be interpreted as a {@link Short}
     * <br>{"@int":0} will be interpreted as an {@link Integer}
     * <br>{"@long":0} will be interpreted as a {@link Long}
     * <br>{"@bint":"0"} will be interpreted as a {@link BigInteger}
     * <br>{"@char":""} will be interpreted as a {@link Character}
     * <br>{"@float":0.0} will be interpreted as a {@link Float}
     * <br>{"@double":0.0} will be interpreted as a {@link Double}
     * <br>{"@bdecimal":"0.0"} will be interpreted as a {@link BigDecimal}
     *
     * <br><br>You can find more information in the classes
     * {@link YAPIONMap}, {@link YAPIONPointer} and {@link YAPIONValue}.
     * These classes have the method {@link ObjectOutput#toJSON(AbstractOutput)}
     * and {@link ObjectOutput#toJSONLossy(AbstractOutput)} overridden with a
     * specific implementation for these types. The difference
     * between those methods should be the {@link YAPIONValue}
     * implementation as it tries to not lose any data with
     * {@link YAPIONValue#toJSON(AbstractOutput)} and will discard some data
     * by using {@link YAPIONValue#toJSONLossy(AbstractOutput)}. The data
     * that gets lost are the type of numbers, floating point
     * numbers and the information whether or not the {@link String}
     * was previously a {@link Character}.
     *
     * @param yapionObject the YAPIONObject to map
     * @return YAPIONObject with mapped YAPIONPointer and YAPIONMap
     */
    public static YAPIONObject mapJSON(YAPIONObject yapionObject) {
        return JSONMapper.map(yapionObject);
    }

    /**
     * This method first parses the String to a YAPIONObject with
     * {@link #parseJSON(String)} and maps the YAPIONObject back.
     * After parsing this method maps every corrupted YAPIONPointer
     * and YAPIONMap back to their corresponding YAPION
     * representations. Useful to serialize a java object with JSON
     * and create the underlying Java object back out of this JSON
     * representation.
     *
     * <br><br>{"@pointer":"0000000000000000"} will be interpreted as a pointer.
     * <br>{"@mapping":[]} will be interpreted as a map.
     *
     * <br><br>Some specialties for non Lossy JSON {@link ObjectOutput#toJSON(AbstractOutput)}
     * instead of {@link ObjectOutput#toJSONLossy(AbstractOutput)} will be
     * the primitive types that are represented by an YAPIONObject
     * instead.
     * This is to ensure that Data loss will not happen.
     * <br>{"@byte":0} will be interpreted as a {@link Byte}
     * <br>{"@short":0} will be interpreted as a {@link Short}
     * <br>{"@int":0} will be interpreted as an {@link Integer}
     * <br>{"@long":0} will be interpreted as a {@link Long}
     * <br>{"@bint":0} will be interpreted as a {@link BigInteger}
     * <br>{"@char":""} will be interpreted as a {@link Character}
     * <br>{"@float":0.0} will be interpreted as a {@link Float}
     * <br>{"@double":0.0} will be interpreted as a {@link Double}
     * <br>{"@bdecimal":0.0} will be interpreted as a {@link BigDecimal}
     *
     * <br><br>You can find more information in the classes
     * {@link YAPIONMap}, {@link YAPIONPointer} and {@link YAPIONValue}.
     * These classes have the method {@link ObjectOutput#toJSON(AbstractOutput)}
     * and {@link ObjectOutput#toJSONLossy(AbstractOutput)} overridden with a
     * specific implementation for these types. The difference
     * between those methods should be the {@link YAPIONValue}
     * implementation as it tries to not lose any data with
     * {@link YAPIONValue#toJSON(AbstractOutput)} and will discard some data
     * by using {@link YAPIONValue#toJSONLossy(AbstractOutput)}. The data
     * that gets lost are the type of numbers, floating point
     * numbers and the information whether or not the {@link String}
     * was previously a {@link Character}.
     *
     * @param s the String to map
     * @return YAPIONObject with mapped YAPIONPointer and YAPIONMap
     */
    public static YAPIONObject mapJSON(String s) {
        return JSONMapper.map(parseJSON(s));
    }

    /**
     * Parses the InputStream to an YAPIONObject.
     * This method only parses the next YAPIONObject and tries to read
     * until the YAPIONObject is finished. It will not cancel even when
     * the end of Stream is reached. It will only cancel after it has a
     * complete and valid YAPIONObject or 1 second without any new
     * Input passed.
     *
     * @param inputStream the inputStream to parse
     * @return YAPIONObject parsed out of the string
     */
    public static YAPIONObject parse(InputStream inputStream) {
        return new YAPIONParser(inputStream).parse().result();
    }

    /**
     * Parses the InputStream to an YAPIONObject.
     * This method only parses the next YAPIONObject and tries to read
     * until the YAPIONObject is finished. It will not cancel even when
     * the end of Stream is reached. It will only cancel after it has a
     * complete and valid YAPIONObject or 1 second without any new
     * Input passed.
     *
     * @param inputStream the inputStream to parse
     * @return YAPIONObject parsed out of the string
     *
     * @deprecated since 0.23.0
     */
    @Deprecated
    public static YAPIONObject parseOld(InputStream inputStream) {
        return new YAPIONParser(inputStream).setReferenceFunction(ReferenceIDUtils.REFERENCE_FUNCTION_OLD).parse().result();
    }

    private YAPIONObject result = null;
    private YAPIONAnyType currentObject = null;
    private String s;
    private int index = 0;

    private InputStream inputStream;
    private boolean finished = false;

    private final TypeStack typeStack = new TypeStack();

    /**
     * Creates a YAPIONParser for parsing an string to an YAPIONObject.
     *
     * @param s to parse from
     */
    public YAPIONParser(String s) {
        this.s = s;
    }

    /**
     * Creates a YAPIONParser for parsing an InputStream to an YAPIONObject.
     *
     * @param inputStream to parse from
     */
    public YAPIONParser(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public YAPIONParser setReferenceFunction(@NonNull ReferenceFunction referenceFunction) {
        this.referenceFunction = referenceFunction;
        return this;
    }

    /**
     * Parses the InputStream or String to an YAPIONObject.
     * If an InputStream is present this method only parses
     * the next YAPIONObject and tries to read until the YAPIONObject
     * is finished. It will not cancel even when the end of Steam
     * is reached. It will only cancel after it has a complete and
     * valid YAPIONObject.
     */
    public YAPIONParser parse() {
        try {
            log.debug("parse    [init]");
            parseInternal();
            log.debug("parse    [finished]");
        } catch (YAPIONParserException e) {
            log.debug("parse    [YAPIONParserException]");
            YAPIONParserException yapionParserException = new YAPIONParserException(((e.getMessage() != null ? e.getMessage() + "\n" : "\n") + generateErrorMessage()), e.getCause());
            yapionParserException.setStackTrace(e.getStackTrace());
            throw yapionParserException;
        } catch (IOException e) {
            log.debug("parse    [IOException]");
            YAPIONIOException yapionioException = new YAPIONIOException(e.getMessage(), e.getCause());
            yapionioException.setStackTrace(e.getStackTrace());
            throw yapionioException;
        }
        return this;
    }

    /**
     * Returns the YAPIONObject parsed by {@code parse()}
     *
     * @return the YAPIONObject
     */
    public YAPIONObject result() {
        return result;
    }

    private String generateErrorMessage() {
        // TODO: Fix inputStream null and String null
        if (inputStream != null) return "";
        StringBuilder st = new StringBuilder();
        st.append(s);
        st.append("\n");
        for (int i = 0; i < s.length(); i++) {
            if (i == index) {
                st.append("^");
            } else {
                st.append(" ");
            }
        }
        return st.toString();
    }

    private ReferenceFunction referenceFunction = ReferenceIDUtils.REFERENCE_FUNCTION;
    private boolean escaped = false;
    private StringBuilder unicode = null;
    private StringBuilder current = new StringBuilder();
    private String key = "";

    private final List<YAPIONObject> yapionObjectList = new ArrayList<>();
    private final List<YAPIONPointer> yapionPointerList = new ArrayList<>();

    private void parseInternal() throws IOException {
        if (s != null) {
            parseString();
        } else if (inputStream != null) {
            parseInputStream();
        } else {
            throw new YAPIONParserException("null input");
        }

        if (typeStack.isNotEmpty()) {
            throw new YAPIONParserException("");
        }
        parseFinish();
    }

    private void parseString() {
        char lastChar;
        char c = '\u0000';

        for (int i = 0; i < s.length(); i++) {
            lastChar = c;
            c = s.charAt(i);
            index = i;

            parseStep(lastChar, c);
            if (finished) {
                break;
            }
        }
    }

    private void parseInputStream() throws IOException {
        char lastChar;
        char c = '\u0000';

        long time = System.currentTimeMillis();
        while (!finished) {
            index++;
            lastChar = c;
            if (System.currentTimeMillis() - time > 1000) {
                break;
            }
            int i = inputStream.read();
            if (i == -1) continue;
            time = System.currentTimeMillis();
            c = (char) i;

            parseStep(lastChar, c);
        }
    }

    private void parseStep(char lastChar, char c) {
        log.debug(typeStack.toString() + " -> " + lastChar + c);
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
        if (parseUTF8Escape(c)) {
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
        log.debug("initial  [EXCEPTION]");
        throw new YAPIONParserException();
    }

    private void add(@NonNull String key, @NonNull YAPIONAnyType value) {
        log.debug("add      [" + key + "=" + value + "]");
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
        if (parseUTF8Escape(c)) {
            return;
        }
        if (!escaped && c == ')') {
            pop(YAPIONType.VALUE);
            add(key, YAPIONValue.parseValue(stringBuilderToUTF8String(current)));
            reset();
        } else {
            if (c == '\\' && !escaped) {
                escaped = true;
                return;
            }
            if (escaped) {
                if (c != '(' && c != ')') {
                    current.append('\\');
                }
                escaped = false;
            }
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
        if (everyType(c, lastChar)) {
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
