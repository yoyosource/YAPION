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

package yapion.utils;

import yapion.annotations.DeprecationInfo;
import yapion.annotations.deserialize.YAPIONLoadExclude;
import yapion.annotations.serialize.YAPIONSaveExclude;
import yapion.exceptions.YAPIONException;
import yapion.hierarchy.types.YAPIONValue;

import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@YAPIONSaveExclude(context = "*")
@YAPIONLoadExclude(context = "*")
public class ReferenceIDUtils {

    private ReferenceIDUtils() {
        throw new IllegalStateException("Utility class");
    }

    private static int cacheSize = 100;

    private static final Map<String, Long> referenceIDMapCache = new HashMap<>();
    private static final Map<String, Long> referenceIDMap = new LinkedHashMap<String, Long>() {
        @Override
        protected boolean removeEldestEntry(Map.Entry<String, Long> eldest) {
            return size() > cacheSize;
        }
    };

    private static final Map<String, Long> referenceIDOldMapCache = new HashMap<>();
    private static final Map<String, Long> referenceIDOldMap = new LinkedHashMap<String, Long>() {
        @Override
        protected boolean removeEldestEntry(Map.Entry<String, Long> eldest) {
            return size() > cacheSize;
        }
    };

    static {
        initCache();
    }

    /**
     * Calculates the reference ID of a given String, primarily used for variable names.
     * This method caches the last 100 inputs for faster reference ID calculation.
     * Use {@link #discardCache()} to discard this Cache.
     */
    public static final ReferenceFunction REFERENCE_FUNCTION = new ReferenceFunction(ReferenceIDUtils::reference);

    /**
     * Calculates the reference ID of a given String, primarily used for variable names.
     * This method caches the last 100 inputs for faster reference ID calculation.
     * Use {@link #discardCache()} to discard this Cache.
     *
     * @deprecated since 0.23.0
     */
    @Deprecated
    public static final ReferenceFunction REFERENCE_FUNCTION_OLD = new ReferenceFunction(ReferenceIDUtils::referenceOld);

    private static long reference(String s) {
        if (referenceIDMap.containsKey(s)) {
            return referenceIDMap.get(s);
        }
        if (referenceIDMapCache.containsKey(s)) {
            return referenceIDMapCache.get(s);
        }
        long l = 0x7D4FA32E5D92B68AL;
        for (int i = 0; i < 8; i++) {
            l ^= s.length() << (i * 8);
        }
        byte[] bytes = s.getBytes(StandardCharsets.UTF_16BE);
        for (int i = 0; i < s.length(); i++) {
            byte b = bytes[i];
            for (int temp = 0; temp < 8; temp++) {
                l ^= (long) b << (long) (temp * 8);
            }
        }
        l &= 0x7FFFFFFFFFFFFFFFL;
        referenceIDMap.put(s, l);
        return l;
    }

    @Deprecated
    @DeprecationInfo(since = "0.23.0")
    private static long referenceOld(String s) {
        if (referenceIDOldMap.containsKey(s)) {
            return referenceIDOldMap.get(s);
        }
        if (referenceIDOldMapCache.containsKey(s)) {
            return referenceIDOldMapCache.get(s);
        }
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            byte[] bytes = digest.digest(s.getBytes(StandardCharsets.UTF_8));
            long value = (long) bytes[0] << 56 | (long) bytes[1] << 48 | (long) bytes[2] << 40 | (long) bytes[3] << 32 | (long) bytes[4] << 24 | (long) bytes[5] << 16 | (long) bytes[6] << 8 | (long) bytes[7];
            referenceIDOldMap.put(s, value);
            return value;
        } catch (NoSuchAlgorithmException e) {
            throw new YAPIONException("MD5 is not supported");
        }
    }

    /**
     * Set the cache size of the internal cache to a specific
     * number above 100. If you set a number below 100 it will
     * default to 100.
     *
     * @param cacheSize the cache Size
     */
    public static void setCacheSize(int cacheSize) {
        if (cacheSize < 100) {
            cacheSize = 100;
        }
        ReferenceIDUtils.cacheSize = cacheSize;
    }

    /**
     * Discard the cache used by {@link #referenceOld(String)}
     */
    public static void discardCache() {
        referenceIDMap.clear();
    }

    /**
     * Formats the input to the standard reference ID structure.
     *
     * @param l the input to format
     * @return the formatted input
     */
    public static String format(long l) {
        return String.format("%016X", l);
    }

    @SuppressWarnings({"java:S3011"})
    private static void initCache() {
        try {
            Field field = YAPIONValue.class.getDeclaredField("allowedTypes");
            field.setAccessible(true);
            for (String s : ((String[]) field.get(null))) {
                referenceIDMapCache.put(s, reference(s));
                referenceIDOldMapCache.put(s, referenceOld(s));
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            // Ignored
        }

        Field[] fields = IdentifierUtils.class.getDeclaredFields();
        for (Field field : fields) {
            try {
                String s = (String) field.get(null);
                referenceIDMapCache.put(s, reference(s));
                referenceIDOldMapCache.put(s, referenceOld(s));
            } catch (IllegalAccessException e) {
                // Ignored
            }
        }
    }

}
