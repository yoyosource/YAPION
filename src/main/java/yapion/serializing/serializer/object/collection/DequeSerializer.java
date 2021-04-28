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
import yapion.serializing.InternalSerializer;
import yapion.serializing.data.DeserializeData;
import yapion.serializing.data.SerializeData;
import yapion.serializing.utils.DeserializeUtils;
import yapion.serializing.utils.SerializeUtils;
import yapion.utils.ReflectionsUtils;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.LinkedBlockingDeque;

import static yapion.utils.IdentifierUtils.TYPE_IDENTIFIER;

@SerializerImplementation(since = "0.23.0", initialSince = "0.7.0, 0.12.0", standsFor = {Deque.class, ArrayDeque.class, BlockingDeque.class, LinkedBlockingDeque.class, ConcurrentLinkedDeque.class})
public class DequeSerializer implements InternalSerializer<Deque<?>> {

    @Override
    public Class<?> type() {
        return Deque.class;
    }

    @Override
    public Class<?> defaultImplementation() {
        return ArrayDeque.class;
    }

    @Override
    public Class<?> interfaceType() {
        return Deque.class;
    }

    @Override
    public YAPIONAnyType serialize(SerializeData<Deque<?>> serializeData) {
        YAPIONObject yapionObject = new YAPIONObject();
        yapionObject.add(TYPE_IDENTIFIER, serializeData.object.getClass().getTypeName());
        return SerializeUtils.serializeDeque(serializeData, yapionObject);
    }

    @SuppressWarnings({"unchecked"})
    @Override
    public Deque<?> deserialize(DeserializeData<? extends YAPIONAnyType> deserializeData) {
        try {
            Object object = ReflectionsUtils.constructObject((YAPIONObject) deserializeData.object, this, false);
            YAPIONArray yapionArray = ((YAPIONObject) deserializeData.object).getArray("values");
            return DeserializeUtils.deserializeDeque(deserializeData, yapionArray, (Deque<Object>) object);
        } catch (Exception e) {
            throw new YAPIONDeserializerException(e.getMessage(), e);
        }
    }
}
