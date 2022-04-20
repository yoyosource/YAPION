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
import yapion.serializing.data.DeserializeData;
import yapion.serializing.data.SerializeData;
import yapion.serializing.serializer.FinalInternalSerializer;

import java.net.Inet6Address;
import java.net.UnknownHostException;
import java.util.Base64;

@SerializerImplementation(since = "0.20.0")
public class Inet6AddressSerializer implements FinalInternalSerializer<Inet6Address> {

    @Override
    public Class<?> type() {
        return Inet6Address.class;
    }

    @Override
    public YAPIONAnyType serialize(SerializeData<Inet6Address> serializeData) {
        YAPIONObject yapionObject = new YAPIONObject(type());
        yapionObject.add("address", Base64.getEncoder().encodeToString(serializeData.object.getAddress()));
        yapionObject.add("hostAddress", serializeData.object.getHostAddress());
        yapionObject.add("scopeId", serializeData.object.getScopeId());
        return yapionObject;
    }

    @Override
    public Inet6Address deserialize(DeserializeData<? extends YAPIONAnyType> deserializeData) {
        YAPIONObject yapionObject = (YAPIONObject) deserializeData.object;
        byte[] address = Base64.getDecoder().decode(yapionObject.getValue("address", "").get());
        String hostAddress = yapionObject.getValue("hostAddress", "").get();
        int scopeId = yapionObject.getValue("scopeId", 0).get();
        try {
            return Inet6Address.getByAddress(hostAddress, address, scopeId);
        } catch (UnknownHostException e) {
            throw new YAPIONException(e.getMessage(), e.getCause());
        }
    }

}
