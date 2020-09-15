// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020 yoyosource

package yapion.serializing.serializer.number;

import yapion.annotations.deserialize.YAPIONLoadExclude;
import yapion.annotations.serialize.YAPIONSaveExclude;
import yapion.hierarchy.typegroups.YAPIONAnyType;
import yapion.hierarchy.types.YAPIONValue;
import yapion.serializing.InternalSerializer;
import yapion.serializing.YAPIONDeserializer;
import yapion.serializing.YAPIONSerializer;
import yapion.serializing.serializer.SerializerImplementation;

import java.math.BigInteger;

@YAPIONSaveExclude(context = "*")
@YAPIONLoadExclude(context = "*")
@SerializerImplementation
public class BigIntegerSerializer implements InternalSerializer<BigInteger> {

    @Override
    public String type() {
        return "java.math.BigInteger";
    }

    @Override
    public YAPIONAnyType serialize(BigInteger object, YAPIONSerializer yapionSerializer) {
        return new YAPIONValue<>(object);
    }

    @Override
    public BigInteger deserialize(YAPIONAnyType yapionAnyType, YAPIONDeserializer yapionDeserializer) {
        return ((YAPIONValue<BigInteger>) yapionAnyType).get();
    }
}