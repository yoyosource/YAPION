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

import java.util.concurrent.atomic.AtomicStampedReference;

import static yapion.utils.IdentifierUtils.TYPE_IDENTIFIER;

@YAPIONSaveExclude
@YAPIONLoadExclude
@SerializerImplementation(since = "0.20.0")
public class AtomicStampedReferenceSerializer implements InternalSerializer<AtomicStampedReference<?>> {

    @Override
    public String type() {
        return "java.util.concurrent.atomic.AtomicStampedReference";
    }

    @Override
    public YAPIONAnyType serialize(SerializeData<AtomicStampedReference<?>> serializeData) {
        YAPIONObject yapionObject = new YAPIONObject();
        yapionObject.add(TYPE_IDENTIFIER, type());
        yapionObject.add("value", serializeData.serialize(serializeData.object.getReference()));
        yapionObject.add("stamp", serializeData.serialize(serializeData.object.getStamp()));
        return yapionObject;
    }

    @Override
    public AtomicStampedReference<?> deserialize(DeserializeData<? extends YAPIONAnyType> deserializeData) {
        YAPIONObject yapionObject = (YAPIONObject) deserializeData.object;
        Object value = deserializeData.deserialize(yapionObject.getObject("value"));
        int stamp = (Integer) deserializeData.deserialize(yapionObject.getValue("stamp", 0));
        return new AtomicStampedReference<>(value, stamp);
    }
}