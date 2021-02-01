// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020,2021 yoyosource

package yapion.serializing.serializer.object.security.iternal;

import yapion.hierarchy.types.YAPIONObject;

import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;

public class RSAKeySpecSerializer implements KeySpecSerializer<RSAPrivateKey, RSAPublicKey> {

    @Override
    public YAPIONObject serializePrivateKey(RSAPrivateKey rsaPrivateKey) throws Exception {
        KeyFactory keyFactory = KeyFactory.getInstance(rsaPrivateKey.getAlgorithm());
        RSAPrivateKeySpec rsaPrivateKeySpec = keyFactory.getKeySpec(rsaPrivateKey, RSAPrivateKeySpec.class);

        YAPIONObject yapionObject = new YAPIONObject();
        yapionObject.add("modulus", rsaPrivateKeySpec.getModulus());
        yapionObject.add("privateExponent", rsaPrivateKeySpec.getPrivateExponent());
        return yapionObject;
    }

    @Override
    public RSAPrivateKey deserializePrivateKey(YAPIONObject yapionObject, String algorithm) throws Exception {
        BigInteger modulus = yapionObject.getValue("modulus", BigInteger.class).get();
        BigInteger privateExponent = yapionObject.getValue("privateExponent", BigInteger.class).get();

        KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
        return (RSAPrivateKey) keyFactory.generatePrivate(new RSAPrivateKeySpec(modulus, privateExponent));
    }

    @Override
    public YAPIONObject serializePublicKey(RSAPublicKey rsaPublicKey) throws Exception {
        KeyFactory keyFactory = KeyFactory.getInstance(rsaPublicKey.getAlgorithm());
        RSAPublicKeySpec rsaPublicKeySpec = keyFactory.getKeySpec(rsaPublicKey, RSAPublicKeySpec.class);

        YAPIONObject yapionObject = new YAPIONObject();
        yapionObject.add("modulus", rsaPublicKeySpec.getModulus());
        yapionObject.add("publicExponent", rsaPublicKeySpec.getPublicExponent());
        return yapionObject;
    }

    @Override
    public RSAPublicKey deserializePublicKey(YAPIONObject yapionObject, String algorithm) throws Exception {
        BigInteger modulus = yapionObject.getValue("modulus", BigInteger.class).get();
        BigInteger publicExponent = yapionObject.getValue("publicExponent", BigInteger.class).get();

        KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
        return (RSAPublicKey) keyFactory.generatePublic(new RSAPublicKeySpec(modulus, publicExponent));
    }

}