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

package yapion.path.elements;

import yapion.hierarchy.api.groups.YAPIONAnyType;
import yapion.hierarchy.types.YAPIONArray;
import yapion.hierarchy.types.YAPIONObject;
import yapion.hierarchy.types.YAPIONValue;
import yapion.path.PathContext;
import yapion.path.PathElement;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class Range<T extends Number & Comparable<T>> implements PathElement, Predicate<YAPIONAnyType> {

    public static Range<Integer> min(int min) {
        return new Range<>(min, null, integer -> integer >= min);
    }

    public static Range<Integer> max(int max) {
        return new Range<>(null, max, integer -> integer <= max);
    }

    public static Range<Integer> range(int min, int max) {
        return new Range<>(min, max, integer -> integer >= min && integer <= max);
    }

    public static <T extends Number & Comparable<T>> Range<T> min(T min) {
        return new Range<>(min, null, t -> t.compareTo(min) >= 0);
    }

    public static <T extends Number & Comparable<T>> Range<T> max(T max) {
        return new Range<>(null, max, t -> t.compareTo(max) <= 0);
    }

    public static <T extends Number & Comparable<T>> Range<T> range(T min, T max) {
        return new Range<>(min, max, t -> t.compareTo(min) >= 0 && t.compareTo(max) <= 0);
    }

    private final T minValue;
    private final T maxValue;
    private final Predicate<T> predicate;

    private Range(T minValue, T maxValue, Predicate<T> predicate) {
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.predicate = predicate;
    }

    public Range(Predicate<T> predicate) {
        this.minValue = null;
        this.maxValue = null;
        this.predicate = predicate;
    }

    @Override
    public boolean check(YAPIONAnyType current) {
        if (current instanceof YAPIONArray yapionArray) {
            try {
                return predicate.test((T) (Integer) yapionArray.size());
            } catch (Exception e) {
                return false;
            }
        }
        return false;
    }

    @Override
    public PathContext apply(PathContext pathContext) {
        if (minValue == null && maxValue == null) {
            throw new IllegalArgumentException("min and max can't be null");
        }
        List<YAPIONAnyType> result = new ArrayList<>();
        int elementMin = Math.max(0, minValue != null ? minValue.intValue() : Integer.MIN_VALUE);
        for (YAPIONAnyType element : pathContext.getCurrent()) {
            if (element instanceof YAPIONArray yapionArray) {
                int elementMax = Math.min(yapionArray.size(), maxValue != null ? maxValue.intValue() : Integer.MAX_VALUE);
                for (int i = elementMin; i < elementMax; i++) {
                    result.add(yapionArray.getAny(i));
                }
            }
        }
        pathContext.setCurrent(result);
        return pathContext;
    }

    @Override
    public boolean test(YAPIONAnyType element) {
        try {
            if (element instanceof YAPIONObject yapionObject) {
                return predicate.test((T) (Integer) yapionObject.getKeys().size());
            } else if (element instanceof YAPIONArray yapionArray) {
                return predicate.test((T) (Integer) yapionArray.length());
            } else if (element instanceof YAPIONValue yapionValue) {
                Object object = yapionValue.get();
                if (object instanceof String value) {
                    return predicate.test((T) (Integer) value.length());
                } else {
                    return predicate.test((T) object);
                }
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }
}
