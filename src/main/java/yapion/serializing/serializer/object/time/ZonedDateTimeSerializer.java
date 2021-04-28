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

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import static yapion.utils.IdentifierUtils.TYPE_IDENTIFIER;

@SerializerImplementation(since = "0.24.0")
public class ZonedDateTimeSerializer implements InternalSerializer<ZonedDateTime> {

    @Override
    public Class<?> type() {
        return ZonedDateTime.class;
    }

    @Override
    public YAPIONAnyType serialize(SerializeData<ZonedDateTime> serializeData) {
        YAPIONObject yapionObject = new YAPIONObject();
        yapionObject.add(TYPE_IDENTIFIER, type());
        yapionObject.add("localDateTime", serializeData.serialize(serializeData.object.toLocalDateTime()));
        yapionObject.add("zoneId", serializeData.serialize(serializeData.object.getZone()));
        return yapionObject;
    }

    @Override
    public ZonedDateTime deserialize(DeserializeData<? extends YAPIONAnyType> deserializeData) {
        YAPIONObject yapionObject = (YAPIONObject) deserializeData.object;
        LocalDateTime localDateTime = (LocalDateTime) deserializeData.deserialize(yapionObject.getObject("localDateTime"));
        ZoneId zoneId = (ZoneId) deserializeData.deserialize(yapionObject.getObject("zoneId"));
        return ZonedDateTime.of(localDateTime, zoneId);
    }

}
