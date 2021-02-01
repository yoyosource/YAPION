// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020,2021 yoyosource

package yapion.serializing.serializer.primitive.number;

import yapion.annotations.deserialize.YAPIONLoadExclude;
import yapion.annotations.serialize.YAPIONSaveExclude;
import yapion.hierarchy.api.groups.YAPIONAnyType;
import yapion.hierarchy.types.YAPIONValue;
import yapion.serializing.InternalSerializer;
import yapion.serializing.data.DeserializeData;
import yapion.serializing.data.SerializeData;
import yapion.serializing.serializer.SerializerImplementation;

import java.math.BigInteger;

@YAPIONSaveExclude(context = "*")
@YAPIONLoadExclude(context = "*")
@SerializerImplementation(since = "0.2.0")
public class BigIntegerSerializer implements InternalSerializer<BigInteger> {

    @Override
    public String type() {
        return "java.math.BigInteger";
    }

    @Override
    public YAPIONAnyType serialize(SerializeData<BigInteger> serializeData) {
        return new YAPIONValue<>(serializeData.object);
    }

    @Override
    public BigInteger deserialize(DeserializeData<? extends YAPIONAnyType> deserializeData) {
        return ((YAPIONValue<BigInteger>) deserializeData.object).get();
    }
}