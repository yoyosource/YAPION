// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020 yoyosource

package yapion.serializing.serializer.primitive.number;

import yapion.annotations.deserialize.YAPIONLoadExclude;
import yapion.annotations.serialize.YAPIONSaveExclude;
import yapion.hierarchy.typegroups.YAPIONAnyType;
import yapion.hierarchy.types.YAPIONValue;
import yapion.serializing.InternalSerializer;
import yapion.serializing.data.DeserializeData;
import yapion.serializing.data.SerializeData;
import yapion.serializing.serializer.SerializerImplementation;

import java.math.BigDecimal;

@YAPIONSaveExclude(context = "*")
@YAPIONLoadExclude(context = "*")
@SerializerImplementation(since = "0.2.0")
public class BigDecimalSerializer implements InternalSerializer<BigDecimal> {

    @Override
    public String type() {
        return "java.math.BigDecimal";
    }

    @Override
    public YAPIONAnyType serialize(SerializeData<BigDecimal> serializeData) {
        return new YAPIONValue<>(serializeData.object);
    }

    @Override
    public BigDecimal deserialize(DeserializeData<? extends YAPIONAnyType> deserializeData) {
        return ((YAPIONValue<BigDecimal>) deserializeData.object).get();
    }
}