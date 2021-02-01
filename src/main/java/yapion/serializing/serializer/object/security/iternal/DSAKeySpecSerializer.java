// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020,2021 yoyosource

package yapion.serializing.serializer.object.security.iternal;

import yapion.hierarchy.types.YAPIONObject;

import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.interfaces.DSAPrivateKey;
import java.security.interfaces.DSAPublicKey;
import java.security.spec.DSAPrivateKeySpec;
import java.security.spec.DSAPublicKeySpec;

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