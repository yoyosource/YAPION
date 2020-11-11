// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020 yoyosource

package yapion.serializing.serializer.object.queue.concurrent;

import yapion.annotations.deserialize.YAPIONLoadExclude;
import yapion.annotations.serialize.YAPIONSaveExclude;
import yapion.hierarchy.typegroups.YAPIONAnyType;
import yapion.hierarchy.types.YAPIONArray;
import yapion.hierarchy.types.YAPIONObject;
import yapion.hierarchy.types.YAPIONVariable;
import yapion.serializing.InternalSerializer;
import yapion.serializing.data.DeserializeData;
import yapion.serializing.data.SerializeData;
import yapion.serializing.serializer.SerializerImplementation;

import java.util.Iterator;
import java.util.concurrent.LinkedTransferQueue;

import static yapion.utils.IdentifierUtils.TYPE_IDENTIFIER;

@YAPIONSaveExclude(context = "*")
@YAPIONLoadExclude(context = "*")
@SerializerImplementation
public class LinkedTransferQueueSerializer implements InternalSerializer<LinkedTransferQueue<?>> {

    @Override
    public String type() {
        return "java.util.concurrent.LinkedTransferQueue";
    }

    @Override
    public YAPIONAnyType serialize(SerializeData<LinkedTransferQueue<?>> serializeData) {
        YAPIONObject yapionObject = new YAPIONObject();
        yapionObject.add(TYPE_IDENTIFIER, type());
        YAPIONArray yapionArray = new YAPIONArray();
        yapionObject.add(new YAPIONVariable("values", yapionArray));
        Iterator<?> iterator = serializeData.object.iterator();
        while (iterator.hasNext()) {
            yapionArray.add(serializeData.serialize(iterator.next()));
        }
        return yapionObject;
    }

    @Override
    public LinkedTransferQueue<?> deserialize(DeserializeData<? extends YAPIONAnyType> deserializeData) {
        YAPIONObject yapionObject = (YAPIONObject) deserializeData.object;
        YAPIONArray yapionArray = yapionObject.getArray("values");
        LinkedTransferQueue<Object> queue = new LinkedTransferQueue<>();
        for (int i = 0; i < yapionArray.length(); i++) {
            queue.add(deserializeData.deserialize(yapionArray.get(i)));
        }
        return queue;
    }
}