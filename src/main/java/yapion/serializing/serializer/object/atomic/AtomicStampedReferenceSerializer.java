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

import yapion.hierarchy.api.groups.YAPIONAnyType;
import yapion.hierarchy.types.YAPIONObject;
import yapion.serializing.InternalSerializer;
import yapion.serializing.data.DeserializeData;
import yapion.serializing.data.SerializeData;
import yapion.serializing.serializer.SerializerImplementation;

import java.util.concurrent.atomic.AtomicStampedReference;

import static yapion.utils.IdentifierUtils.TYPE_IDENTIFIER;

@SerializerImplementation(since = "0.20.0")
public class AtomicStampedReferenceSerializer implements InternalSerializer<AtomicStampedReference<?>> {

    @Override
    public String type() {
        return "java.util.concurrent.atomic.AtomicStampedReference";
    }

    @Override
    public YAPIONAnyType serialize(SerializeData<AtomicStampedReference<?>> serializeData) {
        YAPIONObject yapionObject = new YAPIONObject();
        yapionObject.add(TYPE_IDENTIFIER, type());
        yapionObject.add("value", serializeData.serialize(serializeData.object.getReference()));
        yapionObject.add("stamp", serializeData.serialize(serializeData.object.getStamp()));
        return yapionObject;
    }

    @Override
    public AtomicStampedReference<?> deserialize(DeserializeData<? extends YAPIONAnyType> deserializeData) {
        YAPIONObject yapionObject = (YAPIONObject) deserializeData.object;
        Object value = deserializeData.deserialize(yapionObject.getObject("value"));
        int stamp = (Integer) deserializeData.deserialize(yapionObject.getValue("stamp", 0));
        return new AtomicStampedReference<>(value, stamp);
    }
}
