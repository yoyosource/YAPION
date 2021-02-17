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

import javax.crypto.interfaces.DHPrivateKey;
import javax.crypto.interfaces.DHPublicKey;
import javax.crypto.spec.DHPrivateKeySpec;
import javax.crypto.spec.DHPublicKeySpec;
import java.math.BigInteger;
import java.security.KeyFactory;

public class DHKeySpecSerializer implements KeySpecSerializer<DHPrivateKey, DHPublicKey> {

    @Override
    public YAPIONObject serializePrivateKey(DHPrivateKey dhPrivateKey) throws Exception {
        KeyFactory keyFactory = KeyFactory.getInstance(dhPrivateKey.getAlgorithm());
        DHPrivateKeySpec dhPrivateKeySpec = keyFactory.getKeySpec(dhPrivateKey, DHPrivateKeySpec.class);

        YAPIONObject yapionObject = new YAPIONObject();
        yapionObject.add("x", dhPrivateKeySpec.getX());
        yapionObject.add("p", dhPrivateKeySpec.getP());
        yapionObject.add("g", dhPrivateKeySpec.getG());
        return yapionObject;
    }

    @Override
    public DHPrivateKey deserializePrivateKey(YAPIONObject yapionObject, String algorithm) throws Exception {
        BigInteger x = yapionObject.getValue("x", BigInteger.class).get();
        BigInteger p = yapionObject.getValue("p", BigInteger.class).get();
        BigInteger g = yapionObject.getValue("g", BigInteger.class).get();

        KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
        return (DHPrivateKey) keyFactory.generatePrivate(new DHPrivateKeySpec(x, p, g));
    }

    @Override
    public YAPIONObject serializePublicKey(DHPublicKey dhPublicKey) throws Exception {
        KeyFactory keyFactory = KeyFactory.getInstance(dhPublicKey.getAlgorithm());
        DHPublicKeySpec dhPublicKeySpec = keyFactory.getKeySpec(dhPublicKey, DHPublicKeySpec.class);

        YAPIONObject yapionObject = new YAPIONObject();
        yapionObject.add("y", dhPublicKeySpec.getY());
        yapionObject.add("p", dhPublicKeySpec.getP());
        yapionObject.add("g", dhPublicKeySpec.getG());
        return yapionObject;
    }

    @Override
    public DHPublicKey deserializePublicKey(YAPIONObject yapionObject, String algorithm) throws Exception {
        BigInteger y = yapionObject.getValue("y", BigInteger.class).get();
        BigInteger p = yapionObject.getValue("p", BigInteger.class).get();
        BigInteger g = yapionObject.getValue("g", BigInteger.class).get();

        KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
        return (DHPublicKey) keyFactory.generatePublic(new DHPublicKeySpec(y, p, g));
    }

}
