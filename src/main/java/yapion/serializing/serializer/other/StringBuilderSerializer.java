// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020 yoyosource

package yapion.serializing.serializer.other;

import yapion.annotations.deserialize.YAPIONLoadExclude;
import yapion.annotations.serialize.YAPIONSaveExclude;
import yapion.hierarchy.YAPIONAny;
import yapion.hierarchy.YAPIONVariable;
import yapion.hierarchy.types.YAPIONObject;
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
        YAPIONObject yapionObject = new YAPIONObject();
        yapionObject.add(new YAPIONVariable("@type", new YAPIONValue<>("java.lang.StringBuilder")));
        yapionObject.add(new YAPIONVariable("string", new YAPIONValue<>(object.toString())));
        return yapionObject;
    }

    @Override
    public StringBuilder deserialize(YAPIONAny yapionAny, YAPIONDeserializer yapionDeserializer, Field field) {
        YAPIONObject yapionObject = (YAPIONObject)yapionAny;
        return new StringBuilder().append(yapionObject.getValue("string", "").get());
    }
}