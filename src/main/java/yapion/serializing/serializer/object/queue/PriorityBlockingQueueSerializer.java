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

import java.util.concurrent.PriorityBlockingQueue;

import static yapion.utils.IdentifierUtils.TYPE_IDENTIFIER;

@SerializerImplementation(since = "0.12.0")
public class PriorityBlockingQueueSerializer implements InternalSerializer<PriorityBlockingQueue<?>> {

    @Override
    public String type() {
        return "java.util.concurrent.PriorityBlockingQueue";
    }

    @Override
    public YAPIONAnyType serialize(SerializeData<PriorityBlockingQueue<?>> serializeData) {
        YAPIONObject yapionObject = new YAPIONObject();
        yapionObject.add(TYPE_IDENTIFIER, type());
        return SerializeUtils.serializeQueue(serializeData, yapionObject);
    }

    @Override
    public PriorityBlockingQueue<?> deserialize(DeserializeData<? extends YAPIONAnyType> deserializeData) {
        YAPIONArray yapionArray = ((YAPIONObject) deserializeData.object).getArray("values");
        return DeserializeUtils.deserializeQueue(deserializeData, yapionArray, new PriorityBlockingQueue<>(yapionArray.length()));
    }
}