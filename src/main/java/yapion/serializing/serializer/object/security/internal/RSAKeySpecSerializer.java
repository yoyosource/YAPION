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
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;

@SerializerImplementation(since = "0.23.0")
public class RSAKeySpecSerializer implements KeySpecSerializer<RSAPrivateKey, RSAPublicKey> {

    @Override
    public YAPIONObject serializePrivateKey(RSAPrivateKey rsaPrivateKey) throws GeneralSecurityException {
        KeyFactory keyFactory = KeyFactory.getInstance(rsaPrivateKey.getAlgorithm());
        RSAPrivateKeySpec rsaPrivateKeySpec = keyFactory.getKeySpec(rsaPrivateKey, RSAPrivateKeySpec.class);

        YAPIONObject yapionObject = new YAPIONObject();
        yapionObject.add("modulus", rsaPrivateKeySpec.getModulus());
        yapionObject.add("privateExponent", rsaPrivateKeySpec.getPrivateExponent());
        return yapionObject;
    }

    @Override
    public RSAPrivateKey deserializePrivateKey(YAPIONObject yapionObject, String algorithm) throws GeneralSecurityException {
        BigInteger modulus = yapionObject.getValue("modulus", BigInteger.class).get();
        BigInteger privateExponent = yapionObject.getValue("privateExponent", BigInteger.class).get();

        KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
        return (RSAPrivateKey) keyFactory.generatePrivate(new RSAPrivateKeySpec(modulus, privateExponent));
    }

    @Override
    public YAPIONObject serializePublicKey(RSAPublicKey rsaPublicKey) throws GeneralSecurityException {
        KeyFactory keyFactory = KeyFactory.getInstance(rsaPublicKey.getAlgorithm());
        RSAPublicKeySpec rsaPublicKeySpec = keyFactory.getKeySpec(rsaPublicKey, RSAPublicKeySpec.class);

        YAPIONObject yapionObject = new YAPIONObject();
        yapionObject.add("modulus", rsaPublicKeySpec.getModulus());
        yapionObject.add("publicExponent", rsaPublicKeySpec.getPublicExponent());
        return yapionObject;
    }

    @Override
    public RSAPublicKey deserializePublicKey(YAPIONObject yapionObject, String algorithm) throws GeneralSecurityException {
        BigInteger modulus = yapionObject.getValue("modulus", BigInteger.class).get();
        BigInteger publicExponent = yapionObject.getValue("publicExponent", BigInteger.class).get();

        KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
        return (RSAPublicKey) keyFactory.generatePublic(new RSAPublicKeySpec(modulus, publicExponent));
    }

}
