// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020,2021 yoyosource

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
public class LongSerializer implements InternalSerializer<Long> {

    @Override
    public String type() {
        return "java.lang.Long";
    }

    @Override
    public String primitiveType() {
        return "long";
    }

    @Override
    public YAPIONAnyType serialize(SerializeData<Long> serializeData) {
        return new YAPIONValue<>(serializeData.object);
    }

    @Override
    public Long deserialize(DeserializeData<? extends YAPIONAnyType> deserializeData) {
        return ((YAPIONValue<Long>) deserializeData.object).get();
    }
}