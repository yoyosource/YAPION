// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020,2021 yoyosource

package yapion.serializing.serializer.object.queue.concurrent;

import yapion.hierarchy.api.groups.YAPIONAnyType;
import yapion.hierarchy.types.YAPIONArray;
import yapion.hierarchy.types.YAPIONObject;
import yapion.serializing.InternalSerializer;
import yapion.serializing.data.DeserializeData;
import yapion.serializing.data.SerializeData;
import yapion.serializing.serializer.SerializerImplementation;

import java.util.Iterator;
import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.TransferQueue;

import static yapion.utils.IdentifierUtils.TYPE_IDENTIFIER;

@SerializerImplementation(since = "0.12.0")
public class TransferQueueSerializer implements InternalSerializer<TransferQueue<?>> {

    @Override
    public String type() {
        return "java.util.concurrent.TransferQueue";
    }

    @Override
    public YAPIONAnyType serialize(SerializeData<TransferQueue<?>> serializeData) {
        YAPIONObject yapionObject = new YAPIONObject();
        yapionObject.add(TYPE_IDENTIFIER, type());
        YAPIONArray yapionArray = new YAPIONArray();
        yapionObject.add("values", yapionArray);
        Iterator<?> iterator = serializeData.object.iterator();
        while (iterator.hasNext()) {
            yapionArray.add(serializeData.serialize(iterator.next()));
        }
        return yapionObject;
    }

    @Override
    public TransferQueue<?> deserialize(DeserializeData<? extends YAPIONAnyType> deserializeData) {
        YAPIONObject yapionObject = (YAPIONObject) deserializeData.object;
        YAPIONArray yapionArray = yapionObject.getArray("values");
        TransferQueue<Object> queue = new LinkedTransferQueue<>();
        for (int i = 0; i < yapionArray.length(); i++) {
            queue.add(deserializeData.deserialize(yapionArray.getYAPIONAnyType(i)));
        }
        return queue;
    }
}