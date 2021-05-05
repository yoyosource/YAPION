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

package yapion.hierarchy.api.retrieve;

import yapion.hierarchy.api.groups.YAPIONAnyType;
import yapion.hierarchy.api.groups.YAPIONDataType;
import yapion.hierarchy.api.storage.ObjectRetrieve;
import yapion.hierarchy.api.internal.InternalRetrieve;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class Retriever<K> {

    private Query<YAPIONDataType<?, K>, ?>[] suppliers;
    private InternalRetrieve<K> objectRetrieve;

    Retriever(Query<YAPIONDataType<?, K>, ?>[] suppliers, ObjectRetrieve<K> objectRetrieve) {
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
        for (Query<YAPIONDataType<?, K>, ? extends YAPIONAnyType> supplier : suppliers) {
            YAPIONAnyType yapionAnyType = supplier.get((YAPIONDataType<?, K>) objectRetrieve);
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
        for (Query<YAPIONDataType<?, K>, ? extends YAPIONAnyType> supplier : suppliers) {
            YAPIONAnyType yapionAnyType = supplier.get((YAPIONDataType<?, K>) objectRetrieve);
            if (yapionAnyType != null) {
                containsOneKey.run();
                return this;
            }
        }
        return this;
    }

    public Retriever<K> hasNone(Runnable containsNoKey) {
        for (Query<YAPIONDataType<?, K>, ? extends YAPIONAnyType> supplier : suppliers) {
            YAPIONAnyType yapionAnyType = supplier.get((YAPIONDataType<?, K>) objectRetrieve);
            if (yapionAnyType != null) return this;
        }
        containsNoKey.run();
        return this;
    }

    public Retriever<K> every(Consumer<RetrieveResult> resultConsumer) {
        List<YAPIONAnyType> yapionAnyTypes = new ArrayList<>();
        for (Query<YAPIONDataType<?, K>, ? extends YAPIONAnyType> supplier : suppliers) {
            YAPIONAnyType yapionAnyType = supplier.get((YAPIONDataType<?, K>) objectRetrieve);
            if (yapionAnyType != null) yapionAnyTypes.add(yapionAnyType);
        }
        resultConsumer.accept(new RetrieveResult(yapionAnyTypes.toArray(new YAPIONAnyType[0])));
        return this;
    }

    public RetrieveResult result() {
        YAPIONAnyType[] yapionAnyTypes = new YAPIONAnyType[suppliers.length];
        int index = 0;
        for (Query<YAPIONDataType<?, K>, ? extends YAPIONAnyType> supplier : suppliers) {
            YAPIONAnyType yapionAnyType = supplier.get((YAPIONDataType<?, K>) objectRetrieve);
            if (yapionAnyType == null) {
                return null;
            }
            yapionAnyTypes[index++] = yapionAnyType;
        }
        return new RetrieveResult(yapionAnyTypes);
    }

    public boolean hasOne() {
        for (Query<YAPIONDataType<?, K>, ? extends YAPIONAnyType> supplier : suppliers) {
            YAPIONAnyType yapionAnyType = supplier.get((YAPIONDataType<?, K>) objectRetrieve);
            if (yapionAnyType != null) {
                return true;
            }
        }
        return false;
    }

    public boolean hasNone() {
        for (Query<YAPIONDataType<?, K>, ? extends YAPIONAnyType> supplier : suppliers) {
            YAPIONAnyType yapionAnyType = supplier.get((YAPIONDataType<?, K>) objectRetrieve);
            if (yapionAnyType != null) return false;
        }
        return true;
    }

    public RetrieveResult every() {
        List<YAPIONAnyType> yapionAnyTypes = new ArrayList<>();
        for (Query<YAPIONDataType<?, K>, ? extends YAPIONAnyType> supplier : suppliers) {
            YAPIONAnyType yapionAnyType = supplier.get((YAPIONDataType<?, K>) objectRetrieve);
            if (yapionAnyType != null) yapionAnyTypes.add(yapionAnyType);
        }
        return new RetrieveResult(yapionAnyTypes.toArray(new YAPIONAnyType[0]));
    }

}
