// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020 yoyosource

package yapion;

import yapion.hierarchy.typegroups.YAPIONAnyType;
import yapion.hierarchy.types.YAPIONObject;
import yapion.packet.YAPIONInputStream;
import yapion.parser.YAPIONParser;
import yapion.serializing.YAPIONDeserializer;
import yapion.serializing.YAPIONSerializer;
import yapion.utils.YAPIONObjectIterator;

import java.io.InputStream;
import java.nio.file.Files;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class YAPIONUtils {

    private YAPIONUtils() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * This is the default file ending associated by a file with the format
     * yapion, For more information see {@link YAPIONParser} or
     * {@link YAPIONInputStream}.
     */
    public static final String FILE_ENDING = "yapion";
    /**
     * This is the default file ending associated by a file with the format
     * yapion, For more information see {@link YAPIONParser} or
     * {@link YAPIONInputStream}.
     */
    public static final String FILE_ENDING_POINT = ".yapion";

    public enum CheckResult {
        /**
         * If either {@link #isBase64YAPIONObject(String)} or
         * {@link #isStringYAPIONObject(String)} return this
         * a string can definitely not be a YAPION object.
         * This does not say anything about the string containing
         * a YAPION object.
         */
        NOT,
        /**
         * If either {@link #isBase64YAPIONObject(String)} or
         * {@link #isStringYAPIONObject(String)} return this
         * a string can be a YAPION object. As this is not
         * likely you should not count on this. If you want
         * to be sure use {@link YAPIONParser#parse(String)}
         * or {@link YAPIONParser#parse(InputStream)} and
         * check if any exception get thrown.
         * This does not say anything about the string containing
         * a YAPION object.
         */
        PROBABLE,
        /**
         * If either {@link #isBase64YAPIONObject(String)} or
         * {@link #isStringYAPIONObject(String)} return this
         * a string can be a YAPION object. As this is likely
         * you can use {@link YAPIONParser#parse(String)} or
         * {@link YAPIONParser#parse(InputStream)} to try
         * parsing this String.
         * This does not say anything about the string containing
         * a YAPION object.
         */
        IS
    }

    /**
     * Check if a base64 encoded String can be a YAPION Object serialized
     * by the {@link YAPIONSerializer} that can be deserialized by the
     * {@link YAPIONDeserializer}.
     *
     * @param base64 the base64 string to check
     * @return the estimate if the string is yapion or not
     */
    public static CheckResult isBase64YAPIONObject(String base64) {
        if (base64.length() < 7) {
            return "e0B0eXBlK".startsWith(base64) ? CheckResult.PROBABLE : CheckResult.NOT;
        }
        return base64.startsWith("e0B0eXBlK") ? CheckResult.IS : CheckResult.NOT;
    }

    /**
     * Check ist a generic String can be a YAPION Object serialozed by the
     * {@link YAPIONSerializer} that can be deserialized by the {@link YAPIONDeserializer}.
     *
     * @param string the string to check
     * @return the estimate if the string is yapion or not
     */
    public static CheckResult isStringYAPIONObject(String string) {
        if (string.startsWith("{@type(")) return CheckResult.IS;
        if (string.length() < 8 && "{@type(".startsWith(string)) {
            return CheckResult.PROBABLE;
        }
        return isBase64YAPIONObject(string);
    }

    /**
     * The inputted {@link YAPIONObject} is traversed layer by layer.
     * Outputs an {@link Stream} of {@link YAPIONAnyType} of all keys
     * in the whole {@link YAPIONObject}.
     *
     * @param yapionObject the {@link YAPIONObject} to traverse
     * @return the {@link Stream} which contains all {@link YAPIONAnyType} of the {@link YAPIONObject}
     */
    public static Stream<YAPIONAnyType> walk(YAPIONObject yapionObject) {
        YAPIONObjectIterator iterator = new YAPIONObjectIterator(yapionObject);
        try {
            Spliterator<YAPIONAnyType> spliterator = Spliterators.spliteratorUnknownSize(iterator, Spliterator.DISTINCT);
            return StreamSupport.stream(spliterator, false)
                    .onClose(iterator::close);
        } catch (Error | RuntimeException e) {
            iterator.close();
            throw e;
        }
    }

}