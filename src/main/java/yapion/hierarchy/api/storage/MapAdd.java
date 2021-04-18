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

    default I add(@NonNull String key, @NonNull String value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I add(@NonNull String key, char value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I add(@NonNull String key, boolean value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I add(@NonNull String key, byte value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I add(@NonNull String key, short value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I add(@NonNull String key, int value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I add(@NonNull String key, long value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I add(@NonNull String key, @NonNull BigInteger value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I add(@NonNull String key, float value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I add(@NonNull String key, double value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I add(@NonNull String key, @NonNull BigDecimal value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I add(@NonNull String key, @NonNull YAPIONAnyType value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I add(char key, @NonNull String value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I add(char key, char value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I add(char key, boolean value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I add(char key, byte value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I add(char key, short value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I add(char key, int value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I add(char key, long value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I add(char key, @NonNull BigInteger value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I add(char key, float value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I add(char key, double value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I add(char key, @NonNull BigDecimal value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I add(char key, @NonNull YAPIONAnyType value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I add(boolean key, @NonNull String value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I add(boolean key, char value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I add(boolean key, boolean value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I add(boolean key, byte value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I add(boolean key, short value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I add(boolean key, int value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I add(boolean key, long value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I add(boolean key, @NonNull BigInteger value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I add(boolean key, float value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I add(boolean key, double value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I add(boolean key, @NonNull BigDecimal value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I add(boolean key, @NonNull YAPIONAnyType value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I add(byte key, @NonNull String value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I add(byte key, char value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I add(byte key, boolean value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I add(byte key, byte value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I add(byte key, short value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I add(byte key, int value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I add(byte key, long value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I add(byte key, @NonNull BigInteger value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I add(byte key, float value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I add(byte key, double value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I add(byte key, @NonNull BigDecimal value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I add(byte key, @NonNull YAPIONAnyType value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I add(short key, @NonNull String value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I add(short key, char value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I add(short key, boolean value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I add(short key, byte value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I add(short key, short value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I add(short key, int value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I add(short key, long value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I add(short key, @NonNull BigInteger value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I add(short key, float value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I add(short key, double value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I add(short key, @NonNull BigDecimal value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I add(short key, @NonNull YAPIONAnyType value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I add(int key, @NonNull String value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I add(int key, char value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I add(int key, boolean value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I add(int key, byte value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I add(int key, short value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I add(int key, int value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I add(int key, long value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I add(int key, @NonNull BigInteger value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I add(int key, float value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I add(int key, double value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I add(int key, @NonNull BigDecimal value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I add(int key, @NonNull YAPIONAnyType value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I add(long key, @NonNull String value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I add(long key, char value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I add(long key, boolean value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I add(long key, byte value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I add(long key, short value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I add(long key, int value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I add(long key, long value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I add(long key, @NonNull BigInteger value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I add(long key, float value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I add(long key, double value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I add(long key, @NonNull BigDecimal value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I add(long key, @NonNull YAPIONAnyType value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I add(@NonNull BigInteger key, @NonNull String value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I add(@NonNull BigInteger key, char value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I add(@NonNull BigInteger key, boolean value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I add(@NonNull BigInteger key, byte value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I add(@NonNull BigInteger key, short value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I add(@NonNull BigInteger key, int value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I add(@NonNull BigInteger key, long value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I add(@NonNull BigInteger key, @NonNull BigInteger value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I add(@NonNull BigInteger key, float value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I add(@NonNull BigInteger key, double value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I add(@NonNull BigInteger key, @NonNull BigDecimal value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I add(@NonNull BigInteger key, @NonNull YAPIONAnyType value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I add(float key, @NonNull String value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I add(float key, char value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I add(float key, boolean value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I add(float key, byte value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I add(float key, short value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I add(float key, int value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I add(float key, long value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I add(float key, @NonNull BigInteger value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I add(float key, float value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I add(float key, double value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I add(float key, @NonNull BigDecimal value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I add(float key, @NonNull YAPIONAnyType value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I add(double key, @NonNull String value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I add(double key, char value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I add(double key, boolean value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I add(double key, byte value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I add(double key, short value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I add(double key, int value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I add(double key, long value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I add(double key, @NonNull BigInteger value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I add(double key, float value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I add(double key, double value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I add(double key, @NonNull BigDecimal value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I add(double key, @NonNull YAPIONAnyType value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I add(@NonNull BigDecimal key, @NonNull String value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I add(@NonNull BigDecimal key, char value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I add(@NonNull BigDecimal key, boolean value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I add(@NonNull BigDecimal key, byte value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I add(@NonNull BigDecimal key, short value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I add(@NonNull BigDecimal key, int value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I add(@NonNull BigDecimal key, long value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I add(@NonNull BigDecimal key, @NonNull BigInteger value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I add(@NonNull BigDecimal key, float value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I add(@NonNull BigDecimal key, double value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I add(@NonNull BigDecimal key, @NonNull BigDecimal value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I add(@NonNull BigDecimal key, @NonNull YAPIONAnyType value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I addOrPointer(@NonNull String key, YAPIONAnyType value) {
        return addOrPointer(new YAPIONValue<>(key), value);
    }

    default I addOrPointer(char key, YAPIONAnyType value) {
        return addOrPointer(new YAPIONValue<>(key), value);
    }

    default I addOrPointer(boolean key, YAPIONAnyType value) {
        return addOrPointer(new YAPIONValue<>(key), value);
    }

    default I addOrPointer(byte key, YAPIONAnyType value) {
        return addOrPointer(new YAPIONValue<>(key), value);
    }

    default I addOrPointer(short key, YAPIONAnyType value) {
        return addOrPointer(new YAPIONValue<>(key), value);
    }

    default I addOrPointer(int key, YAPIONAnyType value) {
        return addOrPointer(new YAPIONValue<>(key), value);
    }

    default I addOrPointer(long key, YAPIONAnyType value) {
        return addOrPointer(new YAPIONValue<>(key), value);
    }

    default I addOrPointer(@NonNull BigInteger key, YAPIONAnyType value) {
        return addOrPointer(new YAPIONValue<>(key), value);
    }

    default I addOrPointer(float key, YAPIONAnyType value) {
        return addOrPointer(new YAPIONValue<>(key), value);
    }

    default I addOrPointer(double key, YAPIONAnyType value) {
        return addOrPointer(new YAPIONValue<>(key), value);
    }

    default I addOrPointer(@NonNull BigDecimal key, YAPIONAnyType value) {
        return addOrPointer(new YAPIONValue<>(key), value);
    }

    default I putAndGetItself(@NonNull String key, @NonNull String value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I putAndGetItself(@NonNull String key, char value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I putAndGetItself(@NonNull String key, boolean value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I putAndGetItself(@NonNull String key, byte value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I putAndGetItself(@NonNull String key, short value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I putAndGetItself(@NonNull String key, int value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I putAndGetItself(@NonNull String key, long value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I putAndGetItself(@NonNull String key, @NonNull BigInteger value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I putAndGetItself(@NonNull String key, float value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I putAndGetItself(@NonNull String key, double value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I putAndGetItself(@NonNull String key, @NonNull BigDecimal value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I putAndGetItself(@NonNull String key, @NonNull YAPIONAnyType value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I putAndGetItself(char key, @NonNull String value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I putAndGetItself(char key, char value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I putAndGetItself(char key, boolean value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I putAndGetItself(char key, byte value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I putAndGetItself(char key, short value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I putAndGetItself(char key, int value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I putAndGetItself(char key, long value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I putAndGetItself(char key, @NonNull BigInteger value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I putAndGetItself(char key, float value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I putAndGetItself(char key, double value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I putAndGetItself(char key, @NonNull BigDecimal value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I putAndGetItself(char key, @NonNull YAPIONAnyType value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I putAndGetItself(boolean key, @NonNull String value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I putAndGetItself(boolean key, char value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I putAndGetItself(boolean key, boolean value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I putAndGetItself(boolean key, byte value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I putAndGetItself(boolean key, short value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I putAndGetItself(boolean key, int value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I putAndGetItself(boolean key, long value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I putAndGetItself(boolean key, @NonNull BigInteger value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I putAndGetItself(boolean key, float value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I putAndGetItself(boolean key, double value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I putAndGetItself(boolean key, @NonNull BigDecimal value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I putAndGetItself(boolean key, @NonNull YAPIONAnyType value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I putAndGetItself(byte key, @NonNull String value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I putAndGetItself(byte key, char value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I putAndGetItself(byte key, boolean value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I putAndGetItself(byte key, byte value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I putAndGetItself(byte key, short value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I putAndGetItself(byte key, int value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I putAndGetItself(byte key, long value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I putAndGetItself(byte key, @NonNull BigInteger value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I putAndGetItself(byte key, float value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I putAndGetItself(byte key, double value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I putAndGetItself(byte key, @NonNull BigDecimal value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I putAndGetItself(byte key, @NonNull YAPIONAnyType value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I putAndGetItself(short key, @NonNull String value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I putAndGetItself(short key, char value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I putAndGetItself(short key, boolean value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I putAndGetItself(short key, byte value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I putAndGetItself(short key, short value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I putAndGetItself(short key, int value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I putAndGetItself(short key, long value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I putAndGetItself(short key, @NonNull BigInteger value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I putAndGetItself(short key, float value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I putAndGetItself(short key, double value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I putAndGetItself(short key, @NonNull BigDecimal value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I putAndGetItself(short key, @NonNull YAPIONAnyType value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I putAndGetItself(int key, @NonNull String value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I putAndGetItself(int key, char value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I putAndGetItself(int key, boolean value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I putAndGetItself(int key, byte value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I putAndGetItself(int key, short value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I putAndGetItself(int key, int value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I putAndGetItself(int key, long value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I putAndGetItself(int key, @NonNull BigInteger value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I putAndGetItself(int key, float value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I putAndGetItself(int key, double value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I putAndGetItself(int key, @NonNull BigDecimal value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I putAndGetItself(int key, @NonNull YAPIONAnyType value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I putAndGetItself(long key, @NonNull String value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I putAndGetItself(long key, char value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I putAndGetItself(long key, boolean value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I putAndGetItself(long key, byte value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I putAndGetItself(long key, short value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I putAndGetItself(long key, int value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I putAndGetItself(long key, long value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I putAndGetItself(long key, @NonNull BigInteger value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I putAndGetItself(long key, float value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I putAndGetItself(long key, double value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I putAndGetItself(long key, @NonNull BigDecimal value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I putAndGetItself(long key, @NonNull YAPIONAnyType value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I putAndGetItself(@NonNull BigInteger key, @NonNull String value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I putAndGetItself(@NonNull BigInteger key, char value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I putAndGetItself(@NonNull BigInteger key, boolean value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I putAndGetItself(@NonNull BigInteger key, byte value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I putAndGetItself(@NonNull BigInteger key, short value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I putAndGetItself(@NonNull BigInteger key, int value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I putAndGetItself(@NonNull BigInteger key, long value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I putAndGetItself(@NonNull BigInteger key, @NonNull BigInteger value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I putAndGetItself(@NonNull BigInteger key, float value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I putAndGetItself(@NonNull BigInteger key, double value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I putAndGetItself(@NonNull BigInteger key, @NonNull BigDecimal value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I putAndGetItself(@NonNull BigInteger key, @NonNull YAPIONAnyType value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I putAndGetItself(float key, @NonNull String value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I putAndGetItself(float key, char value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I putAndGetItself(float key, boolean value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I putAndGetItself(float key, byte value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I putAndGetItself(float key, short value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I putAndGetItself(float key, int value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I putAndGetItself(float key, long value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I putAndGetItself(float key, @NonNull BigInteger value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I putAndGetItself(float key, float value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I putAndGetItself(float key, double value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I putAndGetItself(float key, @NonNull BigDecimal value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I putAndGetItself(float key, @NonNull YAPIONAnyType value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I putAndGetItself(double key, @NonNull String value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I putAndGetItself(double key, char value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I putAndGetItself(double key, boolean value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I putAndGetItself(double key, byte value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I putAndGetItself(double key, short value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I putAndGetItself(double key, int value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I putAndGetItself(double key, long value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I putAndGetItself(double key, @NonNull BigInteger value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I putAndGetItself(double key, float value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I putAndGetItself(double key, double value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I putAndGetItself(double key, @NonNull BigDecimal value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I putAndGetItself(double key, @NonNull YAPIONAnyType value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I putAndGetItself(@NonNull BigDecimal key, @NonNull String value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I putAndGetItself(@NonNull BigDecimal key, char value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I putAndGetItself(@NonNull BigDecimal key, boolean value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I putAndGetItself(@NonNull BigDecimal key, byte value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I putAndGetItself(@NonNull BigDecimal key, short value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I putAndGetItself(@NonNull BigDecimal key, int value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I putAndGetItself(@NonNull BigDecimal key, long value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I putAndGetItself(@NonNull BigDecimal key, @NonNull BigInteger value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I putAndGetItself(@NonNull BigDecimal key, float value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I putAndGetItself(@NonNull BigDecimal key, double value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I putAndGetItself(@NonNull BigDecimal key, @NonNull BigDecimal value) {
        return add(new YAPIONValue<>(key), value);
    }

    default I putAndGetItself(@NonNull BigDecimal key, @NonNull YAPIONAnyType value) {
        return add(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType addAndGetPrevious(@NonNull String key, @NonNull String value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType addAndGetPrevious(@NonNull String key, char value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType addAndGetPrevious(@NonNull String key, boolean value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType addAndGetPrevious(@NonNull String key, byte value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType addAndGetPrevious(@NonNull String key, short value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType addAndGetPrevious(@NonNull String key, int value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType addAndGetPrevious(@NonNull String key, long value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType addAndGetPrevious(@NonNull String key, @NonNull BigInteger value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType addAndGetPrevious(@NonNull String key, float value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType addAndGetPrevious(@NonNull String key, double value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType addAndGetPrevious(@NonNull String key, @NonNull BigDecimal value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType addAndGetPrevious(@NonNull String key, @NonNull YAPIONAnyType value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType addAndGetPrevious(char key, @NonNull String value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType addAndGetPrevious(char key, char value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType addAndGetPrevious(char key, boolean value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType addAndGetPrevious(char key, byte value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType addAndGetPrevious(char key, short value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType addAndGetPrevious(char key, int value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType addAndGetPrevious(char key, long value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType addAndGetPrevious(char key, @NonNull BigInteger value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType addAndGetPrevious(char key, float value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType addAndGetPrevious(char key, double value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType addAndGetPrevious(char key, @NonNull BigDecimal value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType addAndGetPrevious(char key, @NonNull YAPIONAnyType value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType addAndGetPrevious(boolean key, @NonNull String value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType addAndGetPrevious(boolean key, char value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType addAndGetPrevious(boolean key, boolean value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType addAndGetPrevious(boolean key, byte value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType addAndGetPrevious(boolean key, short value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType addAndGetPrevious(boolean key, int value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType addAndGetPrevious(boolean key, long value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType addAndGetPrevious(boolean key, @NonNull BigInteger value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType addAndGetPrevious(boolean key, float value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType addAndGetPrevious(boolean key, double value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType addAndGetPrevious(boolean key, @NonNull BigDecimal value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType addAndGetPrevious(boolean key, @NonNull YAPIONAnyType value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType addAndGetPrevious(byte key, @NonNull String value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType addAndGetPrevious(byte key, char value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType addAndGetPrevious(byte key, boolean value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType addAndGetPrevious(byte key, byte value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType addAndGetPrevious(byte key, short value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType addAndGetPrevious(byte key, int value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType addAndGetPrevious(byte key, long value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType addAndGetPrevious(byte key, @NonNull BigInteger value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType addAndGetPrevious(byte key, float value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType addAndGetPrevious(byte key, double value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType addAndGetPrevious(byte key, @NonNull BigDecimal value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType addAndGetPrevious(byte key, @NonNull YAPIONAnyType value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType addAndGetPrevious(short key, @NonNull String value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType addAndGetPrevious(short key, char value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType addAndGetPrevious(short key, boolean value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType addAndGetPrevious(short key, byte value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType addAndGetPrevious(short key, short value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType addAndGetPrevious(short key, int value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType addAndGetPrevious(short key, long value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType addAndGetPrevious(short key, @NonNull BigInteger value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType addAndGetPrevious(short key, float value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType addAndGetPrevious(short key, double value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType addAndGetPrevious(short key, @NonNull BigDecimal value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType addAndGetPrevious(short key, @NonNull YAPIONAnyType value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType addAndGetPrevious(int key, @NonNull String value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType addAndGetPrevious(int key, char value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType addAndGetPrevious(int key, boolean value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType addAndGetPrevious(int key, byte value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType addAndGetPrevious(int key, short value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType addAndGetPrevious(int key, int value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType addAndGetPrevious(int key, long value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType addAndGetPrevious(int key, @NonNull BigInteger value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType addAndGetPrevious(int key, float value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType addAndGetPrevious(int key, double value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType addAndGetPrevious(int key, @NonNull BigDecimal value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType addAndGetPrevious(int key, @NonNull YAPIONAnyType value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType addAndGetPrevious(long key, @NonNull String value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType addAndGetPrevious(long key, char value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType addAndGetPrevious(long key, boolean value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType addAndGetPrevious(long key, byte value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType addAndGetPrevious(long key, short value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType addAndGetPrevious(long key, int value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType addAndGetPrevious(long key, long value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType addAndGetPrevious(long key, @NonNull BigInteger value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType addAndGetPrevious(long key, float value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType addAndGetPrevious(long key, double value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType addAndGetPrevious(long key, @NonNull BigDecimal value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType addAndGetPrevious(long key, @NonNull YAPIONAnyType value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType addAndGetPrevious(@NonNull BigInteger key, @NonNull String value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType addAndGetPrevious(@NonNull BigInteger key, char value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType addAndGetPrevious(@NonNull BigInteger key, boolean value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType addAndGetPrevious(@NonNull BigInteger key, byte value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType addAndGetPrevious(@NonNull BigInteger key, short value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType addAndGetPrevious(@NonNull BigInteger key, int value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType addAndGetPrevious(@NonNull BigInteger key, long value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType addAndGetPrevious(@NonNull BigInteger key, @NonNull BigInteger value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType addAndGetPrevious(@NonNull BigInteger key, float value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType addAndGetPrevious(@NonNull BigInteger key, double value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType addAndGetPrevious(@NonNull BigInteger key, @NonNull BigDecimal value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType addAndGetPrevious(@NonNull BigInteger key, @NonNull YAPIONAnyType value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType addAndGetPrevious(float key, @NonNull String value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType addAndGetPrevious(float key, char value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType addAndGetPrevious(float key, boolean value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType addAndGetPrevious(float key, byte value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType addAndGetPrevious(float key, short value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType addAndGetPrevious(float key, int value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType addAndGetPrevious(float key, long value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType addAndGetPrevious(float key, @NonNull BigInteger value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType addAndGetPrevious(float key, float value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType addAndGetPrevious(float key, double value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType addAndGetPrevious(float key, @NonNull BigDecimal value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType addAndGetPrevious(float key, @NonNull YAPIONAnyType value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType addAndGetPrevious(double key, @NonNull String value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType addAndGetPrevious(double key, char value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType addAndGetPrevious(double key, boolean value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType addAndGetPrevious(double key, byte value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType addAndGetPrevious(double key, short value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType addAndGetPrevious(double key, int value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType addAndGetPrevious(double key, long value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType addAndGetPrevious(double key, @NonNull BigInteger value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType addAndGetPrevious(double key, float value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType addAndGetPrevious(double key, double value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType addAndGetPrevious(double key, @NonNull BigDecimal value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType addAndGetPrevious(double key, @NonNull YAPIONAnyType value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType addAndGetPrevious(@NonNull BigDecimal key, @NonNull String value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType addAndGetPrevious(@NonNull BigDecimal key, char value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType addAndGetPrevious(@NonNull BigDecimal key, boolean value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType addAndGetPrevious(@NonNull BigDecimal key, byte value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType addAndGetPrevious(@NonNull BigDecimal key, short value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType addAndGetPrevious(@NonNull BigDecimal key, int value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType addAndGetPrevious(@NonNull BigDecimal key, long value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType addAndGetPrevious(@NonNull BigDecimal key, @NonNull BigInteger value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType addAndGetPrevious(@NonNull BigDecimal key, float value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType addAndGetPrevious(@NonNull BigDecimal key, double value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType addAndGetPrevious(@NonNull BigDecimal key, @NonNull BigDecimal value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType addAndGetPrevious(@NonNull BigDecimal key, @NonNull YAPIONAnyType value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType addOrPointerAndGetPrevious(@NonNull String key, YAPIONAnyType value) {
        return addOrPointerAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType addOrPointerAndGetPrevious(char key, YAPIONAnyType value) {
        return addOrPointerAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType addOrPointerAndGetPrevious(boolean key, YAPIONAnyType value) {
        return addOrPointerAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType addOrPointerAndGetPrevious(byte key, YAPIONAnyType value) {
        return addOrPointerAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType addOrPointerAndGetPrevious(short key, YAPIONAnyType value) {
        return addOrPointerAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType addOrPointerAndGetPrevious(int key, YAPIONAnyType value) {
        return addOrPointerAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType addOrPointerAndGetPrevious(long key, YAPIONAnyType value) {
        return addOrPointerAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType addOrPointerAndGetPrevious(@NonNull BigInteger key, YAPIONAnyType value) {
        return addOrPointerAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType addOrPointerAndGetPrevious(float key, YAPIONAnyType value) {
        return addOrPointerAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType addOrPointerAndGetPrevious(double key, YAPIONAnyType value) {
        return addOrPointerAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType addOrPointerAndGetPrevious(@NonNull BigDecimal key, YAPIONAnyType value) {
        return addOrPointerAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType put(@NonNull String key, @NonNull String value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType put(@NonNull String key, char value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType put(@NonNull String key, boolean value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType put(@NonNull String key, byte value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType put(@NonNull String key, short value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType put(@NonNull String key, int value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType put(@NonNull String key, long value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType put(@NonNull String key, @NonNull BigInteger value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType put(@NonNull String key, float value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType put(@NonNull String key, double value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType put(@NonNull String key, @NonNull BigDecimal value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType put(@NonNull String key, @NonNull YAPIONAnyType value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType put(char key, @NonNull String value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType put(char key, char value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType put(char key, boolean value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType put(char key, byte value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType put(char key, short value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType put(char key, int value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType put(char key, long value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType put(char key, @NonNull BigInteger value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType put(char key, float value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType put(char key, double value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType put(char key, @NonNull BigDecimal value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType put(char key, @NonNull YAPIONAnyType value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType put(boolean key, @NonNull String value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType put(boolean key, char value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType put(boolean key, boolean value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType put(boolean key, byte value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType put(boolean key, short value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType put(boolean key, int value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType put(boolean key, long value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType put(boolean key, @NonNull BigInteger value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType put(boolean key, float value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType put(boolean key, double value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType put(boolean key, @NonNull BigDecimal value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType put(boolean key, @NonNull YAPIONAnyType value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType put(byte key, @NonNull String value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType put(byte key, char value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType put(byte key, boolean value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType put(byte key, byte value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType put(byte key, short value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType put(byte key, int value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType put(byte key, long value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType put(byte key, @NonNull BigInteger value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType put(byte key, float value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType put(byte key, double value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType put(byte key, @NonNull BigDecimal value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType put(byte key, @NonNull YAPIONAnyType value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType put(short key, @NonNull String value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType put(short key, char value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType put(short key, boolean value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType put(short key, byte value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType put(short key, short value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType put(short key, int value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType put(short key, long value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType put(short key, @NonNull BigInteger value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType put(short key, float value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType put(short key, double value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType put(short key, @NonNull BigDecimal value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType put(short key, @NonNull YAPIONAnyType value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType put(int key, @NonNull String value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType put(int key, char value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType put(int key, boolean value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType put(int key, byte value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType put(int key, short value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType put(int key, int value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType put(int key, long value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType put(int key, @NonNull BigInteger value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType put(int key, float value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType put(int key, double value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType put(int key, @NonNull BigDecimal value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType put(int key, @NonNull YAPIONAnyType value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType put(long key, @NonNull String value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType put(long key, char value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType put(long key, boolean value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType put(long key, byte value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType put(long key, short value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType put(long key, int value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType put(long key, long value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType put(long key, @NonNull BigInteger value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType put(long key, float value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType put(long key, double value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType put(long key, @NonNull BigDecimal value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType put(long key, @NonNull YAPIONAnyType value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType put(@NonNull BigInteger key, @NonNull String value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType put(@NonNull BigInteger key, char value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType put(@NonNull BigInteger key, boolean value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType put(@NonNull BigInteger key, byte value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType put(@NonNull BigInteger key, short value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType put(@NonNull BigInteger key, int value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType put(@NonNull BigInteger key, long value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType put(@NonNull BigInteger key, @NonNull BigInteger value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType put(@NonNull BigInteger key, float value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType put(@NonNull BigInteger key, double value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType put(@NonNull BigInteger key, @NonNull BigDecimal value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType put(@NonNull BigInteger key, @NonNull YAPIONAnyType value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType put(float key, @NonNull String value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType put(float key, char value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType put(float key, boolean value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType put(float key, byte value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType put(float key, short value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType put(float key, int value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType put(float key, long value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType put(float key, @NonNull BigInteger value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType put(float key, float value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType put(float key, double value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType put(float key, @NonNull BigDecimal value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType put(float key, @NonNull YAPIONAnyType value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType put(double key, @NonNull String value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType put(double key, char value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType put(double key, boolean value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType put(double key, byte value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType put(double key, short value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType put(double key, int value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType put(double key, long value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType put(double key, @NonNull BigInteger value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType put(double key, float value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType put(double key, double value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType put(double key, @NonNull BigDecimal value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType put(double key, @NonNull YAPIONAnyType value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType put(@NonNull BigDecimal key, @NonNull String value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType put(@NonNull BigDecimal key, char value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType put(@NonNull BigDecimal key, boolean value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType put(@NonNull BigDecimal key, byte value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType put(@NonNull BigDecimal key, short value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType put(@NonNull BigDecimal key, int value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType put(@NonNull BigDecimal key, long value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType put(@NonNull BigDecimal key, @NonNull BigInteger value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType put(@NonNull BigDecimal key, float value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType put(@NonNull BigDecimal key, double value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType put(@NonNull BigDecimal key, @NonNull BigDecimal value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

    default YAPIONAnyType put(@NonNull BigDecimal key, @NonNull YAPIONAnyType value) {
        return addAndGetPrevious(new YAPIONValue<>(key), value);
    }

}
