// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020,2021 yoyosource

package yapion.hierarchy.api.storage;

import lombok.NonNull;
import yapion.hierarchy.api.groups.YAPIONAnyType;
import yapion.hierarchy.types.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public interface ObjectRetrieve<K> {

    default boolean hasValue(@NonNull K key) {
        return hasValue(key, YAPIONType.ANY);
    }

    boolean hasValue(@NonNull K key, YAPIONType yapionType);

    <T> boolean hasValue(@NonNull K key, Class<T> type);

    YAPIONAnyType getYAPIONAnyType(@NonNull K key);

    default YAPIONObject getObject(@NonNull K key) {
        YAPIONAnyType yapionAnyType = getYAPIONAnyType(key);
        if (yapionAnyType == null) return null;
        if (yapionAnyType instanceof YAPIONObject) {
            return (YAPIONObject) yapionAnyType;
        }
        return null;
    }

    default void getObject(@NonNull K key, Consumer<YAPIONObject> valueConsumer, Runnable noValue) {
        YAPIONAnyType yapionAnyType = getYAPIONAnyType(key);
        if (yapionAnyType == null) {
            noValue.run();
            return;
        }
        if (yapionAnyType instanceof YAPIONObject) {
            valueConsumer.accept((YAPIONObject) yapionAnyType);
        }
    }

    default YAPIONArray getArray(@NonNull K key) {
        YAPIONAnyType yapionAnyType = getYAPIONAnyType(key);
        if (yapionAnyType == null) return null;
        if (yapionAnyType instanceof YAPIONArray) {
            return (YAPIONArray) yapionAnyType;
        }
        return null;
    }

    default void getArray(@NonNull K key, Consumer<YAPIONArray> valueConsumer, Runnable noValue) {
        YAPIONAnyType yapionAnyType = getYAPIONAnyType(key);
        if (yapionAnyType == null) {
            noValue.run();
            return;
        }
        if (yapionAnyType instanceof YAPIONArray) {
            valueConsumer.accept((YAPIONArray) yapionAnyType);
        }
    }

    default YAPIONMap getMap(@NonNull K key) {
        YAPIONAnyType yapionAnyType = getYAPIONAnyType(key);
        if (yapionAnyType == null) return null;
        if (yapionAnyType instanceof YAPIONMap) {
            return (YAPIONMap) yapionAnyType;
        }
        return null;
    }

    default void getMap(@NonNull K key, Consumer<YAPIONMap> valueConsumer, Runnable noValue) {
        YAPIONAnyType yapionAnyType = getYAPIONAnyType(key);
        if (yapionAnyType == null) {
            noValue.run();
            return;
        }
        if (yapionAnyType instanceof YAPIONMap) {
            valueConsumer.accept((YAPIONMap) yapionAnyType);
        }
    }

    default YAPIONPointer getPointer(@NonNull K key) {
        YAPIONAnyType yapionAnyType = getYAPIONAnyType(key);
        if (yapionAnyType == null) return null;
        if (yapionAnyType instanceof YAPIONPointer) {
            return (YAPIONPointer) yapionAnyType;
        }
        return null;
    }

    default void getPointer(@NonNull K key, Consumer<YAPIONPointer> valueConsumer, Runnable noValue) {
        YAPIONAnyType yapionAnyType = getYAPIONAnyType(key);
        if (yapionAnyType == null) {
            noValue.run();
            return;
        }
        if (yapionAnyType instanceof YAPIONPointer) {
            valueConsumer.accept((YAPIONPointer) yapionAnyType);
        }
    }

    @SuppressWarnings({"java:S3740"})
    default YAPIONValue getValue(@NonNull K key) {
        YAPIONAnyType yapionAnyType = getYAPIONAnyType(key);
        if (yapionAnyType == null) return null;
        if (yapionAnyType instanceof YAPIONValue) {
            return (YAPIONValue) yapionAnyType;
        }
        return null;
    }

    @SuppressWarnings({"java:S3740"})
    default void getValue(@NonNull K key, Consumer<YAPIONValue> valueConsumer, Runnable noValue) {
        YAPIONAnyType yapionAnyType = getYAPIONAnyType(key);
        if (yapionAnyType == null) {
            noValue.run();
            return;
        }
        if (yapionAnyType instanceof YAPIONValue) {
            valueConsumer.accept((YAPIONValue) yapionAnyType);
        }
    }

    default <T> YAPIONValue<T> getValue(@NonNull K key, Class<T> type) {
        if (!YAPIONValue.validType(type)) {
            System.out.println("Not valid");
            return null;
        }
        YAPIONAnyType yapionAnyType = getYAPIONAnyType(key);
        if (yapionAnyType == null) return null;
        if (!(yapionAnyType instanceof YAPIONValue)) {
            return null;
        }
        if (!((YAPIONValue) yapionAnyType).getValueType().equalsIgnoreCase(type.getTypeName())) {
            return null;
        }
        return (YAPIONValue<T>) yapionAnyType;
    }

    default <T> void getValue(@NonNull K key, Class<T> type, Consumer<YAPIONValue<T>> valueConsumer, Runnable noValue) {
        if (!YAPIONValue.validType(type)) {
            return;
        }
        YAPIONAnyType yapionAnyType = getYAPIONAnyType(key);
        if (yapionAnyType == null) {
            noValue.run();
            return;
        }
        if (!(yapionAnyType instanceof YAPIONValue)) {
            return;
        }
        if (!((YAPIONValue) yapionAnyType).getValueType().equalsIgnoreCase(type.getTypeName())) {
            return;
        }
        valueConsumer.accept((YAPIONValue<T>) yapionAnyType);
    }

    default <T> YAPIONValue<T> getValue(@NonNull K key, T type) {
        if (!YAPIONValue.validType(type)) {
            return null;
        }
        YAPIONAnyType yapionAnyType = getYAPIONAnyType(key);
        if (yapionAnyType == null) return null;
        if (!(yapionAnyType instanceof YAPIONValue)) {
            return null;
        }
        if (!((YAPIONValue) yapionAnyType).getValueType().equalsIgnoreCase(type.getClass().getTypeName())) {
            return null;
        }
        return (YAPIONValue<T>) yapionAnyType;
    }

    default <T> void getValue(@NonNull K key, T type, Consumer<YAPIONValue<T>> valueConsumer, Runnable noValue) {
        if (!YAPIONValue.validType(type)) {
            return;
        }
        YAPIONAnyType yapionAnyType = getYAPIONAnyType(key);
        if (yapionAnyType == null) {
            noValue.run();
            return;
        }
        if (!(yapionAnyType instanceof YAPIONValue)) {
            return;
        }
        if (!((YAPIONValue) yapionAnyType).getValueType().equalsIgnoreCase(type.getClass().getTypeName())) {
            return;
        }
        valueConsumer.accept((YAPIONValue<T>) yapionAnyType);
    }

    static <K> Function<ObjectRetrieve<K>, YAPIONObject> Object(K key) {
        return kObjectRetrieve -> kObjectRetrieve.getObject(key);
    }

    static <K> Function<ObjectRetrieve<K>, YAPIONMap> Map(K key) {
        return kObjectRetrieve -> kObjectRetrieve.getMap(key);
    }

    static <K> Function<ObjectRetrieve<K>, YAPIONArray> Array(K key) {
        return kObjectRetrieve -> kObjectRetrieve.getArray(key);
    }

    static <K> Function<ObjectRetrieve<K>, YAPIONPointer> Pointer(K key) {
        return kObjectRetrieve -> kObjectRetrieve.getPointer(key);
    }

    static <K> Function<ObjectRetrieve<K>, YAPIONValue> Value(K key) {
        return kObjectRetrieve -> kObjectRetrieve.getValue(key);
    }

    static <K, T> Function<ObjectRetrieve<K>, YAPIONValue<T>> Value(K key, Class<T> clazz) {
        return kObjectRetrieve -> kObjectRetrieve.getValue(key, clazz);
    }

    static <K> Function<ObjectRetrieve<K>, YAPIONValue<Boolean>> BoolValue(K key) {
        return Value(key, Boolean.class);
    }

    static <K> Function<ObjectRetrieve<K>, YAPIONValue<Byte>> ByteValue(K key) {
        return Value(key, Byte.class);
    }

    static <K> Function<ObjectRetrieve<K>, YAPIONValue<Short>> ShortValue(K key) {
        return Value(key, Short.class);
    }

    static <K> Function<ObjectRetrieve<K>, YAPIONValue<Integer>> IntValue(K key) {
        return Value(key, Integer.class);
    }

    static <K> Function<ObjectRetrieve<K>, YAPIONValue<Long>> LongValue(K key) {
        return Value(key, Long.class);
    }

    static <K> Function<ObjectRetrieve<K>, YAPIONValue<BigInteger>> BigIntegerValue(K key) {
        return Value(key, BigInteger.class);
    }

    static <K> Function<ObjectRetrieve<K>, YAPIONValue<Float>> FloatValue(K key) {
        return Value(key, Float.class);
    }

    static <K> Function<ObjectRetrieve<K>, YAPIONValue<Double>> DoubleValue(K key) {
        return Value(key, Double.class);
    }

    static <K> Function<ObjectRetrieve<K>, YAPIONValue<BigDecimal>> BigDecimalValue(K key) {
        return Value(key, BigDecimal.class);
    }

    static <K> Function<ObjectRetrieve<K>, YAPIONValue<Character>> CharValue(K key) {
        return Value(key, Character.class);
    }

    static <K> Function<ObjectRetrieve<K>, YAPIONValue<String>> StringValue(K key) {
        return Value(key, String.class);
    }

    default Retriever<K> retrieve(Function<ObjectRetrieve<K>, ? extends YAPIONAnyType>... suppliers) {
        return new Retriever<>(suppliers, this);
    }

    class Retriever<K> {

        private Function<ObjectRetrieve<K>, ? extends YAPIONAnyType>[] suppliers;
        private ObjectRetrieve<K> objectRetrieve;

        private Retriever(Function<ObjectRetrieve<K>, ? extends YAPIONAnyType>[] suppliers, ObjectRetrieve<K> objectRetrieve) {
            this.suppliers = suppliers;
            this.objectRetrieve = objectRetrieve;
        }

        public Retriever<K> result(Consumer<RetrieveResult> resultConsumer) {
            return result(resultConsumer, () -> {});
        }

        public Retriever<K> result(Runnable valueMissing) {
            return result(result -> {}, valueMissing);
        }

        public Retriever<K> result(Runnable valueMissing, Consumer<RetrieveResult> resultConsumer) {
            return result(resultConsumer, valueMissing);
        }

        public Retriever<K> result(Consumer<RetrieveResult> resultConsumer, Runnable valueMissing) {
            YAPIONAnyType[] yapionAnyTypes = new YAPIONAnyType[suppliers.length];
            int index = 0;
            for (Function<ObjectRetrieve<K>, ? extends YAPIONAnyType> supplier : suppliers) {
                YAPIONAnyType yapionAnyType = supplier.apply(objectRetrieve);
                if (yapionAnyType == null) {
                    valueMissing.run();
                    return this;
                }
                yapionAnyTypes[index++] = yapionAnyType;
            }
            resultConsumer.accept(new RetrieveResult(yapionAnyTypes));
            return this;
        }

        public Retriever<K> hasOne(Runnable containsOneKey) {
            for (Function<ObjectRetrieve<K>, ? extends YAPIONAnyType> supplier : suppliers) {
                YAPIONAnyType yapionAnyType = supplier.apply(objectRetrieve);
                if (yapionAnyType != null) {
                    containsOneKey.run();
                    return this;
                }
            }
            return this;
        }

        public Retriever<K> hasNone(Runnable containsNoKey) {
            for (Function<ObjectRetrieve<K>, ? extends YAPIONAnyType> supplier : suppliers) {
                YAPIONAnyType yapionAnyType = supplier.apply(objectRetrieve);
                if (yapionAnyType != null) return this;
            }
            containsNoKey.run();
            return this;
        }

        public Retriever<K> every(Consumer<RetrieveResult> resultConsumer) {
            List<YAPIONAnyType> yapionAnyTypes = new ArrayList<>();
            for (Function<ObjectRetrieve<K>, ? extends YAPIONAnyType> supplier : suppliers) {
                YAPIONAnyType yapionAnyType = supplier.apply(objectRetrieve);
                if (yapionAnyType != null) yapionAnyTypes.add(yapionAnyType);
            }
            resultConsumer.accept(new RetrieveResult(yapionAnyTypes.toArray(new YAPIONAnyType[0])));
            return this;
        }

    }

    class RetrieveResult {

        private YAPIONAnyType[] yapionAnyTypes;

        public RetrieveResult(YAPIONAnyType... yapionAnyTypes) {
            this.yapionAnyTypes = yapionAnyTypes;
        }

        public int size() {
            return yapionAnyTypes.length;
        }

        public int length() {
            return yapionAnyTypes.length;
        }

        public YAPIONType getType(int index) {
            return yapionAnyTypes[index].getType();
        }

        public YAPIONAnyType getRaw(int index) {
            return yapionAnyTypes[index];
        }

        @SuppressWarnings({"unchecked"})
        public <T extends YAPIONAnyType> T get(int index) {
            return (T) yapionAnyTypes[index];
        }

        @SuppressWarnings({"unchecked"})
        public <T extends YAPIONAnyType> T get(int index, T t) {
            return (T) yapionAnyTypes[index];
        }

        @SuppressWarnings({"unchecked"})
        public <T extends YAPIONAnyType> T get(int index, Class<T> clazz) {
            return (T) yapionAnyTypes[index];
        }

        public YAPIONObject getObject(int index) {
            return get(index);
        }

        public YAPIONArray getArray(int index) {
            return get(index);
        }

        public YAPIONMap getMap(int index) {
            return get(index);
        }

        public YAPIONPointer getPointer(int index) {
            return get(index);
        }

        public YAPIONValue getValue(int index) {
            return get(index);
        }

        @SuppressWarnings({"unchecked"})
        public <T> YAPIONValue<T> getValue(int index, @NonNull T t) {
            return (YAPIONValue<T>) getValue(index);
        }

        @SuppressWarnings({"unchecked"})
        public <T> YAPIONValue<T> getValue(int index, @NonNull Class<T> t) {
            return (YAPIONValue<T>) getValue(index);
        }

    }

}