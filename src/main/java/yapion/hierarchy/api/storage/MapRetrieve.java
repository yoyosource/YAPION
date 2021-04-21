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
import yapion.annotations.api.DeprecationInfo;
import yapion.hierarchy.api.groups.YAPIONAnyType;
import yapion.hierarchy.types.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.function.Consumer;

public interface MapRetrieve extends ObjectRetrieve<YAPIONAnyType> {

    @Deprecated
    @DeprecationInfo(since = "0.25.1", alternative = "containsKey")
    default boolean hasValue(@NonNull String key) {
        return containsKey(new YAPIONValue<>(key));
    }

    @Deprecated
    @DeprecationInfo(since = "0.25.1", alternative = "containsKey")
    default boolean hasValue(char key) {
        return containsKey(new YAPIONValue<>(key));
    }

    @Deprecated
    @DeprecationInfo(since = "0.25.1", alternative = "containsKey")
    default boolean hasValue(boolean key) {
        return containsKey(new YAPIONValue<>(key));
    }

    @Deprecated
    @DeprecationInfo(since = "0.25.1", alternative = "containsKey")
    default boolean hasValue(byte key) {
        return containsKey(new YAPIONValue<>(key));
    }

    @Deprecated
    @DeprecationInfo(since = "0.25.1", alternative = "containsKey")
    default boolean hasValue(short key) {
        return containsKey(new YAPIONValue<>(key));
    }

    @Deprecated
    @DeprecationInfo(since = "0.25.1", alternative = "containsKey")
    default boolean hasValue(int key) {
        return containsKey(new YAPIONValue<>(key));
    }

    @Deprecated
    @DeprecationInfo(since = "0.25.1", alternative = "containsKey")
    default boolean hasValue(long key) {
        return containsKey(new YAPIONValue<>(key));
    }

    @Deprecated
    @DeprecationInfo(since = "0.25.1", alternative = "containsKey")
    default boolean hasValue(@NonNull BigInteger key) {
        return containsKey(new YAPIONValue<>(key));
    }

    @Deprecated
    @DeprecationInfo(since = "0.25.1", alternative = "containsKey")
    default boolean hasValue(float key) {
        return containsKey(new YAPIONValue<>(key));
    }

    @Deprecated
    @DeprecationInfo(since = "0.25.1", alternative = "containsKey")
    default boolean hasValue(double key) {
        return containsKey(new YAPIONValue<>(key));
    }

    @Deprecated
    @DeprecationInfo(since = "0.25.1", alternative = "containsKey")
    default boolean hasValue(@NonNull BigDecimal key) {
        return containsKey(new YAPIONValue<>(key));
    }

    @Deprecated
    @DeprecationInfo(since = "0.25.1", alternative = "containsKey")
    default boolean hasValue(@NonNull String key, YAPIONType yapionType) {
        return containsKey(new YAPIONValue<>(key), yapionType);
    }

    @Deprecated
    @DeprecationInfo(since = "0.25.1", alternative = "containsKey")
    default boolean hasValue(char key, YAPIONType yapionType) {
        return containsKey(new YAPIONValue<>(key), yapionType);
    }

    @Deprecated
    @DeprecationInfo(since = "0.25.1", alternative = "containsKey")
    default boolean hasValue(boolean key, YAPIONType yapionType) {
        return containsKey(new YAPIONValue<>(key), yapionType);
    }

    @Deprecated
    @DeprecationInfo(since = "0.25.1", alternative = "containsKey")
    default boolean hasValue(byte key, YAPIONType yapionType) {
        return containsKey(new YAPIONValue<>(key), yapionType);
    }

    @Deprecated
    @DeprecationInfo(since = "0.25.1", alternative = "containsKey")
    default boolean hasValue(short key, YAPIONType yapionType) {
        return containsKey(new YAPIONValue<>(key), yapionType);
    }

    @Deprecated
    @DeprecationInfo(since = "0.25.1", alternative = "containsKey")
    default boolean hasValue(int key, YAPIONType yapionType) {
        return containsKey(new YAPIONValue<>(key), yapionType);
    }

    @Deprecated
    @DeprecationInfo(since = "0.25.1", alternative = "containsKey")
    default boolean hasValue(long key, YAPIONType yapionType) {
        return containsKey(new YAPIONValue<>(key), yapionType);
    }

    @Deprecated
    @DeprecationInfo(since = "0.25.1", alternative = "containsKey")
    default boolean hasValue(@NonNull BigInteger key, YAPIONType yapionType) {
        return containsKey(new YAPIONValue<>(key), yapionType);
    }

    @Deprecated
    @DeprecationInfo(since = "0.25.1", alternative = "containsKey")
    default boolean hasValue(float key, YAPIONType yapionType) {
        return containsKey(new YAPIONValue<>(key), yapionType);
    }

    @Deprecated
    @DeprecationInfo(since = "0.25.1", alternative = "containsKey")
    default boolean hasValue(double key, YAPIONType yapionType) {
        return containsKey(new YAPIONValue<>(key), yapionType);
    }

    @Deprecated
    @DeprecationInfo(since = "0.25.1", alternative = "containsKey")
    default boolean hasValue(@NonNull BigDecimal key, YAPIONType yapionType) {
        return containsKey(new YAPIONValue<>(key), yapionType);
    }

    @Deprecated
    @DeprecationInfo(since = "0.25.1", alternative = "containsKey")
    default <T> boolean hasValue(@NonNull String key, Class<T> type) {
        return containsKey(new YAPIONValue<>(key), type);
    }

    @Deprecated
    @DeprecationInfo(since = "0.25.1", alternative = "containsKey")
    default <T> boolean hasValue(char key, Class<T> type) {
        return containsKey(new YAPIONValue<>(key), type);
    }

    @Deprecated
    @DeprecationInfo(since = "0.25.1", alternative = "containsKey")
    default <T> boolean hasValue(boolean key, Class<T> type) {
        return containsKey(new YAPIONValue<>(key), type);
    }

    @Deprecated
    @DeprecationInfo(since = "0.25.1", alternative = "containsKey")
    default <T> boolean hasValue(byte key, Class<T> type) {
        return containsKey(new YAPIONValue<>(key), type);
    }

    @Deprecated
    @DeprecationInfo(since = "0.25.1", alternative = "containsKey")
    default <T> boolean hasValue(short key, Class<T> type) {
        return containsKey(new YAPIONValue<>(key), type);
    }

    @Deprecated
    @DeprecationInfo(since = "0.25.1", alternative = "containsKey")
    default <T> boolean hasValue(int key, Class<T> type) {
        return containsKey(new YAPIONValue<>(key), type);
    }

    @Deprecated
    @DeprecationInfo(since = "0.25.1", alternative = "containsKey")
    default <T> boolean hasValue(long key, Class<T> type) {
        return containsKey(new YAPIONValue<>(key), type);
    }

    @Deprecated
    @DeprecationInfo(since = "0.25.1", alternative = "containsKey")
    default <T> boolean hasValue(@NonNull BigInteger key, Class<T> type) {
        return containsKey(new YAPIONValue<>(key), type);
    }

    @Deprecated
    @DeprecationInfo(since = "0.25.1", alternative = "containsKey")
    default <T> boolean hasValue(float key, Class<T> type) {
        return containsKey(new YAPIONValue<>(key), type);
    }

    @Deprecated
    @DeprecationInfo(since = "0.25.1", alternative = "containsKey")
    default <T> boolean hasValue(double key, Class<T> type) {
        return containsKey(new YAPIONValue<>(key), type);
    }

    @Deprecated
    @DeprecationInfo(since = "0.25.1", alternative = "containsKey")
    default <T> boolean hasValue(@NonNull BigDecimal key, Class<T> type) {
        return containsKey(new YAPIONValue<>(key), type);
    }

    default boolean containsKey(@NonNull String key) {
        return containsKey(new YAPIONValue<>(key));
    }

    default boolean containsKey(char key) {
        return containsKey(new YAPIONValue<>(key));
    }

    default boolean containsKey(boolean key) {
        return containsKey(new YAPIONValue<>(key));
    }

    default boolean containsKey(byte key) {
        return containsKey(new YAPIONValue<>(key));
    }

    default boolean containsKey(short key) {
        return containsKey(new YAPIONValue<>(key));
    }

    default boolean containsKey(int key) {
        return containsKey(new YAPIONValue<>(key));
    }

    default boolean containsKey(long key) {
        return containsKey(new YAPIONValue<>(key));
    }

    default boolean containsKey(@NonNull BigInteger key) {
        return containsKey(new YAPIONValue<>(key));
    }

    default boolean containsKey(float key) {
        return containsKey(new YAPIONValue<>(key));
    }

    default boolean containsKey(double key) {
        return containsKey(new YAPIONValue<>(key));
    }

    default boolean containsKey(@NonNull BigDecimal key) {
        return containsKey(new YAPIONValue<>(key));
    }

    default boolean containsKey(@NonNull String key, YAPIONType yapionType) {
        return containsKey(new YAPIONValue<>(key), yapionType);
    }

    default boolean containsKey(char key, YAPIONType yapionType) {
        return containsKey(new YAPIONValue<>(key), yapionType);
    }

    default boolean containsKey(boolean key, YAPIONType yapionType) {
        return containsKey(new YAPIONValue<>(key), yapionType);
    }

    default boolean containsKey(byte key, YAPIONType yapionType) {
        return containsKey(new YAPIONValue<>(key), yapionType);
    }

    default boolean containsKey(short key, YAPIONType yapionType) {
        return containsKey(new YAPIONValue<>(key), yapionType);
    }

    default boolean containsKey(int key, YAPIONType yapionType) {
        return containsKey(new YAPIONValue<>(key), yapionType);
    }

    default boolean containsKey(long key, YAPIONType yapionType) {
        return containsKey(new YAPIONValue<>(key), yapionType);
    }

    default boolean containsKey(@NonNull BigInteger key, YAPIONType yapionType) {
        return containsKey(new YAPIONValue<>(key), yapionType);
    }

    default boolean containsKey(float key, YAPIONType yapionType) {
        return containsKey(new YAPIONValue<>(key), yapionType);
    }

    default boolean containsKey(double key, YAPIONType yapionType) {
        return containsKey(new YAPIONValue<>(key), yapionType);
    }

    default boolean containsKey(@NonNull BigDecimal key, YAPIONType yapionType) {
        return containsKey(new YAPIONValue<>(key), yapionType);
    }

    default <T> boolean containsKey(@NonNull String key, Class<T> type) {
        return containsKey(new YAPIONValue<>(key), type);
    }

    default <T> boolean containsKey(char key, Class<T> type) {
        return containsKey(new YAPIONValue<>(key), type);
    }

    default <T> boolean containsKey(boolean key, Class<T> type) {
        return containsKey(new YAPIONValue<>(key), type);
    }

    default <T> boolean containsKey(byte key, Class<T> type) {
        return containsKey(new YAPIONValue<>(key), type);
    }

    default <T> boolean containsKey(short key, Class<T> type) {
        return containsKey(new YAPIONValue<>(key), type);
    }

    default <T> boolean containsKey(int key, Class<T> type) {
        return containsKey(new YAPIONValue<>(key), type);
    }

    default <T> boolean containsKey(long key, Class<T> type) {
        return containsKey(new YAPIONValue<>(key), type);
    }

    default <T> boolean containsKey(@NonNull BigInteger key, Class<T> type) {
        return containsKey(new YAPIONValue<>(key), type);
    }

    default <T> boolean containsKey(float key, Class<T> type) {
        return containsKey(new YAPIONValue<>(key), type);
    }

    default <T> boolean containsKey(double key, Class<T> type) {
        return containsKey(new YAPIONValue<>(key), type);
    }

    default <T> boolean containsKey(@NonNull BigDecimal key, Class<T> type) {
        return containsKey(new YAPIONValue<>(key), type);
    }

    default YAPIONAnyType getYAPIONAnyType(@NonNull String key) {
        return getYAPIONAnyType(new YAPIONValue<>(key));
    }

    default YAPIONAnyType getYAPIONAnyType(char key) {
        return getYAPIONAnyType(new YAPIONValue<>(key));
    }

    default YAPIONAnyType getYAPIONAnyType(boolean key) {
        return getYAPIONAnyType(new YAPIONValue<>(key));
    }

    default YAPIONAnyType getYAPIONAnyType(byte key) {
        return getYAPIONAnyType(new YAPIONValue<>(key));
    }

    default YAPIONAnyType getYAPIONAnyType(short key) {
        return getYAPIONAnyType(new YAPIONValue<>(key));
    }

    default YAPIONAnyType getYAPIONAnyType(int key) {
        return getYAPIONAnyType(new YAPIONValue<>(key));
    }

    default YAPIONAnyType getYAPIONAnyType(long key) {
        return getYAPIONAnyType(new YAPIONValue<>(key));
    }

    default YAPIONAnyType getYAPIONAnyType(@NonNull BigInteger key) {
        return getYAPIONAnyType(new YAPIONValue<>(key));
    }

    default YAPIONAnyType getYAPIONAnyType(float key) {
        return getYAPIONAnyType(new YAPIONValue<>(key));
    }

    default YAPIONAnyType getYAPIONAnyType(double key) {
        return getYAPIONAnyType(new YAPIONValue<>(key));
    }

    default YAPIONAnyType getYAPIONAnyType(@NonNull BigDecimal key) {
        return getYAPIONAnyType(new YAPIONValue<>(key));
    }

    default YAPIONObject getObject(@NonNull String key) {
        return getObject(new YAPIONValue<>(key));
    }

    default YAPIONObject getObject(char key) {
        return getObject(new YAPIONValue<>(key));
    }

    default YAPIONObject getObject(boolean key) {
        return getObject(new YAPIONValue<>(key));
    }

    default YAPIONObject getObject(byte key) {
        return getObject(new YAPIONValue<>(key));
    }

    default YAPIONObject getObject(short key) {
        return getObject(new YAPIONValue<>(key));
    }

    default YAPIONObject getObject(int key) {
        return getObject(new YAPIONValue<>(key));
    }

    default YAPIONObject getObject(long key) {
        return getObject(new YAPIONValue<>(key));
    }

    default YAPIONObject getObject(@NonNull BigInteger key) {
        return getObject(new YAPIONValue<>(key));
    }

    default YAPIONObject getObject(float key) {
        return getObject(new YAPIONValue<>(key));
    }

    default YAPIONObject getObject(double key) {
        return getObject(new YAPIONValue<>(key));
    }

    default YAPIONObject getObject(@NonNull BigDecimal key) {
        return getObject(new YAPIONValue<>(key));
    }

    default void getObject(@NonNull String key, Consumer<YAPIONObject> valueConsumer, Runnable noValue) {
        getObject(new YAPIONValue<>(key), valueConsumer, noValue);
    }

    default void getObject(char key, Consumer<YAPIONObject> valueConsumer, Runnable noValue) {
        getObject(new YAPIONValue<>(key), valueConsumer, noValue);
    }

    default void getObject(boolean key, Consumer<YAPIONObject> valueConsumer, Runnable noValue) {
        getObject(new YAPIONValue<>(key), valueConsumer, noValue);
    }

    default void getObject(byte key, Consumer<YAPIONObject> valueConsumer, Runnable noValue) {
        getObject(new YAPIONValue<>(key), valueConsumer, noValue);
    }

    default void getObject(short key, Consumer<YAPIONObject> valueConsumer, Runnable noValue) {
        getObject(new YAPIONValue<>(key), valueConsumer, noValue);
    }

    default void getObject(int key, Consumer<YAPIONObject> valueConsumer, Runnable noValue) {
        getObject(new YAPIONValue<>(key), valueConsumer, noValue);
    }

    default void getObject(long key, Consumer<YAPIONObject> valueConsumer, Runnable noValue) {
        getObject(new YAPIONValue<>(key), valueConsumer, noValue);
    }

    default void getObject(@NonNull BigInteger key, Consumer<YAPIONObject> valueConsumer, Runnable noValue) {
        getObject(new YAPIONValue<>(key), valueConsumer, noValue);
    }

    default void getObject(float key, Consumer<YAPIONObject> valueConsumer, Runnable noValue) {
        getObject(new YAPIONValue<>(key), valueConsumer, noValue);
    }

    default void getObject(double key, Consumer<YAPIONObject> valueConsumer, Runnable noValue) {
        getObject(new YAPIONValue<>(key), valueConsumer, noValue);
    }

    default void getObject(@NonNull BigDecimal key, Consumer<YAPIONObject> valueConsumer, Runnable noValue) {
        getObject(new YAPIONValue<>(key), valueConsumer, noValue);
    }

    default YAPIONArray getArray(@NonNull String key) {
        return getArray(new YAPIONValue<>(key));
    }

    default YAPIONArray getArray(char key) {
        return getArray(new YAPIONValue<>(key));
    }

    default YAPIONArray getArray(boolean key) {
        return getArray(new YAPIONValue<>(key));
    }

    default YAPIONArray getArray(byte key) {
        return getArray(new YAPIONValue<>(key));
    }

    default YAPIONArray getArray(short key) {
        return getArray(new YAPIONValue<>(key));
    }

    default YAPIONArray getArray(int key) {
        return getArray(new YAPIONValue<>(key));
    }

    default YAPIONArray getArray(long key) {
        return getArray(new YAPIONValue<>(key));
    }

    default YAPIONArray getArray(@NonNull BigInteger key) {
        return getArray(new YAPIONValue<>(key));
    }

    default YAPIONArray getArray(float key) {
        return getArray(new YAPIONValue<>(key));
    }

    default YAPIONArray getArray(double key) {
        return getArray(new YAPIONValue<>(key));
    }

    default YAPIONArray getArray(@NonNull BigDecimal key) {
        return getArray(new YAPIONValue<>(key));
    }

    default void getArray(@NonNull String key, Consumer<YAPIONArray> valueConsumer, Runnable noValue) {
        getArray(new YAPIONValue<>(key), valueConsumer, noValue);
    }

    default void getArray(char key, Consumer<YAPIONArray> valueConsumer, Runnable noValue) {
        getArray(new YAPIONValue<>(key), valueConsumer, noValue);
    }

    default void getArray(boolean key, Consumer<YAPIONArray> valueConsumer, Runnable noValue) {
        getArray(new YAPIONValue<>(key), valueConsumer, noValue);
    }

    default void getArray(byte key, Consumer<YAPIONArray> valueConsumer, Runnable noValue) {
        getArray(new YAPIONValue<>(key), valueConsumer, noValue);
    }

    default void getArray(short key, Consumer<YAPIONArray> valueConsumer, Runnable noValue) {
        getArray(new YAPIONValue<>(key), valueConsumer, noValue);
    }

    default void getArray(int key, Consumer<YAPIONArray> valueConsumer, Runnable noValue) {
        getArray(new YAPIONValue<>(key), valueConsumer, noValue);
    }

    default void getArray(long key, Consumer<YAPIONArray> valueConsumer, Runnable noValue) {
        getArray(new YAPIONValue<>(key), valueConsumer, noValue);
    }

    default void getArray(@NonNull BigInteger key, Consumer<YAPIONArray> valueConsumer, Runnable noValue) {
        getArray(new YAPIONValue<>(key), valueConsumer, noValue);
    }

    default void getArray(float key, Consumer<YAPIONArray> valueConsumer, Runnable noValue) {
        getArray(new YAPIONValue<>(key), valueConsumer, noValue);
    }

    default void getArray(double key, Consumer<YAPIONArray> valueConsumer, Runnable noValue) {
        getArray(new YAPIONValue<>(key), valueConsumer, noValue);
    }

    default void getArray(@NonNull BigDecimal key, Consumer<YAPIONArray> valueConsumer, Runnable noValue) {
        getArray(new YAPIONValue<>(key), valueConsumer, noValue);
    }

    default YAPIONMap getMap(@NonNull String key) {
        return getMap(new YAPIONValue<>(key));
    }

    default YAPIONMap getMap(char key) {
        return getMap(new YAPIONValue<>(key));
    }

    default YAPIONMap getMap(boolean key) {
        return getMap(new YAPIONValue<>(key));
    }

    default YAPIONMap getMap(byte key) {
        return getMap(new YAPIONValue<>(key));
    }

    default YAPIONMap getMap(short key) {
        return getMap(new YAPIONValue<>(key));
    }

    default YAPIONMap getMap(int key) {
        return getMap(new YAPIONValue<>(key));
    }

    default YAPIONMap getMap(long key) {
        return getMap(new YAPIONValue<>(key));
    }

    default YAPIONMap getMap(@NonNull BigInteger key) {
        return getMap(new YAPIONValue<>(key));
    }

    default YAPIONMap getMap(float key) {
        return getMap(new YAPIONValue<>(key));
    }

    default YAPIONMap getMap(double key) {
        return getMap(new YAPIONValue<>(key));
    }

    default YAPIONMap getMap(@NonNull BigDecimal key) {
        return getMap(new YAPIONValue<>(key));
    }

    default void getMap(@NonNull String key, Consumer<YAPIONMap> valueConsumer, Runnable noValue) {
        getMap(new YAPIONValue<>(key), valueConsumer, noValue);
    }

    default void getMap(char key, Consumer<YAPIONMap> valueConsumer, Runnable noValue) {
        getMap(new YAPIONValue<>(key), valueConsumer, noValue);
    }

    default void getMap(boolean key, Consumer<YAPIONMap> valueConsumer, Runnable noValue) {
        getMap(new YAPIONValue<>(key), valueConsumer, noValue);
    }

    default void getMap(byte key, Consumer<YAPIONMap> valueConsumer, Runnable noValue) {
        getMap(new YAPIONValue<>(key), valueConsumer, noValue);
    }

    default void getMap(short key, Consumer<YAPIONMap> valueConsumer, Runnable noValue) {
        getMap(new YAPIONValue<>(key), valueConsumer, noValue);
    }

    default void getMap(int key, Consumer<YAPIONMap> valueConsumer, Runnable noValue) {
        getMap(new YAPIONValue<>(key), valueConsumer, noValue);
    }

    default void getMap(long key, Consumer<YAPIONMap> valueConsumer, Runnable noValue) {
        getMap(new YAPIONValue<>(key), valueConsumer, noValue);
    }

    default void getMap(@NonNull BigInteger key, Consumer<YAPIONMap> valueConsumer, Runnable noValue) {
        getMap(new YAPIONValue<>(key), valueConsumer, noValue);
    }

    default void getMap(float key, Consumer<YAPIONMap> valueConsumer, Runnable noValue) {
        getMap(new YAPIONValue<>(key), valueConsumer, noValue);
    }

    default void getMap(double key, Consumer<YAPIONMap> valueConsumer, Runnable noValue) {
        getMap(new YAPIONValue<>(key), valueConsumer, noValue);
    }

    default void getMap(@NonNull BigDecimal key, Consumer<YAPIONMap> valueConsumer, Runnable noValue) {
        getMap(new YAPIONValue<>(key), valueConsumer, noValue);
    }

    default YAPIONPointer getPointer(@NonNull String key) {
        return getPointer(new YAPIONValue<>(key));
    }

    default YAPIONPointer getPointer(char key) {
        return getPointer(new YAPIONValue<>(key));
    }

    default YAPIONPointer getPointer(boolean key) {
        return getPointer(new YAPIONValue<>(key));
    }

    default YAPIONPointer getPointer(byte key) {
        return getPointer(new YAPIONValue<>(key));
    }

    default YAPIONPointer getPointer(short key) {
        return getPointer(new YAPIONValue<>(key));
    }

    default YAPIONPointer getPointer(int key) {
        return getPointer(new YAPIONValue<>(key));
    }

    default YAPIONPointer getPointer(long key) {
        return getPointer(new YAPIONValue<>(key));
    }

    default YAPIONPointer getPointer(@NonNull BigInteger key) {
        return getPointer(new YAPIONValue<>(key));
    }

    default YAPIONPointer getPointer(float key) {
        return getPointer(new YAPIONValue<>(key));
    }

    default YAPIONPointer getPointer(double key) {
        return getPointer(new YAPIONValue<>(key));
    }

    default YAPIONPointer getPointer(@NonNull BigDecimal key) {
        return getPointer(new YAPIONValue<>(key));
    }

    default void getPointer(@NonNull String key, Consumer<YAPIONPointer> valueConsumer, Runnable noValue) {
        getPointer(new YAPIONValue<>(key), valueConsumer, noValue);
    }

    default void getPointer(char key, Consumer<YAPIONPointer> valueConsumer, Runnable noValue) {
        getPointer(new YAPIONValue<>(key), valueConsumer, noValue);
    }

    default void getPointer(boolean key, Consumer<YAPIONPointer> valueConsumer, Runnable noValue) {
        getPointer(new YAPIONValue<>(key), valueConsumer, noValue);
    }

    default void getPointer(byte key, Consumer<YAPIONPointer> valueConsumer, Runnable noValue) {
        getPointer(new YAPIONValue<>(key), valueConsumer, noValue);
    }

    default void getPointer(short key, Consumer<YAPIONPointer> valueConsumer, Runnable noValue) {
        getPointer(new YAPIONValue<>(key), valueConsumer, noValue);
    }

    default void getPointer(int key, Consumer<YAPIONPointer> valueConsumer, Runnable noValue) {
        getPointer(new YAPIONValue<>(key), valueConsumer, noValue);
    }

    default void getPointer(long key, Consumer<YAPIONPointer> valueConsumer, Runnable noValue) {
        getPointer(new YAPIONValue<>(key), valueConsumer, noValue);
    }

    default void getPointer(@NonNull BigInteger key, Consumer<YAPIONPointer> valueConsumer, Runnable noValue) {
        getPointer(new YAPIONValue<>(key), valueConsumer, noValue);
    }

    default void getPointer(float key, Consumer<YAPIONPointer> valueConsumer, Runnable noValue) {
        getPointer(new YAPIONValue<>(key), valueConsumer, noValue);
    }

    default void getPointer(double key, Consumer<YAPIONPointer> valueConsumer, Runnable noValue) {
        getPointer(new YAPIONValue<>(key), valueConsumer, noValue);
    }

    default void getPointer(@NonNull BigDecimal key, Consumer<YAPIONPointer> valueConsumer, Runnable noValue) {
        getPointer(new YAPIONValue<>(key), valueConsumer, noValue);
    }

    default YAPIONValue getValue(@NonNull String key) {
        return getValue(new YAPIONValue<>(key));
    }

    default YAPIONValue getValue(char key) {
        return getValue(new YAPIONValue<>(key));
    }

    default YAPIONValue getValue(boolean key) {
        return getValue(new YAPIONValue<>(key));
    }

    default YAPIONValue getValue(byte key) {
        return getValue(new YAPIONValue<>(key));
    }

    default YAPIONValue getValue(short key) {
        return getValue(new YAPIONValue<>(key));
    }

    default YAPIONValue getValue(int key) {
        return getValue(new YAPIONValue<>(key));
    }

    default YAPIONValue getValue(long key) {
        return getValue(new YAPIONValue<>(key));
    }

    default YAPIONValue getValue(@NonNull BigInteger key) {
        return getValue(new YAPIONValue<>(key));
    }

    default YAPIONValue getValue(float key) {
        return getValue(new YAPIONValue<>(key));
    }

    default YAPIONValue getValue(double key) {
        return getValue(new YAPIONValue<>(key));
    }

    default YAPIONValue getValue(@NonNull BigDecimal key) {
        return getValue(new YAPIONValue<>(key));
    }

    default void getValue(@NonNull String key, Consumer<YAPIONValue> valueConsumer, Runnable noValue) {
        getValue(new YAPIONValue<>(key), valueConsumer, noValue);
    }

    default void getValue(char key, Consumer<YAPIONValue> valueConsumer, Runnable noValue) {
        getValue(new YAPIONValue<>(key), valueConsumer, noValue);
    }

    default void getValue(boolean key, Consumer<YAPIONValue> valueConsumer, Runnable noValue) {
        getValue(new YAPIONValue<>(key), valueConsumer, noValue);
    }

    default void getValue(byte key, Consumer<YAPIONValue> valueConsumer, Runnable noValue) {
        getValue(new YAPIONValue<>(key), valueConsumer, noValue);
    }

    default void getValue(short key, Consumer<YAPIONValue> valueConsumer, Runnable noValue) {
        getValue(new YAPIONValue<>(key), valueConsumer, noValue);
    }

    default void getValue(int key, Consumer<YAPIONValue> valueConsumer, Runnable noValue) {
        getValue(new YAPIONValue<>(key), valueConsumer, noValue);
    }

    default void getValue(long key, Consumer<YAPIONValue> valueConsumer, Runnable noValue) {
        getValue(new YAPIONValue<>(key), valueConsumer, noValue);
    }

    default void getValue(@NonNull BigInteger key, Consumer<YAPIONValue> valueConsumer, Runnable noValue) {
        getValue(new YAPIONValue<>(key), valueConsumer, noValue);
    }

    default void getValue(float key, Consumer<YAPIONValue> valueConsumer, Runnable noValue) {
        getValue(new YAPIONValue<>(key), valueConsumer, noValue);
    }

    default void getValue(double key, Consumer<YAPIONValue> valueConsumer, Runnable noValue) {
        getValue(new YAPIONValue<>(key), valueConsumer, noValue);
    }

    default void getValue(@NonNull BigDecimal key, Consumer<YAPIONValue> valueConsumer, Runnable noValue) {
        getValue(new YAPIONValue<>(key), valueConsumer, noValue);
    }

    default <T> YAPIONValue<T> getValue(@NonNull String key, Class<T> type) {
        return getValue(new YAPIONValue<>(key), type);
    }

    default <T> YAPIONValue<T> getValue(char key, Class<T> type) {
        return getValue(new YAPIONValue<>(key), type);
    }

    default <T> YAPIONValue<T> getValue(boolean key, Class<T> type) {
        return getValue(new YAPIONValue<>(key), type);
    }

    default <T> YAPIONValue<T> getValue(byte key, Class<T> type) {
        return getValue(new YAPIONValue<>(key), type);
    }

    default <T> YAPIONValue<T> getValue(short key, Class<T> type) {
        return getValue(new YAPIONValue<>(key), type);
    }

    default <T> YAPIONValue<T> getValue(int key, Class<T> type) {
        return getValue(new YAPIONValue<>(key), type);
    }

    default <T> YAPIONValue<T> getValue(long key, Class<T> type) {
        return getValue(new YAPIONValue<>(key), type);
    }

    default <T> YAPIONValue<T> getValue(@NonNull BigInteger key, Class<T> type) {
        return getValue(new YAPIONValue<>(key), type);
    }

    default <T> YAPIONValue<T> getValue(float key, Class<T> type) {
        return getValue(new YAPIONValue<>(key), type);
    }

    default <T> YAPIONValue<T> getValue(double key, Class<T> type) {
        return getValue(new YAPIONValue<>(key), type);
    }

    default <T> YAPIONValue<T> getValue(@NonNull BigDecimal key, Class<T> type) {
        return getValue(new YAPIONValue<>(key), type);
    }

    default <T> YAPIONValue<T> getValueOrDefault(@NonNull String key, Class<T> type, T defaultValue) {
        return getValueOrDefault(new YAPIONValue<>(key), type, defaultValue);
    }

    default <T> YAPIONValue<T> getValueOrDefault(char key, Class<T> type, T defaultValue) {
        return getValueOrDefault(new YAPIONValue<>(key), type, defaultValue);
    }

    default <T> YAPIONValue<T> getValueOrDefault(boolean key, Class<T> type, T defaultValue) {
        return getValueOrDefault(new YAPIONValue<>(key), type, defaultValue);
    }

    default <T> YAPIONValue<T> getValueOrDefault(byte key, Class<T> type, T defaultValue) {
        return getValueOrDefault(new YAPIONValue<>(key), type, defaultValue);
    }

    default <T> YAPIONValue<T> getValueOrDefault(short key, Class<T> type, T defaultValue) {
        return getValueOrDefault(new YAPIONValue<>(key), type, defaultValue);
    }

    default <T> YAPIONValue<T> getValueOrDefault(int key, Class<T> type, T defaultValue) {
        return getValueOrDefault(new YAPIONValue<>(key), type, defaultValue);
    }

    default <T> YAPIONValue<T> getValueOrDefault(long key, Class<T> type, T defaultValue) {
        return getValueOrDefault(new YAPIONValue<>(key), type, defaultValue);
    }

    default <T> YAPIONValue<T> getValueOrDefault(@NonNull BigInteger key, Class<T> type, T defaultValue) {
        return getValueOrDefault(new YAPIONValue<>(key), type, defaultValue);
    }

    default <T> YAPIONValue<T> getValueOrDefault(float key, Class<T> type, T defaultValue) {
        return getValueOrDefault(new YAPIONValue<>(key), type, defaultValue);
    }

    default <T> YAPIONValue<T> getValueOrDefault(double key, Class<T> type, T defaultValue) {
        return getValueOrDefault(new YAPIONValue<>(key), type, defaultValue);
    }

    default <T> YAPIONValue<T> getValueOrDefault(@NonNull BigDecimal key, Class<T> type, T defaultValue) {
        return getValueOrDefault(new YAPIONValue<>(key), type, defaultValue);
    }

    default <T> void getValue(@NonNull String key, Class<T> type, Consumer<YAPIONValue<T>> valueConsumer, Runnable noValue) {
        getValue(new YAPIONValue<>(key), type, valueConsumer, noValue);
    }

    default <T> void getValue(char key, Class<T> type, Consumer<YAPIONValue<T>> valueConsumer, Runnable noValue) {
        getValue(new YAPIONValue<>(key), type, valueConsumer, noValue);
    }

    default <T> void getValue(boolean key, Class<T> type, Consumer<YAPIONValue<T>> valueConsumer, Runnable noValue) {
        getValue(new YAPIONValue<>(key), type, valueConsumer, noValue);
    }

    default <T> void getValue(byte key, Class<T> type, Consumer<YAPIONValue<T>> valueConsumer, Runnable noValue) {
        getValue(new YAPIONValue<>(key), type, valueConsumer, noValue);
    }

    default <T> void getValue(short key, Class<T> type, Consumer<YAPIONValue<T>> valueConsumer, Runnable noValue) {
        getValue(new YAPIONValue<>(key), type, valueConsumer, noValue);
    }

    default <T> void getValue(int key, Class<T> type, Consumer<YAPIONValue<T>> valueConsumer, Runnable noValue) {
        getValue(new YAPIONValue<>(key), type, valueConsumer, noValue);
    }

    default <T> void getValue(long key, Class<T> type, Consumer<YAPIONValue<T>> valueConsumer, Runnable noValue) {
        getValue(new YAPIONValue<>(key), type, valueConsumer, noValue);
    }

    default <T> void getValue(@NonNull BigInteger key, Class<T> type, Consumer<YAPIONValue<T>> valueConsumer, Runnable noValue) {
        getValue(new YAPIONValue<>(key), type, valueConsumer, noValue);
    }

    default <T> void getValue(float key, Class<T> type, Consumer<YAPIONValue<T>> valueConsumer, Runnable noValue) {
        getValue(new YAPIONValue<>(key), type, valueConsumer, noValue);
    }

    default <T> void getValue(double key, Class<T> type, Consumer<YAPIONValue<T>> valueConsumer, Runnable noValue) {
        getValue(new YAPIONValue<>(key), type, valueConsumer, noValue);
    }

    default <T> void getValue(@NonNull BigDecimal key, Class<T> type, Consumer<YAPIONValue<T>> valueConsumer, Runnable noValue) {
        getValue(new YAPIONValue<>(key), type, valueConsumer, noValue);
    }

    default <T> YAPIONValue<T> getValue(@NonNull String key, T type) {
        return getValue(new YAPIONValue<>(key), type);
    }

    default <T> YAPIONValue<T> getValue(char key, T type) {
        return getValue(new YAPIONValue<>(key), type);
    }

    default <T> YAPIONValue<T> getValue(boolean key, T type) {
        return getValue(new YAPIONValue<>(key), type);
    }

    default <T> YAPIONValue<T> getValue(byte key, T type) {
        return getValue(new YAPIONValue<>(key), type);
    }

    default <T> YAPIONValue<T> getValue(short key, T type) {
        return getValue(new YAPIONValue<>(key), type);
    }

    default <T> YAPIONValue<T> getValue(int key, T type) {
        return getValue(new YAPIONValue<>(key), type);
    }

    default <T> YAPIONValue<T> getValue(long key, T type) {
        return getValue(new YAPIONValue<>(key), type);
    }

    default <T> YAPIONValue<T> getValue(@NonNull BigInteger key, T type) {
        return getValue(new YAPIONValue<>(key), type);
    }

    default <T> YAPIONValue<T> getValue(float key, T type) {
        return getValue(new YAPIONValue<>(key), type);
    }

    default <T> YAPIONValue<T> getValue(double key, T type) {
        return getValue(new YAPIONValue<>(key), type);
    }

    default <T> YAPIONValue<T> getValue(@NonNull BigDecimal key, T type) {
        return getValue(new YAPIONValue<>(key), type);
    }

    default <T> YAPIONValue<T> getValueOrDefault(@NonNull String key, T defaultValue) {
        return getValueOrDefault(new YAPIONValue<>(key), defaultValue);
    }

    default <T> YAPIONValue<T> getValueOrDefault(char key, T defaultValue) {
        return getValueOrDefault(new YAPIONValue<>(key), defaultValue);
    }

    default <T> YAPIONValue<T> getValueOrDefault(boolean key, T defaultValue) {
        return getValueOrDefault(new YAPIONValue<>(key), defaultValue);
    }

    default <T> YAPIONValue<T> getValueOrDefault(byte key, T defaultValue) {
        return getValueOrDefault(new YAPIONValue<>(key), defaultValue);
    }

    default <T> YAPIONValue<T> getValueOrDefault(short key, T defaultValue) {
        return getValueOrDefault(new YAPIONValue<>(key), defaultValue);
    }

    default <T> YAPIONValue<T> getValueOrDefault(int key, T defaultValue) {
        return getValueOrDefault(new YAPIONValue<>(key), defaultValue);
    }

    default <T> YAPIONValue<T> getValueOrDefault(long key, T defaultValue) {
        return getValueOrDefault(new YAPIONValue<>(key), defaultValue);
    }

    default <T> YAPIONValue<T> getValueOrDefault(@NonNull BigInteger key, T defaultValue) {
        return getValueOrDefault(new YAPIONValue<>(key), defaultValue);
    }

    default <T> YAPIONValue<T> getValueOrDefault(float key, T defaultValue) {
        return getValueOrDefault(new YAPIONValue<>(key), defaultValue);
    }

    default <T> YAPIONValue<T> getValueOrDefault(double key, T defaultValue) {
        return getValueOrDefault(new YAPIONValue<>(key), defaultValue);
    }

    default <T> YAPIONValue<T> getValueOrDefault(@NonNull BigDecimal key, T defaultValue) {
        return getValueOrDefault(new YAPIONValue<>(key), defaultValue);
    }

    default <T> T getPlainValue(@NonNull String key) {
        return getPlainValue(new YAPIONValue<>(key));
    }

    default <T> T getPlainValue(char key) {
        return getPlainValue(new YAPIONValue<>(key));
    }

    default <T> T getPlainValue(boolean key) {
        return getPlainValue(new YAPIONValue<>(key));
    }

    default <T> T getPlainValue(byte key) {
        return getPlainValue(new YAPIONValue<>(key));
    }

    default <T> T getPlainValue(short key) {
        return getPlainValue(new YAPIONValue<>(key));
    }

    default <T> T getPlainValue(int key) {
        return getPlainValue(new YAPIONValue<>(key));
    }

    default <T> T getPlainValue(long key) {
        return getPlainValue(new YAPIONValue<>(key));
    }

    default <T> T getPlainValue(@NonNull BigInteger key) {
        return getPlainValue(new YAPIONValue<>(key));
    }

    default <T> T getPlainValue(float key) {
        return getPlainValue(new YAPIONValue<>(key));
    }

    default <T> T getPlainValue(double key) {
        return getPlainValue(new YAPIONValue<>(key));
    }

    default <T> T getPlainValue(@NonNull BigDecimal key) {
        return getPlainValue(new YAPIONValue<>(key));
    }

    default <T> T getPlainValueOrDefault(@NonNull String key, T defaultValue) {
        return getPlainValueOrDefault(new YAPIONValue<>(key), defaultValue);
    }

    default <T> T getPlainValueOrDefault(char key, T defaultValue) {
        return getPlainValueOrDefault(new YAPIONValue<>(key), defaultValue);
    }

    default <T> T getPlainValueOrDefault(boolean key, T defaultValue) {
        return getPlainValueOrDefault(new YAPIONValue<>(key), defaultValue);
    }

    default <T> T getPlainValueOrDefault(byte key, T defaultValue) {
        return getPlainValueOrDefault(new YAPIONValue<>(key), defaultValue);
    }

    default <T> T getPlainValueOrDefault(short key, T defaultValue) {
        return getPlainValueOrDefault(new YAPIONValue<>(key), defaultValue);
    }

    default <T> T getPlainValueOrDefault(int key, T defaultValue) {
        return getPlainValueOrDefault(new YAPIONValue<>(key), defaultValue);
    }

    default <T> T getPlainValueOrDefault(long key, T defaultValue) {
        return getPlainValueOrDefault(new YAPIONValue<>(key), defaultValue);
    }

    default <T> T getPlainValueOrDefault(@NonNull BigInteger key, T defaultValue) {
        return getPlainValueOrDefault(new YAPIONValue<>(key), defaultValue);
    }

    default <T> T getPlainValueOrDefault(float key, T defaultValue) {
        return getPlainValueOrDefault(new YAPIONValue<>(key), defaultValue);
    }

    default <T> T getPlainValueOrDefault(double key, T defaultValue) {
        return getPlainValueOrDefault(new YAPIONValue<>(key), defaultValue);
    }

    default <T> T getPlainValueOrDefault(@NonNull BigDecimal key, T defaultValue) {
        return getPlainValueOrDefault(new YAPIONValue<>(key), defaultValue);
    }

    default <T> void getPlainValue(@NonNull String key, Consumer<T> valueConsumer, Runnable noValue) {
        getPlainValue(new YAPIONValue<>(key), valueConsumer, noValue);
    }

    default <T> void getPlainValue(char key, Consumer<T> valueConsumer, Runnable noValue) {
        getPlainValue(new YAPIONValue<>(key), valueConsumer, noValue);
    }

    default <T> void getPlainValue(boolean key, Consumer<T> valueConsumer, Runnable noValue) {
        getPlainValue(new YAPIONValue<>(key), valueConsumer, noValue);
    }

    default <T> void getPlainValue(byte key, Consumer<T> valueConsumer, Runnable noValue) {
        getPlainValue(new YAPIONValue<>(key), valueConsumer, noValue);
    }

    default <T> void getPlainValue(short key, Consumer<T> valueConsumer, Runnable noValue) {
        getPlainValue(new YAPIONValue<>(key), valueConsumer, noValue);
    }

    default <T> void getPlainValue(int key, Consumer<T> valueConsumer, Runnable noValue) {
        getPlainValue(new YAPIONValue<>(key), valueConsumer, noValue);
    }

    default <T> void getPlainValue(long key, Consumer<T> valueConsumer, Runnable noValue) {
        getPlainValue(new YAPIONValue<>(key), valueConsumer, noValue);
    }

    default <T> void getPlainValue(@NonNull BigInteger key, Consumer<T> valueConsumer, Runnable noValue) {
        getPlainValue(new YAPIONValue<>(key), valueConsumer, noValue);
    }

    default <T> void getPlainValue(float key, Consumer<T> valueConsumer, Runnable noValue) {
        getPlainValue(new YAPIONValue<>(key), valueConsumer, noValue);
    }

    default <T> void getPlainValue(double key, Consumer<T> valueConsumer, Runnable noValue) {
        getPlainValue(new YAPIONValue<>(key), valueConsumer, noValue);
    }

    default <T> void getPlainValue(@NonNull BigDecimal key, Consumer<T> valueConsumer, Runnable noValue) {
        getPlainValue(new YAPIONValue<>(key), valueConsumer, noValue);
    }

}
