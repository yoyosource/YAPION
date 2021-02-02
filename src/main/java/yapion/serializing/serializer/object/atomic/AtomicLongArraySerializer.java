// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020,2021 yoyosource

package yapion.serializing.serializer.object.atomic;

import yapion.annotations.deserialize.YAPIONLoadExclude;
import yapion.annotations.serialize.YAPIONSaveExclude;
import yapion.hierarchy.api.groups.YAPIONAnyType;
import yapion.hierarchy.types.YAPIONArray;
import yapion.hierarchy.types.YAPIONObject;
import yapion.serializing.InternalSerializer;
import yapion.serializing.data.DeserializeData;
import yapion.serializing.data.SerializeData;
import yapion.serializing.serializer.SerializerImplementation;

import java.util.concurrent.atomic.AtomicLongArray;

import static yapion.utils.IdentifierUtils.TYPE_IDENTIFIER;

@YAPIONSaveExclude
@YAPIONLoadExclude
@SerializerImplementation(since = "0.20.0")
public class AtomicLongArraySerializer implements InternalSerializer<AtomicLongArray> {

    @Override
    public String type() {
        return "java.util.concurrent.atomic.AtomicLongArray";
    }

    @Override
    public YAPIONAnyType serialize(SerializeData<AtomicLongArray> serializeData) {
        YAPIONObject yapionObject = new YAPIONObject();
        yapionObject.add(TYPE_IDENTIFIER, type());
        yapionObject.add("length", serializeData.object.length());

        long[] longs = new long[serializeData.object.length()];
        for (int i = 0; i < longs.length; i++) {
            longs[i] = serializeData.object.get(i);
        }
        yapionObject.add("values", serializeData.serialize(longs));
        return yapionObject;
    }

    @Override
    public AtomicLongArray deserialize(DeserializeData<? extends YAPIONAnyType> deserializeData) {
        YAPIONObject yapionObject = (YAPIONObject) deserializeData.object;
        int length = yapionObject.getValue("length", 0).get();
        YAPIONArray yapionArray = yapionObject.getArray("values");
        long[] longs = new long[length];
        for (int i = 0; i < length; i++) {
            longs[i] = (Long) deserializeData.deserialize(yapionArray.getYAPIONAnyType(i));
        }
        return new AtomicLongArray(longs);
    }

}