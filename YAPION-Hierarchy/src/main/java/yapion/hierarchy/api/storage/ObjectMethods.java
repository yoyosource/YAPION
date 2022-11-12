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
import yapion.annotations.api.OptionalAPI;
import yapion.annotations.api.YAPIONPrimitive;
import yapion.exceptions.serializing.YAPIONClassTypeException;
import yapion.exceptions.utils.YAPIONRetrieveException;
import yapion.hierarchy.api.groups.YAPIONAnyType;
import yapion.hierarchy.api.internal.InternalMethods;
import yapion.hierarchy.types.*;
import yapion.utils.ClassUtils;

import java.util.function.Consumer;

public interface ObjectMethods<I, K> extends InternalMethods<I, K> {

    // -------- Add --------

    default I set(@NonNull K key, @NonNull YAPIONAnyType value) {
        return internalAdd(key, value);
    }

    default I add(@NonNull K key, @NonNull YAPIONAnyType value) {
        return internalAdd(key, value);
    }

    default <@YAPIONPrimitive T> I add(@NonNull K key, T value) {
        if (!YAPIONValue.validType(value)) {
            throw new YAPIONClassTypeException("The type '" + value.getClass().getTypeName() + "' is not a valid YAPIONPrimitive");
        }
        return add(key, new YAPIONValue<>(value));
    }

    default I add(@NonNull K key, @NonNull Class<?> value) {
        return add(key, new YAPIONValue<>(value.getTypeName()));
    }

    /**
     * Optional API.
     */
    @OptionalAPI
    default I addOrPointer(@NonNull K key, @NonNull YAPIONAnyType value) {
        throw new UnsupportedOperationException();
    }

    default YAPIONAnyType addAndGetPrevious(@NonNull K key, @NonNull YAPIONAnyType value) {
        return internalAddAndGetPrevious(key, value);
    }

    default <@YAPIONPrimitive T> YAPIONAnyType addAndGetPrevious(@NonNull K key, T value) {
        if (!YAPIONValue.validType(value)) {
            throw new YAPIONClassTypeException("The type '" + value.getClass().getTypeName() + "' is not a valid YAPIONPrimitive");
        }
        return addAndGetPrevious(key, new YAPIONValue<>(value));
    }

    /**
     * Optional API.
     */
    @OptionalAPI
    default YAPIONAnyType addOrPointerAndGetPrevious(@NonNull K key, @NonNull YAPIONAnyType value) {
        throw new UnsupportedOperationException();
    }

    default I putAndGetItself(@NonNull K key, @NonNull YAPIONAnyType value) {
        return add(key, value);
    }

    default <@YAPIONPrimitive T> I putAndGetItself(@NonNull K key, T value) {
        if (!YAPIONValue.validType(value)) {
            throw new YAPIONClassTypeException("The type '" + value.getClass().getTypeName() + "' is not a valid YAPIONPrimitive");
        }
        return putAndGetItself(key, new YAPIONValue<>(value));
    }

    /**
     * Optional API.
     */
    @OptionalAPI
    default I putOrPointerAndGetItself(@NonNull K key, @NonNull YAPIONAnyType value) {
        return addOrPointer(key, value);
    }

    default YAPIONAnyType put(@NonNull K key, @NonNull YAPIONAnyType value) {
        return addAndGetPrevious(key, value);
    }

    default <@YAPIONPrimitive T> YAPIONAnyType put(@NonNull K key, T value) {
        if (!YAPIONValue.validType(value)) {
            throw new YAPIONClassTypeException("The type '" + value.getClass().getTypeName() + "' is not a valid YAPIONPrimitive");
        }
        return put(key, new YAPIONValue<>(value));
    }

    /**
     * Optional API.
     */
    @OptionalAPI
    default YAPIONAnyType putOrPointer(@NonNull K key, @NonNull YAPIONAnyType value) {
        return addOrPointerAndGetPrevious(key, value);
    }

    // -------- Remove --------

    default I remove(@NonNull K key) {
        return internalRemove(key);
    }

    default YAPIONAnyType removeAndGet(@NonNull K key) {
        return internalRemoveAndGet(key);
    }

    default I clear() {
        return internalClear();
    }

    // -------- Get --------

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

    @Deprecated
    @DeprecationInfo(since = "0.26.0", alternative = "#getAnyType(K)")
    default YAPIONAnyType getYAPIONAnyType(@NonNull K key) {
        return internalGetAnyType(key);
    }

    default YAPIONAnyType getAnyType(@NonNull K key) {
        return internalGetAnyType(key);
    }

    default YAPIONAnyType getAny(@NonNull K key) {
        return internalGetAnyType(key);
    }

    default YAPIONAnyType get(@NonNull K key) {
        return internalGetAnyType(key);
    }

    default void getAny(@NonNull K key, Consumer<YAPIONAnyType> valueConsumer, Runnable noValue) {
        if (containsKey(key)) {
            valueConsumer.accept(internalGetAnyType(key));
        } else {
            noValue.run();
        }
    }

    default void get(@NonNull K key, Consumer<YAPIONAnyType> valueConsumer, Runnable noValue) {
        getAny(key, valueConsumer, noValue);
    }

    default YAPIONObject getObject(@NonNull K key) {
        YAPIONAnyType yapionAnyType = getAnyType(key);
        if (yapionAnyType == null) return null;
        if (yapionAnyType instanceof YAPIONObject yapionObject) {
            return yapionObject;
        }
        return null;
    }

    default YAPIONObject getObjectOrDefault(@NonNull K key, YAPIONObject defaultValue) {
        YAPIONAnyType yapionAnyType = getAnyType(key);
        if (yapionAnyType == null) return defaultValue;
        if (yapionAnyType instanceof YAPIONObject yapionObject) {
            return yapionObject;
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
        YAPIONAnyType yapionAnyType = getAnyType(key);
        if (yapionAnyType == null) return null;
        if (yapionAnyType instanceof YAPIONArray yapionArray) {
            return yapionArray;
        }
        return null;
    }

    default YAPIONArray getArrayOrDefault(@NonNull K key, YAPIONArray defaultValue) {
        YAPIONAnyType yapionAnyType = getAnyType(key);
        if (yapionAnyType == null) return defaultValue;
        if (yapionAnyType instanceof YAPIONArray yapionArray) {
            return yapionArray;
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
        YAPIONAnyType yapionAnyType = getAnyType(key);
        if (yapionAnyType == null) return null;
        if (yapionAnyType instanceof YAPIONMap yapionMap) {
            return yapionMap;
        }
        return null;
    }

    default YAPIONMap getMapOrDefault(@NonNull K key, YAPIONMap defaultValue) {
        YAPIONAnyType yapionAnyType = getAnyType(key);
        if (yapionAnyType == null) return defaultValue;
        if (yapionAnyType instanceof YAPIONMap yapionMap) {
            return yapionMap;
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
        YAPIONAnyType yapionAnyType = getAnyType(key);
        if (yapionAnyType == null) return null;
        if (yapionAnyType instanceof YAPIONPointer yapionPointer) {
            return yapionPointer;
        }
        return null;
    }

    default YAPIONPointer getPointerOrDefault(@NonNull K key, YAPIONPointer defaultValue) {
        YAPIONAnyType yapionAnyType = getAnyType(key);
        if (yapionAnyType == null) return defaultValue;
        if (yapionAnyType instanceof YAPIONPointer yapionPointer) {
            return yapionPointer;
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
        YAPIONAnyType yapionAnyType = getAnyType(key);
        if (yapionAnyType == null) return null;
        if (yapionAnyType instanceof YAPIONValue yapionValue) {
            return yapionValue;
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
        YAPIONAnyType yapionAnyType = getAnyType(key);
        if (yapionAnyType == null) return null;
        if (yapionAnyType instanceof YAPIONValue<?> yapionValue && yapionValue.isValidCastType(type)) {
            return (YAPIONValue<T>) yapionAnyType;
        }
        return null;
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
        YAPIONAnyType yapionAnyType = getAnyType(key);
        if (yapionAnyType == null) return null;
        if (yapionAnyType instanceof YAPIONValue<?> yapionValue && yapionValue.isValidCastType(type == null ? null : type.getClass())) {
            return (YAPIONValue<T>) yapionAnyType;
        }
        return null;
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
