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
import yapion.hierarchy.api.OptionalAPI;
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
    @OptionalAPI
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
    @OptionalAPI
    default I setOrPointer(@NonNull K key, @NonNull YAPIONAnyType value) {
        throw new UnsupportedOperationException();
    }

}
