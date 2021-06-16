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

package yapion.serializing.serializer.object.net;

import yapion.annotations.api.SerializerImplementation;
import yapion.exceptions.YAPIONException;
import yapion.hierarchy.api.groups.YAPIONAnyType;
import yapion.hierarchy.types.YAPIONObject;
import yapion.serializing.InternalSerializer;
import yapion.serializing.data.DeserializeData;
import yapion.serializing.data.SerializeData;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Base64;

@SerializerImplementation(since = "0.20.0")
public class InetAddressSerializer implements InternalSerializer<InetAddress> {

    @Override
    public Class<?> type() {
        return InetAddress.class;
    }

    @Override
    public YAPIONAnyType serialize(SerializeData<InetAddress> serializeData) {
        YAPIONObject yapionObject = new YAPIONObject(type());
        yapionObject.add("address", Base64.getEncoder().encodeToString(serializeData.object.getAddress()));
        return yapionObject;
    }

    @Override
    public InetAddress deserialize(DeserializeData<? extends YAPIONAnyType> deserializeData) {
        try {
            return InetAddress.getByAddress(Base64.getDecoder().decode(((YAPIONObject) deserializeData.object).getValue("address", "").get()));
        } catch (UnknownHostException e) {
            throw new YAPIONException(e.getMessage(), e.getCause());
        }
    }

}
