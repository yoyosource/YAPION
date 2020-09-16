// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020 yoyosource

package yapion.serializing.serializer.other;

import yapion.annotations.deserialize.YAPIONLoadExclude;
import yapion.annotations.serialize.YAPIONSaveExclude;
import yapion.hierarchy.typegroups.YAPIONAnyType;
import yapion.hierarchy.types.YAPIONVariable;
import yapion.hierarchy.types.YAPIONObject;
import yapion.hierarchy.types.YAPIONValue;
import yapion.serializing.InternalSerializer;
import yapion.serializing.SerializeManager;
import yapion.serializing.YAPIONDeserializer;
import yapion.serializing.YAPIONSerializer;
import yapion.serializing.serializer.SerializerImplementation;

import java.util.Optional;

@YAPIONSaveExclude(context = "*")
@YAPIONLoadExclude(context = "*")
@SerializerImplementation
public class OptionalSerializer implements InternalSerializer<Optional<?>> {

    @Override
    public String type() {
        return "java.util.Optional";
    }

    @Override
    public YAPIONAnyType serialize(Optional<?> object, YAPIONSerializer yapionSerializer) {
        YAPIONObject yapionObject = new YAPIONObject();
        yapionObject.add(new YAPIONVariable(SerializeManager.TYPE_IDENTIFIER, new YAPIONValue<>("java.util.Optional")));
        yapionObject.add(new YAPIONVariable("present", new YAPIONValue<>(object.isPresent())));
        object.ifPresent(o -> yapionObject.add(new YAPIONVariable("value", yapionSerializer.parse(o))));
        return yapionObject;
    }

    @Override
    @SuppressWarnings({"java:S5411"})
    public Optional<?> deserialize(YAPIONAnyType yapionAnyType, YAPIONDeserializer yapionDeserializer) {
        YAPIONObject yapionObject = (YAPIONObject) yapionAnyType;
        if (yapionObject.getValue("present", true).get()) {
            return Optional.ofNullable(yapionDeserializer.parse(yapionObject.getVariable("value").getValue()));
        }
        return Optional.empty();
    }
}