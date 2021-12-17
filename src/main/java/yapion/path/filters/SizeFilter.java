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

package yapion.path.filters;

import yapion.hierarchy.api.groups.YAPIONAnyType;
import yapion.hierarchy.types.YAPIONArray;
import yapion.hierarchy.types.YAPIONObject;
import yapion.hierarchy.types.YAPIONValue;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.function.Predicate;

public abstract class SizeFilter<T extends Number & Comparable<T>> implements Predicate<YAPIONAnyType> {

    private Predicate<T> sizeCheck;

    protected SizeFilter(Predicate<T> sizeCheck) {
        this.sizeCheck = sizeCheck;
    }

    @Override
    public boolean test(YAPIONAnyType element) {
        try {
            if (element instanceof YAPIONObject yapionObject) {
                return sizeCheck.test((T) (Integer) yapionObject.getKeys().size());
            } else if (element instanceof YAPIONArray yapionArray) {
                return sizeCheck.test((T) (Integer) yapionArray.length());
            } else if (element instanceof YAPIONValue yapionValue) {
                Object object = yapionValue.get();
                if (object instanceof String value) {
                    return sizeCheck.test((T) (Integer) value.length());
                } else if (object instanceof Byte value) {
                    return sizeCheck.test((T) value);
                } else if (object instanceof Short value) {
                    return sizeCheck.test((T) value);
                } else if (object instanceof Integer value) {
                    return sizeCheck.test((T) value);
                } else if (object instanceof Long value) {
                    return sizeCheck.test((T) value);
                } else if (object instanceof BigInteger value) {
                    return sizeCheck.test((T) value);
                } else if (object instanceof Float value) {
                    return sizeCheck.test((T) value);
                } else if (object instanceof Double value) {
                    return sizeCheck.test((T) value);
                } else if (object instanceof BigDecimal value) {
                    return sizeCheck.test((T) value);
                }
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }
}
