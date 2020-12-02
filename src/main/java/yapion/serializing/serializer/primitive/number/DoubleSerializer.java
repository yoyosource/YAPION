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

@YAPIONSaveExclude(context = "*")
@YAPIONLoadExclude(context = "*")
@SerializerImplementation(since = "0.2.0")
public class DoubleSerializer implements InternalSerializer<Double> {

    @Override
    public String type() {
        return "java.lang.Double";
    }

    @Override
    public String primitiveType() {
        return "double";
    }

    @Override
    public YAPIONAnyType serialize(SerializeData<Double> serializeData) {
        return new YAPIONValue<>(serializeData.object);
    }

    @Override
    public Double deserialize(DeserializeData<? extends YAPIONAnyType> deserializeData) {
        return ((YAPIONValue<Double>) deserializeData.object).get();
    }
}