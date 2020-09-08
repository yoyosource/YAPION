// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020 yoyosource

package yapion.serializing.serializer.number;

import yapion.annotations.deserialize.YAPIONLoadExclude;
import yapion.annotations.serialize.YAPIONSaveExclude;
import yapion.hierarchy.YAPIONAny;
import yapion.hierarchy.types.YAPIONValue;
import yapion.serializing.InternalSerializer;
import yapion.serializing.YAPIONDeserializer;
import yapion.serializing.YAPIONSerializer;
import yapion.serializing.serializer.SerializerImplementation;

@YAPIONSaveExclude(context = "*")
@YAPIONLoadExclude(context = "*")
@SerializerImplementation
public class IntegerSerializer implements InternalSerializer<Integer> {

    @Override
    public String type() {
        return "java.lang.Integer";
    }

    @Override
    public String primitiveType() {
        return "int";
    }

    @Override
    public YAPIONAny serialize(Integer object, YAPIONSerializer yapionSerializer) {
        return new YAPIONValue<>(object);
    }

    @Override
    public Integer deserialize(YAPIONAny yapionAny, YAPIONDeserializer yapionDeserializer) {
        return ((YAPIONValue<Integer>) yapionAny).get();
    }
}