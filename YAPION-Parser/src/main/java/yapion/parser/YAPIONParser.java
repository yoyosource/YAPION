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
import yapion.annotations.api.DeprecationInfo;
import yapion.exceptions.YAPIONException;
import yapion.exceptions.parser.YAPIONParserException;
import yapion.hierarchy.api.ObjectOutput;
import yapion.hierarchy.api.groups.YAPIONAnyType;
import yapion.hierarchy.output.AbstractOutput;
import yapion.hierarchy.types.*;
import yapion.parser.charreader.ASCIIInputStreamCharReader;
import yapion.parser.charreader.Latin1InputStreamCharReader;
import yapion.parser.charreader.UTF8InputStreamCharReader;
import yapion.parser.options.FileOptions;
import yapion.parser.options.ParseOptions;
import yapion.parser.options.StreamOptions;
import yapion.utils.ReferenceFunction;

import java.io.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;

@Slf4j
public final class YAPIONParser {

    /**
     * Parses the String to an YAPIONObject.
     *
     * @param s the string to parse
     * @return YAPIONObject parsed out of the string
     */
    public static YAPIONObject parse(String s) {
        return new YAPIONParser(s, new ParseOptions()).parse().result();
    }

    /**
     * Parses the String to an YAPIONObject.
     *
     * @param s the string to parse
     * @param parseOptions the parse options
     * @return YAPIONObject parsed out of the string
     */
    public static YAPIONObject parse(String s, ParseOptions parseOptions) {
        return new YAPIONParser(s, parseOptions).parse().result();
    }

    /**
     * Parses the String to an YAPIONObject.
     *
     * @param s the string to parse
     * @param commentParsing how comments should be parsed
     * @return YAPIONObject parsed out of the string
     */
    @Deprecated
    @DeprecationInfo(since = "0.26.0", alternative = "#parse(String, ParseOptions)")
    public static YAPIONObject parse(String s, CommentParsing commentParsing) {
        return new YAPIONParser(s, new ParseOptions().commentParsing(commentParsing)).parse().result();
    }

    /**
     * Parses the File content to an YAPIONObject.
     *
     * @param file the file content to parse
     * @return The YAPIONObject parsed out of the file content
     * @throws IOException by FileInputStream creation
     */
    public static YAPIONObject parse(File file) throws IOException {
        return new YAPIONParser(file, new FileOptions()).parse().result();
    }

    /**
     * Parses the File content to an YAPIONObject.
     *
     * @param file the file content to parse
     * @param fileOptions the parse options
     * @return The YAPIONObject parsed out of the file content
     * @throws IOException by FileInputStream creation
     */
    public static YAPIONObject parse(File file, FileOptions fileOptions) throws IOException {
        return new YAPIONParser(file, fileOptions).parse().result();
    }

    /**
     * Parses the File content to an YAPIONObject.
     *
     * @param file the file content to parse
     * @param commentParsing how comments should be parsed
     * @return The YAPIONObject parsed out of the file content
     * @throws IOException by FileInputStream creation
     */
    @Deprecated
    @DeprecationInfo(since = "0.26.0", alternative = "#parse(File, FileOptions)")
    public static YAPIONObject parse(File file, CommentParsing commentParsing) throws IOException {
        return new YAPIONParser(file, new FileOptions().commentParsing(commentParsing)).parse().result();
    }

    /**
     * Parses the File content to an YAPIONObject.
     *
     * @param file the file content to parse
     * @param charset to use
     * @return The YAPIONObject parsed out of the file content
     * @throws IOException by FileInputStream creation
     */
    @Deprecated
    @DeprecationInfo(since = "0.26.0", alternative = "#parse(File, FileOptions)")
    public static YAPIONObject parse(File file, InputStreamCharsets charset) throws IOException {
        return new YAPIONParser(file, new FileOptions().charset(charset)).result();
    }

    /**
     * Parses the File content to an YAPIONObject.
     *
     * @param file the file content to parse
     * @param charset to use
     * @param commentParsing how comments should be parsed
     * @return The YAPIONObject parsed out of the file content
     * @throws IOException by FileInputStream creation
     */
    @Deprecated
    @DeprecationInfo(since = "0.26.0", alternative = "#parse(File, FileOptions)")
    public static YAPIONObject parse(File file, InputStreamCharsets charset, CommentParsing commentParsing) throws IOException {
        return new YAPIONParser(file, new FileOptions().charset(charset).commentParsing(commentParsing)).parse().result();
    }

    /**
     * Parses the File content to an YAPIONObject.
     *
     * @param file the file content to parse
     * @param stopOnStreamEnd stop on Stream end or not
     * @return The YAPIONObject parsed out of the file content
     * @throws IOException by FileInputStream creation
     */
    @Deprecated
    @DeprecationInfo(since = "0.26.0", alternative = "#parse(File, FileOptions)")
    public static YAPIONObject parse(File file, boolean stopOnStreamEnd) throws IOException {
        return new YAPIONParser(file, new FileOptions().stopOnStreamEnd(stopOnStreamEnd)).parse().result();
    }

    /**
     * Parses the File content to an YAPIONObject.
     *
     * @param file the file content to parse
     * @param stopOnStreamEnd stop on Stream end or not
     * @param commentParsing how comments should be parsed
     * @return The YAPIONObject parsed out of the file content
     * @throws IOException by FileInputStream creation
     */
    @Deprecated
    @DeprecationInfo(since = "0.26.0", alternative = "#parse(File, FileOptions)")
    public static YAPIONObject parse(File file, boolean stopOnStreamEnd, CommentParsing commentParsing) throws IOException {
        return new YAPIONParser(file, new FileOptions().stopOnStreamEnd(stopOnStreamEnd).commentParsing(commentParsing)).parse().result();
    }

    /**
     * Parses the File content to an YAPIONObject.
     *
     * @param file the file content to parse
     * @param stopOnStreamEnd stop on Stream end or not
     * @param charset to use
     * @return The YAPIONObject parsed out of the file content
     * @throws IOException by FileInputStream creation
     */
    @Deprecated
    @DeprecationInfo(since = "0.26.0", alternative = "#parse(File, FileOptions)")
    public static YAPIONObject parse(File file, boolean stopOnStreamEnd, InputStreamCharsets charset) throws IOException {
        return new YAPIONParser(file, new FileOptions().stopOnStreamEnd(stopOnStreamEnd).charset(charset)).parse().result();
    }

    /**
     * Parses the File content to an YAPIONObject.
     *
     * @param file the file content to parse
     * @param stopOnStreamEnd stop on Stream end or not
     * @param charset to use
     * @param commentParsing how comments should be parsed
     * @return The YAPIONObject parsed out of the file content
     * @throws IOException by FileInputStream creation
     */
    @Deprecated
    @DeprecationInfo(since = "0.26.0", alternative = "#parse(File, FileOptions)")
    public static YAPIONObject parse(File file, boolean stopOnStreamEnd, InputStreamCharsets charset, CommentParsing commentParsing) throws IOException {
        return new YAPIONParser(file, new FileOptions().stopOnStreamEnd(stopOnStreamEnd).charset(charset).commentParsing(commentParsing)).parse().result();
    }

    /**
     * Parses the InputStream to an YAPIONObject.
     * This method only parses the next YAPIONObject and tries to read
     * until the YAPIONObject is finished. It will cancel on EOF and
     * throw an {@link YAPIONParserException} if reached without
     * a finished {@link YAPIONObject}.
     *
     * @param inputStream the inputStream to parse
     * @return YAPIONObject parsed out of the string
     */
    public static YAPIONObject parse(InputStream inputStream) {
        return new YAPIONParser(inputStream, new StreamOptions()).parse().result();
    }

    /**
     * Parses the InputStream to an YAPIONObject.
     * This method only parses the next YAPIONObject and tries to read
     * until the YAPIONObject is finished. It will cancel on EOF and
     * throw an {@link YAPIONParserException} if reached without
     * a finished {@link YAPIONObject}.
     *
     * @param inputStream the inputStream to parse
     * @param streamOptions the parse options
     * @return YAPIONObject parsed out of the string
     */
    public static YAPIONObject parse(InputStream inputStream, StreamOptions streamOptions) {
        return new YAPIONParser(inputStream, streamOptions).parse().result();
    }

    /**
     * Parses the InputStream to an YAPIONObject.
     * This method only parses the next YAPIONObject and tries to read
     * until the YAPIONObject is finished. It will cancel on EOF and
     * throw an {@link YAPIONParserException} if reached without
     * a finished {@link YAPIONObject}.
     *
     * @param inputStream the inputStream to parse
     * @param commentParsing how comments should be parsed
     * @return YAPIONObject parsed out of the string
     */
    @Deprecated
    @DeprecationInfo(since = "0.26.0", alternative = "#parse(InputStream, StreamOptions)")
    public static YAPIONObject parse(InputStream inputStream, CommentParsing commentParsing) {
        return new YAPIONParser(inputStream, new StreamOptions().commentParsing(commentParsing)).parse().result();
    }

    /**
     * Parses the InputStream to an YAPIONObject.
     * This method only parses the next YAPIONObject and tries to read
     * until the YAPIONObject is finished. It will cancel on EOF and
     * throw an {@link YAPIONParserException} if reached without
     * a finished {@link YAPIONObject}.
     *
     * @param inputStream the inputStream to parse
     * @param charset to use
     * @return YAPIONObject parsed out of the string
     */
    @Deprecated
    @DeprecationInfo(since = "0.26.0", alternative = "#parse(InputStream, StreamOptions)")
    public static YAPIONObject parse(InputStream inputStream, InputStreamCharsets charset) {
        return new YAPIONParser(inputStream, new StreamOptions().charset(charset)).parse().result();
    }

    /**
     * Parses the InputStream to an YAPIONObject.
     * This method only parses the next YAPIONObject and tries to read
     * until the YAPIONObject is finished. It will cancel on EOF and
     * throw an {@link YAPIONParserException} if reached without
     * a finished {@link YAPIONObject}.
     *
     * @param inputStream the inputStream to parse
     * @param charset to use
     * @param commentParsing how comments should be parsed
     * @return YAPIONObject parsed out of the string
     */
    @Deprecated
    @DeprecationInfo(since = "0.26.0", alternative = "#parse(InputStream, StreamOptions)")
    public static YAPIONObject parse(InputStream inputStream, InputStreamCharsets charset, CommentParsing commentParsing) {
        return new YAPIONParser(inputStream, new StreamOptions().charset(charset).commentParsing(commentParsing)).parse().result();
    }

    /**
     * Parses the InputStream to an YAPIONObject.
     *
     * @param inputStream the inputStream to parse
     * @param stopOnStreamEnd stop on Stream end or not
     * @return YAPIONObject parsed out of the string
     */
    @Deprecated
    @DeprecationInfo(since = "0.26.0", alternative = "#parse(InputStream, StreamOptions)")
    public static YAPIONObject parse(InputStream inputStream, boolean stopOnStreamEnd) {
        return new YAPIONParser(inputStream, new StreamOptions().stopOnStreamEnd(stopOnStreamEnd)).parse().result();
    }

    /**
     * Parses the InputStream to an YAPIONObject.
     *
     * @param inputStream the inputStream to parse
     * @param stopOnStreamEnd stop on Stream end or not
     * @param commentParsing how comments should be parsed
     * @return YAPIONObject parsed out of the string
     */
    @Deprecated
    @DeprecationInfo(since = "0.26.0", alternative = "#parse(InputStream, StreamOptions)")
    public static YAPIONObject parse(InputStream inputStream, boolean stopOnStreamEnd, CommentParsing commentParsing) {
        return new YAPIONParser(inputStream, new StreamOptions().stopOnStreamEnd(stopOnStreamEnd).commentParsing(commentParsing)).parse().result();
    }

    /**
     * Parses the InputStream to an YAPIONObject.
     *
     * @param inputStream the inputStream to parse
     * @param stopOnStreamEnd stop on Stream end or not
     * @param charset to use
     * @return YAPIONObject parsed out of the string
     */
    @Deprecated
    @DeprecationInfo(since = "0.26.0", alternative = "#parse(InputStream, StreamOptions)")
    public static YAPIONObject parse(InputStream inputStream, boolean stopOnStreamEnd, InputStreamCharsets charset) {
        return new YAPIONParser(inputStream, new StreamOptions().stopOnStreamEnd(stopOnStreamEnd).charset(charset)).parse().result();
    }

    /**
     * Parses the InputStream to an YAPIONObject.
     *
     * @param inputStream the inputStream to parse
     * @param stopOnStreamEnd stop on Stream end or not
     * @param charset to use
     * @param commentParsing how comments should be parsed
     * @return YAPIONObject parsed out of the string
     */
    @Deprecated
    @DeprecationInfo(since = "0.26.0", alternative = "#parse(InputStream, StreamOptions)")
    public static YAPIONObject parse(InputStream inputStream, boolean stopOnStreamEnd, InputStreamCharsets charset, CommentParsing commentParsing) {
        return new YAPIONParser(inputStream, new StreamOptions().stopOnStreamEnd(stopOnStreamEnd).charset(charset)).parse().result();
    }

    /**
     * Parses the JSON String to an YAPIONObject.
     *
     * @param s the string to parse
     * @return YAPIONObject parsed out of the string
     */
    @Deprecated
    @DeprecationInfo(since = "0.26.0", alternative = "#parse(String)")
    public static YAPIONObject parseJSON(String s) {
        return YAPIONParser.parse(s);
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
    @Deprecated
    @DeprecationInfo(since = "0.26.0", alternative = "#mapJSON(YAPIONObject)")
    public static YAPIONObject mapJSON(String s) {
        return JSONMapper.map(parseJSON(s));
    }

    private final YAPIONInternalParser yapionInternalParser = new YAPIONInternalParser();
    private final CharReader charReader;
    private Runnable finishRunnable = () -> {};

    /**
     * Creates a YAPIONParser for parsing a string to an YAPIONObject.
     *
     * @param string to parse from
     * @param parseOptions the parse options
     */
    public YAPIONParser(@NonNull String string, ParseOptions parseOptions) {
        this(string.toCharArray(), parseOptions);
    }

    /**
     * Creates a YAPIONParser for parsing a StringBuilder to an YAPIONObject.
     *
     * @param stringBuilder to parse from
     */
    public YAPIONParser(@NonNull StringBuilder stringBuilder, ParseOptions parseOptions) {
        char[] chars = new char[stringBuilder.length()];
        stringBuilder.getChars(0, stringBuilder.length(), chars, 0);
        charReader = new CharReader() {
            private int index = 0;
            private int available = chars.length;

            @Override
            public char next() {
                return chars[index++];
            }

            @Override
            public boolean hasNext() {
                return index < available;
            }
        };
        setupInternalParser(parseOptions);
    }

    /**
     * Creates a YAPIONParser for parsing a byte array to an YAPIONObject.
     *
     * @param bytes to parse from
     */
    public YAPIONParser(@NonNull byte[] bytes) {
        this(bytes, new StreamOptions());
    }

    /**
     * Creates a YAPIONParser for parsing a byte array to an YAPIONObject.
     *
     * @param bytes to parse from
     * @param streamOptions the parse options
     */
    public YAPIONParser(@NonNull byte[] bytes, StreamOptions streamOptions) {
        this(new ByteArrayInputStream(bytes), streamOptions);
    }

    /**
     * Creates a YAPIONParser for parsing a char array to an YAPIONObject.
     *
     * @param chars to parse from
     */
    public YAPIONParser(@NonNull char[] chars) {
        this(chars, new ParseOptions());
    }

    /**
     * Creates a YAPIONParser for parsing a char array to an YAPIONObject.
     *
     * @param chars to parse from
     * @param parseOptions the parse options
     */
    public YAPIONParser(@NonNull char[] chars, ParseOptions parseOptions) {
        charReader = new CharReader() {
            private int index = 0;
            private int available = chars.length;

            @Override
            public char next() {
                return chars[index++];
            }

            @Override
            public boolean hasNext() {
                return index < available;
            }
        };
        setupInternalParser(parseOptions);
    }

    /**
     * Creates a YAPIONParser for parsing an InputStream to an YAPIONObject.
     *
     * @param inputStream to parse from
     * @param streamOptions the parse options
     */
    public YAPIONParser(@NonNull InputStream inputStream, StreamOptions streamOptions) {
        this(inputStream, streamOptions, false);
    }

    /**
     * Creates a YAPIONParser for parsing a file content to an YAPIONObject.
     *
     * @param file to parse from
     * @param fileOptions the parse options
     * @throws IOException by FileInputStream creation
     */
    public YAPIONParser(File file, FileOptions fileOptions) throws IOException {
        this(fileOptions.isGzipped()
                ? new GZIPInputStream(new BufferedInputStream(new FileInputStream(file)))
                : new BufferedInputStream(new FileInputStream(file)), fileOptions, true);
    }

    public YAPIONParser(@NonNull CharReader charReader, ParseOptions parseOptions) {
        this.charReader = charReader;
        setupInternalParser(parseOptions);
    }

    private YAPIONParser(InputStream inputStream, StreamOptions streamOptions, boolean closeAfterRead) {
        charReader = switch (streamOptions.getCharset()) {
            case US_ASCII -> new ASCIIInputStreamCharReader(inputStream, streamOptions.isStopOnStreamEnd());
            case LATIN_1, EXTENDED_US_ASCII -> new Latin1InputStreamCharReader(inputStream, streamOptions.isStopOnStreamEnd());
            case UTF_8 -> new UTF8InputStreamCharReader(inputStream, streamOptions.isStopOnStreamEnd());
        };
        if (closeAfterRead) {
            finishRunnable = () -> {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    log.warn(e.getMessage(), e);
                }
            };
        }
        setupInternalParser(streamOptions);
    }

    private void setupInternalParser(ParseOptions parseOptions) {
        if (parseOptions.isForceOnlyJSON()) {
            if (parseOptions.isForceOnlyYAPION()) {
                throw new YAPIONException("Can't force YAPION and JSON at the same time");
            }
            if (parseOptions.getCommentParsing() != CommentParsing.IGNORE) {
                throw new YAPIONException("Can't force JSON with comments at the same time");
            }
        }
        yapionInternalParser.comments = parseOptions.getCommentParsing();
        yapionInternalParser.lazy = parseOptions.isLazy();
        yapionInternalParser.forceOnlyJSON = parseOptions.isForceOnlyJSON();
        yapionInternalParser.forceOnlyYAPION = parseOptions.isForceOnlyYAPION();
        yapionInternalParser.parseCallbackMap = parseOptions.getParseCallbackMap();
        yapionInternalParser.disabledObject = parseOptions.getDisabledTypes().contains(YAPIONType.OBJECT);
        yapionInternalParser.disabledArray = parseOptions.getDisabledTypes().contains(YAPIONType.ARRAY);
        yapionInternalParser.disabledMap = parseOptions.getDisabledTypes().contains(YAPIONType.MAP);
        yapionInternalParser.disabledValue = parseOptions.getDisabledTypes().contains(YAPIONType.VALUE);
        yapionInternalParser.disabledPointer = parseOptions.getDisabledTypes().contains(YAPIONType.POINTER);
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
    @SuppressWarnings({"java:S1141"})
    public YAPIONParser parse() {
        try {
            log.debug("parse    [init]");
            while (charReader.hasNext() && !yapionInternalParser.isFinished()) {
                try {
                    yapionInternalParser.advance(charReader.next());
                } catch (ParserSkipException e) {
                    // Ignored
                }
            }
            finishRunnable.run();
            log.debug("parse    [finished]");
        } catch (YAPIONParserException e) {
            log.debug("parse    [YAPIONParserException]");
            throw wrapException(e);
        }
        return this;
    }

    /**
     * Returns the YAPIONObject parsed by {@code parse()}
     *
     * @return the YAPIONObject
     */
    public YAPIONObject result() {
        try {
            return yapionInternalParser.finish();
        } catch (YAPIONParserException e) {
            log.debug("parse    [YAPIONParserException]");
            throw wrapException(e);
        }
    }

    private YAPIONParserException wrapException(YAPIONException e) {
        if (e.getMessage() != null) {
            return new YAPIONParserException(e.getMessage() + "\n" + generateErrorMessage(), e);
        } else {
            return new YAPIONParserException(generateErrorMessage(), e);
        }
    }

    /**
     * Returns the YAPIONObject parsed by {@code parse()} and unwraps it when it only contains one variable with an empty key.
     *
     * @return the YAPIONObject
     */
    public YAPIONObject resultObject() {
        YAPIONObject yapionObject = result();
        if (yapionInternalParser.initialType != YAPIONType.OBJECT) {
            throw new YAPIONException("The parsed YAPIONObject is more than an just one YAPIONObject");
        }
        if (yapionObject.size() == 1 && yapionObject.containsKey("")) {
            return yapionObject.getObject("");
        }
        return yapionObject;
    }

    /**
     * Returns the YAPIONObject parsed by {@code parse()} and unwraps it to an YAPIONArray when it only contains one variable with an empty key.
     *
     * @return the YAPIONArray
     */
    public YAPIONArray resultArray() {
        YAPIONObject yapionObject = result();
        if (yapionInternalParser.initialType != YAPIONType.ARRAY) {
            throw new YAPIONException("The parsed YAPIONObject is more than an just one YAPIONArray");
        }
        return yapionObject.getArray("");
    }

    /**
     * Returns the YAPIONObject parsed by {@code parse()} and unwraps it to an YAPIONMap when it only contains one variable with an empty key.
     *
     * @return the YAPIONMap
     */
    public YAPIONMap resultMap() {
        YAPIONObject yapionObject = result();
        if (yapionInternalParser.initialType != YAPIONType.MAP) {
            throw new YAPIONException("The parsed YAPIONObject is more than an just one YAPIONMap");
        }
        return yapionObject.getMap("");
    }

    /**
     * Returns how many characters have been read.
     *
     * @return the number of characters read
     */
    public long getReadCharCount() {
        return yapionInternalParser.count();
    }

    private String generateErrorMessage() {
        StringBuilder st = new StringBuilder();
        st.append("In line ").append(yapionInternalParser.line()).append(" at column ").append(yapionInternalParser.column()).append(" after ").append(yapionInternalParser.count()).append(" reads.");
        if (!yapionInternalParser.getKey().isEmpty()) {
            st.append("   Last read key: '").append(yapionInternalParser.getKey()).append("'");
        }
        if (yapionInternalParser.getCurrent().length() != 0) {
            st.append("   Currently reading: '").append(yapionInternalParser.getCurrent()).append("'");
        }
        if (yapionInternalParser.getLastReadChar() != '\0') {
            st.append("   Current char: '").append(yapionInternalParser.getLastReadChar()).append("'");
        }
        if (yapionInternalParser.getUnicode() != null) {
            st.append("   Currently reading unicode: '").append(yapionInternalParser.getUnicode()).append("'");
        }
        st.append("   Type Stack: ").append(yapionInternalParser.getTypeStack());
        return st.toString();
    }

    public static class ParserSkipException extends RuntimeException {
    }

    /**
     * Converts the input to a YAPIONObject. It can only convert from a Map to a
     * YAPIONObject, from a List to an YAPIONArray and everything else will be converted
     * to a YAPIONValue if applicable.
     *
     * @param map the map to convert
     * @return the YAPIONObject
     */
    public static YAPIONObject fromMap(Map<String, Object> map) {
        YAPIONObject result = new YAPIONObject();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            result.add(entry.getKey(), convertSingle(entry.getValue()));
        }
        return result;
    }

    /**
     * Converts the input to a YAPIONArray. It can only convert from a Map to a
     * YAPIONObject, from a List to an YAPIONArray and everything else will be converted
     * to a YAPIONValue if applicable.
     *
     * @param list the list to convert
     * @return the YAPIONArray
     */
    public static YAPIONArray fromList(List<Object> list) {
        YAPIONArray result = new YAPIONArray();
        for (Object object : list) {
            result.add(convertSingle(object));
        }
        return result;
    }

    private static YAPIONAnyType convertSingle(Object value) {
        if (value instanceof Map toConvert) {
            return fromMap(toConvert);
        } else if (value instanceof List toConvert) {
            return fromList(toConvert);
        } else {
            return new YAPIONValue<>(value);
        }
    }
}
