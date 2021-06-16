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

import yapion.annotations.api.SerializerImplementation;
import yapion.hierarchy.types.YAPIONObject;
import yapion.serializing.data.DeserializeData;
import yapion.serializing.data.SerializeData;

import javax.crypto.interfaces.DHPrivateKey;
import javax.crypto.interfaces.DHPublicKey;
import javax.crypto.spec.DHPrivateKeySpec;
import javax.crypto.spec.DHPublicKeySpec;
import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;

@SerializerImplementation(since = "0.23.0")
public class DHKeySpecSerializer implements KeySpecSerializer<DHPrivateKey, DHPublicKey> {

    @Override
    public YAPIONObject serializePrivateKey(SerializeData<DHPrivateKey> serializeData) throws GeneralSecurityException {
        KeyFactory keyFactory = KeyFactory.getInstance(serializeData.object.getAlgorithm());
        DHPrivateKeySpec dhPrivateKeySpec = keyFactory.getKeySpec(serializeData.object, DHPrivateKeySpec.class);

        YAPIONObject yapionObject = new YAPIONObject();
        yapionObject.add("x", dhPrivateKeySpec.getX());
        yapionObject.add("p", dhPrivateKeySpec.getP());
        yapionObject.add("g", dhPrivateKeySpec.getG());
        return yapionObject;
    }

    @Override
    public DHPrivateKey deserializePrivateKey(DeserializeData<YAPIONObject> deserializeData, String algorithm) throws GeneralSecurityException {
        BigInteger x = deserializeData.object.getValue("x", BigInteger.class).get();
        BigInteger p = deserializeData.object.getValue("p", BigInteger.class).get();
        BigInteger g = deserializeData.object.getValue("g", BigInteger.class).get();

        KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
        return (DHPrivateKey) keyFactory.generatePrivate(new DHPrivateKeySpec(x, p, g));
    }

    @Override
    public YAPIONObject serializePublicKey(SerializeData<DHPublicKey> serializeData) throws GeneralSecurityException {
        KeyFactory keyFactory = KeyFactory.getInstance(serializeData.object.getAlgorithm());
        DHPublicKeySpec dhPublicKeySpec = keyFactory.getKeySpec(serializeData.object, DHPublicKeySpec.class);

        YAPIONObject yapionObject = new YAPIONObject();
        yapionObject.add("y", dhPublicKeySpec.getY());
        yapionObject.add("p", dhPublicKeySpec.getP());
        yapionObject.add("g", dhPublicKeySpec.getG());
        return yapionObject;
    }

    @Override
    public DHPublicKey deserializePublicKey(DeserializeData<YAPIONObject> deserializeData, String algorithm) throws GeneralSecurityException {
        BigInteger y = deserializeData.object.getValue("y", BigInteger.class).get();
        BigInteger p = deserializeData.object.getValue("p", BigInteger.class).get();
        BigInteger g = deserializeData.object.getValue("g", BigInteger.class).get();

        KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
        return (DHPublicKey) keyFactory.generatePublic(new DHPublicKeySpec(y, p, g));
    }

}
