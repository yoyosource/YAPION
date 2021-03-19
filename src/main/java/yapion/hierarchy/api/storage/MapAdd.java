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

    default I add(char key, @NonNull YAPIONAnyType value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I add(char key, String value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I add(char key, char value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I add(char key, boolean value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I add(char key, byte value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I add(char key, short value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I add(char key, int value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I add(char key, long value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I add(char key, BigInteger value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I add(char key, float value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I add(char key, double value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I add(char key, BigDecimal value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I add(boolean key, @NonNull YAPIONAnyType value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I add(boolean key, String value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I add(boolean key, char value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I add(boolean key, boolean value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I add(boolean key, byte value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I add(boolean key, short value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I add(boolean key, int value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I add(boolean key, long value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I add(boolean key, BigInteger value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I add(boolean key, float value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I add(boolean key, double value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I add(boolean key, BigDecimal value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I add(byte key, @NonNull YAPIONAnyType value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I add(byte key, String value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I add(byte key, char value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I add(byte key, boolean value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I add(byte key, byte value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I add(byte key, short value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I add(byte key, int value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I add(byte key, long value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I add(byte key, BigInteger value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I add(byte key, float value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I add(byte key, double value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I add(byte key, BigDecimal value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I add(short key, @NonNull YAPIONAnyType value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I add(short key, String value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I add(short key, char value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I add(short key, boolean value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I add(short key, byte value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I add(short key, short value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I add(short key, int value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I add(short key, long value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I add(short key, BigInteger value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I add(short key, float value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I add(short key, double value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I add(short key, BigDecimal value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I add(int key, @NonNull YAPIONAnyType value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I add(int key, String value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I add(int key, char value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I add(int key, boolean value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I add(int key, byte value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I add(int key, short value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I add(int key, int value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I add(int key, long value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I add(int key, BigInteger value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I add(int key, float value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I add(int key, double value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I add(int key, BigDecimal value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I add(long key, @NonNull YAPIONAnyType value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I add(long key, String value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I add(long key, char value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I add(long key, boolean value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I add(long key, byte value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I add(long key, short value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I add(long key, int value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I add(long key, long value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I add(long key, BigInteger value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I add(long key, float value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I add(long key, double value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I add(long key, BigDecimal value) {
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

    default I add(float key, @NonNull YAPIONAnyType value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I add(float key, String value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I add(float key, char value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I add(float key, boolean value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I add(float key, byte value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I add(float key, short value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I add(float key, int value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I add(float key, long value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I add(float key, BigInteger value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I add(float key, float value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I add(float key, double value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I add(float key, BigDecimal value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I add(double key, @NonNull YAPIONAnyType value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I add(double key, String value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I add(double key, char value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I add(double key, boolean value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I add(double key, byte value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I add(double key, short value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I add(double key, int value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I add(double key, long value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I add(double key, BigInteger value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I add(double key, float value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I add(double key, double value) {
        return add(key, new YAPIONValue<>(value));
    }

    default I add(double key, BigDecimal value) {
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

    default I addOrPointer(char key, @NonNull YAPIONAnyType value) {
        return addOrPointer(new YAPIONValue<>(key), value);
    }

    default I addOrPointer(boolean key, @NonNull YAPIONAnyType value) {
        return addOrPointer(new YAPIONValue<>(key), value);
    }

    default I addOrPointer(byte key, @NonNull YAPIONAnyType value) {
        return addOrPointer(new YAPIONValue<>(key), value);
    }

    default I addOrPointer(short key, @NonNull YAPIONAnyType value) {
        return addOrPointer(new YAPIONValue<>(key), value);
    }

    default I addOrPointer(int key, @NonNull YAPIONAnyType value) {
        return addOrPointer(new YAPIONValue<>(key), value);
    }

    default I addOrPointer(long key, @NonNull YAPIONAnyType value) {
        return addOrPointer(new YAPIONValue<>(key), value);
    }

    default I addOrPointer(@NonNull BigInteger key, @NonNull YAPIONAnyType value) {
        return addOrPointer(new YAPIONValue<>(key), value);
    }

    default I addOrPointer(float key, @NonNull YAPIONAnyType value) {
        return addOrPointer(new YAPIONValue<>(key), value);
    }

    default I addOrPointer(double key, @NonNull YAPIONAnyType value) {
        return addOrPointer(new YAPIONValue<>(key), value);
    }

    default I addOrPointer(@NonNull BigDecimal key, @NonNull YAPIONAnyType value) {
        return addOrPointer(new YAPIONValue<>(key), value);
    }

}
