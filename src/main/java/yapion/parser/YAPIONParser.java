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
import java.util.function.Supplier;

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

    private YAPIONInternalParser yapionInternalParser = new YAPIONInternalParser();
    private CharReader charReader;

    /**
     * Creates a YAPIONParser for parsing a string to an YAPIONObject.
     *
     * @param string to parse from
     */
    public YAPIONParser(@NonNull String string) {
        charReader = new CharReader() {
            private int index = 0;

            @Override
            char next() {
                return string.charAt(index++);
            }

            @Override
            boolean hasNext() {
                return index < string.length();
            }
        };
    }

    /**
     * Creates a YAPIONParser for parsing a StringBuilder to an YAPIONObject.
     *
     * @param stringBuilder to parse from
     */
    public YAPIONParser(@NonNull StringBuilder stringBuilder) {
        charReader = new CharReader() {
            private int index = 0;

            @Override
            char next() {
                return stringBuilder.charAt(index++);
            }

            @Override
            boolean hasNext() {
                return index < stringBuilder.length();
            }
        };
    }

    /**
     * Creates a YAPIONParser for parsing a byte array to an YAPIONObject.
     *
     * @param bytes to parse from
     */
    public YAPIONParser(@NonNull byte[] bytes) {
        charReader = new CharReader() {
            private int index = 0;

            @Override
            char next() {
                return (char) bytes[index++];
            }

            @Override
            boolean hasNext() {
                return index < bytes.length;
            }
        };
    }

    /**
     * Creates a YAPIONParser for parsing a char array to an YAPIONObject.
     *
     * @param chars to parse from
     */
    public YAPIONParser(@NonNull char[] chars) {
        charReader = new CharReader() {
            private int index = 0;

            @Override
            char next() {
                return chars[index++];
            }

            @Override
            boolean hasNext() {
                return index < chars.length;
            }
        };
    }

    /**
     * Creates a YAPIONParser for parsing an InputStream to an YAPIONObject.
     *
     * @param inputStream to parse from
     */
    public YAPIONParser(@NonNull InputStream inputStream) {
        charReader = new CharReader() {
            @Override
            char next() {
                try {
                    int i = inputStream.read();
                    if (i == -1) {
                        throw new ParserSkipException();
                    }
                    return (char) i;
                } catch (IOException e) {
                    throw new YAPIONParserException();
                }
            }

            @Override
            boolean hasNext() {
                return true;
            }
        };
    }

    public YAPIONParser setReferenceFunction(@NonNull ReferenceFunction referenceFunction) {
        yapionInternalParser.setReferenceFunction(referenceFunction);
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
            throw new YAPIONParserException(((e.getMessage() != null ? e.getMessage() + "\n" : "\n") + generateErrorMessage()), e);
        }
        return this;
    }

    /**
     * Returns the YAPIONObject parsed by {@code parse()}
     *
     * @return the YAPIONObject
     */
    public YAPIONObject result() {
        return yapionInternalParser.finish();
    }

    private String generateErrorMessage() {
        return "Error after " + yapionInternalParser.count() + " reads";
    }

    private void parseInternal() {
        while (charReader.hasNext() && !yapionInternalParser.isFinished()) {
            try {
                yapionInternalParser.advance(charReader.next());
            } catch (ParserSkipException e) {
                // Ignored
            }
        }
    }

    private static class ParserSkipException extends RuntimeException {}

}
