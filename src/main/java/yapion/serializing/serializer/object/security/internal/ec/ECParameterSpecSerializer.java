// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020,2021 yoyosource

package yapion.serializing.serializer.object.security.internal.ec;

import yapion.hierarchy.api.groups.YAPIONAnyType;
import yapion.hierarchy.types.YAPIONObject;
import yapion.serializing.InternalSerializer;
import yapion.serializing.data.DeserializeData;
import yapion.serializing.data.SerializeData;
import yapion.serializing.serializer.SerializerImplementation;

import java.math.BigInteger;
import java.security.spec.ECParameterSpec;
import java.security.spec.ECPoint;
import java.security.spec.EllipticCurve;

import static yapion.utils.IdentifierUtils.TYPE_IDENTIFIER;

@SerializerImplementation(since = "0.23.0")
public class ECParameterSpecSerializer implements InternalSerializer<ECParameterSpec> {

    @Override
    public String type() {
        return "java.security.spec.ECParameterSpec";
    }

    @Override
    public YAPIONAnyType serialize(SerializeData<ECParameterSpec> serializeData) {
        YAPIONObject yapionObject = new YAPIONObject();
        yapionObject.add(TYPE_IDENTIFIER, type());
        yapionObject.add("curve", serializeData.serialize(serializeData.object.getCurve()));
        yapionObject.add("g", serializeData.serialize(serializeData.object.getGenerator()));
        yapionObject.add("n", serializeData.object.getOrder());
        yapionObject.add("h", serializeData.object.getCofactor());
        return yapionObject;
    }

    @Override
    public ECParameterSpec deserialize(DeserializeData<? extends YAPIONAnyType> deserializeData) {
        YAPIONObject yapionObject = (YAPIONObject) deserializeData.object;
        EllipticCurve curve = (EllipticCurve) deserializeData.deserialize(yapionObject.getObject("curve"));
        ECPoint g = (ECPoint) deserializeData.deserialize(yapionObject.getObject("g"));
        BigInteger n = yapionObject.getValue("n", BigInteger.class).get();
        int h = yapionObject.getValue("h", Integer.class).get();
        return new ECParameterSpec(curve, g, n, h);
    }

}