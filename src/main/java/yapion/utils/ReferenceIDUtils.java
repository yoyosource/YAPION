// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020 yoyosource

package yapion.utils;

import yapion.annotations.deserialize.YAPIONLoadExclude;
import yapion.annotations.serialize.YAPIONSaveExclude;
import yapion.exceptions.YAPIONException;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedHashMap;
import java.util.Map;

@YAPIONSaveExclude(context = "*")
@YAPIONLoadExclude(context = "*")
public class ReferenceIDUtils {

    private ReferenceIDUtils() {
        throw new IllegalStateException("Utility class");
    }

    private static int cacheSize = 100;

    private static final Map<String, Long> referenceIDMap = new LinkedHashMap<String, Long>() {
        @Override
        protected boolean removeEldestEntry(Map.Entry<String, Long> eldest) {
            return size() > cacheSize;
        }
    };

    /**
     * Calculates the reference ID of a given String, primarily used for variable names.
     * This method caches the last 100 inputs for faster reference ID calculation.
     * Use {@link #discardCache()} to discard this Cache.
     *
     * @param s the input string to calculate a reference ID from
     * @return the reference ID of the given String
     */
    public static long calc(String s) {
        if (referenceIDMap.containsKey(s)) {
            return referenceIDMap.get(s);
        }
        try {
            // TODO: Replace this by some other referenceID system before 1.0.0
            MessageDigest digest = MessageDigest.getInstance("MD5");
            byte[] bytes = digest.digest(s.getBytes(StandardCharsets.UTF_8));
            long l = (long) bytes[0] << 56 | (long) bytes[1] << 48 | (long) bytes[2] << 40 | (long) bytes[3] << 32 | (long) bytes[4] << 24 | (long) bytes[5] << 16 | (long) bytes[6] << 8 | (long) bytes[7];
            referenceIDMap.put(s, l);
            return l;
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
     * Discard the cache used by {@link #calc(String)}
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

}