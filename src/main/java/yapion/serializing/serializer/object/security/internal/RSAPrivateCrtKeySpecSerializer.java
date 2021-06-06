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

import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateCrtKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.RSAPrivateCrtKeySpec;

@SerializerImplementation(since = "0.23.0")
public class RSAPrivateCrtKeySpecSerializer implements KeySpecSerializer<RSAPrivateCrtKey, RSAPublicKey> {

    @Override
    public YAPIONObject serializePrivateKey(SerializeData<RSAPrivateCrtKey> serializeData) throws GeneralSecurityException {
        KeyFactory keyFactory = KeyFactory.getInstance(serializeData.object.getAlgorithm());
        RSAPrivateCrtKeySpec rsaPrivateCrtKeySpec = keyFactory.getKeySpec(serializeData.object, RSAPrivateCrtKeySpec.class);

        YAPIONObject yapionObject = new YAPIONObject();
        yapionObject.add("modulus", rsaPrivateCrtKeySpec.getModulus());
        yapionObject.add("publicExponent", rsaPrivateCrtKeySpec.getPublicExponent());
        yapionObject.add("privateExponent", rsaPrivateCrtKeySpec.getPrivateExponent());
        yapionObject.add("primeP", rsaPrivateCrtKeySpec.getPrimeP());
        yapionObject.add("primeQ", rsaPrivateCrtKeySpec.getPrimeQ());
        yapionObject.add("primeExponentP", rsaPrivateCrtKeySpec.getPrimeExponentP());
        yapionObject.add("primeExponentQ", rsaPrivateCrtKeySpec.getPrimeExponentQ());
        yapionObject.add("crtCoefficient", rsaPrivateCrtKeySpec.getCrtCoefficient());
        return yapionObject;
    }

    @Override
    public RSAPrivateCrtKey deserializePrivateKey(DeserializeData<YAPIONObject> deserializeData, String algorithm) throws GeneralSecurityException {
        BigInteger modulus = deserializeData.object.getValue("modulus", BigInteger.class).get();
        BigInteger publicExponent = deserializeData.object.getValue("publicExponent", BigInteger.class).get();
        BigInteger privateExponent = deserializeData.object.getValue("privateExponent", BigInteger.class).get();
        BigInteger primeP = deserializeData.object.getValue("primeP", BigInteger.class).get();
        BigInteger primeQ = deserializeData.object.getValue("primeQ", BigInteger.class).get();
        BigInteger primeExponentP = deserializeData.object.getValue("primeExponentP", BigInteger.class).get();
        BigInteger primeExponentQ = deserializeData.object.getValue("primeExponentQ", BigInteger.class).get();
        BigInteger crtCoefficient = deserializeData.object.getValue("crtCoefficient", BigInteger.class).get();

        KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
        return (RSAPrivateCrtKey) keyFactory.generatePrivate(new RSAPrivateCrtKeySpec(modulus, publicExponent, privateExponent, primeP, primeQ, primeExponentP, primeExponentQ, crtCoefficient));
    }

    @Override
    public YAPIONObject serializePublicKey(SerializeData<RSAPublicKey> serializeData) throws GeneralSecurityException {
        throw new UnsupportedOperationException();
    }

    @Override
    public RSAPublicKey deserializePublicKey(DeserializeData<YAPIONObject> deserializeData, String algorithm) throws GeneralSecurityException {
        throw new UnsupportedOperationException();
    }

}
