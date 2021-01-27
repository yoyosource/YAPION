// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020,2021 yoyosource

package yapion.utils;

import java.util.Optional;

public final class MethodReturnValue<T> {

    private static final MethodReturnValue<?> EMPTY = new MethodReturnValue<>();

    private final T value;

    private MethodReturnValue() {
        value = null;
    }

    private MethodReturnValue(T value) {
        this.value = value;
    }

    public static <T> MethodReturnValue<T> empty() {
        return (MethodReturnValue<T>) EMPTY;
    }

    public static <T> MethodReturnValue<T> of(T value) {
        return new MethodReturnValue<>(value);
    }

    public boolean isEmpty() {
        return this == EMPTY;
    }

    public boolean isPresent() {
        return this != EMPTY;
    }

    public boolean nonNullValuePresent() {
        return value != null;
    }

    public T get() {
        if (isEmpty()) throw new IllegalArgumentException();
        return value;
    }

    public Optional<T> asOptional() {
        return Optional.ofNullable(value);
    }
}