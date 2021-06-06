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
import yapion.hierarchy.api.groups.YAPIONAnyType;
import yapion.hierarchy.types.YAPIONArray;
import yapion.hierarchy.types.YAPIONObject;
import yapion.serializing.InternalSerializer;
import yapion.serializing.data.DeserializeData;
import yapion.serializing.data.SerializeData;

import java.util.Vector;

@SerializerImplementation(since = "0.7.0")
public class VectorSerializer implements InternalSerializer<Vector<?>> {

    @Override
    public Class<?> type() {
        return Vector.class;
    }

    @Override
    public YAPIONAnyType serialize(SerializeData<Vector<?>> serializeData) {
        YAPIONObject yapionObject = new YAPIONObject(type());
        YAPIONArray yapionArray = new YAPIONArray();
        yapionObject.add("values", yapionArray);
        for (int i = 0; i < serializeData.object.size(); i++) {
            yapionArray.add(serializeData.serialize(serializeData.object.get(i)));
        }
        return yapionObject;
    }

    @Override
    @SuppressWarnings({"java:S1149"})
    public Vector<?> deserialize(DeserializeData<? extends YAPIONAnyType> deserializeData) {
        YAPIONObject yapionObject = (YAPIONObject) deserializeData.object;
        YAPIONArray yapionArray = yapionObject.getArray("values");
        Vector<Object> vector = new Vector<>();
        for (int i = 0; i < yapionArray.length(); i++) {
            vector.add(deserializeData.deserialize(yapionArray.getYAPIONAnyType(i)));
        }
        return vector;
    }
}
