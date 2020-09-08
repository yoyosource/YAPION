// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020 yoyosource

package yapion.serializing.serializer.object;

import yapion.annotations.deserialize.YAPIONLoadExclude;
import yapion.annotations.serialize.YAPIONSaveExclude;
import yapion.hierarchy.YAPIONAny;
import yapion.hierarchy.YAPIONVariable;
import yapion.hierarchy.types.YAPIONArray;
import yapion.hierarchy.types.YAPIONObject;
import yapion.hierarchy.types.YAPIONValue;
import yapion.serializing.InternalSerializer;
import yapion.serializing.SerializeManager;
import yapion.serializing.YAPIONDeserializer;
import yapion.serializing.YAPIONSerializer;
import yapion.serializing.serializer.SerializeLoader;

import java.util.Iterator;
import java.util.PriorityQueue;

@YAPIONSaveExclude(context = "*")
@YAPIONLoadExclude(context = "*")
@SerializeLoader
public class QueueSerializerPriority implements InternalSerializer<PriorityQueue<?>> {

    @Override
    public String type() {
        return "java.util.PriorityQueue";
    }

    @Override
    public YAPIONAny serialize(PriorityQueue<?> object, YAPIONSerializer yapionSerializer) {
        YAPIONObject yapionObject = new YAPIONObject();
        yapionObject.add(new YAPIONVariable(SerializeManager.TYPE_IDENTIFIER, new YAPIONValue<>(type())));
        YAPIONArray yapionArray = new YAPIONArray();
        yapionObject.add(new YAPIONVariable("values", yapionArray));
        Iterator<?> iterator = object.iterator();
        while (iterator.hasNext()) {
            yapionArray.add(yapionSerializer.parse(iterator.next(), yapionSerializer));
        }
        return yapionObject;
    }

    @Override
    public PriorityQueue<?> deserialize(YAPIONAny yapionAny, YAPIONDeserializer yapionDeserializer) {
        YAPIONObject yapionObject = (YAPIONObject) yapionAny;
        YAPIONArray yapionArray = yapionObject.getArray("values");
        PriorityQueue<Object> queue = new PriorityQueue<>();
        for (int i = 0; i < yapionArray.length(); i++) {
            queue.add(yapionDeserializer.parse(yapionArray.get(i), yapionDeserializer));
        }
        return queue;
    }
}