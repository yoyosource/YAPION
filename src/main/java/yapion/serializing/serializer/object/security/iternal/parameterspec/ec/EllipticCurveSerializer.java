// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020,2021 yoyosource

package yapion.serializing.serializer.object.security.iternal.parameterspec.ec;

import yapion.annotations.deserialize.YAPIONLoadExclude;
import yapion.annotations.serialize.YAPIONSaveExclude;
import yapion.hierarchy.typegroups.YAPIONAnyType;
import yapion.hierarchy.types.YAPIONObject;
import yapion.serializing.InternalSerializer;
import yapion.serializing.data.DeserializeData;
import yapion.serializing.data.SerializeData;
import yapion.serializing.serializer.SerializerImplementation;

import java.math.BigInteger;
import java.security.spec.ECField;
import java.security.spec.EllipticCurve;

import static yapion.utils.IdentifierUtils.TYPE_IDENTIFIER;

@YAPIONSaveExclude
@YAPIONLoadExclude
@SerializerImplementation(since = "0.23.0")
public class EllipticCurveSerializer implements InternalSerializer<EllipticCurve> {

    @Override
    public String type() {
        return "java.security.spec.EllipticCurve";
    }

    @Override
    public YAPIONAnyType serialize(SerializeData<EllipticCurve> serializeData) {
        YAPIONObject yapionObject = new YAPIONObject();
        yapionObject.add(TYPE_IDENTIFIER, type());
        yapionObject.add("field", serializeData.serialize(serializeData.object.getField()));
        yapionObject.add("a", serializeData.object.getA());
        yapionObject.add("b", serializeData.object.getB());
        yapionObject.add("seed", serializeData.serialize(serializeData.object.getSeed()));
        return yapionObject;
    }

    @Override
    public EllipticCurve deserialize(DeserializeData<? extends YAPIONAnyType> deserializeData) {
        YAPIONObject yapionObject = (YAPIONObject) deserializeData.object;
        ECField field = (ECField) deserializeData.deserialize(yapionObject.getObject("field"));
        BigInteger a = yapionObject.getValue("a", BigInteger.class).get();
        BigInteger b = yapionObject.getValue("b", BigInteger.class).get();
        byte[] seed = (byte[]) deserializeData.deserialize(yapionObject.getArray("seed"));
        return new EllipticCurve(field, a, b, seed);
    }

}