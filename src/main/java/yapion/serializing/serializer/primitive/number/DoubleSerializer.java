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

package yapion.serializing.serializer.primitive.number;

import yapion.annotations.api.SerializerImplementation;
import yapion.hierarchy.api.groups.YAPIONAnyType;
import yapion.hierarchy.types.YAPIONValue;
import yapion.serializing.data.DeserializeData;
import yapion.serializing.data.SerializeData;
import yapion.serializing.serializer.FinalInternalSerializer;

@SerializerImplementation(since = "0.2.0")
public class DoubleSerializer implements FinalInternalSerializer<Double> {

    @Override
    public Class<?> type() {
        return Double.class;
    }

    @Override
    public Class<?> primitiveType() {
        return double.class;
    }

    @Override
    public YAPIONAnyType serialize(SerializeData<Double> serializeData) {
        return new YAPIONValue<>(serializeData.object);
    }

    @Override
    public Double deserialize(DeserializeData<? extends YAPIONAnyType> deserializeData) {
        return ((YAPIONValue<Double>) deserializeData.object).get();
    }
}
