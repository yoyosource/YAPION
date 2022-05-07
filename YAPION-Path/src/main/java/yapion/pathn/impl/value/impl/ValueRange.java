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

package yapion.pathn.impl.value.impl;

import yapion.hierarchy.api.groups.YAPIONAnyType;
import yapion.hierarchy.types.YAPIONValue;
import yapion.pathn.PathContext;
import yapion.pathn.PathElement;
import yapion.pathn.impl.value.ValueElement;

import java.util.Optional;

public class ValueRange<T extends Number & Comparable<T>> implements ValueElement {

    public static <T extends Number & Comparable<T>> ValueRange<T> min(T min) {
        return new ValueRange<>(min, null);
    }

    public static <T extends Number & Comparable<T>> ValueRange<T> max(T max) {
        return new ValueRange<>(null, max);
    }

    public static <T extends Number & Comparable<T>> ValueRange<T> range(T min, T max) {
        return new ValueRange<>(min, max);
    }

    private T min;
    private T max;

    private ValueRange(T min, T max) {
        this.min = min;
        this.max = max;
    }

    @Override
    public boolean check(YAPIONAnyType yapionAnyType) {
        if (!(yapionAnyType instanceof YAPIONValue yapionValue)) {
            return true;
        }
        Object value = yapionValue.get();
        if (!(value instanceof Number number)) {
            return false;
        }
        T numberValue;
        try {
            numberValue = (T) number;
        } catch (ClassCastException e) {
            return false;
        }
        if (min != null && numberValue.compareTo(min) < 0) {
            return false;
        }
        if (max != null && numberValue.compareTo(max) > 0) {
            return false;
        }
        return true;
    }

    @Override
    public PathContext apply(PathContext pathContext, Optional<PathElement> possibleNextPathElement) {
        return pathContext.retainIf(this::check);
    }
}
