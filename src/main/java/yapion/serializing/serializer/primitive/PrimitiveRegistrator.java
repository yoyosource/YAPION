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

package yapion.serializing.serializer.primitive;

import yapion.annotations.api.SerializerImplementation;
import yapion.hierarchy.api.groups.YAPIONAnyType;
import yapion.hierarchy.types.YAPIONValue;
import yapion.serializing.SerializeManager;
import yapion.serializing.api.YAPIONSerializerRegistrator;
import yapion.serializing.data.DeserializeData;
import yapion.serializing.data.SerializeData;
import yapion.serializing.serializer.FinalInternalSerializer;
import yapion.utils.ClassUtils;

import java.math.BigDecimal;
import java.math.BigInteger;

public class PrimitiveRegistrator implements YAPIONSerializerRegistrator {

    @Override
    public void register() {
        SerializeManager.add(new PrimitiveSerializer<>(Character.class)); // 0.2.0
        SerializeManager.add(new PrimitiveSerializer<>(Boolean.class)); // 0.11.0
        SerializeManager.add(new PrimitiveSerializer<>(String.class)); // 0.2.0
        SerializeManager.add(new PrimitiveSerializer<>(BigDecimal.class)); // 0.2.0
        SerializeManager.add(new PrimitiveSerializer<>(BigInteger.class)); // 0.2.0
        SerializeManager.add(new PrimitiveSerializer<>(Byte.class)); // 0.2.0
        SerializeManager.add(new PrimitiveSerializer<>(Double.class)); // 0.2.0
        SerializeManager.add(new PrimitiveSerializer<>(Float.class)); // 0.2.0
        SerializeManager.add(new PrimitiveSerializer<>(Integer.class)); // 0.2.0
        SerializeManager.add(new PrimitiveSerializer<>(Long.class)); // 0.2.0
        SerializeManager.add(new PrimitiveSerializer<>(Short.class)); // 0.2.0
    }

    @SerializerImplementation(since = "0.26.0")
    public static class PrimitiveSerializer<T> implements FinalInternalSerializer<T> {

        protected Class<T> clazz;

        protected PrimitiveSerializer(Class<T> clazz) {
            this.clazz = clazz;
        }

        public Class<?> type() {
            return clazz;
        }

        public Class<?> primitiveType() {
            return ClassUtils.getPrimitive(clazz);
        }

        public YAPIONAnyType serialize(SerializeData<T> serializeData) {
            return new YAPIONValue<>(serializeData.object);
        }

        public T deserialize(DeserializeData<? extends YAPIONAnyType> deserializeData) {
            return ((YAPIONValue<T>) deserializeData.object).get();
        }
    }
}
