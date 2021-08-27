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
import yapion.hierarchy.api.internal.InternalRetrieve;
import yapion.hierarchy.types.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class RetrieveBuilder<K> {

    private List<Query<YAPIONDataType<?, K>, ?>> suppliers = new ArrayList<>();

    private RetrieveBuilder() {

    }

    public static RetrieveBuilder<String> ObjectRetrieveBuilder() {
        return new RetrieveBuilder<>();
    }

    public static RetrieveBuilder<YAPIONAnyType> MapRetrieveBuilder() {
        return new RetrieveBuilder<>();
    }

    public static RetrieveBuilder<Integer> ArrayRetrieveBuilder() {
        return new RetrieveBuilder<>();
    }

    public static Query<YAPIONObject, YAPIONObject> ObjectQuery() {
        return input -> input;
    }

    public static Query<YAPIONMap, YAPIONMap> MapQuery() {
        return input -> input;
    }

    public static Query<YAPIONArray, YAPIONArray> ArrayQuery() {
        return input -> input;
    }

    public RetrieveBuilder<K> object(K key) {
        suppliers.add(kObjectRetrieve -> {
            return (YAPIONObject) kObjectRetrieve.internalGetYAPIONAnyType(key);
        });
        return this;
    }

    public RetrieveBuilder<K> map(K key) {
        suppliers.add(kObjectRetrieve -> {
            return (YAPIONMap) kObjectRetrieve.internalGetYAPIONAnyType(key);
        });
        return this;
    }

    public RetrieveBuilder<K> array(K key) {
        suppliers.add(kObjectRetrieve -> {
            return (YAPIONArray) kObjectRetrieve.internalGetYAPIONAnyType(key);
        });
        return this;
    }

    public RetrieveBuilder<K> pointer(K key) {
        suppliers.add(kObjectRetrieve -> {
            return (YAPIONPointer) kObjectRetrieve.internalGetYAPIONAnyType(key);
        });
        return this;
    }

    public RetrieveBuilder<K> value(K key) {
        suppliers.add(kObjectRetrieve -> {
            return (YAPIONValue) kObjectRetrieve.internalGetYAPIONAnyType(key);
        });
        return this;
    }

    public <T> RetrieveBuilder<K> value(K key, Class<T> clazz) {
        suppliers.add(kObjectRetrieve -> {
            return (YAPIONValue<T>) kObjectRetrieve.internalGetYAPIONAnyType(key);
        });
        return this;
    }

    public RetrieveBuilder<K> query(Query<YAPIONDataType<?, K>, ?> query) {
        suppliers.add(query);
        return this;
    }

    public RetrieveBuilder<K> boolValue(K key) {
        return value(key, Boolean.class);
    }

    public RetrieveBuilder<K> byteValue(K key) {
        return value(key, Byte.class);
    }

    public RetrieveBuilder<K> shortValue(K key) {
        return value(key, Short.class);
    }

    public RetrieveBuilder<K> intValue(K key) {
        return value(key, Integer.class);
    }

    public RetrieveBuilder<K> longValue(K key) {
        return value(key, Long.class);
    }

    public RetrieveBuilder<K> bigIntegerValue(K key) {
        return value(key, BigInteger.class);
    }

    public RetrieveBuilder<K> floatValue(K key) {
        return value(key, Float.class);
    }

    public RetrieveBuilder<K> doubleValue(K key) {
        return value(key, Double.class);
    }

    public RetrieveBuilder<K> bigDecimalValue(K key) {
        return value(key, BigDecimal.class);
    }

    public RetrieveBuilder<K> charValue(K key) {
        return value(key, Character.class);
    }

    public RetrieveBuilder<K> stringValue(K key) {
        return value(key, String.class);
    }

    @SuppressWarnings("all")
    public Retriever<K> retrieve(InternalRetrieve<K> internalRetrieve) {
        Query<YAPIONDataType<?, K>, ?>[] supplyArray = new Query[suppliers.size()];
        for (int i = 0; i < supplyArray.length; i++) {
            supplyArray[i] = suppliers.get(i);
        }
        return new Retriever<>(supplyArray, internalRetrieve);
    }
}
