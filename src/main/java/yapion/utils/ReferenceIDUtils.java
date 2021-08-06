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

import lombok.experimental.UtilityClass;
import yapion.hierarchy.types.YAPIONValue;

import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@UtilityClass
public class ReferenceIDUtils {

    private static final Map<String, Long> referenceIDMap = new LinkedHashMap<String, Long>() {
        @Override
        protected boolean removeEldestEntry(Map.Entry<String, Long> eldest) {
            return size() > 256;
        }
    };

    /**
     * Calculates the reference ID of a given String, primarily used for variable names.
     * This method caches the last 256 inputs for faster reference ID calculation.
     * Use {@link #discardCache()} to discard this Cache.
     */
    public static final ReferenceFunction REFERENCE_FUNCTION = new ReferenceFunction(ReferenceIDUtils::reference);

    private static long reference(String s) {
        if (referenceIDMap.containsKey(s)) {
            return referenceIDMap.get(s);
        }
        long l = 0x7D4FA32E5D92B68AL;
        l = applyToBytes(s.length(), l);
        l = applyToBytes(s.hashCode(), l);
        byte[] bytes = s.getBytes(StandardCharsets.UTF_8);
        for (int i = 0; i < s.length(); i++) {
            int value = bytes[i] & 0xFF;
            l += value;
            l = applyToBytes(value, l);
        }
        l &= 0x7FFFFFFFFFFFFFFFL;
        referenceIDMap.put(s, l);
        return l;
    }

    private static long applyToBytes(int toApply, long current) {
        int shiftBy = (int) ((current) % 8);
        toApply = (toApply >>> shiftBy | toApply << (8 - shiftBy)) & 0b11111111;

        for (int temp = 0; temp < 8; temp++) {
            current ^= (long) toApply << (long) (temp * 8);
        }
        return current;
    }

    /**
     * Discard the cache used by {@link #reference(String)}
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
