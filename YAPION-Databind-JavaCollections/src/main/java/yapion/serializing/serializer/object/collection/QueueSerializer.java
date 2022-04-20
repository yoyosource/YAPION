/*
 * Copyright 2019,2020,2021 yoyosource
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package yapion.serializing.serializer.object.collection;

import yapion.annotations.api.SerializerImplementation;
import yapion.exceptions.serializing.YAPIONDeserializerException;
import yapion.hierarchy.api.groups.YAPIONAnyType;
import yapion.hierarchy.types.YAPIONArray;
import yapion.hierarchy.types.YAPIONObject;
import yapion.serializing.data.DeserializeData;
import yapion.serializing.data.SerializeData;
import yapion.serializing.serializer.FinalInternalSerializer;
import yapion.serializing.utils.SerializingUtils;
import yapion.utils.ReflectionsUtils;

import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.*;

@SerializerImplementation(since = "0.23.0", initialSince = "0.7.0, 0.12.0", standsFor = {Queue.class, PriorityQueue.class, ConcurrentLinkedQueue.class, LinkedBlockingQueue.class, LinkedTransferQueue.class, SynchronousQueue.class, ArrayBlockingQueue.class, PriorityBlockingQueue.class})
public class QueueSerializer implements FinalInternalSerializer<Queue<?>> {

    @Override
    public void init() {
        ReflectionsUtils.addSpecialCreator(ArrayBlockingQueue.class, yapionObject -> {
            return new ArrayBlockingQueue<>(yapionObject.getArray("values").length());
        });

        ReflectionsUtils.addSpecialCreator(PriorityQueue.class, yapionObject -> {
            return new PriorityQueue<>(yapionObject.getArray("values").length());
        });
    }

    @Override
    public Class<?> type() {
        return Queue.class;
    }

    @Override
    public Class<?> defaultImplementation() {
        return PriorityQueue.class;
    }

    @Override
    public Class<?> interfaceType() {
        return Queue.class;
    }

    @Override
    public YAPIONAnyType serialize(SerializeData<Queue<?>> serializeData) {
        YAPIONObject yapionObject = new YAPIONObject(serializeData.object.getClass());
        return SerializingUtils.serializeCollection(serializeData, yapionObject);
    }

    @SuppressWarnings({"unchecked"})
    @Override
    public Queue<?> deserialize(DeserializeData<? extends YAPIONAnyType> deserializeData) {
        try {
            Object object = ReflectionsUtils.constructObject((YAPIONObject) deserializeData.object, this, false, deserializeData.typeReMapper);
            YAPIONArray yapionArray = ((YAPIONObject) deserializeData.object).getArray("values");
            return SerializingUtils.deserializeCollection(deserializeData, yapionArray, (Queue<Object>) object);
        } catch (Exception e) {
            throw new YAPIONDeserializerException(e.getMessage(), e);
        }
    }
}
