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

import java.util.concurrent.atomic.AtomicBoolean;

import static yapion.utils.IdentifierUtils.TYPE_IDENTIFIER;

@YAPIONSaveExclude
@YAPIONLoadExclude
@SerializerImplementation(since = "0.20.0")
public class AtomicBooleanSerializer implements InternalSerializer<AtomicBoolean> {

    @Override
    public String type() {
        return "java.util.concurrent.atomic.AtomicBoolean";
    }

    @Override
    public YAPIONAnyType serialize(SerializeData<AtomicBoolean> serializeData) {
        YAPIONObject yapionObject = new YAPIONObject();
        yapionObject.add(TYPE_IDENTIFIER, type());
        yapionObject.add("value", serializeData.object.get());
        return yapionObject;
    }

    @Override
    public AtomicBoolean deserialize(DeserializeData<? extends YAPIONAnyType> deserializeData) {
        return new AtomicBoolean(((YAPIONObject) deserializeData.object).getValue("value", false).get());
    }

}