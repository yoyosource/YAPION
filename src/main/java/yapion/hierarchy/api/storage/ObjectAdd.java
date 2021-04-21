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
import yapion.annotations.api.OptionalAPI;
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

    YAPIONAnyType addAndGetPrevious(@NonNull K key, @NonNull YAPIONAnyType value);

    default YAPIONAnyType addAndGetPrevious(@NonNull K key, String value) {
        return addAndGetPrevious(key, new YAPIONValue<>(value));
    }

    default YAPIONAnyType addAndGetPrevious(@NonNull K key, char value) {
        return addAndGetPrevious(key, new YAPIONValue<>(value));
    }

    default YAPIONAnyType addAndGetPrevious(@NonNull K key, boolean value) {
        return addAndGetPrevious(key, new YAPIONValue<>(value));
    }

    default YAPIONAnyType addAndGetPrevious(@NonNull K key, byte value) {
        return addAndGetPrevious(key, new YAPIONValue<>(value));
    }

    default YAPIONAnyType addAndGetPrevious(@NonNull K key, short value) {
        return addAndGetPrevious(key, new YAPIONValue<>(value));
    }

    default YAPIONAnyType addAndGetPrevious(@NonNull K key, int value) {
        return addAndGetPrevious(key, new YAPIONValue<>(value));
    }

    default YAPIONAnyType addAndGetPrevious(@NonNull K key, long value) {
        return addAndGetPrevious(key, new YAPIONValue<>(value));
    }

    default YAPIONAnyType addAndGetPrevious(@NonNull K key, BigInteger value) {
        return addAndGetPrevious(key, new YAPIONValue<>(value));
    }

    default YAPIONAnyType addAndGetPrevious(@NonNull K key, float value) {
        return addAndGetPrevious(key, new YAPIONValue<>(value));
    }

    default YAPIONAnyType addAndGetPrevious(@NonNull K key, double value) {
        return addAndGetPrevious(key, new YAPIONValue<>(value));
    }

    default YAPIONAnyType addAndGetPrevious(@NonNull K key, BigDecimal value) {
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

    default I putAndGetItself(@NonNull K key, String value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I putAndGetItself(@NonNull K key, char value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I putAndGetItself(@NonNull K key, boolean value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I putAndGetItself(@NonNull K key, byte value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I putAndGetItself(@NonNull K key, short value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I putAndGetItself(@NonNull K key, int value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I putAndGetItself(@NonNull K key, long value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I putAndGetItself(@NonNull K key, BigInteger value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I putAndGetItself(@NonNull K key, float value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I putAndGetItself(@NonNull K key, double value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I putAndGetItself(@NonNull K key, BigDecimal value) {
        return add(key, new YAPIONValue<>(value));
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

    default YAPIONAnyType put(@NonNull K key, String value) {
        return addAndGetPrevious(key, new YAPIONValue<>(value));
    }

    default YAPIONAnyType put(@NonNull K key, char value) {
        return addAndGetPrevious(key, new YAPIONValue<>(value));
    }

    default YAPIONAnyType put(@NonNull K key, boolean value) {
        return addAndGetPrevious(key, new YAPIONValue<>(value));
    }

    default YAPIONAnyType put(@NonNull K key, byte value) {
        return addAndGetPrevious(key, new YAPIONValue<>(value));
    }

    default YAPIONAnyType put(@NonNull K key, short value) {
        return addAndGetPrevious(key, new YAPIONValue<>(value));
    }

    default YAPIONAnyType put(@NonNull K key, int value) {
        return addAndGetPrevious(key, new YAPIONValue<>(value));
    }

    default YAPIONAnyType put(@NonNull K key, long value) {
        return addAndGetPrevious(key, new YAPIONValue<>(value));
    }

    default YAPIONAnyType put(@NonNull K key, BigInteger value) {
        return addAndGetPrevious(key, new YAPIONValue<>(value));
    }

    default YAPIONAnyType put(@NonNull K key, float value) {
        return addAndGetPrevious(key, new YAPIONValue<>(value));
    }

    default YAPIONAnyType put(@NonNull K key, double value) {
        return addAndGetPrevious(key, new YAPIONValue<>(value));
    }

    default YAPIONAnyType put(@NonNull K key, BigDecimal value) {
        return addAndGetPrevious(key, new YAPIONValue<>(value));
    }

    /**
     * Optional API.
     */
    @OptionalAPI
    default YAPIONAnyType putOrPointer(@NonNull K key, @NonNull YAPIONAnyType value) {
        return addOrPointerAndGetPrevious(key, value);
    }

}
