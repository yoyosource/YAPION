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
import yapion.annotations.api.YAPIONEveryType;
import yapion.exceptions.serializing.YAPIONClassTypeException;
import yapion.exceptions.utils.YAPIONRetrieveException;
import yapion.hierarchy.api.groups.YAPIONAnyType;
import yapion.hierarchy.api.internal.InternalRetrieve;
import yapion.hierarchy.types.*;
import yapion.utils.ClassUtils;

import java.util.function.Consumer;

public interface MapRetrieve<K> extends InternalRetrieve<K> {

    default <@YAPIONEveryType T> boolean containsKey(@NonNull T key) {
        return containsKey(key, YAPIONType.ANY);
    }

    default <@YAPIONEveryType T> boolean containsKey(@NonNull T key, YAPIONType type) {
        if (key instanceof YAPIONAnyType) {
            return internalContainsKey((K) key, type);
        }
        if (!YAPIONValue.validType(key)) {
            throw new YAPIONClassTypeException("The type '" + key.getClass().getTypeName() + "' is not a valid YAPIONEveryType");
        }
        return internalContainsKey((K) new YAPIONValue<>(key), type);
    }

    default <@YAPIONEveryType T, C> boolean containsKey(@NonNull T key, Class<C> type) {
        if (key instanceof YAPIONAnyType) {
            return internalContainsKey((K) key, type);
        }
        if (!YAPIONValue.validType(key)) {
            throw new YAPIONClassTypeException("The type '" + key.getClass().getTypeName() + "' is not a valid YAPIONEveryType");
        }
        return internalContainsKey((K) new YAPIONValue<>(key), type);
    }

    default <@YAPIONEveryType T> YAPIONAnyType getYAPIONAnyType(@NonNull T key) {
        if (key instanceof YAPIONAnyType) {
            return internalGetYAPIONAnyType((K) key);
        }
        if (!YAPIONValue.validType(key)) {
            throw new YAPIONClassTypeException("The type '" + key.getClass().getTypeName() + "' is not a valid YAPIONEveryType");
        }
        return internalGetYAPIONAnyType((K) new YAPIONValue<>(key));
    }

    default <@YAPIONEveryType T> YAPIONObject getObject(@NonNull T key) {
        YAPIONAnyType yapionAnyType = getYAPIONAnyType(key);
        if (yapionAnyType == null) return null;
        if (yapionAnyType instanceof YAPIONObject yapionObject) {
            return yapionObject;
        }
        return null;
    }

    default <@YAPIONEveryType T> void getObject(@NonNull T key, Consumer<YAPIONObject> valueConsumer, Runnable noValue) {
        YAPIONObject yapionObject = getObject(key);
        if (yapionObject == null) {
            noValue.run();
        } else {
            valueConsumer.accept(yapionObject);
        }
    }

    default <@YAPIONEveryType T> YAPIONArray getArray(@NonNull T key) {
        YAPIONAnyType yapionAnyType = getYAPIONAnyType(key);
        if (yapionAnyType == null) return null;
        if (yapionAnyType instanceof YAPIONArray yapionArray) {
            return yapionArray;
        }
        return null;
    }

    default <@YAPIONEveryType T> void getArray(@NonNull T key, Consumer<YAPIONArray> valueConsumer, Runnable noValue) {
        YAPIONArray yapionArray = getArray(key);
        if (yapionArray == null) {
            noValue.run();
        } else {
            valueConsumer.accept(yapionArray);
        }
    }

    default <@YAPIONEveryType T> YAPIONMap getMap(@NonNull T key) {
        YAPIONAnyType yapionAnyType = getYAPIONAnyType(key);
        if (yapionAnyType == null) return null;
        if (yapionAnyType instanceof YAPIONMap yapionMap) {
            return yapionMap;
        }
        return null;
    }

    default <@YAPIONEveryType T> void getMap(@NonNull T key, Consumer<YAPIONMap> valueConsumer, Runnable noValue) {
        YAPIONMap yapionMap = getMap(key);
        if (yapionMap == null) {
            noValue.run();
        } else {
            valueConsumer.accept(yapionMap);
        }
    }

    default <@YAPIONEveryType T> YAPIONPointer getPointer(@NonNull T key) {
        YAPIONAnyType yapionAnyType = getYAPIONAnyType(key);
        if (yapionAnyType == null) return null;
        if (yapionAnyType instanceof YAPIONPointer yapionPointer) {
            return yapionPointer;
        }
        return null;
    }

    default <@YAPIONEveryType T> void getPointer(@NonNull T key, Consumer<YAPIONPointer> valueConsumer, Runnable noValue) {
        YAPIONPointer yapionPointer = getPointer(key);
        if (yapionPointer == null) {
            noValue.run();
        } else {
            valueConsumer.accept(yapionPointer);
        }
    }

    @SuppressWarnings({"java:S3740"})
    default <@YAPIONEveryType T> YAPIONValue getValue(@NonNull T key) {
        YAPIONAnyType yapionAnyType = getYAPIONAnyType(key);
        if (yapionAnyType == null) return null;
        if (yapionAnyType instanceof YAPIONValue<?> yapionValue) {
            return yapionValue;
        }
        return null;
    }

    @SuppressWarnings({"java:S3740"})
    default <@YAPIONEveryType T> void getValue(@NonNull T key, Consumer<YAPIONValue> valueConsumer, Runnable noValue) {
        YAPIONValue yapionValue = getValue(key);
        if (yapionValue == null) {
            noValue.run();
        } else {
            valueConsumer.accept(yapionValue);
        }
    }

    @SuppressWarnings("unchecked")
    default <@YAPIONEveryType T, C> YAPIONValue<C> getValue(@NonNull T key, Class<C> type) {
        if (ClassUtils.isPrimitive(type)) {
            return (YAPIONValue<C>) getValue(key, ClassUtils.getBoxed(type));
        }
        if (!YAPIONValue.validType(type)) {
            throw new YAPIONRetrieveException("The type '" + type.getTypeName() + "' is not supported as a YAPIONValue value");
        }
        YAPIONAnyType yapionAnyType = getYAPIONAnyType(key);
        if (yapionAnyType == null) return null;
        if (yapionAnyType instanceof YAPIONValue<?> yapionValue && yapionValue.isValidCastType(type)) {
            return (YAPIONValue<C>) yapionAnyType;
        }
        return null;
    }

    default <@YAPIONEveryType T, C> YAPIONValue<C> getValueOrDefault(@NonNull T key, Class<C> type, C defaultValue) {
        YAPIONValue<C> yapionValue = getValue(key, type);
        if (yapionValue == null) {
            return new YAPIONValue<>(defaultValue);
        } else {
            return yapionValue;
        }
    }

    default <@YAPIONEveryType T, C> void getValue(@NonNull T key, Class<C> type, Consumer<YAPIONValue<C>> valueConsumer, Runnable noValue) {
        YAPIONValue<C> yapionValue = getValue(key, type);
        if (yapionValue == null) {
            noValue.run();
        } else {
            valueConsumer.accept(yapionValue);
        }
    }

    @SuppressWarnings("unchecked")
    default <@YAPIONEveryType T, C> YAPIONValue<C> getValue(@NonNull T key, C type) {
        if (!YAPIONValue.validType(type)) {
            throw new YAPIONRetrieveException("The type '" + type.getClass().getTypeName() + "' is not supported as a YAPIONValue value");
        }
        YAPIONAnyType yapionAnyType = getYAPIONAnyType(key);
        if (yapionAnyType == null) return null;
        if (yapionAnyType instanceof YAPIONValue<?> yapionValue && yapionValue.isValidCastType(type == null ? null : type.getClass())) {
            return (YAPIONValue<C>) yapionAnyType;
        }
        return null;
    }

    default <@YAPIONEveryType T, C> YAPIONValue<C> getValueOrDefault(@NonNull T key, C defaultValue) {
        YAPIONValue<C> yapionValue = getValue(key, defaultValue);
        if (yapionValue == null) {
            return new YAPIONValue<>(defaultValue);
        } else {
            return yapionValue;
        }
    }

    @SuppressWarnings("unchecked")
    default <@YAPIONEveryType T, C> C getPlainValue(@NonNull T key) {
        YAPIONValue<C> yapionValue = getValue(key);
        if (yapionValue == null) throw new YAPIONRetrieveException("Key '" + key + "' has no YAPIONValue associated with it");
        return yapionValue.get();
    }

    @SuppressWarnings("unchecked")
    default <@YAPIONEveryType T, C> C getPlainValueOrDefault(@NonNull T key, C defaultValue) {
        YAPIONValue<C> yapionValue = getValue(key);
        if (yapionValue == null) {
            return defaultValue;
        } else {
            return yapionValue.get();
        }
    }

    @SuppressWarnings("unchecked")
    default <@YAPIONEveryType T, C> void getPlainValue(@NonNull T key, Consumer<C> valueConsumer, Runnable noValue) {
        YAPIONValue<C> yapionValue = getValue(key);
        if (yapionValue == null) {
            noValue.run();
        } else {
            valueConsumer.accept(yapionValue.get());
        }
    }
}
