// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020,2021 yoyosource

package yapion.serializing.serializer.object.net;

import yapion.annotations.deserialize.YAPIONLoadExclude;
import yapion.annotations.serialize.YAPIONSaveExclude;
import yapion.exceptions.YAPIONException;
import yapion.hierarchy.typegroups.YAPIONAnyType;
import yapion.hierarchy.types.YAPIONObject;
import yapion.serializing.InternalSerializer;
import yapion.serializing.data.DeserializeData;
import yapion.serializing.data.SerializeData;
import yapion.serializing.serializer.SerializerImplementation;

import java.net.Inet6Address;
import java.net.UnknownHostException;
import java.util.Base64;

import static yapion.utils.IdentifierUtils.TYPE_IDENTIFIER;

@YAPIONSaveExclude
@YAPIONLoadExclude
@SerializerImplementation(since = "0.20.0")
public class Inet6AddressSerializer implements InternalSerializer<Inet6Address> {

    @Override
    public String type() {
        return "java.net.Inet6Address";
    }

    @Override
    public YAPIONAnyType serialize(SerializeData<Inet6Address> serializeData) {
        YAPIONObject yapionObject = new YAPIONObject();
        yapionObject.add(TYPE_IDENTIFIER, type());
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