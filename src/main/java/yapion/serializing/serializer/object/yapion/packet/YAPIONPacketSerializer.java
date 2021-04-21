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

package yapion.serializing.serializer.object.yapion.packet;

import yapion.hierarchy.api.groups.YAPIONAnyType;
import yapion.packet.YAPIONPacket;
import yapion.serializing.InternalSerializer;
import yapion.serializing.data.DeserializeData;
import yapion.serializing.data.SerializeData;
import yapion.annotations.api.SerializerImplementation;

@SerializerImplementation(since = "0.10.0")
public class YAPIONPacketSerializer implements InternalSerializer<YAPIONPacket> {

    @Override
    public String type() {
        return "yapion.packet.YAPIONPacket";
    }

    @Override
    public boolean empty() {
        return true;
    }

    @Override
    public Class<?> classType() {
        return YAPIONPacket.class;
    }

    @Override
    public boolean saveWithoutAnnotation() {
        return true;
    }

    @Override
    public boolean loadWithoutAnnotation() {
        return true;
    }

    @Override
    public boolean createWithObjenesis() {
        return true;
    }

    @Override
    public YAPIONAnyType serialize(SerializeData<YAPIONPacket> serializeData) {
        return null;
    }

    @Override
    public YAPIONPacket deserialize(DeserializeData<? extends YAPIONAnyType> deserializeData) {
        return null;
    }

}
