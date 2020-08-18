// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020 yoyosource

package yapion.serializing.serializer.number;

import yapion.annotations.deserialize.YAPIONLoadExclude;
import yapion.annotations.serialize.YAPIONSaveExclude;
import yapion.hierarchy.YAPIONAny;
import yapion.hierarchy.types.YAPIONValue;
import yapion.serializing.Serializer;
import yapion.serializing.YAPIONDeserializer;
import yapion.serializing.YAPIONSerializer;

import java.lang.reflect.Field;

@YAPIONSaveExclude(context = "*")
@YAPIONLoadExclude(context = "*")
public class ShortSerializer implements Serializer<Short> {

    @Override
    public String type() {
        return "java.lang.Short";
    }

    @Override
    public String primitiveType() {
        return "short";
    }

    @Override
    public YAPIONAny serialize(Short object, YAPIONSerializer yapionSerializer) {
        return new YAPIONValue<>(object);
    }

    @Override
    public Short deserialize(YAPIONAny yapionAny, YAPIONDeserializer yapionDeserializer, Field field) {
        return ((YAPIONValue<Short>) yapionAny).get();
    }
}