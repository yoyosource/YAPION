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

@UtilityClass
public class ParsingUtils {

    public static <T> void checkValue(yapion.hierarchy.types.YAPIONValue<?> value, boolean nonNull, Class<T> type, java.util.function.Supplier<T> defaultValue, java.util.function.Consumer<T> callback, java.util.function.Predicate<T> checkPredicate) {
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

    public static <T> void checkObject(yapion.hierarchy.types.YAPIONObject object, boolean nonNull, java.util.function.Function<yapion.hierarchy.types.YAPIONObject, T> converter, java.util.function.Consumer<T> callback) {
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

    public static <T, K extends yapion.hierarchy.api.groups.YAPIONAnyType> void checkArray(yapion.hierarchy.types.YAPIONArray array, boolean nonNull, Class<K> yapionType, Class<T> type, java.util.function.Function<K, T> converter, java.util.function.Predicate<T> checkPredicate, java.util.function.Consumer<java.util.List<T>> callback) {
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

    public static <T> void checkMap(yapion.hierarchy.types.YAPIONObject object, java.util.function.Predicate<String> keys, boolean nonNull, Class<T> type, java.util.function.Function<yapion.hierarchy.api.groups.YAPIONAnyType, T> converter, java.util.function.Consumer<java.util.Map<String, T>> callback) {
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
