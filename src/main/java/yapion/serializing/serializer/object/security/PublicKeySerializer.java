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

package yapion.serializing.serializer.object.security;

import yapion.annotations.api.SerializerImplementation;
import yapion.exceptions.serializing.YAPIONDeserializerException;
import yapion.exceptions.serializing.YAPIONSerializerException;
import yapion.hierarchy.api.groups.YAPIONAnyType;
import yapion.hierarchy.types.YAPIONObject;
import yapion.serializing.InternalSerializer;
import yapion.serializing.data.DeserializeData;
import yapion.serializing.data.SerializeData;
import yapion.serializing.serializer.object.security.internal.KeySpecSerializerProvider;

import java.security.GeneralSecurityException;
import java.security.PublicKey;

import static yapion.utils.IdentifierUtils.KEY_IDENTIFIER;
import static yapion.utils.IdentifierUtils.TYPE_IDENTIFIER;

@SerializerImplementation(since = "0.20.0")
public class PublicKeySerializer implements InternalSerializer<PublicKey> {

    @Override
    public Class<?> type() {
        return PublicKey.class;
    }

    @Override
    public Class<?> interfaceType() {
        return PublicKey.class;
    }

    @Override
    public YAPIONAnyType serialize(SerializeData<PublicKey> serializeData) {
        YAPIONObject yapionObject = new YAPIONObject(serializeData.object.getClass());
        yapionObject.add("algorithm", serializeData.object.getAlgorithm());

        try {
            yapionObject.add("publicKey", KeySpecSerializerProvider.serializePublicKey(serializeData, serializeData.object));
        } catch (GeneralSecurityException e) {
            throw new YAPIONSerializerException(e.getMessage(), e);
        }
        return yapionObject;
    }

    @Override
    public PublicKey deserialize(DeserializeData<? extends YAPIONAnyType> deserializeData) {
        YAPIONObject yapionObject = (YAPIONObject) deserializeData.object;
        String key;
        if (yapionObject.containsKey(KEY_IDENTIFIER, String.class)) {
            key = yapionObject.getPlainValue(KEY_IDENTIFIER);
        } else {
            key = yapionObject.getPlainValue(TYPE_IDENTIFIER);
        }
        String algorithm = yapionObject.getValue("algorithm", String.class).get();
        YAPIONObject publicKey = yapionObject.getObject("publicKey");

        try {
            return KeySpecSerializerProvider.deserializePublicKey(deserializeData, Class.forName(key), publicKey, algorithm);
        } catch (GeneralSecurityException | ClassNotFoundException e) {
            throw new YAPIONDeserializerException(e.getMessage(), e);
        }
    }

}
