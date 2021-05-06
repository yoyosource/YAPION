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
import yapion.hierarchy.diff.DiffMove;
import yapion.hierarchy.types.YAPIONObject;
import yapion.serializing.InternalSerializer;
import yapion.serializing.data.DeserializeData;
import yapion.serializing.data.SerializeData;

import static yapion.utils.IdentifierUtils.TYPE_IDENTIFIER;

@SerializerImplementation(since = "0.26.0")
public class DiffMoveSerializer implements InternalSerializer<DiffMove> {

    @Override
    public Class<?> type() {
        return DiffMove.class;
    }

    @Override
    public YAPIONAnyType serialize(SerializeData<DiffMove> serializeData) {
        YAPIONObject yapionObject = new YAPIONObject();
        yapionObject.add(TYPE_IDENTIFIER, type());
        yapionObject.add("fromPath", serializeData.serialize(serializeData.object.getFromPath()));
        yapionObject.add("toPath", serializeData.serialize(serializeData.object.getToPath()));
        return yapionObject;
    }

    @Override
    public DiffMove deserialize(DeserializeData<? extends YAPIONAnyType> deserializeData) {
        YAPIONObject yapionObject = (YAPIONObject) deserializeData.object;
        String[] fromPath = (String[]) deserializeData.deserialize(yapionObject.getArray("fromPath"));
        String[] toPath = (String[]) deserializeData.deserialize(yapionObject.getArray("toPath"));
        return new DiffMove(fromPath, toPath);
    }
}
