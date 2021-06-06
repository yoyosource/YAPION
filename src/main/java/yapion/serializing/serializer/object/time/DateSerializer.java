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

import java.util.Date;

@SerializerImplementation(since = "0.20.0")
public class DateSerializer implements InternalSerializer<Date> {

    @Override
    public Class<?> type() {
        return Date.class;
    }

    @Override
    public YAPIONAnyType serialize(SerializeData<Date> serializeData) {
        YAPIONObject yapionObject = new YAPIONObject(type());
        yapionObject.add("date", serializeData.object.getTime());
        return yapionObject;
    }

    @Override
    public Date deserialize(DeserializeData<? extends YAPIONAnyType> deserializeData) {
        return new Date(((YAPIONObject) deserializeData.object).getValue("date", 0L).get());
    }
}
