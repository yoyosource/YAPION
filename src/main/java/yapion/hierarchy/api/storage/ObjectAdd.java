// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020,2021 yoyosource

package yapion.hierarchy.api.storage;

import lombok.NonNull;
import yapion.hierarchy.api.OptionalAPI;
import yapion.hierarchy.api.groups.YAPIONAnyType;
import yapion.hierarchy.types.YAPIONValue;

import java.math.BigDecimal;
import java.math.BigInteger;

public interface ObjectAdd<I, K> {

    I add(@NonNull K key, @NonNull YAPIONAnyType value);

    default I add(@NonNull K key, String value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I add(@NonNull K key, char value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I add(@NonNull K key, boolean value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I add(@NonNull K key, byte value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I add(@NonNull K key, short value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I add(@NonNull K key, int value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I add(@NonNull K key, long value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I add(@NonNull K key, BigInteger value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I add(@NonNull K key, float value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I add(@NonNull K key, double value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I add(@NonNull K key, BigDecimal value) {
        return add(key, new YAPIONValue<>(value));
    }

    /**
     * Optional API.
     */
    @OptionalAPI
    default I addOrPointer(@NonNull K key, @NonNull YAPIONAnyType value) {
        throw new UnsupportedOperationException();
    }

}