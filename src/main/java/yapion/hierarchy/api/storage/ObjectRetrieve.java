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
import yapion.hierarchy.api.internal.InternalRetrieve;
import yapion.hierarchy.types.*;
import yapion.utils.ClassUtils;

import java.util.function.Consumer;

public interface ObjectRetrieve<K> extends InternalRetrieve<K> {

    default boolean containsKey(@NonNull K key) {
        return containsKey(key, YAPIONType.ANY);
    }

    default boolean containsKey(@NonNull K key, YAPIONType yapionType) {
        return internalContainsKey(key, yapionType);
    }

    default <T> boolean containsKey(@NonNull K key, Class<T> type) {
        return internalContainsKey(key, type);
    }

    default boolean containsValue(@NonNull YAPIONAnyType yapionAnyType) {
        return internalContainsValue(yapionAnyType);
    }

    default YAPIONAnyType getYAPIONAnyType(@NonNull K key) {
        return internalGetYAPIONAnyType(key);
    }

    default YAPIONObject getObject(@NonNull K key) {
        YAPIONAnyType yapionAnyType = getYAPIONAnyType(key);
        if (yapionAnyType == null) return null;
        if (yapionAnyType instanceof YAPIONObject) {
            return (YAPIONObject) yapionAnyType;
        }
        return null;
    }

    default YAPIONObject getObjectOrDefault(@NonNull K key, YAPIONObject defaultValue) {
        YAPIONAnyType yapionAnyType = getYAPIONAnyType(key);
        if (yapionAnyType == null) return defaultValue;
        if (yapionAnyType instanceof YAPIONObject) {
            return (YAPIONObject) yapionAnyType;
        }
        return defaultValue;
    }

    default void getObject(@NonNull K key, Consumer<YAPIONObject> valueConsumer, Runnable noValue) {
        YAPIONObject yapionObject = getObject(key);
        if (yapionObject == null) {
            noValue.run();
        } else {
            valueConsumer.accept(yapionObject);
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

    default YAPIONArray getArrayOrDefault(@NonNull K key, YAPIONArray defaultValue) {
        YAPIONAnyType yapionAnyType = getYAPIONAnyType(key);
        if (yapionAnyType == null) return defaultValue;
        if (yapionAnyType instanceof YAPIONArray) {
            return (YAPIONArray) yapionAnyType;
        }
        return defaultValue;
    }

    default void getArray(@NonNull K key, Consumer<YAPIONArray> valueConsumer, Runnable noValue) {
        YAPIONArray yapionArray = getArray(key);
        if (yapionArray == null) {
            noValue.run();
        } else {
            valueConsumer.accept(yapionArray);
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

    default YAPIONMap getMapOrDefault(@NonNull K key, YAPIONMap defaultValue) {
        YAPIONAnyType yapionAnyType = getYAPIONAnyType(key);
        if (yapionAnyType == null) return defaultValue;
        if (yapionAnyType instanceof YAPIONMap) {
            return (YAPIONMap) yapionAnyType;
        }
        return defaultValue;
    }

    default void getMap(@NonNull K key, Consumer<YAPIONMap> valueConsumer, Runnable noValue) {
        YAPIONMap yapionMap = getMap(key);
        if (yapionMap == null) {
            noValue.run();
        } else {
            valueConsumer.accept(yapionMap);
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

    default YAPIONPointer getPointerOrDefault(@NonNull K key, YAPIONPointer defaultValue) {
        YAPIONAnyType yapionAnyType = getYAPIONAnyType(key);
        if (yapionAnyType == null) return defaultValue;
        if (yapionAnyType instanceof YAPIONPointer) {
            return (YAPIONPointer) yapionAnyType;
        }
        return defaultValue;
    }

    default void getPointer(@NonNull K key, Consumer<YAPIONPointer> valueConsumer, Runnable noValue) {
        YAPIONPointer yapionPointer = getPointer(key);
        if (yapionPointer == null) {
            noValue.run();
        } else {
            valueConsumer.accept(yapionPointer);
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
        YAPIONValue yapionValue = getValue(key);
        if (yapionValue == null) {
            noValue.run();
        } else {
            valueConsumer.accept(yapionValue);
        }
    }

    @SuppressWarnings("unchecked")
    default <T> YAPIONValue<T> getValue(@NonNull K key, Class<T> type) {
        if (ClassUtils.isPrimitive(type)) {
            return (YAPIONValue<T>) getValue(key, ClassUtils.getBoxed(type));
        }
        if (!YAPIONValue.validType(type)) {
            throw new YAPIONRetrieveException("The type '" + type.getTypeName() + "' is not supported as a YAPIONValue value");
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

    default <T> YAPIONValue<T> getValueOrDefault(@NonNull K key, Class<T> type, T defaultValue) {
        YAPIONValue<T> yapionValue = getValue(key, type);
        if (yapionValue == null) {
            return new YAPIONValue<>(defaultValue);
        } else {
            return yapionValue;
        }
    }

    default <T> void getValue(@NonNull K key, Class<T> type, Consumer<YAPIONValue<T>> valueConsumer, Runnable noValue) {
        YAPIONValue<T> yapionValue = getValue(key, type);
        if (yapionValue == null) {
            noValue.run();
        } else {
            valueConsumer.accept(yapionValue);
        }
    }

    @SuppressWarnings("unchecked")
    default <T> YAPIONValue<T> getValue(@NonNull K key, T type) {
        if (!YAPIONValue.validType(type)) {
            throw new YAPIONRetrieveException("The type '" + type.getClass().getTypeName() + "' is not supported as a YAPIONValue value");
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

    default <T> YAPIONValue<T> getValueOrDefault(@NonNull K key, T defaultValue) {
        YAPIONValue<T> yapionValue = getValue(key, defaultValue);
        if (yapionValue == null) {
            return new YAPIONValue<>(defaultValue);
        } else {
            return yapionValue;
        }
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
        } else {
            valueConsumer.accept(yapionValue.get());
        }
    }

}
