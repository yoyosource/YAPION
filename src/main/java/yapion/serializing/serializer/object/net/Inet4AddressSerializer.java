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

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Base64;

import static yapion.utils.IdentifierUtils.TYPE_IDENTIFIER;

@YAPIONSaveExclude
@YAPIONLoadExclude
@SerializerImplementation(since = "0.20.0")
public class Inet4AddressSerializer implements InternalSerializer<Inet4Address> {

    @Override
    public String type() {
        return "java.net.Inet4Address";
    }

    @Override
    public YAPIONAnyType serialize(SerializeData<Inet4Address> serializeData) {
        YAPIONObject yapionObject = new YAPIONObject();
        yapionObject.add(TYPE_IDENTIFIER, type());
        yapionObject.add("address", Base64.getEncoder().encodeToString(serializeData.object.getAddress()));
        return yapionObject;
    }

    @Override
    public Inet4Address deserialize(DeserializeData<? extends YAPIONAnyType> deserializeData) {
        try {
            return (Inet4Address) InetAddress.getByAddress(Base64.getDecoder().decode(((YAPIONObject) deserializeData.object).getValue("address", "").get()));
        } catch (UnknownHostException e) {
            throw new YAPIONException(e.getMessage(), e.getCause());
        }
    }
}