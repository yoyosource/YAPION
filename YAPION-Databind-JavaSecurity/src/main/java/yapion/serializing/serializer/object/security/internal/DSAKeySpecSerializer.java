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
import yapion.annotations.api.SerializerImplementation;
import yapion.serializing.data.DeserializeData;
import yapion.serializing.data.SerializeData;

import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.interfaces.DSAPrivateKey;
import java.security.interfaces.DSAPublicKey;
import java.security.spec.DSAPrivateKeySpec;
import java.security.spec.DSAPublicKeySpec;

@SerializerImplementation(since = "0.23.0")
public class DSAKeySpecSerializer implements KeySpecSerializer<DSAPrivateKey, DSAPublicKey> {

    @Override
    public YAPIONObject serializePrivateKey(SerializeData<DSAPrivateKey> serializeData) throws GeneralSecurityException {
        KeyFactory keyFactory = KeyFactory.getInstance(serializeData.object.getAlgorithm());
        DSAPrivateKeySpec dsaPrivateKeySpec = keyFactory.getKeySpec(serializeData.object, DSAPrivateKeySpec.class);

        YAPIONObject yapionObject = new YAPIONObject();
        yapionObject.add("x", dsaPrivateKeySpec.getX());
        yapionObject.add("p", dsaPrivateKeySpec.getP());
        yapionObject.add("q", dsaPrivateKeySpec.getQ());
        yapionObject.add("g", dsaPrivateKeySpec.getG());
        return yapionObject;
    }

    @Override
    public DSAPrivateKey deserializePrivateKey(DeserializeData<YAPIONObject> deserializeData, String algorithm) throws GeneralSecurityException {
        BigInteger x = deserializeData.object.getValue("x", BigInteger.class).get();
        BigInteger p = deserializeData.object.getValue("p", BigInteger.class).get();
        BigInteger q = deserializeData.object.getValue("q", BigInteger.class).get();
        BigInteger g = deserializeData.object.getValue("g", BigInteger.class).get();

        KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
        return (DSAPrivateKey) keyFactory.generatePrivate(new DSAPrivateKeySpec(x, p, q, g));
    }

    @Override
    public YAPIONObject serializePublicKey(SerializeData<DSAPublicKey> serializeData) throws GeneralSecurityException {
        KeyFactory keyFactory = KeyFactory.getInstance(serializeData.object.getAlgorithm());
        DSAPublicKeySpec dsaPublicKeySpec = keyFactory.getKeySpec(serializeData.object, DSAPublicKeySpec.class);

        YAPIONObject yapionObject = new YAPIONObject();
        yapionObject.add("y", dsaPublicKeySpec.getY());
        yapionObject.add("p", dsaPublicKeySpec.getP());
        yapionObject.add("q", dsaPublicKeySpec.getQ());
        yapionObject.add("g", dsaPublicKeySpec.getG());
        return yapionObject;
    }

    @Override
    public DSAPublicKey deserializePublicKey(DeserializeData<YAPIONObject> deserializeData, String algorithm) throws GeneralSecurityException {
        BigInteger y = deserializeData.object.getValue("y", BigInteger.class).get();
        BigInteger p = deserializeData.object.getValue("p", BigInteger.class).get();
        BigInteger q = deserializeData.object.getValue("q", BigInteger.class).get();
        BigInteger g = deserializeData.object.getValue("g", BigInteger.class).get();

        KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
        return (DSAPublicKey) keyFactory.generatePublic(new DSAPublicKeySpec(y, p, q, g));
    }

}
