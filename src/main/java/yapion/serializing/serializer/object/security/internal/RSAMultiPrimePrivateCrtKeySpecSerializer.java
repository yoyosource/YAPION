// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020,2021 yoyosource

package yapion.serializing.serializer.object.security.internal;

import yapion.hierarchy.api.groups.YAPIONAnyType;
import yapion.hierarchy.types.YAPIONArray;
import yapion.hierarchy.types.YAPIONObject;
import yapion.serializing.YAPIONDeserializer;
import yapion.serializing.YAPIONSerializer;

import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.interfaces.RSAMultiPrimePrivateCrtKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.RSAMultiPrimePrivateCrtKeySpec;
import java.security.spec.RSAOtherPrimeInfo;

public class RSAMultiPrimePrivateCrtKeySpecSerializer implements KeySpecSerializer<RSAMultiPrimePrivateCrtKey, RSAPublicKey> {

    @Override
    public YAPIONObject serializePrivateKey(RSAMultiPrimePrivateCrtKey rsaMultiPrimePrivateCrtKey) throws Exception {
        KeyFactory keyFactory = KeyFactory.getInstance(rsaMultiPrimePrivateCrtKey.getAlgorithm());
        RSAMultiPrimePrivateCrtKeySpec rsaMultiPrimePrivateCrtKeySpec = keyFactory.getKeySpec(rsaMultiPrimePrivateCrtKey, RSAMultiPrimePrivateCrtKeySpec.class);

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
    public RSAMultiPrimePrivateCrtKey deserializePrivateKey(YAPIONObject yapionObject, String algorithm) throws Exception {
        BigInteger modulus = yapionObject.getValue("modulus", BigInteger.class).get();
        BigInteger publicExponent = yapionObject.getValue("publicExponent", BigInteger.class).get();
        BigInteger privateExponent = yapionObject.getValue("privateExponent", BigInteger.class).get();
        BigInteger primeP = yapionObject.getValue("primeP", BigInteger.class).get();
        BigInteger primeQ = yapionObject.getValue("primeQ", BigInteger.class).get();
        BigInteger primeExponentP = yapionObject.getValue("primeExponentP", BigInteger.class).get();
        BigInteger primeExponentQ = yapionObject.getValue("primeExponentQ", BigInteger.class).get();
        BigInteger crtCoefficient = yapionObject.getValue("crtCoefficient", BigInteger.class).get();

        YAPIONArray yapionArray = yapionObject.getArray("rsaOtherPrimeInfo");
        RSAOtherPrimeInfo[] rsaOtherPrimeInfos = new RSAOtherPrimeInfo[yapionArray.length()];
        int index = 0;
        for (YAPIONAnyType yapionAnyType : yapionArray.getAllValues()) {
            rsaOtherPrimeInfos[index++] = (RSAOtherPrimeInfo) YAPIONDeserializer.deserialize((YAPIONObject) yapionAnyType);
        }

        KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
        return (RSAMultiPrimePrivateCrtKey) keyFactory.generatePrivate(new RSAMultiPrimePrivateCrtKeySpec(modulus, publicExponent, privateExponent, primeP, primeQ, primeExponentP, primeExponentQ, crtCoefficient, rsaOtherPrimeInfos));
    }

    @Override
    public YAPIONObject serializePublicKey(RSAPublicKey rsaPublicKey) throws Exception {
        throw new UnsupportedOperationException();
    }

    @Override
    public RSAPublicKey deserializePublicKey(YAPIONObject yapionObject, String algorithm) throws Exception {
        throw new UnsupportedOperationException();
    }

}