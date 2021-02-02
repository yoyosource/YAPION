// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020,2021 yoyosource

package yapion.hierarchy.api.storage;

import lombok.NonNull;
import yapion.hierarchy.api.groups.YAPIONAnyType;
import yapion.hierarchy.types.YAPIONValue;

import java.math.BigDecimal;
import java.math.BigInteger;

public interface ArrayAdd<I, K> extends ObjectAdd<I, K> {

    I add(@NonNull YAPIONAnyType value);

    default I add(String value) {
        return add(new YAPIONValue<>(value));
    }

    default I add(char value) {
        return add(new YAPIONValue<>(value));
    }

    default I add(boolean value) {
        return add(new YAPIONValue<>(value));
    }

    default I add(byte value) {
        return add(new YAPIONValue<>(value));
    }

    default I add(short value) {
        return add(new YAPIONValue<>(value));
    }

    default I add(int value) {
        return add(new YAPIONValue<>(value));
    }

    default I add(long value) {
        return add(new YAPIONValue<>(value));
    }

    default I add(BigInteger value) {
        return add(new YAPIONValue<>(value));
    }

    default I add(float value) {
        return add(new YAPIONValue<>(value));
    }

    default I add(double value) {
        return add(new YAPIONValue<>(value));
    }

    default I add(BigDecimal value) {
        return add(new YAPIONValue<>(value));
    }

    /**
     * Optional API.
     */
    default I addOrPointer(@NonNull YAPIONAnyType value) {
        throw new UnsupportedOperationException();
    }

    I set(@NonNull K key, @NonNull YAPIONAnyType value);

    default I set(@NonNull K key, String value) {
        return set(key, new YAPIONValue<>(value));
    }

    default I set(@NonNull K key, char value) {
        return set(key, new YAPIONValue<>(value));
    }

    default I set(@NonNull K key, boolean value) {
        return set(key, new YAPIONValue<>(value));
    }

    default I set(@NonNull K key, byte value) {
        return set(key, new YAPIONValue<>(value));
    }

    default I set(@NonNull K key, short value) {
        return set(key, new YAPIONValue<>(value));
    }

    default I set(@NonNull K key, int value) {
        return set(key, new YAPIONValue<>(value));
    }

    default I set(@NonNull K key, long value) {
        return set(key, new YAPIONValue<>(value));
    }

    default I set(@NonNull K key, BigInteger value) {
        return set(key, new YAPIONValue<>(value));
    }

    default I set(@NonNull K key, float value) {
        return set(key, new YAPIONValue<>(value));
    }

    default I set(@NonNull K key, double value) {
        return set(key, new YAPIONValue<>(value));
    }

    default I set(@NonNull K key, BigDecimal value) {
        return set(key, new YAPIONValue<>(value));
    }

    /**
     * Optional API.
     */
    default I setOrPointer(@NonNull K key, @NonNull YAPIONAnyType value) {
        throw new UnsupportedOperationException();
    }

}