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

package yapion.hierarchy.api.storage;

import lombok.NonNull;
import yapion.annotations.api.DeprecationInfo;
import yapion.exceptions.utils.YAPIONRetrieveException;
import yapion.hierarchy.api.groups.YAPIONAnyType;
import yapion.hierarchy.types.*;
import yapion.utils.ClassUtils;

import java.util.function.Consumer;

public interface ObjectRetrieve<K> {

    @Deprecated
    @DeprecationInfo(since = "0.25.1", alternative = "containsKey")
    default boolean hasValue(@NonNull K key) {
        return hasValue(key, YAPIONType.ANY);
    }

    @Deprecated
    @DeprecationInfo(since = "0.25.1", alternative = "containsKey")
    default boolean hasValue(@NonNull K key, YAPIONType yapionType) {
        return containsKey(key, yapionType);
    }

    @Deprecated
    @DeprecationInfo(since = "0.25.1", alternative = "containsKey")
    default <T> boolean hasValue(@NonNull K key, Class<T> type) {
        return containsKey(key, type);
    }

    default boolean containsKey(@NonNull K key) {
        return containsKey(key, YAPIONType.ANY);
    }

    boolean containsKey(@NonNull K key, YAPIONType yapionType);

    <T> boolean containsKey(@NonNull K key, Class<T> type);

    boolean containsValue(@NonNull YAPIONAnyType yapionAnyType);

    YAPIONAnyType getYAPIONAnyType(@NonNull K key);

    default YAPIONObject getObject(@NonNull K key) {
        YAPIONAnyType yapionAnyType = getYAPIONAnyType(key);
        if (yapionAnyType == null) return null;
        if (yapionAnyType instanceof YAPIONObject) {
            return (YAPIONObject) yapionAnyType;
        }
        return null;
    }

    default void getObject(@NonNull K key, Consumer<YAPIONObject> valueConsumer, Runnable noValue) {
        YAPIONAnyType yapionAnyType = getYAPIONAnyType(key);
        if (yapionAnyType == null) {
            noValue.run();
            return;
        }
        if (yapionAnyType instanceof YAPIONObject) {
            valueConsumer.accept((YAPIONObject) yapionAnyType);
        }
    }

    default YAPIONArray getArray(@NonNull K key) {
        YAPIONAnyType yapionAnyType = getYAPIONAnyType(key);
        if (yapionAnyType == null) return null;
        if (yapionAnyType instanceof YAPIONArray) {
            return (YAPIONArray) yapionAnyType;
        }
        return null;
    }

    default void getArray(@NonNull K key, Consumer<YAPIONArray> valueConsumer, Runnable noValue) {
        YAPIONAnyType yapionAnyType = getYAPIONAnyType(key);
        if (yapionAnyType == null) {
            noValue.run();
            return;
        }
        if (yapionAnyType instanceof YAPIONArray) {
            valueConsumer.accept((YAPIONArray) yapionAnyType);
        }
    }

    default YAPIONMap getMap(@NonNull K key) {
        YAPIONAnyType yapionAnyType = getYAPIONAnyType(key);
        if (yapionAnyType == null) return null;
        if (yapionAnyType instanceof YAPIONMap) {
            return (YAPIONMap) yapionAnyType;
        }
        return null;
    }

    default void getMap(@NonNull K key, Consumer<YAPIONMap> valueConsumer, Runnable noValue) {
        YAPIONAnyType yapionAnyType = getYAPIONAnyType(key);
        if (yapionAnyType == null) {
            noValue.run();
            return;
        }
        if (yapionAnyType instanceof YAPIONMap) {
            valueConsumer.accept((YAPIONMap) yapionAnyType);
        }
    }

    default YAPIONPointer getPointer(@NonNull K key) {
        YAPIONAnyType yapionAnyType = getYAPIONAnyType(key);
        if (yapionAnyType == null) return null;
        if (yapionAnyType instanceof YAPIONPointer) {
            return (YAPIONPointer) yapionAnyType;
        }
        return null;
    }

    default void getPointer(@NonNull K key, Consumer<YAPIONPointer> valueConsumer, Runnable noValue) {
        YAPIONAnyType yapionAnyType = getYAPIONAnyType(key);
        if (yapionAnyType == null) {
            noValue.run();
            return;
        }
        if (yapionAnyType instanceof YAPIONPointer) {
            valueConsumer.accept((YAPIONPointer) yapionAnyType);
        }
    }

    @SuppressWarnings({"java:S3740"})
    default YAPIONValue getValue(@NonNull K key) {
        YAPIONAnyType yapionAnyType = getYAPIONAnyType(key);
        if (yapionAnyType == null) return null;
        if (yapionAnyType instanceof YAPIONValue) {
            return (YAPIONValue) yapionAnyType;
        }
        return null;
    }

    @SuppressWarnings({"java:S3740"})
    default void getValue(@NonNull K key, Consumer<YAPIONValue> valueConsumer, Runnable noValue) {
        YAPIONAnyType yapionAnyType = getYAPIONAnyType(key);
        if (yapionAnyType == null) {
            noValue.run();
            return;
        }
        if (yapionAnyType instanceof YAPIONValue) {
            valueConsumer.accept((YAPIONValue) yapionAnyType);
        }
    }

    @SuppressWarnings("unchecked")
    default <T> YAPIONValue<T> getValue(@NonNull K key, Class<T> type) {
        if (ClassUtils.isPrimitive(type)) {
            return (YAPIONValue<T>) getValue(key, ClassUtils.getBoxed(type));
        }
        if (!YAPIONValue.validType(type)) {
            throw new YAPIONRetrieveException();
        }
        YAPIONAnyType yapionAnyType = getYAPIONAnyType(key);
        if (yapionAnyType == null) return null;
        if (!(yapionAnyType instanceof YAPIONValue)) {
            return null;
        }
        if (!((YAPIONValue) yapionAnyType).isValidCastType(type.getTypeName())) {
            return null;
        }
        return (YAPIONValue<T>) yapionAnyType;
    }

    @SuppressWarnings("unchecked")
    default <T> YAPIONValue<T> getValueOrDefault(@NonNull K key, Class<T> type, T defaultValue) {
        if (ClassUtils.isPrimitive(type)) {
            YAPIONValue<?> yapionValue = getValue(key, ClassUtils.getBoxed(type));
            if (yapionValue == null) {
                return new YAPIONValue<>(defaultValue);
            }
            return (YAPIONValue<T>) yapionValue;
        }
        if (!YAPIONValue.validType(type)) {
            throw new YAPIONRetrieveException();
        }
        YAPIONAnyType yapionAnyType = getYAPIONAnyType(key);
        if (yapionAnyType == null) return new YAPIONValue<>(defaultValue);
        if (!(yapionAnyType instanceof YAPIONValue)) {
            return new YAPIONValue<>(defaultValue);
        }
        if (!((YAPIONValue) yapionAnyType).isValidCastType(type.getTypeName())) {
            return new YAPIONValue<>(defaultValue);
        }
        return (YAPIONValue<T>) yapionAnyType;
    }

    @SuppressWarnings("unchecked")
    default <T> void getValue(@NonNull K key, Class<T> type, Consumer<YAPIONValue<T>> valueConsumer, Runnable noValue) {
        if (ClassUtils.isPrimitive(type)) {
            YAPIONValue<?> yapionValue = getValue(key, ClassUtils.getBoxed(type));
            if (yapionValue == null) {
                noValue.run();
            }
            valueConsumer.accept((YAPIONValue<T>) yapionValue);
            return;
        }
        if (!YAPIONValue.validType(type)) {
            throw new YAPIONRetrieveException();
        }
        YAPIONAnyType yapionAnyType = getYAPIONAnyType(key);
        if (yapionAnyType == null) {
            noValue.run();
            return;
        }
        if (!(yapionAnyType instanceof YAPIONValue)) {
            return;
        }
        if (!((YAPIONValue) yapionAnyType).isValidCastType(type.getTypeName())) {
            return;
        }
        valueConsumer.accept((YAPIONValue<T>) yapionAnyType);
    }

    @SuppressWarnings("unchecked")
    default <T> YAPIONValue<T> getValue(@NonNull K key, T type) {
        if (!YAPIONValue.validType(type)) {
            throw new YAPIONRetrieveException();
        }
        YAPIONAnyType yapionAnyType = getYAPIONAnyType(key);
        if (yapionAnyType == null) return null;
        if (!(yapionAnyType instanceof YAPIONValue)) {
            return null;
        }
        if (!((YAPIONValue) yapionAnyType).isValidCastType(type.getClass().getTypeName())) {
            return null;
        }
        return (YAPIONValue<T>) yapionAnyType;
    }

    @SuppressWarnings("unchecked")
    default <T> YAPIONValue<T> getValueOrDefault(@NonNull K key, T defaultValue) {
        if (!YAPIONValue.validType(defaultValue)) {
            throw new YAPIONRetrieveException();
        }
        YAPIONAnyType yapionAnyType = getYAPIONAnyType(key);
        if (yapionAnyType == null) return new YAPIONValue<>(defaultValue);
        if (!(yapionAnyType instanceof YAPIONValue)) {
            return new YAPIONValue<>(defaultValue);
        }
        if (!((YAPIONValue) yapionAnyType).isValidCastType(defaultValue.getClass().getTypeName())) {
            return new YAPIONValue<>(defaultValue);
        }
        return (YAPIONValue<T>) yapionAnyType;
    }

    @SuppressWarnings("unchecked")
    @Deprecated
    @DeprecationInfo(since = "0.25.0")
    default <T> void getValue(@NonNull K key, T type, Consumer<YAPIONValue<T>> valueConsumer, Runnable noValue) {
        if (!YAPIONValue.validType(type)) {
            throw new YAPIONRetrieveException();
        }
        YAPIONAnyType yapionAnyType = getYAPIONAnyType(key);
        if (yapionAnyType == null) {
            noValue.run();
            return;
        }
        if (!(yapionAnyType instanceof YAPIONValue)) {
            return;
        }
        if (!((YAPIONValue) yapionAnyType).isValidCastType(type.getClass().getTypeName())) {
            return;
        }
        valueConsumer.accept((YAPIONValue<T>) yapionAnyType);
    }

    @SuppressWarnings("unchecked")
    default <T> T getPlainValue(@NonNull K key) {
        YAPIONValue<T> yapionValue = getValue(key);
        if (yapionValue == null) throw new YAPIONRetrieveException("Key '" + key.toString() + "' has no YAPIONValue associated with it");
        return yapionValue.get();
    }

    @SuppressWarnings("unchecked")
    default <T> T getPlainValueOrDefault(@NonNull K key, T defaultValue) {
        YAPIONValue<T> yapionValue = getValue(key);
        if (yapionValue == null) {
            return defaultValue;
        } else {
            return yapionValue.get();
        }
    }

    @SuppressWarnings("unchecked")
    default <T> void getPlainValue(@NonNull K key, Consumer<T> valueConsumer, Runnable noValue) {
        YAPIONValue<T> yapionValue = getValue(key);
        if (yapionValue == null) {
            noValue.run();
            return;
        }
        valueConsumer.accept(yapionValue.get());
    }

}
