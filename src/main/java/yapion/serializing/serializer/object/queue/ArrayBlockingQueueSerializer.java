// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020,2021 yoyosource

package yapion.serializing.serializer.object.queue;

import yapion.hierarchy.api.groups.YAPIONAnyType;
import yapion.hierarchy.types.YAPIONArray;
import yapion.hierarchy.types.YAPIONObject;
import yapion.serializing.InternalSerializer;
import yapion.serializing.data.DeserializeData;
import yapion.serializing.data.SerializeData;
import yapion.serializing.serializer.SerializerImplementation;
import yapion.serializing.utils.DeserializeUtils;
import yapion.serializing.utils.SerializeUtils;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import static yapion.utils.IdentifierUtils.TYPE_IDENTIFIER;

@SerializerImplementation(since = "0.12.0")
public class ArrayBlockingQueueSerializer implements InternalSerializer<BlockingQueue<?>> {

    @Override
    public String type() {
        return "java.util.concurrent.ArrayBlockingQueue";
    }

    @Override
    public String primitiveType() {
        return "java.util.concurrent.BlockingQueue";
    }

    @Override
    public YAPIONAnyType serialize(SerializeData<BlockingQueue<?>> serializeData) {
        YAPIONObject yapionObject = new YAPIONObject();
        yapionObject.add(TYPE_IDENTIFIER, type());
        return SerializeUtils.serializeQueue(serializeData, yapionObject);
    }

    @Override
    public BlockingQueue<Object> deserialize(DeserializeData<? extends YAPIONAnyType> deserializeData) {
        YAPIONArray yapionArray = ((YAPIONObject) deserializeData.object).getArray("values");
        return DeserializeUtils.deserializeQueue(deserializeData, yapionArray, new ArrayBlockingQueue<>(yapionArray.length()));
    }
}