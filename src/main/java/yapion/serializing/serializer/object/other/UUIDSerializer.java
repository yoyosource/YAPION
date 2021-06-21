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

package yapion.serializing.serializer.object.other;

import yapion.annotations.api.SerializerImplementation;
import yapion.hierarchy.api.groups.YAPIONAnyType;
import yapion.hierarchy.types.YAPIONObject;
import yapion.serializing.data.DeserializeData;
import yapion.serializing.data.SerializeData;
import yapion.serializing.serializer.FinalInternalSerializer;

import java.util.UUID;

@SerializerImplementation(since = "0.18.0")
public class UUIDSerializer implements FinalInternalSerializer<UUID> {

    @Override
    public Class<?> type() {
        return UUID.class;
    }

    @Override
    public YAPIONAnyType serialize(SerializeData<UUID> serializeData) {
        YAPIONObject yapionObject = new YAPIONObject(type());
        yapionObject.add("uuid", serializeData.object.toString());
        return yapionObject;
    }

    @Override
    public UUID deserialize(DeserializeData<? extends YAPIONAnyType> deserializeData) {
        YAPIONObject yapionObject = (YAPIONObject) deserializeData.object;
        return UUID.fromString(yapionObject.getValue("uuid", "").get());
    }
}
