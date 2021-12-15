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

package yapion.config.annotationproccessing;

import lombok.experimental.UtilityClass;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.function.Predicate;

@UtilityClass
public class ConstraintUtils {

    public static <T> boolean oneOf(T what, T... elements) {
        for (T t : elements) {
            if (t.equals(what)) {
                return true;
            }
        }
        return false;
    }

    public static boolean oneOfIgnoreCase(String what, String... elements) {
        for (String t : elements) {
            if (t.equalsIgnoreCase(what)) {
                return true;
            }
        }
        return false;
    }

    public static boolean oneOfContains(String what, String... elements) {
        for (String t : elements) {
            if (t.contains(what)) {
                return true;
            }
        }
        return false;
    }

    public static boolean oneOfContainsIgnoreCase(String what, String... elements) {
        what = what.toLowerCase();
        for (String t : elements) {
            if (t.toLowerCase().contains(what)) {
                return true;
            }
        }
        return false;
    }

    public static <T> boolean noneOf(T what, T... elements) {
        return !oneOf(what, elements);
    }

    public static boolean noneOfIgnoreCase(String what, String... elements) {
        return !oneOfIgnoreCase(what, elements);
    }

    public static boolean noneOfContains(String what, String... elements) {
        return !oneOfContains(what, elements);
    }

    public static boolean noneOfContainsIgnoreCase(String what, String... elements) {
        return !oneOfContainsIgnoreCase(what, elements);
    }

    public static <T> T or(T what, T alternative) {
        if (what == null) return alternative;
        return what;
    }

    public static <T> T or(T what, T alternative, Predicate<T> test) {
        if (!test.test(what)) return alternative;
        return what;
    }

    public static <T> boolean nonNull(T what) {
        return what != null;
    }

    public static <T> boolean isNull(T what) {
        return what == null;
    }

    public static <T extends Number> boolean between(T what, T min, T max) {
        if (what instanceof Byte checkedWhat && min instanceof Byte checkedMin && max instanceof Byte checkedMax) {
            return checkedWhat >= checkedMin && checkedWhat < checkedMax;
        }
        if (what instanceof Integer checkedWhat && min instanceof Integer checkedMin && max instanceof Integer checkedMax) {
            return checkedWhat >= checkedMin && checkedWhat < checkedMax;
        }
        if (what instanceof Long checkedWhat && min instanceof Long checkedMin && max instanceof Long checkedMax) {
            return checkedWhat >= checkedMin && checkedWhat < checkedMax;
        }
        if (what instanceof Float checkedWhat && min instanceof Float checkedMin && max instanceof Float checkedMax) {
            return checkedWhat >= checkedMin && checkedWhat < checkedMax;
        }
        if (what instanceof Double checkedWhat && min instanceof Double checkedMin && max instanceof Double checkedMax) {
            return checkedWhat >= checkedMin && checkedWhat < checkedMax;
        }
        if (what instanceof BigInteger checkedWhat && min instanceof BigInteger checkedMin && max instanceof BigInteger checkedMax) {
            return checkedWhat.compareTo(checkedMin) >= 0 && checkedWhat.compareTo(checkedMax) < 0;
        }
        if (what instanceof BigDecimal checkedWhat && min instanceof BigDecimal checkedMin && max instanceof BigDecimal checkedMax) {
            return checkedWhat.compareTo(checkedMin) >= 0 && checkedWhat.compareTo(checkedMax) < 0;
        }
        return false;
    }

    public static boolean between(char what, char min, char max) {
        return what >= min && what < max;
    }

    public static <T extends Number> boolean greater(T what, T min) {
        if (what instanceof Byte checkedWhat && min instanceof Byte checkedMin) {
            return checkedWhat > checkedMin;
        }
        if (what instanceof Integer checkedWhat && min instanceof Integer checkedMin) {
            return checkedWhat > checkedMin;
        }
        if (what instanceof Long checkedWhat && min instanceof Long checkedMin) {
            return checkedWhat > checkedMin;
        }
        if (what instanceof Float checkedWhat && min instanceof Float checkedMin) {
            return checkedWhat > checkedMin;
        }
        if (what instanceof Double checkedWhat && min instanceof Double checkedMin) {
            return checkedWhat > checkedMin;
        }
        if (what instanceof BigInteger checkedWhat && min instanceof BigInteger checkedMin) {
            return checkedWhat.compareTo(checkedMin) > 0;
        }
        if (what instanceof BigDecimal checkedWhat && min instanceof BigDecimal checkedMin) {
            return checkedWhat.compareTo(checkedMin) > 0;
        }
        return false;
    }

    public static boolean greater(char what, char min) {
        return what > min;
    }

    public static <T extends Number> boolean less(T what, T max) {
        if (what instanceof Byte checkedWhat && max instanceof Byte checkedMin) {
            return checkedWhat < checkedMin;
        }
        if (what instanceof Integer checkedWhat && max instanceof Integer checkedMin) {
            return checkedWhat < checkedMin;
        }
        if (what instanceof Long checkedWhat && max instanceof Long checkedMin) {
            return checkedWhat < checkedMin;
        }
        if (what instanceof Float checkedWhat && max instanceof Float checkedMin) {
            return checkedWhat < checkedMin;
        }
        if (what instanceof Double checkedWhat && max instanceof Double checkedMin) {
            return checkedWhat < checkedMin;
        }
        if (what instanceof BigInteger checkedWhat && max instanceof BigInteger checkedMin) {
            return checkedWhat.compareTo(checkedMin) < 0;
        }
        if (what instanceof BigDecimal checkedWhat && max instanceof BigDecimal checkedMin) {
            return checkedWhat.compareTo(checkedMin) < 0;
        }
        return false;
    }

    public static boolean less(char what, char max) {
        return what < max;
    }

    public static boolean containsIgnoreCase(String what, String toContain) {
        return what.toLowerCase().contains(toContain.toLowerCase());
    }
}
