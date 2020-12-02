// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020 yoyosource

package yapion.serializing.serializer.object.security;

import yapion.annotations.deserialize.YAPIONLoadExclude;
import yapion.annotations.serialize.YAPIONSaveExclude;
import yapion.hierarchy.typegroups.YAPIONAnyType;
import yapion.hierarchy.types.YAPIONObject;
import yapion.serializing.InternalSerializer;
import yapion.serializing.data.DeserializeData;
import yapion.serializing.data.SerializeData;
import yapion.serializing.serializer.SerializerImplementation;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

import static yapion.utils.IdentifierUtils.TYPE_IDENTIFIER;

@YAPIONSaveExclude
@YAPIONLoadExclude
// TODO: Change this version to the proper one
@SerializerImplementation(since = "0.?.0")
public class KeyPairSerializer implements InternalSerializer<KeyPair> {

    @Override
    public String type() {
        return "java.security.KeyPair";
    }

    @Override
    public YAPIONAnyType serialize(SerializeData<KeyPair> serializeData) {
        YAPIONObject yapionObject = new YAPIONObject();
        yapionObject.add(TYPE_IDENTIFIER, type());
        yapionObject.add("privateKey", serializeData.serialize(serializeData.object.getPrivate()));
        yapionObject.add("publicKey", serializeData.serialize(serializeData.object.getPublic()));
        return yapionObject;
    }

    @Override
    public KeyPair deserialize(DeserializeData<? extends YAPIONAnyType> deserializeData) {
        YAPIONObject yapionObject = (YAPIONObject) deserializeData.object;
        PrivateKey privateKey = (PrivateKey) deserializeData.deserialize(yapionObject.getObject("privateKey"));
        PublicKey publicKey = (PublicKey) deserializeData.deserialize(yapionObject.getObject("publicKey"));
        return new KeyPair(publicKey, privateKey);
    }

}