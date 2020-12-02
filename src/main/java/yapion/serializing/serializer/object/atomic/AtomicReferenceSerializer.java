// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020 yoyosource

package yapion.serializing.serializer.object.atomic;

import yapion.annotations.deserialize.YAPIONLoadExclude;
import yapion.annotations.serialize.YAPIONSaveExclude;
import yapion.hierarchy.typegroups.YAPIONAnyType;
import yapion.hierarchy.types.YAPIONArray;
import yapion.hierarchy.types.YAPIONObject;
import yapion.serializing.InternalSerializer;
import yapion.serializing.data.DeserializeData;
import yapion.serializing.data.SerializeData;
import yapion.serializing.serializer.SerializerImplementation;

import java.util.concurrent.atomic.AtomicReference;

import static yapion.utils.IdentifierUtils.TYPE_IDENTIFIER;

@YAPIONSaveExclude
@YAPIONLoadExclude
// TODO: Change this version to the proper one
@SerializerImplementation(since = "0.?.0")
public class AtomicReferenceSerializer implements InternalSerializer<AtomicReference<?>> {

    @Override
    public String type() {
        return "java.util.concurrent.atomic.AtomicReference";
    }

    @Override
    public YAPIONAnyType serialize(SerializeData<AtomicReference<?>> serializeData) {
        YAPIONObject yapionObject = new YAPIONObject();
        yapionObject.add(TYPE_IDENTIFIER, type());
        yapionObject.add("value", serializeData.serialize(serializeData.object.get()));
        return yapionObject;
    }

    @Override
    public AtomicReference<?> deserialize(DeserializeData<? extends YAPIONAnyType> deserializeData) {
        return new AtomicReference<>(deserializeData.deserialize(((YAPIONObject) deserializeData.object).getObject("value")));
    }
}