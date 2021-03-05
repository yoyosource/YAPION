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

package yapion;

import yapion.exceptions.YAPIONException;
import yapion.hierarchy.api.groups.YAPIONAnyType;
import yapion.hierarchy.output.StringOutput;
import yapion.hierarchy.types.YAPIONObject;
import yapion.hierarchy.types.YAPIONPointer;
import yapion.packet.YAPIONInputStream;
import yapion.parser.YAPIONParser;
import yapion.serializing.YAPIONDeserializer;
import yapion.serializing.YAPIONSerializer;
import yapion.utils.MethodReturnValue;
import yapion.utils.ReflectionsUtils;
import yapion.utils.YAPIONTreeIterator;
import yapion.utils.YAPIONTreeIterator.YAPIONTreeIteratorOption;

import java.io.InputStream;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
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
         * check if any exception gets thrown.
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
     * Check if a generic String can be a YAPION Object serialized by the
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
     * Check if a generic String has YAPIONObjects embedded which can be deserialized
     * by the {@link YAPIONDeserializer}.
     *
     * @param string the string to check
     * @return the result if the String contains an {@link YAPIONObject}
     */
    public static CheckResult stringContainsYAPIONObject(String string) {
        for (int i = 0; i < string.length(); i++) {
            char c = string.charAt(i);
            if (c == '{') {
                try {
                    YAPIONParser.parse(string.substring(i));
                    return CheckResult.IS;
                } catch (YAPIONException e) {
                    // Ignored
                }
            }
        }
        return CheckResult.NOT;
    }

    /**
     * Retrieve any {@link YAPIONObject} embedded in an generic String.
     *
     * @param string the string to extract from
     * @return all {@link YAPIONObject}'s that got extracted
     */
    public static List<YAPIONObject> stringGetAllYAPIONObjects(String string) {
        List<YAPIONObject> yapionObjects = new ArrayList<>();
        int i = 0;
        while (i < string.length()) {
            char c = string.charAt(i);
            if (c == '{') {
                try {
                    YAPIONObject yapionObject = YAPIONParser.parse(string.substring(i));
                    i += yapionObject.toYAPION(new StringOutput()).length();
                    yapionObjects.add(yapionObject);
                } catch (YAPIONException e) {
                    // Ignored
                }
            }
            i++;
        }
        return yapionObjects;
    }

    /**
     * The inputted {@link YAPIONObject} is traversed depth first, values beforehand.
     * Outputs an {@link Stream} of {@link YAPIONAnyType} of all keys
     * in the whole {@link YAPIONObject}. This method is inspired by {@link Files#walk(Path, FileVisitOption...)}
     *
     * @param yapionObject the {@link YAPIONObject} to traverse
     * @param option the {@link YAPIONTreeIteratorOption} to use
     * @return the {@link Stream} which contains all {@link YAPIONAnyType} of the {@link YAPIONObject}
     */
    @SuppressWarnings({"java:S1181"})
    public static Stream<YAPIONAnyType> walk(YAPIONObject yapionObject, YAPIONTreeIteratorOption option) {
        YAPIONTreeIterator iterator = new YAPIONTreeIterator(yapionObject, option);
        try {
            Spliterator<YAPIONAnyType> spliterator = Spliterators.spliteratorUnknownSize(iterator, Spliterator.DISTINCT);
            return StreamSupport.stream(spliterator, false).onClose(iterator::close);
        } catch (Error | RuntimeException e) {
            iterator.close();
            throw e;
        }
    }

    /**
     * The inputted {@link YAPIONObject} is traversed depth first, values beforehand.
     * Outputs an {@link Stream} of {@link YAPIONAnyType} of all keys
     * in the whole {@link YAPIONObject}. This method is inspired by {@link Files#walk(Path, FileVisitOption...)}
     *
     * @param yapionObject to traverse
     * @param classToWalk the class of given type
     * @param <T> the type to walk
     * @return the {@link Stream} which contains all 'T' of the {@link YAPIONObject}
     */
    @SuppressWarnings({"java:S1181"})
    public static <T extends YAPIONAnyType> Stream<T> walk(YAPIONObject yapionObject, Class<T> classToWalk) {
        YAPIONTreeIterator iterator = new YAPIONTreeIterator(yapionObject, YAPIONTreeIteratorOption.TRAVERSE_ALL);
        try {
            Spliterator<YAPIONAnyType> spliterator = Spliterators.spliteratorUnknownSize(iterator, Spliterator.DISTINCT);
            return StreamSupport.stream(spliterator, false).filter(classToWalk::isInstance).map(classToWalk::cast).onClose(iterator::close);
        } catch (Error | RuntimeException e) {
            iterator.close();
            throw e;
        }
    }

    /**
     * The inputted {@link YAPIONObject} is traversed depth first, values beforehand.
     * Outputs an {@link Stream} of {@link YAPIONAnyType} of all keys
     * in the whole {@link YAPIONObject}. This method is inspired by {@link Files#walk(Path, FileVisitOption...)}
     *
     * @param yapionObject the {@link YAPIONObject} to traverse
     * @return the {@link Stream} which contains all {@link YAPIONAnyType} of the {@link YAPIONObject}
     */
    public static Stream<YAPIONAnyType> walk(YAPIONObject yapionObject) {
        return walk(yapionObject, YAPIONTreeIteratorOption.TRAVERSE_ALL);
    }

    /**
     * The inputted {@link YAPIONObject} is flattened to one {@link YAPIONObject} as the
     * main layer. Every value will be put under this and every structure will be removed.
     * This will also remove any information about maps arrays and such.
     *
     * <br><br>(@pointer:...) will represent a pointer
     *
     * @param yapionObject the {@link YAPIONObject} to flatten
     * @return the flattened {@link YAPIONObject} this is a new instance
     */
    public static YAPIONObject flatten(YAPIONObject yapionObject) {
        YAPIONObject output = new YAPIONObject();
        walk(yapionObject, YAPIONTreeIteratorOption.TRAVERSE_VALUE_TYPES).forEach(s -> {
            String path = s.getPath().join(".");
            if (s instanceof YAPIONPointer) {
                MethodReturnValue<Object> objectOptional = ReflectionsUtils.invokeMethod("getYAPIONObject", s);
                if (!objectOptional.isPresent()) return;
                output.add(path, "@pointer:" + String.join(",", ((YAPIONObject) objectOptional.get()).getPath().getPath()));
            } else {
                output.add(path, s.copy());
            }
        });
        return output;
    }

    public static YAPIONObject unflatten(YAPIONObject yapionObject) {
        YAPIONObject output = new YAPIONObject();
        walk(yapionObject, YAPIONTreeIteratorOption.TRAVERSE_VALUE_TYPES).forEach(s -> {
            String[] path = s.getPath().join().split("\\.");
            YAPIONObject current = output;
            for (int i = 0; i < path.length - 1; i++) {
                String p = path[i];
                if (current.getYAPIONAnyType(p) == null) {
                    current.add(p, new YAPIONObject());
                }
                current = current.getObject(p);
            }
            current.add(path[path.length - 1], s);
        });
        return output;
    }

}
