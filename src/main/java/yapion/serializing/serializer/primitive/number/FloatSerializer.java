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
@SerializerImplementation
public class FloatSerializer implements InternalSerializer<Float> {

    @Override
    public String type() {
        return "java.lang.Float";
    }

    @Override
    public String primitiveType() {
        return "float";
    }

    @Override
    public YAPIONAnyType serialize(SerializeData<Float> serializeData) {
        return new YAPIONValue<>(serializeData.object);
    }

    @Override
    public Float deserialize(DeserializeData<? extends YAPIONAnyType> deserializeData) {
        return ((YAPIONValue<Float>) deserializeData.object).get();
    }
}