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

package yapion.serializing.api;

import yapion.hierarchy.api.groups.YAPIONAnyType;
import yapion.serializing.InternalSerializer;
import yapion.serializing.data.DeserializeData;
import yapion.serializing.data.SerializeData;

public interface SerializerBaseInterface<T, K extends YAPIONAnyType> {

    Class<T> type();
    K serialize(SerializeData<T> serializeData);
    T deserialize(DeserializeData<K> deserializeData);

    default InternalSerializer<T> convert() {
        return new InternalSerializer<T>() {
            @Override
            public String type() {
                return SerializerBaseInterface.this.type().getTypeName();
            }

            @Override
            public YAPIONAnyType serialize(SerializeData<T> serializeData) {
                return SerializerBaseInterface.this.serialize(serializeData);
            }

            @Override
            @SuppressWarnings("unchecked")
            public T deserialize(DeserializeData<? extends YAPIONAnyType> deserializeData) {
                return SerializerBaseInterface.this.deserialize((DeserializeData<K>) deserializeData);
            }
        };
    }

}
