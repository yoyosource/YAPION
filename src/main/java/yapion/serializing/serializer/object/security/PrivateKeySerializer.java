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

import java.security.PrivateKey;
import java.util.Base64;

import static yapion.utils.IdentifierUtils.TYPE_IDENTIFIER;

@YAPIONSaveExclude
@YAPIONLoadExclude
@SerializerImplementation(since = "0.20.0")
public class PrivateKeySerializer implements InternalSerializer<PrivateKey> {

    @Override
    public String type() {
        return "java.security.PrivateKey";
    }

    @Override
    public YAPIONAnyType serialize(SerializeData<PrivateKey> serializeData) {
        YAPIONObject yapionObject = new YAPIONObject();
        yapionObject.add(TYPE_IDENTIFIER, type());
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
    public PrivateKey deserialize(DeserializeData<? extends YAPIONAnyType> deserializeData) {
        YAPIONObject yapionObject = (YAPIONObject) deserializeData.object;
        String format = yapionObject.getValue("format", "").get();
        String algorithm = yapionObject.getValue("algorithm", "").get();

        String encodedString = yapionObject.getValue("encoded", "").get();
        byte[] encoded;
        if (encodedString == null) {
            encoded = null;
        } else {
            encoded = Base64.getDecoder().decode(encodedString);
        }

        return new PrivateKey() {
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