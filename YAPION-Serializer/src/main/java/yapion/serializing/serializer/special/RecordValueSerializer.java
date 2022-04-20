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

package yapion.serializing.serializer.special;

import yapion.annotations.api.SerializerImplementation;
import yapion.exceptions.serializing.YAPIONDeserializerException;
import yapion.hierarchy.api.groups.YAPIONAnyType;
import yapion.hierarchy.types.YAPIONObject;
import yapion.serializing.data.DeserializeData;
import yapion.serializing.data.SerializeData;
import yapion.serializing.serializer.FinalInternalSerializer;

@SerializerImplementation(since = "0.26.0")
public class RecordValueSerializer implements FinalInternalSerializer<RecordValue> {

    @Override
    public Class<?> type() {
        return RecordValue.class;
    }

    @Override
    public YAPIONAnyType serialize(SerializeData<RecordValue> serializeData) {
        YAPIONObject yapionObject = new YAPIONObject(type());
        yapionObject.add("name", serializeData.object.name);
        yapionObject.add("index", serializeData.object.index);
        yapionObject.add("type", serializeData.object.type.getTypeName());
        yapionObject.add("value", serializeData.serialize(serializeData.object.value));
        return yapionObject;
    }

    @Override
    public RecordValue deserialize(DeserializeData<? extends YAPIONAnyType> deserializeData) {
        YAPIONObject yapionObject = (YAPIONObject) deserializeData.object;
        try {
            return new RecordValue(
                    yapionObject.getString("name"),
                    yapionObject.getInt("index"),
                    Class.forName(yapionObject.getString("type")),
                    deserializeData.deserialize(yapionObject.getAnyType("value"))
            );
        } catch (ClassNotFoundException e) {
            throw new YAPIONDeserializerException(e.getMessage(), e);
        }
    }
}
