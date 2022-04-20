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

import lombok.experimental.UtilityClass;
import yapion.exceptions.serializing.YAPIONDeserializerException;
import yapion.exceptions.serializing.YAPIONSerializerException;
import yapion.hierarchy.types.YAPIONObject;
import yapion.serializing.data.DeserializeData;
import yapion.serializing.data.SerializeData;

import javax.crypto.interfaces.DHPrivateKey;
import javax.crypto.interfaces.DHPublicKey;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.*;
import java.util.HashMap;
import java.util.Map;

@UtilityClass
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

    public static YAPIONObject serializePrivateKey(SerializeData<?> serializeData, PrivateKey privateKey) throws GeneralSecurityException {
        KeySpecSerializer keySpecSerializer = retrieveKeySpecSerializer(privateKey.getClass());
        if (keySpecSerializer == null) throw new YAPIONSerializerException("Unknown PrivateKey type");
        return keySpecSerializer.serializePrivateKey(serializeData.copy(privateKey));
    }

    public static PrivateKey deserializePrivateKey(DeserializeData<?> deserializeData, Class<?> clazz, YAPIONObject yapionObject, String algorithm) throws GeneralSecurityException {
        KeySpecSerializer keySpecSerializer = retrieveKeySpecSerializer(clazz);
        if (keySpecSerializer == null) throw new YAPIONDeserializerException("Unknown PrivateKey type");
        return keySpecSerializer.deserializePrivateKey(deserializeData.copy(yapionObject), algorithm);
    }

    public static YAPIONObject serializePublicKey(SerializeData<?> serializeData, PublicKey publicKey) throws GeneralSecurityException {
        KeySpecSerializer keySpecSerializer = retrieveKeySpecSerializer(publicKey.getClass());
        if (keySpecSerializer == null) throw new YAPIONSerializerException("Unknown PublicKey type");
        return keySpecSerializer.serializePublicKey(serializeData.copy(publicKey));
    }

    public static PublicKey deserializePublicKey(DeserializeData<?> deserializeData, Class<?> clazz, YAPIONObject yapionObject, String algorithm) throws GeneralSecurityException {
        KeySpecSerializer keySpecSerializer = retrieveKeySpecSerializer(clazz);
        if (keySpecSerializer == null) throw new YAPIONDeserializerException("Unknown PublicKey type");
        return keySpecSerializer.deserializePublicKey(deserializeData.copy(yapionObject), algorithm);
    }

}
