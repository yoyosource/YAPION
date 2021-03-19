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
import yapion.hierarchy.api.groups.YAPIONAnyType;
import yapion.hierarchy.types.YAPIONValue;

import java.math.BigDecimal;
import java.math.BigInteger;

public interface MapAdd<I> extends ObjectAdd<I, YAPIONAnyType> {

    default I add(@NonNull String key, @NonNull YAPIONAnyType value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I add(@NonNull String key, String value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I add(@NonNull String key, char value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I add(@NonNull String key, boolean value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I add(@NonNull String key, byte value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I add(@NonNull String key, short value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I add(@NonNull String key, int value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I add(@NonNull String key, long value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I add(@NonNull String key, BigInteger value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I add(@NonNull String key, float value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I add(@NonNull String key, double value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I add(@NonNull String key, BigDecimal value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I add(@NonNull char key, @NonNull YAPIONAnyType value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I add(@NonNull char key, String value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I add(@NonNull char key, char value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I add(@NonNull char key, boolean value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I add(@NonNull char key, byte value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I add(@NonNull char key, short value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I add(@NonNull char key, int value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I add(@NonNull char key, long value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I add(@NonNull char key, BigInteger value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I add(@NonNull char key, float value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I add(@NonNull char key, double value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I add(@NonNull char key, BigDecimal value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I add(@NonNull boolean key, @NonNull YAPIONAnyType value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I add(@NonNull boolean key, String value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I add(@NonNull boolean key, char value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I add(@NonNull boolean key, boolean value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I add(@NonNull boolean key, byte value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I add(@NonNull boolean key, short value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I add(@NonNull boolean key, int value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I add(@NonNull boolean key, long value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I add(@NonNull boolean key, BigInteger value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I add(@NonNull boolean key, float value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I add(@NonNull boolean key, double value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I add(@NonNull boolean key, BigDecimal value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I add(@NonNull byte key, @NonNull YAPIONAnyType value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I add(@NonNull byte key, String value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I add(@NonNull byte key, char value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I add(@NonNull byte key, boolean value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I add(@NonNull byte key, byte value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I add(@NonNull byte key, short value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I add(@NonNull byte key, int value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I add(@NonNull byte key, long value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I add(@NonNull byte key, BigInteger value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I add(@NonNull byte key, float value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I add(@NonNull byte key, double value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I add(@NonNull byte key, BigDecimal value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I add(@NonNull short key, @NonNull YAPIONAnyType value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I add(@NonNull short key, String value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I add(@NonNull short key, char value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I add(@NonNull short key, boolean value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I add(@NonNull short key, byte value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I add(@NonNull short key, short value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I add(@NonNull short key, int value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I add(@NonNull short key, long value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I add(@NonNull short key, BigInteger value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I add(@NonNull short key, float value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I add(@NonNull short key, double value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I add(@NonNull short key, BigDecimal value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I add(@NonNull int key, @NonNull YAPIONAnyType value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I add(@NonNull int key, String value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I add(@NonNull int key, char value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I add(@NonNull int key, boolean value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I add(@NonNull int key, byte value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I add(@NonNull int key, short value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I add(@NonNull int key, int value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I add(@NonNull int key, long value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I add(@NonNull int key, BigInteger value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I add(@NonNull int key, float value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I add(@NonNull int key, double value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I add(@NonNull int key, BigDecimal value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I add(@NonNull long key, @NonNull YAPIONAnyType value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I add(@NonNull long key, String value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I add(@NonNull long key, char value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I add(@NonNull long key, boolean value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I add(@NonNull long key, byte value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I add(@NonNull long key, short value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I add(@NonNull long key, int value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I add(@NonNull long key, long value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I add(@NonNull long key, BigInteger value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I add(@NonNull long key, float value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I add(@NonNull long key, double value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I add(@NonNull long key, BigDecimal value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I add(@NonNull BigInteger key, @NonNull YAPIONAnyType value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I add(@NonNull BigInteger key, String value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I add(@NonNull BigInteger key, char value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I add(@NonNull BigInteger key, boolean value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I add(@NonNull BigInteger key, byte value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I add(@NonNull BigInteger key, short value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I add(@NonNull BigInteger key, int value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I add(@NonNull BigInteger key, long value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I add(@NonNull BigInteger key, BigInteger value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I add(@NonNull BigInteger key, float value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I add(@NonNull BigInteger key, double value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I add(@NonNull BigInteger key, BigDecimal value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I add(@NonNull float key, @NonNull YAPIONAnyType value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I add(@NonNull float key, String value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I add(@NonNull float key, char value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I add(@NonNull float key, boolean value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I add(@NonNull float key, byte value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I add(@NonNull float key, short value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I add(@NonNull float key, int value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I add(@NonNull float key, long value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I add(@NonNull float key, BigInteger value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I add(@NonNull float key, float value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I add(@NonNull float key, double value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I add(@NonNull float key, BigDecimal value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I add(@NonNull double key, @NonNull YAPIONAnyType value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I add(@NonNull double key, String value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I add(@NonNull double key, char value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I add(@NonNull double key, boolean value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I add(@NonNull double key, byte value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I add(@NonNull double key, short value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I add(@NonNull double key, int value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I add(@NonNull double key, long value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I add(@NonNull double key, BigInteger value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I add(@NonNull double key, float value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I add(@NonNull double key, double value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I add(@NonNull double key, BigDecimal value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I add(@NonNull BigDecimal key, @NonNull YAPIONAnyType value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I add(@NonNull BigDecimal key, String value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I add(@NonNull BigDecimal key, char value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I add(@NonNull BigDecimal key, boolean value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I add(@NonNull BigDecimal key, byte value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I add(@NonNull BigDecimal key, short value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I add(@NonNull BigDecimal key, int value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I add(@NonNull BigDecimal key, long value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I add(@NonNull BigDecimal key, BigInteger value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I add(@NonNull BigDecimal key, float value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I add(@NonNull BigDecimal key, double value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I add(@NonNull BigDecimal key, BigDecimal value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I addOrPointer(@NonNull String key, @NonNull YAPIONAnyType value) {
        return addOrPointer(new YAPIONValue<>(key), value);
    }

    default I addOrPointer(@NonNull char key, @NonNull YAPIONAnyType value) {
        return addOrPointer(new YAPIONValue<>(key), value);
    }

    default I addOrPointer(@NonNull boolean key, @NonNull YAPIONAnyType value) {
        return addOrPointer(new YAPIONValue<>(key), value);
    }

    default I addOrPointer(@NonNull byte key, @NonNull YAPIONAnyType value) {
        return addOrPointer(new YAPIONValue<>(key), value);
    }

    default I addOrPointer(@NonNull short key, @NonNull YAPIONAnyType value) {
        return addOrPointer(new YAPIONValue<>(key), value);
    }

    default I addOrPointer(@NonNull int key, @NonNull YAPIONAnyType value) {
        return addOrPointer(new YAPIONValue<>(key), value);
    }

    default I addOrPointer(@NonNull long key, @NonNull YAPIONAnyType value) {
        return addOrPointer(new YAPIONValue<>(key), value);
    }

    default I addOrPointer(@NonNull BigInteger key, @NonNull YAPIONAnyType value) {
        return addOrPointer(new YAPIONValue<>(key), value);
    }

    default I addOrPointer(@NonNull float key, @NonNull YAPIONAnyType value) {
        return addOrPointer(new YAPIONValue<>(key), value);
    }

    default I addOrPointer(@NonNull double key, @NonNull YAPIONAnyType value) {
        return addOrPointer(new YAPIONValue<>(key), value);
    }

    default I addOrPointer(@NonNull BigDecimal key, @NonNull YAPIONAnyType value) {
        return addOrPointer(new YAPIONValue<>(key), value);
    }

}
