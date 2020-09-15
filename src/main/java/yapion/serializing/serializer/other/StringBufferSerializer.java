// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020 yoyosource

package yapion.serializing.serializer.other;

import yapion.annotations.deserialize.YAPIONLoadExclude;
import yapion.annotations.serialize.YAPIONSaveExclude;
import yapion.hierarchy.typegroups.YAPIONAnyType;
import yapion.hierarchy.YAPIONVariable;
import yapion.hierarchy.types.YAPIONObject;
import yapion.hierarchy.types.YAPIONValue;
import yapion.serializing.InternalSerializer;
import yapion.serializing.SerializeManager;
import yapion.serializing.YAPIONDeserializer;
import yapion.serializing.YAPIONSerializer;
import yapion.serializing.serializer.SerializerImplementation;

@YAPIONSaveExclude(context = "*")
@YAPIONLoadExclude(context = "*")
@SerializerImplementation
public class StringBufferSerializer implements InternalSerializer<StringBuffer> {

    @Override
    public String type() {
        return "java.lang.StringBuffer";
    }

    @Override
    public YAPIONAnyType serialize(StringBuffer object, YAPIONSerializer yapionSerializer) {
        YAPIONObject yapionObject = new YAPIONObject();
        yapionObject.add(new YAPIONVariable(SerializeManager.TYPE_IDENTIFIER, new YAPIONValue<>("java.lang.StringBuffer")));
        yapionObject.add(new YAPIONVariable("string", new YAPIONValue<>(object.toString())));
        return yapionObject;
    }

    @Override
    public StringBuffer deserialize(YAPIONAnyType yapionAnyType, YAPIONDeserializer yapionDeserializer) {
        YAPIONObject yapionObject = (YAPIONObject) yapionAnyType;
        return new StringBuffer().append(yapionObject.getValue("string", "").get());
    }
}