// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020,2021 yoyosource

package yapion.serializing.serializer.object.security;

import yapion.exceptions.serializing.YAPIONSerializerException;
import yapion.hierarchy.api.groups.YAPIONAnyType;
import yapion.hierarchy.types.YAPIONObject;
import yapion.serializing.InternalSerializer;
import yapion.serializing.data.DeserializeData;
import yapion.serializing.data.SerializeData;
import yapion.serializing.serializer.SerializerImplementation;
import yapion.serializing.serializer.object.security.internal.KeySpecSerializerProvider;

import java.security.PublicKey;

import static yapion.utils.IdentifierUtils.KEY_IDENTIFIER;
import static yapion.utils.IdentifierUtils.TYPE_IDENTIFIER;

@SerializerImplementation(since = "0.20.0")
public class PublicKeySerializer implements InternalSerializer<PublicKey> {

    @Override
    public String type() {
        return "java.security.PublicKey";
    }

    @Override
    public Class<?> interfaceType() {
        return PublicKey.class;
    }

    @Override
    public YAPIONAnyType serialize(SerializeData<PublicKey> serializeData) {
        YAPIONObject yapionObject = new YAPIONObject();
        yapionObject.add(TYPE_IDENTIFIER, type());
        yapionObject.add(KEY_IDENTIFIER, serializeData.object.getClass().getTypeName());
        yapionObject.add("algorithm", serializeData.object.getAlgorithm());

        try {
            yapionObject.add("publicKey", KeySpecSerializerProvider.serializePublicKey(serializeData.object));
        } catch (Exception e) {
            throw new YAPIONSerializerException(e.getMessage(), e);
        }
        return yapionObject;
    }

    @Override
    public PublicKey deserialize(DeserializeData<? extends YAPIONAnyType> deserializeData) {
        YAPIONObject yapionObject = (YAPIONObject) deserializeData.object;
        String key = yapionObject.getValue(KEY_IDENTIFIER, String.class).get();
        String algorithm = yapionObject.getValue("algorithm", String.class).get();
        YAPIONObject publicKey = yapionObject.getObject("publicKey");

        try {
            return KeySpecSerializerProvider.deserializePublicKey(Class.forName(key), publicKey, algorithm);
        } catch (Exception e) {
            throw new YAPIONSerializerException(e.getMessage(), e);
        }
    }

}