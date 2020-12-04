// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020 yoyosource

package yapion.serializing.serializer.object.security;

import sun.security.provider.DSAPublicKeyImpl;
import sun.security.rsa.RSAPublicKeyImpl;
import yapion.annotations.deserialize.YAPIONLoadExclude;
import yapion.annotations.serialize.YAPIONSaveExclude;
import yapion.exceptions.YAPIONException;
import yapion.hierarchy.typegroups.YAPIONAnyType;
import yapion.hierarchy.types.YAPIONObject;
import yapion.serializing.InternalSerializer;
import yapion.serializing.data.DeserializeData;
import yapion.serializing.data.SerializeData;
import yapion.serializing.serializer.SerializerImplementation;

import java.security.InvalidKeyException;
import java.security.PublicKey;
import java.util.Base64;

import static yapion.utils.IdentifierUtils.KEY_IDENTIFIER;
import static yapion.utils.IdentifierUtils.TYPE_IDENTIFIER;

@YAPIONSaveExclude
@YAPIONLoadExclude
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
        yapionObject.add("format", serializeData.object.getFormat());
        yapionObject.add("algorithm", serializeData.object.getAlgorithm());

        byte[] encoded = serializeData.object.getEncoded();
        String encodedString;
        if (encoded == null) {
            encodedString = null;
        } else {
            encodedString = Base64.getEncoder().encodeToString(serializeData.object.getEncoded());
        }
        yapionObject.add("encoded", encodedString);

        return yapionObject;
    }

    @Override
    public PublicKey deserialize(DeserializeData<? extends YAPIONAnyType> deserializeData) {
        YAPIONObject yapionObject = (YAPIONObject) deserializeData.object;
        String keyType = yapionObject.getValue(KEY_IDENTIFIER, "").get();
        String format = yapionObject.getValue("format", "").get();
        String algorithm = yapionObject.getValue("algorithm", "").get();

        String encodedString = yapionObject.getValue("encoded", "").get();
        byte[] encoded;
        if (encodedString == null) {
            throw new YAPIONException("'encoded' was null");
        } else {
            encoded = Base64.getDecoder().decode(encodedString);
        }

        if (keyType.equals("sun.security.rsa.RSAPublicKeyImpl")) {
            try {
                return RSAPublicKeyImpl.newKey(encoded);
            } catch (InvalidKeyException e) {
                throw new YAPIONException(e.getMessage(), e.getCause());
            }
        }
        if (keyType.equals("sun.security.provider.DSAPublicKeyImpl")) {
            try {
                return new DSAPublicKeyImpl(encoded);
            } catch (InvalidKeyException e) {
                throw new YAPIONException(e.getMessage(), e.getCause());
            }
        }

        return new PublicKey() {
            @Override
            public String getAlgorithm() {
                return algorithm;
            }

            @Override
            public String getFormat() {
                return format;
            }

            @Override
            public byte[] getEncoded() {
                return encoded;
            }
        };
    }

}