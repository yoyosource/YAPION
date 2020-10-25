// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020 yoyosource

package yapion.serializing.serializer.number;

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
public class ByteSerializer implements InternalSerializer<Byte> {

    @Override
    public String type() {
        return "java.lang.Byte";
    }

    @Override
    public String primitiveType() {
        return "byte";
    }

    @Override
    public YAPIONAnyType serialize(SerializeData<Byte> serializeData) {
        return new YAPIONValue<>(serializeData.object);
    }

    @Override
    public Byte deserialize(DeserializeData<? extends YAPIONAnyType> deserializeData) {
        return ((YAPIONValue<Byte>) deserializeData.object).get();
    }
}