// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020,2021 yoyosource

package yapion.hierarchy.api.retrieve;

import yapion.hierarchy.api.groups.YAPIONAnyType;
import yapion.hierarchy.api.storage.ObjectRetrieve;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class RetrieveBuilder<K> {

    private List<Function<ObjectRetrieve<K>, ? extends YAPIONAnyType>> suppliers = new ArrayList<>();

    public RetrieveBuilder<K> object(K key) {
        suppliers.add(kObjectRetrieve -> kObjectRetrieve.getObject(key));
        return this;
    }

    public RetrieveBuilder<K> map(K key) {
        suppliers.add(kObjectRetrieve -> kObjectRetrieve.getMap(key));
        return this;
    }

    public RetrieveBuilder<K> array(K key) {
        suppliers.add(kObjectRetrieve -> kObjectRetrieve.getArray(key));
        return this;
    }

    public RetrieveBuilder<K> pointer(K key) {
        suppliers.add(kObjectRetrieve -> kObjectRetrieve.getPointer(key));
        return this;
    }

    public RetrieveBuilder<K> value(K key) {
        suppliers.add(kObjectRetrieve -> kObjectRetrieve.getValue(key));
        return this;
    }

    public <T> RetrieveBuilder<K> value(K key, Class<T> clazz) {
        suppliers.add(kObjectRetrieve -> kObjectRetrieve.getValue(key, clazz));
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
    public Retriever<K> retrieve(ObjectRetrieve<K> objectRetrieve) {
        Function<ObjectRetrieve<K>, ? extends YAPIONAnyType>[] supplyArray = new Function[suppliers.size()];
        for (int i = 0; i < supplyArray.length; i++) {
            supplyArray[i] = suppliers.get(i);
        }
        return new Retriever<>(supplyArray, objectRetrieve);
    }

}