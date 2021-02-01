// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020,2021 yoyosource

package yapion.hierarchy.api.storage;

import lombok.NonNull;
import yapion.hierarchy.api.groups.YAPIONAnyType;
import yapion.hierarchy.types.*;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

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

    default Supplier<YAPIONObject> ObjectRetriever(K key) {
        return () -> getObject(key);
    }

    default Supplier<YAPIONMap> MapRetriever(K key) {
        return () -> getMap(key);
    }

    default Supplier<YAPIONArray> ArrayRetriever(K key) {
        return () -> getArray(key);
    }

    default Supplier<YAPIONPointer> PointerRetriever(K key) {
        return () -> getPointer(key);
    }

    default Supplier<YAPIONValue> ValueRetriever(K key) {
        return () -> getValue(key);
    }

    default <T> Supplier<YAPIONValue<T>> ValueRetriever(K key, Class<T> clazz) {
        return () -> getValue(key, clazz);
    }

    default Retriever retrieve(Supplier<? extends YAPIONAnyType>... suppliers) {
        return new Retriever(suppliers);
    }

    class Retriever {

        private Supplier<? extends YAPIONAnyType>[] suppliers;

        private Retriever(Supplier<? extends YAPIONAnyType>[] suppliers) {
            this.suppliers = suppliers;
        }

        public Retriever result(Consumer<RetrieveResult> resultConsumer) {
            return result(resultConsumer, () -> {});
        }

        public Retriever result(Runnable valueMissing) {
            return result(result -> {}, valueMissing);
        }

        public Retriever result(Runnable valueMissing, Consumer<RetrieveResult> resultConsumer) {
            return result(resultConsumer, valueMissing);
        }

        public Retriever result(Consumer<RetrieveResult> resultConsumer, Runnable valueMissing) {
            YAPIONAnyType[] yapionAnyTypes = new YAPIONAnyType[suppliers.length];
            int index = 0;
            for (Supplier<? extends YAPIONAnyType> supplier : suppliers) {
                YAPIONAnyType yapionAnyType = supplier.get();
                if (yapionAnyType == null) {
                    valueMissing.run();
                    return this;
                }
                yapionAnyTypes[index++] = yapionAnyType;
            }
            resultConsumer.accept(new RetrieveResult(yapionAnyTypes));
            return this;
        }

        public Retriever hasOne(Runnable containsOneKey) {
            for (Supplier<? extends YAPIONAnyType> supplier : suppliers) {
                YAPIONAnyType yapionAnyType = supplier.get();
                if (yapionAnyType != null) {
                    containsOneKey.run();
                    return this;
                }
            }
            return this;
        }

        public Retriever hasNone(Runnable containsNoKey) {
            for (Supplier<? extends YAPIONAnyType> supplier : suppliers) {
                YAPIONAnyType yapionAnyType = supplier.get();
                if (yapionAnyType != null) return this;
            }
            containsNoKey.run();
            return this;
        }

        public Retriever every(Consumer<RetrieveResult> resultConsumer) {
            List<YAPIONAnyType> yapionAnyTypes = new ArrayList<>();
            for (Supplier<? extends YAPIONAnyType> supplier : suppliers) {
                YAPIONAnyType yapionAnyType = supplier.get();
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