// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020,2021 yoyosource

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

import java.util.concurrent.atomic.AtomicIntegerArray;

import static yapion.utils.IdentifierUtils.TYPE_IDENTIFIER;

@YAPIONSaveExclude
@YAPIONLoadExclude
@SerializerImplementation(since = "0.20.0")
public class AtomicIntegerArraySerializer implements InternalSerializer<AtomicIntegerArray> {

    @Override
    public String type() {
        return "java.util.concurrent.atomic.AtomicIntegerArray";
    }

    @Override
    public YAPIONAnyType serialize(SerializeData<AtomicIntegerArray> serializeData) {
        YAPIONObject yapionObject = new YAPIONObject();
        yapionObject.add(TYPE_IDENTIFIER, type());
        yapionObject.add("length", serializeData.object.length());

        int[] ints = new int[serializeData.object.length()];
        for (int i = 0; i < ints.length; i++) {
            ints[i] = serializeData.object.get(i);
        }
        yapionObject.add("values", serializeData.serialize(ints));
        return yapionObject;
    }

    @Override
    public AtomicIntegerArray deserialize(DeserializeData<? extends YAPIONAnyType> deserializeData) {
        YAPIONObject yapionObject = (YAPIONObject) deserializeData.object;
        int length = yapionObject.getValue("length", 0).get();
        YAPIONArray yapionArray = yapionObject.getArray("values");
        int[] ints = new int[length];
        for (int i = 0; i < length; i++) {
            ints[i] = (Integer) deserializeData.deserialize(yapionArray.get(i));
        }
        return new AtomicIntegerArray(ints);
    }
}