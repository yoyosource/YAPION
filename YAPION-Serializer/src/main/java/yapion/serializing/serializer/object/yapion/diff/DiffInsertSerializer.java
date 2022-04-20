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

package yapion.serializing.serializer.object.yapion.diff;

import yapion.annotations.api.SerializerImplementation;
import yapion.hierarchy.api.groups.YAPIONAnyType;
import yapion.hierarchy.diff.DiffInsert;
import yapion.hierarchy.types.YAPIONObject;
import yapion.serializing.data.DeserializeData;
import yapion.serializing.data.SerializeData;
import yapion.serializing.serializer.FinalInternalSerializer;

@SerializerImplementation(since = "0.25.0")
public class DiffInsertSerializer implements FinalInternalSerializer<DiffInsert> {

    @Override
    public Class<?> type() {
        return DiffInsert.class;
    }

    @Override
    public YAPIONAnyType serialize(SerializeData<DiffInsert> serializeData) {
        YAPIONObject yapionObject = new YAPIONObject(type());
        yapionObject.add("path", serializeData.serialize(serializeData.object.getPath()));
        yapionObject.add("inserted", serializeData.object.getInserted());
        return yapionObject;
    }

    @Override
    public DiffInsert deserialize(DeserializeData<? extends YAPIONAnyType> deserializeData) {
        YAPIONObject yapionObject = (YAPIONObject) deserializeData.object;
        String[] path = deserializeData.deserialize(yapionObject.getArray("path"));
        return new DiffInsert(path, yapionObject.getAnyType("inserted"));
    }
}
