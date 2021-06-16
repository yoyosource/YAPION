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

package yapion.serializing.serializer.object.atomic;

import yapion.annotations.api.SerializerImplementation;
import yapion.hierarchy.api.groups.YAPIONAnyType;
import yapion.hierarchy.types.YAPIONObject;
import yapion.serializing.InternalSerializer;
import yapion.serializing.data.DeserializeData;
import yapion.serializing.data.SerializeData;

import java.util.concurrent.atomic.AtomicStampedReference;

@SerializerImplementation(since = "0.20.0")
public class AtomicStampedReferenceSerializer implements InternalSerializer<AtomicStampedReference<?>> {

    @Override
    public Class<?> type() {
        return AtomicStampedReference.class;
    }

    @Override
    public YAPIONAnyType serialize(SerializeData<AtomicStampedReference<?>> serializeData) {
        YAPIONObject yapionObject = new YAPIONObject(type());
        yapionObject.add("value", serializeData.serialize(serializeData.object.getReference()));
        yapionObject.add("stamp", serializeData.serialize(serializeData.object.getStamp()));
        return yapionObject;
    }

    @Override
    public AtomicStampedReference<?> deserialize(DeserializeData<? extends YAPIONAnyType> deserializeData) {
        YAPIONObject yapionObject = (YAPIONObject) deserializeData.object;
        Object value = deserializeData.deserialize(yapionObject.getObject("value"));
        int stamp = deserializeData.deserialize(yapionObject.getValue("stamp", 0));
        return new AtomicStampedReference<>(value, stamp);
    }
}
