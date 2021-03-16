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
import yapion.serializing.serializer.SerializerImplementation;

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
    public YAPIONObject serializePrivateKey(ECPrivateKey ecPrivateKey) throws GeneralSecurityException {
        KeyFactory keyFactory = KeyFactory.getInstance(ecPrivateKey.getAlgorithm());
        ECPrivateKeySpec ecPrivateKeySpec = keyFactory.getKeySpec(ecPrivateKey, ECPrivateKeySpec.class);

        YAPIONObject yapionObject = new YAPIONObject();
        yapionObject.add("s", ecPrivateKeySpec.getS());
        yapionObject.add("params", YAPIONSerializer.serialize(ecPrivateKeySpec.getParams()));
        return yapionObject;
    }

    @Override
    public ECPrivateKey deserializePrivateKey(YAPIONObject yapionObject, String algorithm) throws GeneralSecurityException {
        BigInteger s = yapionObject.getValue("s", BigInteger.class).get();
        ECParameterSpec params = (ECParameterSpec) YAPIONDeserializer.deserialize(yapionObject.getObject("params"));

        KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
        return (ECPrivateKey) keyFactory.generatePrivate(new ECPrivateKeySpec(s, params));
    }

    @Override
    public YAPIONObject serializePublicKey(ECPublicKey ecPublicKey) throws GeneralSecurityException {
        KeyFactory keyFactory = KeyFactory.getInstance(ecPublicKey.getAlgorithm());
        ECPublicKeySpec ecPublicKeySpec = keyFactory.getKeySpec(ecPublicKey, ECPublicKeySpec.class);

        YAPIONObject yapionObject = new YAPIONObject();
        yapionObject.add("w", YAPIONSerializer.serialize(ecPublicKeySpec.getW()));
        yapionObject.add("params", YAPIONSerializer.serialize(ecPublicKeySpec.getParams()));
        return yapionObject;
    }

    @Override
    public ECPublicKey deserializePublicKey(YAPIONObject yapionObject, String algorithm) throws GeneralSecurityException {
        ECPoint w = (ECPoint) YAPIONDeserializer.deserialize(yapionObject.getObject("w"));
        ECParameterSpec params = (ECParameterSpec) YAPIONDeserializer.deserialize(yapionObject.getObject("params"));

        KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
        return (ECPublicKey) keyFactory.generatePublic(new ECPublicKeySpec(w, params));
    }

}
