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
import yapion.annotations.api.YAPIONEveryType;
import yapion.exceptions.serializing.YAPIONClassTypeException;
import yapion.exceptions.utils.YAPIONRetrieveException;
import yapion.hierarchy.api.groups.YAPIONAnyType;
import yapion.hierarchy.api.storage.internal.InternalRetrieve;
import yapion.hierarchy.types.*;
import yapion.utils.ClassUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.function.Consumer;

public interface MapRetrieve<K> extends InternalRetrieve<K> {

    @Deprecated
    @DeprecationInfo(since = "0.25.1", alternative = "containsKey")
    default boolean hasValue(@NonNull String key) {
        return containsKey(new YAPIONValue<>(key));
    }

    @Deprecated
    @DeprecationInfo(since = "0.25.1", alternative = "containsKey")
    default boolean hasValue(char key) {
        return containsKey(new YAPIONValue<>(key));
    }

    @Deprecated
    @DeprecationInfo(since = "0.25.1", alternative = "containsKey")
    default boolean hasValue(boolean key) {
        return containsKey(new YAPIONValue<>(key));
    }

    @Deprecated
    @DeprecationInfo(since = "0.25.1", alternative = "containsKey")
    default boolean hasValue(byte key) {
        return containsKey(new YAPIONValue<>(key));
    }

    @Deprecated
    @DeprecationInfo(since = "0.25.1", alternative = "containsKey")
    default boolean hasValue(short key) {
        return containsKey(new YAPIONValue<>(key));
    }

    @Deprecated
    @DeprecationInfo(since = "0.25.1", alternative = "containsKey")
    default boolean hasValue(int key) {
        return containsKey(new YAPIONValue<>(key));
    }

    @Deprecated
    @DeprecationInfo(since = "0.25.1", alternative = "containsKey")
    default boolean hasValue(long key) {
        return containsKey(new YAPIONValue<>(key));
    }

    @Deprecated
    @DeprecationInfo(since = "0.25.1", alternative = "containsKey")
    default boolean hasValue(@NonNull BigInteger key) {
        return containsKey(new YAPIONValue<>(key));
    }

    @Deprecated
    @DeprecationInfo(since = "0.25.1", alternative = "containsKey")
    default boolean hasValue(float key) {
        return containsKey(new YAPIONValue<>(key));
    }

    @Deprecated
    @DeprecationInfo(since = "0.25.1", alternative = "containsKey")
    default boolean hasValue(double key) {
        return containsKey(new YAPIONValue<>(key));
    }

    @Deprecated
    @DeprecationInfo(since = "0.25.1", alternative = "containsKey")
    default boolean hasValue(@NonNull BigDecimal key) {
        return containsKey(new YAPIONValue<>(key));
    }

    @Deprecated
    @DeprecationInfo(since = "0.25.1", alternative = "containsKey")
    default boolean hasValue(@NonNull String key, YAPIONType yapionType) {
        return containsKey(new YAPIONValue<>(key), yapionType);
    }

    @Deprecated
    @DeprecationInfo(since = "0.25.1", alternative = "containsKey")
    default boolean hasValue(char key, YAPIONType yapionType) {
        return containsKey(new YAPIONValue<>(key), yapionType);
    }

    @Deprecated
    @DeprecationInfo(since = "0.25.1", alternative = "containsKey")
    default boolean hasValue(boolean key, YAPIONType yapionType) {
        return containsKey(new YAPIONValue<>(key), yapionType);
    }

    @Deprecated
    @DeprecationInfo(since = "0.25.1", alternative = "containsKey")
    default boolean hasValue(byte key, YAPIONType yapionType) {
        return containsKey(new YAPIONValue<>(key), yapionType);
    }

    @Deprecated
    @DeprecationInfo(since = "0.25.1", alternative = "containsKey")
    default boolean hasValue(short key, YAPIONType yapionType) {
        return containsKey(new YAPIONValue<>(key), yapionType);
    }

    @Deprecated
    @DeprecationInfo(since = "0.25.1", alternative = "containsKey")
    default boolean hasValue(int key, YAPIONType yapionType) {
        return containsKey(new YAPIONValue<>(key), yapionType);
    }

    @Deprecated
    @DeprecationInfo(since = "0.25.1", alternative = "containsKey")
    default boolean hasValue(long key, YAPIONType yapionType) {
        return containsKey(new YAPIONValue<>(key), yapionType);
    }

    @Deprecated
    @DeprecationInfo(since = "0.25.1", alternative = "containsKey")
    default boolean hasValue(@NonNull BigInteger key, YAPIONType yapionType) {
        return containsKey(new YAPIONValue<>(key), yapionType);
    }

    @Deprecated
    @DeprecationInfo(since = "0.25.1", alternative = "containsKey")
    default boolean hasValue(float key, YAPIONType yapionType) {
        return containsKey(new YAPIONValue<>(key), yapionType);
    }

    @Deprecated
    @DeprecationInfo(since = "0.25.1", alternative = "containsKey")
    default boolean hasValue(double key, YAPIONType yapionType) {
        return containsKey(new YAPIONValue<>(key), yapionType);
    }

    @Deprecated
    @DeprecationInfo(since = "0.25.1", alternative = "containsKey")
    default boolean hasValue(@NonNull BigDecimal key, YAPIONType yapionType) {
        return containsKey(new YAPIONValue<>(key), yapionType);
    }

    @Deprecated
    @DeprecationInfo(since = "0.25.1", alternative = "containsKey")
    default <T> boolean hasValue(@NonNull String key, Class<T> type) {
        return containsKey(new YAPIONValue<>(key), type);
    }

    @Deprecated
    @DeprecationInfo(since = "0.25.1", alternative = "containsKey")
    default <T> boolean hasValue(char key, Class<T> type) {
        return containsKey(new YAPIONValue<>(key), type);
    }

    @Deprecated
    @DeprecationInfo(since = "0.25.1", alternative = "containsKey")
    default <T> boolean hasValue(boolean key, Class<T> type) {
        return containsKey(new YAPIONValue<>(key), type);
    }

    @Deprecated
    @DeprecationInfo(since = "0.25.1", alternative = "containsKey")
    default <T> boolean hasValue(byte key, Class<T> type) {
        return containsKey(new YAPIONValue<>(key), type);
    }

    @Deprecated
    @DeprecationInfo(since = "0.25.1", alternative = "containsKey")
    default <T> boolean hasValue(short key, Class<T> type) {
        return containsKey(new YAPIONValue<>(key), type);
    }

    @Deprecated
    @DeprecationInfo(since = "0.25.1", alternative = "containsKey")
    default <T> boolean hasValue(int key, Class<T> type) {
        return containsKey(new YAPIONValue<>(key), type);
    }

    @Deprecated
    @DeprecationInfo(since = "0.25.1", alternative = "containsKey")
    default <T> boolean hasValue(long key, Class<T> type) {
        return containsKey(new YAPIONValue<>(key), type);
    }

    @Deprecated
    @DeprecationInfo(since = "0.25.1", alternative = "containsKey")
    default <T> boolean hasValue(@NonNull BigInteger key, Class<T> type) {
        return containsKey(new YAPIONValue<>(key), type);
    }

    @Deprecated
    @DeprecationInfo(since = "0.25.1", alternative = "containsKey")
    default <T> boolean hasValue(float key, Class<T> type) {
        return containsKey(new YAPIONValue<>(key), type);
    }

    @Deprecated
    @DeprecationInfo(since = "0.25.1", alternative = "containsKey")
    default <T> boolean hasValue(double key, Class<T> type) {
        return containsKey(new YAPIONValue<>(key), type);
    }

    @Deprecated
    @DeprecationInfo(since = "0.25.1", alternative = "containsKey")
    default <T> boolean hasValue(@NonNull BigDecimal key, Class<T> type) {
        return containsKey(new YAPIONValue<>(key), type);
    }

    default <@YAPIONEveryType T> boolean containsKey(@NonNull T key) {
        if (key instanceof YAPIONAnyType) {
            return internalContainsKey((K) key);
        }
        if (!YAPIONValue.validType(key)) {
            throw new YAPIONClassTypeException();
        }
        return internalContainsKey((K) new YAPIONValue<>(key));
    }

    default <@YAPIONEveryType T> boolean containsKey(@NonNull T key, YAPIONType type) {
        if (key instanceof YAPIONAnyType) {
            return internalContainsKey((K) key, type);
        }
        if (!YAPIONValue.validType(key)) {
            throw new YAPIONClassTypeException();
        }
        return internalContainsKey((K) new YAPIONValue<>(key), type);
    }

    default <@YAPIONEveryType T, C> boolean containsKey(@NonNull T key, Class<C> type) {
        if (key instanceof YAPIONAnyType) {
            return internalContainsKey((K) key, type);
        }
        if (!YAPIONValue.validType(key)) {
            throw new YAPIONClassTypeException();
        }
        return internalContainsKey((K) new YAPIONValue<>(key), type);
    }

    default <@YAPIONEveryType T> YAPIONAnyType getYAPIONAnyType(@NonNull T key) {
        if (key instanceof YAPIONAnyType) {
            return internalGetYAPIONAnyType((K) key);
        }
        if (!YAPIONValue.validType(key)) {
            throw new YAPIONClassTypeException();
        }
        return internalGetYAPIONAnyType((K) new YAPIONValue<>(key));
    }

    default <@YAPIONEveryType T> YAPIONObject getObject(@NonNull T key) {
        YAPIONAnyType yapionAnyType = getYAPIONAnyType(key);
        if (yapionAnyType == null) return null;
        if (yapionAnyType instanceof YAPIONObject) {
            return (YAPIONObject) yapionAnyType;
        }
        return null;
    }

    default <@YAPIONEveryType T> void getObject(@NonNull T key, Consumer<YAPIONObject> valueConsumer, Runnable noValue) {
        YAPIONAnyType yapionAnyType = getYAPIONAnyType(key);
        if (yapionAnyType == null) {
            noValue.run();
            return;
        }
        if (yapionAnyType instanceof YAPIONObject) {
            valueConsumer.accept((YAPIONObject) yapionAnyType);
        }
    }

    default <@YAPIONEveryType T> YAPIONArray getArray(@NonNull T key) {
        YAPIONAnyType yapionAnyType = getYAPIONAnyType(key);
        if (yapionAnyType == null) return null;
        if (yapionAnyType instanceof YAPIONArray) {
            return (YAPIONArray) yapionAnyType;
        }
        return null;
    }

    default <@YAPIONEveryType T> void getArray(@NonNull T key, Consumer<YAPIONArray> valueConsumer, Runnable noValue) {
        YAPIONAnyType yapionAnyType = getYAPIONAnyType(key);
        if (yapionAnyType == null) {
            noValue.run();
            return;
        }
        if (yapionAnyType instanceof YAPIONArray) {
            valueConsumer.accept((YAPIONArray) yapionAnyType);
        }
    }

    default <@YAPIONEveryType T> YAPIONMap getMap(@NonNull T key) {
        YAPIONAnyType yapionAnyType = getYAPIONAnyType(key);
        if (yapionAnyType == null) return null;
        if (yapionAnyType instanceof YAPIONMap) {
            return (YAPIONMap) yapionAnyType;
        }
        return null;
    }

    default <@YAPIONEveryType T> void getMap(@NonNull T key, Consumer<YAPIONMap> valueConsumer, Runnable noValue) {
        YAPIONAnyType yapionAnyType = getYAPIONAnyType(key);
        if (yapionAnyType == null) {
            noValue.run();
            return;
        }
        if (yapionAnyType instanceof YAPIONMap) {
            valueConsumer.accept((YAPIONMap) yapionAnyType);
        }
    }

    default <@YAPIONEveryType T> YAPIONPointer getPointer(@NonNull T key) {
        YAPIONAnyType yapionAnyType = getYAPIONAnyType(key);
        if (yapionAnyType == null) return null;
        if (yapionAnyType instanceof YAPIONPointer) {
            return (YAPIONPointer) yapionAnyType;
        }
        return null;
    }

    default <@YAPIONEveryType T> void getPointer(@NonNull T key, Consumer<YAPIONPointer> valueConsumer, Runnable noValue) {
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
    default <@YAPIONEveryType T> YAPIONValue getValue(@NonNull T key) {
        YAPIONAnyType yapionAnyType = getYAPIONAnyType(key);
        if (yapionAnyType == null) return null;
        if (yapionAnyType instanceof YAPIONValue) {
            return (YAPIONValue) yapionAnyType;
        }
        return null;
    }

    @SuppressWarnings({"java:S3740"})
    default <@YAPIONEveryType T> void getValue(@NonNull T key, Consumer<YAPIONValue> valueConsumer, Runnable noValue) {
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
    default <@YAPIONEveryType T, C> YAPIONValue<C> getValue(@NonNull T key, Class<C> type) {
        if (ClassUtils.isPrimitive(type)) {
            return (YAPIONValue<C>) getValue(key, ClassUtils.getBoxed(type));
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
        return (YAPIONValue<C>) yapionAnyType;
    }

    @SuppressWarnings("unchecked")
    default <@YAPIONEveryType T, C> YAPIONValue<C> getValueOrDefault(@NonNull T key, Class<C> type, C defaultValue) {
        if (ClassUtils.isPrimitive(type)) {
            YAPIONValue<?> yapionValue = getValue(key, ClassUtils.getBoxed(type));
            if (yapionValue == null) {
                return new YAPIONValue<>(defaultValue);
            }
            return (YAPIONValue<C>) yapionValue;
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
        return (YAPIONValue<C>) yapionAnyType;
    }

    @SuppressWarnings("unchecked")
    default <@YAPIONEveryType T, C> void getValue(@NonNull T key, Class<C> type, Consumer<YAPIONValue<C>> valueConsumer, Runnable noValue) {
        if (ClassUtils.isPrimitive(type)) {
            YAPIONValue<?> yapionValue = getValue(key, ClassUtils.getBoxed(type));
            if (yapionValue == null) {
                noValue.run();
            }
            valueConsumer.accept((YAPIONValue<C>) yapionValue);
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
        valueConsumer.accept((YAPIONValue<C>) yapionAnyType);
    }

    @SuppressWarnings("unchecked")
    default <@YAPIONEveryType T, C> YAPIONValue<C> getValue(@NonNull T key, C type) {
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
        return (YAPIONValue<C>) yapionAnyType;
    }

    @SuppressWarnings("unchecked")
    default <@YAPIONEveryType T, C> YAPIONValue<C> getValueOrDefault(@NonNull T key, C defaultValue) {
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
        return (YAPIONValue<C>) yapionAnyType;
    }

    @SuppressWarnings("unchecked")
    @Deprecated
    @DeprecationInfo(since = "0.25.0")
    default <@YAPIONEveryType T, C> void getValue(@NonNull T key, C type, Consumer<YAPIONValue<C>> valueConsumer, Runnable noValue) {
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
        valueConsumer.accept((YAPIONValue<C>) yapionAnyType);
    }

    @SuppressWarnings("unchecked")
    default <@YAPIONEveryType T, C> C getPlainValue(@NonNull T key) {
        YAPIONValue<C> yapionValue = getValue(key);
        if (yapionValue == null) throw new YAPIONRetrieveException("Key '" + key.toString() + "' has no YAPIONValue associated with it");
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
            return;
        }
        valueConsumer.accept(yapionValue.get());
    }

}
