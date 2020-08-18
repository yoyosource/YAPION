// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020 yoyosource

package yapion.serializing.serializer.other;

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
public class StringBuilderSerializer implements Serializer<StringBuilder> {

    @Override
    public String type() {
        return "java.lang.StringBuilder";
    }

    @Override
    public YAPIONAny serialize(StringBuilder object, YAPIONSerializer yapionSerializer) {
        return new YAPIONValue<>(object.toString());
    }

    @Override
    public StringBuilder deserialize(YAPIONAny yapionAny, YAPIONDeserializer yapionDeserializer, Field field) {
        return new StringBuilder().append(((YAPIONValue<String>) yapionAny).get());
    }
}