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

package yapion.serializing.serializer.object.time;

import yapion.annotations.api.SerializerImplementation;
import yapion.hierarchy.api.groups.YAPIONAnyType;
import yapion.hierarchy.types.YAPIONObject;
import yapion.serializing.InternalSerializer;
import yapion.serializing.data.DeserializeData;
import yapion.serializing.data.SerializeData;

import java.time.LocalTime;
import java.time.OffsetTime;
import java.time.ZoneOffset;

import static yapion.utils.IdentifierUtils.TYPE_IDENTIFIER;

@SerializerImplementation(since = "0.24.0")
public class OffsetTimeSerializer implements InternalSerializer<OffsetTime> {

    @Override
    public Class<?> type() {
        return OffsetTime.class;
    }

    @Override
    public YAPIONAnyType serialize(SerializeData<OffsetTime> serializeData) {
        YAPIONObject yapionObject = new YAPIONObject();
        yapionObject.add(TYPE_IDENTIFIER, type());
        yapionObject.add("localTime", serializeData.serialize(serializeData.object.toLocalTime()));
        yapionObject.add("offset", serializeData.serialize(serializeData.object.getOffset()));
        return yapionObject;
    }

    @Override
    public OffsetTime deserialize(DeserializeData<? extends YAPIONAnyType> deserializeData) {
        YAPIONObject yapionObject = (YAPIONObject) deserializeData.object;
        LocalTime localTime = (LocalTime) deserializeData.deserialize(yapionObject.getObject("localTime"));
        ZoneOffset zoneOffset = (ZoneOffset) deserializeData.deserialize(yapionObject.getObject("offset"));
        return OffsetTime.of(localTime, zoneOffset);
    }
}
