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
import yapion.hierarchy.api.groups.YAPIONAnyType;
import yapion.hierarchy.types.YAPIONArray;
import yapion.hierarchy.types.YAPIONObject;
import yapion.hierarchy.types.YAPIONValue;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

@UtilityClass
public class ParsingUtils {

    public static <T> void checkValue(YAPIONValue<?> value, boolean nonNull, Class<T> type, Supplier<T> defaultValue, Consumer<T> callback, Predicate<T> checkPredicate) {
        if (value == null) {
            if (defaultValue != null) {
                callback.accept(defaultValue.get());
                return;
            }
            if (nonNull) {
                throw new yapion.exceptions.YAPIONException("Value is not present");
            } else {
                callback.accept(null);
                return;
            }
        }
        T t = (T) value.get();
        if (t == null) {
            if (defaultValue != null) {
                callback.accept(defaultValue.get());
                return;
            }
            if (nonNull) {
                throw new yapion.exceptions.YAPIONException("Value is null");
            } else {
                callback.accept(null);
                return;
            }
        }
        if (!checkPredicate.test(t)) {
            if (defaultValue != null) {
                callback.accept(defaultValue.get());
                return;
            }
            throw new yapion.exceptions.YAPIONException("Checks failed");
        }
        callback.accept(t);
    }

    public static <T> void checkObject(YAPIONObject object, boolean nonNull, Function<YAPIONObject, T> converter, Consumer<T> callback) {
        if (object == null) {
            if (nonNull) {
                throw new yapion.exceptions.YAPIONException("Object is not present");
            } else {
                callback.accept(null);
                return;
            }
        }
        T t = converter.apply(object);
        if (t == null && nonNull) {
            throw new yapion.exceptions.YAPIONException("Object is null");
        }
        callback.accept(t);
    }

    public static <T, K extends YAPIONAnyType> void checkArray(YAPIONArray array, boolean nonNull, Class<K> yapionType, Class<T> type, Function<K, T> converter, Predicate<T> checkPredicate, Consumer<java.util.List<T>> callback) {
        if (array == null) {
            if (nonNull) {
                throw new yapion.exceptions.YAPIONException("Array is not present");
            } else {
                callback.accept(null);
                return;
            }
        }
        java.util.List<T> list = new java.util.ArrayList<>();
        array.forEach(yapionAnyType -> {
            if (!yapionType.isInstance(yapionAnyType)) {
                throw new yapion.exceptions.YAPIONException("Illegal Instance");
            }
            T converted = converter.apply((K) yapionAnyType);
            if (checkPredicate != null && !checkPredicate.test(converted)) {
                throw new yapion.exceptions.YAPIONException("Checks failed");
            }
            list.add(converted);
        });
        callback.accept(list);
    }

    public static <T> void checkMap(YAPIONObject object, Predicate<String> keys, boolean nonNull, Class<T> type, Function<YAPIONAnyType, T> converter, Consumer<java.util.Map<String, T>> callback) {
        java.util.Map<String, T> map = new java.util.HashMap<>();
        object.allKeys().stream().filter(keys).forEach(s -> {
            try {
                map.put(s, converter.apply(object.getAnyType(s)));
            } catch (Exception e) {
                // Ignored
            }
        });
        if (nonNull && map.isEmpty()) {
            throw new yapion.exceptions.YAPIONException("Map is empty (null)");
        }
        if (map.isEmpty()) {
            callback.accept(null);
        } else {
            callback.accept(map);
        }
    }
}
