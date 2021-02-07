// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020,2021 yoyosource

package yapion.serializing.serializer.object.security.internal;

import yapion.exceptions.serializing.YAPIONSerializerException;
import yapion.hierarchy.types.YAPIONObject;

import javax.crypto.interfaces.DHPrivateKey;
import javax.crypto.interfaces.DHPublicKey;
import java.security.*;
import java.security.interfaces.*;
import java.util.HashMap;
import java.util.Map;

public class KeySpecSerializerProvider {

    private static Map<Class<? extends Key>, KeySpecSerializer<? extends PrivateKey, ? extends PublicKey>> keySpecSerializerMap = new HashMap<>();

    static {
        add(DHPrivateKey.class, DHPublicKey.class, new DHKeySpecSerializer());
        add(DSAPrivateKey.class, DSAPublicKey.class, new DSAKeySpecSerializer());
        add(ECPrivateKey.class, ECPublicKey.class, new ECKeySpecSerializer());
        add(RSAPrivateKey.class, RSAPublicKey.class, new RSAKeySpecSerializer());
        add(RSAMultiPrimePrivateCrtKey.class, null, new RSAMultiPrimePrivateCrtKeySpecSerializer());
        add(RSAPrivateCrtKey.class, null, new RSAPrivateCrtKeySpecSerializer());
    }

    private static void add(Class<? extends PrivateKey> privateKey, Class<? extends PublicKey> publicKey, KeySpecSerializer<? extends PrivateKey, ? extends PublicKey> keySpecSerializer) {
        keySpecSerializerMap.put(privateKey, keySpecSerializer);
        if (publicKey == null) return;
        keySpecSerializerMap.put(publicKey, keySpecSerializer);
    }

    private KeySpecSerializerProvider() {
        throw new IllegalStateException("Utility Class");
    }

    private static KeySpecSerializer<? extends PrivateKey, ? extends PublicKey> retrieveKeySpecSerializer(Class<?> current) {
        if (current == null) return null;
        if (current == Key.class) return null;
        if (keySpecSerializerMap.containsKey(current)) return keySpecSerializerMap.get(current);
        for (Class<?> anInterface : current.getInterfaces()) {
            KeySpecSerializer<? extends PrivateKey, ? extends PublicKey> keySpecSerializer = retrieveKeySpecSerializer(anInterface);
            if (keySpecSerializer != null) return keySpecSerializer;
        }
        return null;
    }

    public static YAPIONObject serializePrivateKey(PrivateKey privateKey) throws Exception {
        KeySpecSerializer keySpecSerializer = retrieveKeySpecSerializer(privateKey.getClass());
        if (keySpecSerializer == null) throw new YAPIONSerializerException("Unknown PrivateKey type");
        return keySpecSerializer.serializePrivateKey(privateKey);
    }

    public static PrivateKey deserializePrivateKey(Class<?> clazz, YAPIONObject yapionObject, String algorithm) throws Exception {
        KeySpecSerializer keySpecSerializer = retrieveKeySpecSerializer(clazz);
        if (keySpecSerializer == null) throw new YAPIONSerializerException("Unknown PrivateKey type");
        return keySpecSerializer.deserializePrivateKey(yapionObject, algorithm);
    }

    public static YAPIONObject serializePublicKey(PublicKey publicKey) throws Exception {
        KeySpecSerializer keySpecSerializer = retrieveKeySpecSerializer(publicKey.getClass());
        if (keySpecSerializer == null) throw new YAPIONSerializerException("Unknown PrivateKey type");
        return keySpecSerializer.serializePublicKey(publicKey);
    }

    public static PublicKey deserializePublicKey(Class<?> clazz, YAPIONObject yapionObject, String algorithm) throws Exception {
        KeySpecSerializer keySpecSerializer = retrieveKeySpecSerializer(clazz);
        if (keySpecSerializer == null) throw new YAPIONSerializerException("Unknown PublicKey type");
        return keySpecSerializer.deserializePublicKey(yapionObject, algorithm);
    }

}