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
import yapion.annotations.api.InternalAPI;
import yapion.exceptions.utils.YAPIONRetrieveException;
import yapion.hierarchy.api.groups.YAPIONAnyType;
import yapion.hierarchy.api.internal.InternalMethods;
import yapion.hierarchy.types.YAPIONValue;

import java.math.BigDecimal;
import java.math.BigInteger;

public interface ValueRetrieve<I, K> extends InternalMethods<I, K> {

    @InternalAPI
    default <T> T genericGet(K key, @NonNull Class<T> type) {
        YAPIONAnyType yapionAnyType = internalGetAnyType(key);
        if (yapionAnyType == null) {
            throw new YAPIONRetrieveException("Key " + key + " has no value");
        }
        if (!(yapionAnyType instanceof YAPIONValue<?> yapionValue)) {
            throw new YAPIONRetrieveException("Key " + key + " is not a value");
        }
        if (!yapionValue.getValueType().equals(type.getTypeName())) {
            throw new YAPIONRetrieveException("Key " + key + " is not a " + type.getTypeName());
        }
        return (T) yapionValue.get();
    }

    @InternalAPI
    default <T> T genericGetOrDefault(K key, @NonNull Class<T> type, T defaultValue) {
        YAPIONAnyType yapionAnyType = internalGetAnyType(key);
        if (yapionAnyType == null) {
            return defaultValue;
        }
        if (!(yapionAnyType instanceof YAPIONValue<?> yapionValue)) {
            return defaultValue;
        }
        if (!yapionValue.getValueType().equals(type.getTypeName())) {
            return defaultValue;
        }
        return (T) yapionValue.get();
    }

    @InternalAPI
    default <T> T genericGetOrSetDefault(K key, @NonNull Class<T> type, T defaultValue) {
        YAPIONAnyType yapionAnyType = internalGetAnyType(key);
        if (yapionAnyType == null) {
            internalAdd(key, new YAPIONValue<>(defaultValue));
            return defaultValue;
        }
        if (!(yapionAnyType instanceof YAPIONValue<?> yapionValue)) {
            internalAdd(key, new YAPIONValue<>(defaultValue));
            return defaultValue;
        }
        if (!yapionValue.getValueType().equals(type.getTypeName())) {
            internalAdd(key, new YAPIONValue<>(defaultValue));
            return defaultValue;
        }
        return (T) yapionValue.get();
    }

    default boolean getBoolean(K key) {
        return genericGet(key, Boolean.class);
    }

    default boolean getBooleanOrDefault(K key, boolean value) {
        return genericGetOrDefault(key, Boolean.class, value);
    }

    default boolean getBooleanOrSetDefault(K key, boolean value) {
        return genericGetOrSetDefault(key, Boolean.class, value);
    }

    default byte getByte(K key) {
        return genericGet(key, Byte.class);
    }

    default byte getByteOrDefault(K key, byte value) {
        return genericGetOrDefault(key, Byte.class, value);
    }

    default byte getByteOrSetDefault(K key, byte value) {
        return genericGetOrSetDefault(key, Byte.class, value);
    }

    default short getShort(K key) {
        return genericGet(key, Short.class);
    }

    default short getShortOrDefault(K key, short value) {
        return genericGetOrDefault(key, Short.class, value);
    }

    default short getShortOrSetDefault(K key, short value) {
        return genericGetOrSetDefault(key, Short.class, value);
    }

    default int getInt(K key) {
        return genericGet(key, Integer.class);
    }

    default int getIntOrDefault(K key, int value) {
        return genericGetOrDefault(key, Integer.class, value);
    }

    default int getIntOrSetDefault(K key, int value) {
        return genericGetOrSetDefault(key, Integer.class, value);
    }

    default long getLong(K key) {
        return genericGet(key, Long.class);
    }

    default long getLongOrDefault(K key, long value) {
        return genericGetOrDefault(key, Long.class, value);
    }

    default long getLongOrSetDefault(K key, long value) {
        return genericGetOrSetDefault(key, Long.class, value);
    }

    default BigInteger getBigInteger(K key) {
        return genericGet(key, BigInteger.class);
    }

    default BigInteger getBigIntegerOrDefault(K key, BigInteger value) {
        return genericGetOrDefault(key, BigInteger.class, value);
    }

    default BigInteger getBigIntegerOrSetDefault(K key, BigInteger value) {
        return genericGetOrSetDefault(key, BigInteger.class, value);
    }

    default float getFloat(K key) {
        return genericGet(key, Float.class);
    }

    default float getFloatOrDefault(K key, float value) {
        return genericGetOrDefault(key, Float.class, value);
    }

    default float getFloatOrSetDefault(K key, float value) {
        return genericGetOrSetDefault(key, Float.class, value);
    }

    default double getDouble(K key) {
        return genericGet(key, Double.class);
    }

    default double getDoubleOrDefault(K key, double value) {
        return genericGetOrDefault(key, Double.class, value);
    }

    default double getDoubleOrSetDefault(K key, double value) {
        return genericGetOrSetDefault(key, Double.class, value);
    }

    default BigDecimal getBigDecimal(K key) {
        return genericGet(key, BigDecimal.class);
    }

    default BigDecimal getBigDecimalOrDefault(K key, BigDecimal value) {
        return genericGetOrDefault(key, BigDecimal.class, value);
    }

    default BigDecimal getBigDecimalOrSetDefault(K key, BigDecimal value) {
        return genericGetOrSetDefault(key, BigDecimal.class, value);
    }

    default char getChar(K key) {
        return genericGet(key, Character.class);
    }

    default char getCharOrDefault(K key, char value) {
        return genericGetOrDefault(key, Character.class, value);
    }

    default char getCharOrSetDefault(K key, char value) {
        return genericGetOrSetDefault(key, Character.class, value);
    }

    default String getString(K key) {
        return genericGet(key, String.class);
    }

    default String getStringOrDefault(K key, String value) {
        return genericGetOrDefault(key, String.class, value);
    }

    default String getStringOrSetDefault(K key, String value) {
        return genericGetOrSetDefault(key, String.class, value);
    }

    default Object getNull(K key) {
        if (internalContainsKey(key)) {
            return null;
        }
        throw new YAPIONRetrieveException("Key " + key + " has no value");
    }

    default Object getNullOrDefault(K key) {
        return null;
    }

    default Object getNullOrSetDefault(K key) {
        if (internalContainsKey(key)) {
            return null;
        }
        internalAdd(key, new YAPIONValue<>(null));
        return null;
    }
}
