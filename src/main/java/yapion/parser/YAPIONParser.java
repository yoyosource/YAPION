// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020,2021 yoyosource

package yapion.parser;

import lombok.NonNull;
import yapion.annotations.deserialize.YAPIONLoadExclude;
import yapion.annotations.serialize.YAPIONSaveExclude;
import yapion.exceptions.parser.YAPIONParserException;
import yapion.exceptions.utils.YAPIONIOException;
import yapion.hierarchy.output.AbstractOutput;
import yapion.hierarchy.typegroups.YAPIONAnyType;
import yapion.hierarchy.typeinterfaces.ObjectOutput;
import yapion.hierarchy.types.*;
import yapion.utils.ReflectionsUtils;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@YAPIONSaveExclude(context = "*")
@YAPIONLoadExclude(context = "*")
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
            parseInternal();
        } catch (YAPIONParserException e) {
            YAPIONParserException yapionParserException = new YAPIONParserException(((e.getMessage() != null ? e.getMessage() + "\n" : "\n") + generateErrorMessage()), e.getCause());
            yapionParserException.setStackTrace(e.getStackTrace());
            throw yapionParserException;
        } catch (IOException e) {
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
        if (current.length() == 0 && c == ' ' && escaped) {
            current.append(c);
        }
        if (current.length() != 0 || (c != ' ' && c != '\n')) {
            current.append(c);
        }

        if (escaped) {
            escaped = false;
        }
        if (c == '\\') {
            escaped = true;
        }
    }

    private void parseFinish() {
        Map<Long, YAPIONObject> yapionObjectMap = new HashMap<>();
        for (YAPIONObject yapionObject : yapionObjectList) {
            yapionObjectMap.put(new YAPIONPointer(yapionObject).getPointerID(), yapionObject);
        }
        for (YAPIONPointer yapionPointer : yapionPointerList) {
            long id = yapionPointer.getPointerID();
            YAPIONObject yapionObject = yapionObjectMap.get(id);
            if (yapionObject == null) continue;
            ReflectionsUtils.invokeMethod("setYAPIONObject", yapionPointer, yapionObject);
        }
    }

    private void push(YAPIONType YAPIONType) {
        typeStack.push(YAPIONType);
        key = current.toString();
        current = new StringBuilder();
    }

    private void pop(YAPIONType YAPIONType) {
        ReflectionsUtils.invokeMethod("setParseTime", currentObject, new ReflectionsUtils.Parameter(long.class, typeStack.peekTime()));
        typeStack.pop(YAPIONType);
    }

    private void reset() {
        current = new StringBuilder();
        key = "";
    }

    private void initialType(char c) {
        if (c == '{' && typeStack.isEmpty() && result == null) {
            typeStack.push(YAPIONType.OBJECT);
            result = new YAPIONObject();
            yapionObjectList.add(result);
            currentObject = result;
            return;
        }
        throw new YAPIONParserException();
    }

    private void add(@NonNull String key, @NonNull YAPIONAnyType value) {
        if (currentObject instanceof YAPIONObject) {
            ((YAPIONObject) currentObject).add(key, value);
        } else if (currentObject instanceof YAPIONMap) {
            if (key.startsWith("#")) {
                ((YAPIONMap) currentObject).add(new YAPIONParserMapObject(key, value));
            } else {
                ((YAPIONMap) currentObject).add(new YAPIONValue<>(key), value);
            }
        } else if (currentObject instanceof YAPIONArray) {
            ((YAPIONArray) currentObject).add(value);
        }
    }

    private void add(YAPIONValue<String> yapionValue) {
        if (currentObject instanceof YAPIONMap) {
            ((YAPIONMap) currentObject).add(new YAPIONParserMapMapping(yapionValue));
        }
    }

    private boolean everyType(char c, char lastChar) {
        if (escaped) {
            return false;
        }
        if (c == '{') {
            push(YAPIONType.OBJECT);
            YAPIONObject yapionObject = new YAPIONObject();
            yapionObjectList.add(yapionObject);
            add(key, yapionObject);
            currentObject = yapionObject;
            key = "";
            return true;
        }
        if (c == '[') {
            push(YAPIONType.ARRAY);
            YAPIONArray yapionArray = new YAPIONArray();
            add(key, yapionArray);
            currentObject = yapionArray;
            key = "";
            return true;
        }
        if (c == '(') {
            push(YAPIONType.VALUE);
            return true;
        }
        if (lastChar == '-' && c == '>') {
            current.deleteCharAt(current.length() - 1);
            push(YAPIONType.POINTER);
            return true;
        }
        if (c == '<') {
            push(YAPIONType.MAP);
            YAPIONMap yapionMap = new YAPIONMap();
            add(key, yapionMap);
            currentObject = yapionMap;
            key = "";
            return true;
        }
        return false;
    }

    private void parseValue(char c) {
        if (escaped && c == 'u') {
            unicode = new StringBuilder();
            escaped = false;
            return;
        }
        if (unicode != null && unicode.length() < 4) {
            unicode.append(c);
            if (unicode.length() == 4) {
                current.append((char) Integer.parseInt(unicode.toString(), 16));
                unicode = null;
            }
            return;
        }
        if (!escaped && c == ')') {
            pop(YAPIONType.VALUE);
            add(key, YAPIONValue.parseValue(current.toString()));
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
            YAPIONPointer yapionPointer = new YAPIONPointer(current.toString());
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
            if (current.length() != 0) {
                add(new YAPIONValue<>(current.toString()));
            }
            ((YAPIONMap) currentObject).finishMapping();
            currentObject = currentObject.getParent();
            reset();
            return;
        }

        if (c == '#') {
            if (current.length() != 0) {
                add(new YAPIONValue<>(current.toString()));
            }
            current = new StringBuilder();
        } else if (c == ',') {
            if (current.length() != 0) {
                add(new YAPIONValue<>(current.toString()));
            }
            current = new StringBuilder();
            return;
        }
        if (isHex(c) || (c == ':' || c == '#' || c == '-')) {
            current.append(c);
        }
    }

    private void parseArray(char c, char lastChar) {
        key = "";
        if (!escaped) {
            if (c == ',') {
                if (current.length() != 0) {
                    add("", YAPIONValue.parseValue(current.toString()));
                }
                current = new StringBuilder();
                return;
            }
            if (c == ']') {
                if (current.length() != 0) {
                    add("", YAPIONValue.parseValue(current.toString()));
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

}