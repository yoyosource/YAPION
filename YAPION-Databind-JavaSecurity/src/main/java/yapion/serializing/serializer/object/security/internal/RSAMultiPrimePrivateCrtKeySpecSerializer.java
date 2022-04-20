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
import yapion.hierarchy.api.groups.YAPIONAnyType;
import yapion.hierarchy.types.YAPIONArray;
import yapion.hierarchy.types.YAPIONObject;
import yapion.serializing.YAPIONSerializer;
import yapion.serializing.data.DeserializeData;
import yapion.serializing.data.SerializeData;

import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.interfaces.RSAMultiPrimePrivateCrtKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.RSAMultiPrimePrivateCrtKeySpec;
import java.security.spec.RSAOtherPrimeInfo;

@SerializerImplementation(since = "0.23.0")
public class RSAMultiPrimePrivateCrtKeySpecSerializer implements KeySpecSerializer<RSAMultiPrimePrivateCrtKey, RSAPublicKey> {

    @Override
    public YAPIONObject serializePrivateKey(SerializeData<RSAMultiPrimePrivateCrtKey> serializeData) throws GeneralSecurityException {
        KeyFactory keyFactory = KeyFactory.getInstance(serializeData.object.getAlgorithm());
        RSAMultiPrimePrivateCrtKeySpec rsaMultiPrimePrivateCrtKeySpec = keyFactory.getKeySpec(serializeData.object, RSAMultiPrimePrivateCrtKeySpec.class);

        YAPIONObject yapionObject = new YAPIONObject();
        yapionObject.add("modulus", rsaMultiPrimePrivateCrtKeySpec.getModulus());
        yapionObject.add("publicExponent", rsaMultiPrimePrivateCrtKeySpec.getPublicExponent());
        yapionObject.add("privateExponent", rsaMultiPrimePrivateCrtKeySpec.getPrivateExponent());
        yapionObject.add("primeP", rsaMultiPrimePrivateCrtKeySpec.getPrimeP());
        yapionObject.add("primeQ", rsaMultiPrimePrivateCrtKeySpec.getPrimeQ());
        yapionObject.add("primeExponentP", rsaMultiPrimePrivateCrtKeySpec.getPrimeExponentP());
        yapionObject.add("primeExponentQ", rsaMultiPrimePrivateCrtKeySpec.getPrimeExponentQ());
        yapionObject.add("crtCoefficient", rsaMultiPrimePrivateCrtKeySpec.getCrtCoefficient());
        YAPIONArray yapionArray = new YAPIONArray();
        yapionObject.add("rsaOtherPrimeInfo", yapionArray);
        for (RSAOtherPrimeInfo rsaOtherPrimeInfo : rsaMultiPrimePrivateCrtKeySpec.getOtherPrimeInfo()) {
            yapionArray.add(YAPIONSerializer.serialize(rsaOtherPrimeInfo));
        }
        return yapionObject;
    }

    @Override
    public RSAMultiPrimePrivateCrtKey deserializePrivateKey(DeserializeData<YAPIONObject> deserializeData, String algorithm) throws GeneralSecurityException {
        BigInteger modulus = deserializeData.object.getValue("modulus", BigInteger.class).get();
        BigInteger publicExponent = deserializeData.object.getValue("publicExponent", BigInteger.class).get();
        BigInteger privateExponent = deserializeData.object.getValue("privateExponent", BigInteger.class).get();
        BigInteger primeP = deserializeData.object.getValue("primeP", BigInteger.class).get();
        BigInteger primeQ = deserializeData.object.getValue("primeQ", BigInteger.class).get();
        BigInteger primeExponentP = deserializeData.object.getValue("primeExponentP", BigInteger.class).get();
        BigInteger primeExponentQ = deserializeData.object.getValue("primeExponentQ", BigInteger.class).get();
        BigInteger crtCoefficient = deserializeData.object.getValue("crtCoefficient", BigInteger.class).get();

        YAPIONArray yapionArray = deserializeData.object.getArray("rsaOtherPrimeInfo");
        RSAOtherPrimeInfo[] rsaOtherPrimeInfos = new RSAOtherPrimeInfo[yapionArray.length()];
        int index = 0;
        for (YAPIONAnyType yapionAnyType : yapionArray.getAllValues()) {
            rsaOtherPrimeInfos[index++] = deserializeData.deserialize(yapionAnyType);
        }

        KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
        return (RSAMultiPrimePrivateCrtKey) keyFactory.generatePrivate(new RSAMultiPrimePrivateCrtKeySpec(modulus, publicExponent, privateExponent, primeP, primeQ, primeExponentP, primeExponentQ, crtCoefficient, rsaOtherPrimeInfos));
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
