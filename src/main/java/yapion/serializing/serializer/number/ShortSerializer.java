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

@YAPIONSaveExclude(context = "*")
@YAPIONLoadExclude(context = "*")
@SerializerImplementation
public class ShortSerializer implements InternalSerializer<Short> {

    @Override
    public String type() {
        return "java.lang.Short";
    }

    @Override
    public String primitiveType() {
        return "short";
    }

    @Override
    public YAPIONAnyType serialize(Short object, YAPIONSerializer yapionSerializer) {
        return new YAPIONValue<>(object);
    }

    @Override
    public Short deserialize(YAPIONAnyType yapionAnyType, YAPIONDeserializer yapionDeserializer) {
        return ((YAPIONValue<Short>) yapionAnyType).get();
    }
}