// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020,2021 yoyosource

package yapion.serializing.serializer.object.security.internal.rsa;

import yapion.hierarchy.api.groups.YAPIONAnyType;
import yapion.hierarchy.types.YAPIONObject;
import yapion.serializing.InternalSerializer;
import yapion.serializing.data.DeserializeData;
import yapion.serializing.data.SerializeData;
import yapion.serializing.serializer.SerializerImplementation;

import java.math.BigInteger;
import java.security.spec.RSAOtherPrimeInfo;

import static yapion.utils.IdentifierUtils.TYPE_IDENTIFIER;

@SerializerImplementation(since = "0.23.0")
public class RSAOtherPrimeInfoSerializer implements InternalSerializer<RSAOtherPrimeInfo> {

    @Override
    public String type() {
        return "java.security.spec.RSAOtherPrimeInfo";
    }

    @Override
    public YAPIONAnyType serialize(SerializeData<RSAOtherPrimeInfo> serializeData) {
        YAPIONObject yapionObject = new YAPIONObject();
        yapionObject.add(TYPE_IDENTIFIER, type());
        yapionObject.add("prime", serializeData.object.getPrime());
        yapionObject.add("primeExponent", serializeData.object.getExponent());
        yapionObject.add("crtCoefficient", serializeData.object.getCrtCoefficient());
        return yapionObject;
    }

    @Override
    public RSAOtherPrimeInfo deserialize(DeserializeData<? extends YAPIONAnyType> deserializeData) {
        YAPIONObject yapionObject = (YAPIONObject) deserializeData.object;
        BigInteger prime = yapionObject.getValue("prime", BigInteger.class).get();
        BigInteger primeExponent = yapionObject.getValue("primeExponent", BigInteger.class).get();
        BigInteger crtCoefficient = yapionObject.getValue("crtCoefficient", BigInteger.class).get();
        return new RSAOtherPrimeInfo(prime, primeExponent, crtCoefficient);
    }

}