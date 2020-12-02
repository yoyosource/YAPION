// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020 yoyosource

package yapion.serializing.serializer.object.atomic;

import yapion.annotations.deserialize.YAPIONLoadExclude;
import yapion.annotations.serialize.YAPIONSaveExclude;
import yapion.hierarchy.typegroups.YAPIONAnyType;
import yapion.hierarchy.types.YAPIONObject;
import yapion.serializing.InternalSerializer;
import yapion.serializing.data.DeserializeData;
import yapion.serializing.data.SerializeData;
import yapion.serializing.serializer.SerializerImplementation;

import java.util.concurrent.atomic.AtomicInteger;

import static yapion.utils.IdentifierUtils.TYPE_IDENTIFIER;

@YAPIONSaveExclude
@YAPIONLoadExclude
// TODO: Change this version to the proper one
@SerializerImplementation(since = "0.?.0")
public class AtomicIntegerSerializer implements InternalSerializer<AtomicInteger> {

    @Override
    public String type() {
        return "java.util.concurrent.atomic.AtomicInteger";
    }

    @Override
    public YAPIONAnyType serialize(SerializeData<AtomicInteger> serializeData) {
        YAPIONObject yapionObject = new YAPIONObject();
        yapionObject.add(TYPE_IDENTIFIER, type());
        yapionObject.add("value", serializeData.object.get());
        return yapionObject;
    }

    @Override
    public AtomicInteger deserialize(DeserializeData<? extends YAPIONAnyType> deserializeData) {
        return new AtomicInteger(((YAPIONObject) deserializeData.object).getValue("value", 0).get());
    }

}