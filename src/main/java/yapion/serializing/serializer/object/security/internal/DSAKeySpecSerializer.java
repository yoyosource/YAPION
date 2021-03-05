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
import yapion.serializing.serializer.SerializerImplementation;

import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.interfaces.DSAPrivateKey;
import java.security.interfaces.DSAPublicKey;
import java.security.spec.DSAPrivateKeySpec;
import java.security.spec.DSAPublicKeySpec;

@SerializerImplementation(since = "0.23.0")
public class DSAKeySpecSerializer implements KeySpecSerializer<DSAPrivateKey, DSAPublicKey> {

    @Override
    public YAPIONObject serializePrivateKey(DSAPrivateKey dsaPrivateKey) throws Exception {
        KeyFactory keyFactory = KeyFactory.getInstance(dsaPrivateKey.getAlgorithm());
        DSAPrivateKeySpec dsaPrivateKeySpec = keyFactory.getKeySpec(dsaPrivateKey, DSAPrivateKeySpec.class);

        YAPIONObject yapionObject = new YAPIONObject();
        yapionObject.add("x", dsaPrivateKeySpec.getX());
        yapionObject.add("p", dsaPrivateKeySpec.getP());
        yapionObject.add("q", dsaPrivateKeySpec.getQ());
        yapionObject.add("g", dsaPrivateKeySpec.getG());
        return yapionObject;
    }

    @Override
    public DSAPrivateKey deserializePrivateKey(YAPIONObject yapionObject, String algorithm) throws Exception {
        BigInteger x = yapionObject.getValue("x", BigInteger.class).get();
        BigInteger p = yapionObject.getValue("p", BigInteger.class).get();
        BigInteger q = yapionObject.getValue("q", BigInteger.class).get();
        BigInteger g = yapionObject.getValue("g", BigInteger.class).get();

        KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
        return (DSAPrivateKey) keyFactory.generatePrivate(new DSAPrivateKeySpec(x, p, q, g));
    }

    @Override
    public YAPIONObject serializePublicKey(DSAPublicKey dsaPublicKey) throws Exception {
        KeyFactory keyFactory = KeyFactory.getInstance(dsaPublicKey.getAlgorithm());
        DSAPublicKeySpec dsaPublicKeySpec = keyFactory.getKeySpec(dsaPublicKey, DSAPublicKeySpec.class);

        YAPIONObject yapionObject = new YAPIONObject();
        yapionObject.add("y", dsaPublicKeySpec.getY());
        yapionObject.add("p", dsaPublicKeySpec.getP());
        yapionObject.add("q", dsaPublicKeySpec.getQ());
        yapionObject.add("g", dsaPublicKeySpec.getG());
        return yapionObject;
    }

    @Override
    public DSAPublicKey deserializePublicKey(YAPIONObject yapionObject, String algorithm) throws Exception {
        BigInteger y = yapionObject.getValue("y", BigInteger.class).get();
        BigInteger p = yapionObject.getValue("p", BigInteger.class).get();
        BigInteger q = yapionObject.getValue("q", BigInteger.class).get();
        BigInteger g = yapionObject.getValue("g", BigInteger.class).get();

        KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
        return (DSAPublicKey) keyFactory.generatePublic(new DSAPublicKeySpec(y, p, q, g));
    }

}
