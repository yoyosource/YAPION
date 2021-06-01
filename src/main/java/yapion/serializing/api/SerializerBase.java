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
import yapion.serializing.SerializeManager;
import yapion.serializing.data.DeserializeData;
import yapion.serializing.data.SerializeData;

public abstract class SerializerBase<T, K extends YAPIONAnyType> {

    private InternalSerializer<T> generated = null;

    public void init() {
    }

    public abstract Class<T> type();

    public abstract K serialize(SerializeData<T> serializeData);

    public abstract T deserialize(DeserializeData<K> deserializeData);

    /**
     * Add this SerializerBase to the SerializeManager by calling
     * {@link SerializeManager#add(SerializerBase)}.
     */
    public final void add() {
        SerializeManager.add(this);
    }

    public final InternalSerializer<T> convert() {
        if (generated == null) {
            generated = convertInternal();
        }
        return generated;
    }

    protected InternalSerializer<T> convertInternal() {
        return new InternalSerializer<T>() {
            @Override
            public void init() {
                SerializerBase.this.init();
            }

            @Override
            public Class<T> type() {
                return SerializerBase.this.type();
            }

            @Override
            public YAPIONAnyType serialize(SerializeData<T> serializeData) {
                return SerializerBase.this.serialize(serializeData);
            }

            @Override
            @SuppressWarnings("unchecked")
            public T deserialize(DeserializeData<? extends YAPIONAnyType> deserializeData) {
                return SerializerBase.this.deserialize((DeserializeData<K>) deserializeData);
            }
        };
    }
}
