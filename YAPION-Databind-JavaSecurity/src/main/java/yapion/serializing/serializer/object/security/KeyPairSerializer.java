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
import yapion.exceptions.serializing.YAPIONDataLossException;
import yapion.hierarchy.api.groups.YAPIONAnyType;
import yapion.hierarchy.types.YAPIONObject;
import yapion.hierarchy.types.YAPIONValue;
import yapion.serializing.data.DeserializeData;
import yapion.serializing.data.SerializeData;
import yapion.serializing.serializer.FinalInternalSerializer;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

import static yapion.serializing.YAPIONFlag.KEY_PAIR_AS_NULL;
import static yapion.serializing.YAPIONFlag.KEY_PAIR_EXCEPTION;

@SerializerImplementation(since = "0.20.0")
public class KeyPairSerializer implements FinalInternalSerializer<KeyPair> {

    @Override
    public Class<?> type() {
        return KeyPair.class;
    }

    @Override
    public YAPIONAnyType serialize(SerializeData<KeyPair> serializeData) {
        serializeData.isSet(KEY_PAIR_EXCEPTION, () -> {
            throw new YAPIONDataLossException("KEY_PAIR_EXCEPTION does not allow keyPair to be saved");
        });
        if (serializeData.getYAPIONFlags().isSet(KEY_PAIR_AS_NULL)) {
            return new YAPIONValue<>(null);
        }

        YAPIONObject yapionObject = new YAPIONObject(type());
        yapionObject.add("privateKey", serializeData.serialize(serializeData.object.getPrivate()));
        yapionObject.add("publicKey", serializeData.serialize(serializeData.object.getPublic()));
        return yapionObject;
    }

    @Override
    public KeyPair deserialize(DeserializeData<? extends YAPIONAnyType> deserializeData) {
        YAPIONObject yapionObject = (YAPIONObject) deserializeData.object;
        PrivateKey privateKey = deserializeData.deserialize(yapionObject.getObject("privateKey"));
        PublicKey publicKey = deserializeData.deserialize(yapionObject.getObject("publicKey"));
        return new KeyPair(publicKey, privateKey);
    }

}
