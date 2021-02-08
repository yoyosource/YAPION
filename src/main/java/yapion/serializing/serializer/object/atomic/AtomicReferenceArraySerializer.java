// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020,2021 yoyosource

package yapion.serializing.serializer.object.atomic;

import yapion.hierarchy.api.groups.YAPIONAnyType;
import yapion.hierarchy.types.YAPIONArray;
import yapion.hierarchy.types.YAPIONObject;
import yapion.serializing.InternalSerializer;
import yapion.serializing.data.DeserializeData;
import yapion.serializing.data.SerializeData;
import yapion.serializing.serializer.SerializerImplementation;

import java.util.concurrent.atomic.AtomicReferenceArray;

import static yapion.utils.IdentifierUtils.TYPE_IDENTIFIER;

@SerializerImplementation(since = "0.20.0")
public class AtomicReferenceArraySerializer implements InternalSerializer<AtomicReferenceArray<?>> {

    @Override
    public String type() {
        return "java.util.concurrent.atomic.AtomicReferenceArray";
    }

    @Override
    public YAPIONAnyType serialize(SerializeData<AtomicReferenceArray<?>> serializeData) {
        YAPIONObject yapionObject = new YAPIONObject();
        yapionObject.add(TYPE_IDENTIFIER, type());
        yapionObject.add("length", serializeData.object.length());

        Object[] objects = new Object[serializeData.object.length()];
        for (int i = 0; i < objects.length; i++) {
            objects[i] = serializeData.object.get(i);
        }
        yapionObject.add("values", serializeData.serialize(objects));
        return yapionObject;
    }

    @Override
    public AtomicReferenceArray<?> deserialize(DeserializeData<? extends YAPIONAnyType> deserializeData) {
        YAPIONObject yapionObject = (YAPIONObject) deserializeData.object;
        int length = yapionObject.getValue("length", 0).get();
        YAPIONArray yapionArray = yapionObject.getArray("values");
        Object[] objects = new Object[length];
        for (int i = 0; i < length; i++) {
            objects[i] = deserializeData.deserialize(yapionArray.getYAPIONAnyType(i));
        }
        return new AtomicReferenceArray<>(objects);
    }

}