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

package yapion.utils;

import java.util.Optional;

public final class MethodReturnValue<T> {

    private static final MethodReturnValue<?> EMPTY = new MethodReturnValue<>(null);

    private final T value;

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
