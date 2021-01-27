// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020,2021 yoyosource

package yapion.serializing.serializer.object.atomic;

import yapion.annotations.deserialize.YAPIONLoadExclude;
import yapion.annotations.serialize.YAPIONSaveExclude;
import yapion.hierarchy.typegroups.YAPIONAnyType;
import yapion.hierarchy.types.YAPIONObject;
import yapion.serializing.InternalSerializer;
import yapion.serializing.data.DeserializeData;
import yapion.serializing.data.SerializeData;
import yapion.serializing.serializer.SerializerImplementation;

import java.util.concurrent.atomic.AtomicLong;

import static yapion.utils.IdentifierUtils.TYPE_IDENTIFIER;

@YAPIONSaveExclude
@YAPIONLoadExclude
@SerializerImplementation(since = "0.20.0")
public class AtomicLongSerializer implements InternalSerializer<AtomicLong> {

    @Override
    public String type() {
        return "java.util.concurrent.atomic.AtomicLong";
    }

    @Override
    public YAPIONAnyType serialize(SerializeData<AtomicLong> serializeData) {
        YAPIONObject yapionObject = new YAPIONObject();
        yapionObject.add(TYPE_IDENTIFIER, type());
        yapionObject.add("value", serializeData.object.get());
        return yapionObject;
    }

    @Override
    public AtomicLong deserialize(DeserializeData<? extends YAPIONAnyType> deserializeData) {
        return new AtomicLong(((YAPIONObject) deserializeData.object).getValue("value", 0L).get());
    }

}