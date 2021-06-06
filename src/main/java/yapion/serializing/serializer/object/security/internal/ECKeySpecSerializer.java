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

package yapion.serializing.serializer.object.security.internal;

import yapion.hierarchy.types.YAPIONObject;
import yapion.serializing.YAPIONDeserializer;
import yapion.serializing.YAPIONSerializer;
import yapion.annotations.api.SerializerImplementation;
import yapion.serializing.data.DeserializeData;
import yapion.serializing.data.SerializeData;

import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.security.spec.ECParameterSpec;
import java.security.spec.ECPoint;
import java.security.spec.ECPrivateKeySpec;
import java.security.spec.ECPublicKeySpec;

@SerializerImplementation(since = "0.23.0")
public class ECKeySpecSerializer implements KeySpecSerializer<ECPrivateKey, ECPublicKey> {

    @Override
    public YAPIONObject serializePrivateKey(SerializeData<ECPrivateKey> serializeData) throws GeneralSecurityException {
        KeyFactory keyFactory = KeyFactory.getInstance(serializeData.object.getAlgorithm());
        ECPrivateKeySpec ecPrivateKeySpec = keyFactory.getKeySpec(serializeData.object, ECPrivateKeySpec.class);

        YAPIONObject yapionObject = new YAPIONObject();
        yapionObject.add("s", ecPrivateKeySpec.getS());
        yapionObject.add("params", YAPIONSerializer.serialize(ecPrivateKeySpec.getParams()));
        return yapionObject;
    }

    @Override
    public ECPrivateKey deserializePrivateKey(DeserializeData<YAPIONObject> deserializeData, String algorithm) throws GeneralSecurityException {
        BigInteger s = deserializeData.object.getValue("s", BigInteger.class).get();
        ECParameterSpec params = deserializeData.deserialize(deserializeData.object.getObject("params"));

        KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
        return (ECPrivateKey) keyFactory.generatePrivate(new ECPrivateKeySpec(s, params));
    }

    @Override
    public YAPIONObject serializePublicKey(SerializeData<ECPublicKey> serializeData) throws GeneralSecurityException {
        KeyFactory keyFactory = KeyFactory.getInstance(serializeData.object.getAlgorithm());
        ECPublicKeySpec ecPublicKeySpec = keyFactory.getKeySpec(serializeData.object, ECPublicKeySpec.class);

        YAPIONObject yapionObject = new YAPIONObject();
        yapionObject.add("w", YAPIONSerializer.serialize(ecPublicKeySpec.getW()));
        yapionObject.add("params", YAPIONSerializer.serialize(ecPublicKeySpec.getParams()));
        return yapionObject;
    }

    @Override
    public ECPublicKey deserializePublicKey(DeserializeData<YAPIONObject> deserializeData, String algorithm) throws GeneralSecurityException {
        ECPoint w = deserializeData.deserialize(deserializeData.object.getObject("w"));
        ECParameterSpec params = deserializeData.deserialize(deserializeData.object.getObject("params"));

        KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
        return (ECPublicKey) keyFactory.generatePublic(new ECPublicKeySpec(w, params));
    }

}
