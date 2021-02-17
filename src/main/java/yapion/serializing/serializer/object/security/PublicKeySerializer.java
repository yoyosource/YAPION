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
