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

package yapion.serializing.serializer.notserializable;

import yapion.annotations.api.SerializerImplementation;
import yapion.hierarchy.api.groups.YAPIONAnyType;
import yapion.hierarchy.types.YAPIONValue;
import yapion.serializing.SerializeManager;
import yapion.serializing.api.YAPIONSerializerRegistrator;
import yapion.serializing.data.DeserializeData;
import yapion.serializing.data.SerializeData;
import yapion.serializing.serializer.FinalInternalSerializer;

import java.net.ServerSocket;
import java.net.Socket;

public class NotSerializableRegistrator implements YAPIONSerializerRegistrator {

    @Override
    public void register() {
        SerializeManager.add(new NotSerializableSerializer<>(ServerSocket.class)); // 0.12.0
        SerializeManager.add(new NotSerializableSerializer<>(Socket.class)); // 0.12.0
        SerializeManager.add(new NotSerializableSerializer<>(ProcessBuilder.class)); // 0.12.0
        SerializeManager.add(new NotSerializableSerializer<>(Process.class)); // 0.12.0
        SerializeManager.add(new NotSerializableSerializer<>(ThreadGroup.class)); // 0.12.0
        SerializeManager.add(new NotSerializableSerializer<>(ThreadLocal.class)); // 0.12.0
        SerializeManager.add(new NotSerializableSerializer<>(Thread.class)); // 0.10.0
    }

    @SerializerImplementation(since = "0.26.0")
    public static class NotSerializableSerializer<T> implements FinalInternalSerializer<T> {

        protected Class<T> clazz;

        protected NotSerializableSerializer(Class<T> clazz) {
            this.clazz = clazz;
        }

        public Class<?> type() {
            return clazz;
        }

        public YAPIONAnyType serialize(SerializeData<T> serializeData) {
            serializeData.signalDataLoss();
            return new YAPIONValue<>(null);
        }

        public T deserialize(DeserializeData<? extends YAPIONAnyType> deserializeData) {
            return null;
        }
    }
}
