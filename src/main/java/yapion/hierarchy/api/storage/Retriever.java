// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020,2021 yoyosource

package yapion.hierarchy.api.storage;

import yapion.hierarchy.api.groups.YAPIONAnyType;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class Retriever<K> {

    private Function<ObjectRetrieve<K>, ? extends YAPIONAnyType>[] suppliers;
    private ObjectRetrieve<K> objectRetrieve;

    Retriever(Function<ObjectRetrieve<K>, ? extends YAPIONAnyType>[] suppliers, ObjectRetrieve<K> objectRetrieve) {
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

    public RetrieveResult result() {
        YAPIONAnyType[] yapionAnyTypes = new YAPIONAnyType[suppliers.length];
        int index = 0;
        for (Function<ObjectRetrieve<K>, ? extends YAPIONAnyType> supplier : suppliers) {
            YAPIONAnyType yapionAnyType = supplier.apply(objectRetrieve);
            if (yapionAnyType == null) {
                return null;
            }
            yapionAnyTypes[index++] = yapionAnyType;
        }
        return new RetrieveResult(yapionAnyTypes);
    }

    public boolean hasOne() {
        for (Function<ObjectRetrieve<K>, ? extends YAPIONAnyType> supplier : suppliers) {
            YAPIONAnyType yapionAnyType = supplier.apply(objectRetrieve);
            if (yapionAnyType != null) {
                return true;
            }
        }
        return false;
    }

    public boolean hasNone() {
        for (Function<ObjectRetrieve<K>, ? extends YAPIONAnyType> supplier : suppliers) {
            YAPIONAnyType yapionAnyType = supplier.apply(objectRetrieve);
            if (yapionAnyType != null) return false;
        }
        return true;
    }

    public RetrieveResult every() {
        List<YAPIONAnyType> yapionAnyTypes = new ArrayList<>();
        for (Function<ObjectRetrieve<K>, ? extends YAPIONAnyType> supplier : suppliers) {
            YAPIONAnyType yapionAnyType = supplier.apply(objectRetrieve);
            if (yapionAnyType != null) yapionAnyTypes.add(yapionAnyType);
        }
        return new RetrieveResult(yapionAnyTypes.toArray(new YAPIONAnyType[0]));
    }

}