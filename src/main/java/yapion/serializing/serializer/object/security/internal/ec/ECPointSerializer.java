// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020,2021 yoyosource

package yapion.serializing.serializer.object.security.internal.ec;

import yapion.annotations.deserialize.YAPIONLoadExclude;
import yapion.annotations.serialize.YAPIONSaveExclude;
import yapion.hierarchy.api.groups.YAPIONAnyType;
import yapion.hierarchy.types.YAPIONObject;
import yapion.serializing.InternalSerializer;
import yapion.serializing.data.DeserializeData;
import yapion.serializing.data.SerializeData;
import yapion.serializing.serializer.SerializerImplementation;

import java.math.BigInteger;
import java.security.spec.ECPoint;

import static yapion.utils.IdentifierUtils.TYPE_IDENTIFIER;

@YAPIONSaveExclude
@YAPIONLoadExclude
@SerializerImplementation(since = "0.23.0")
public class ECPointSerializer implements InternalSerializer<ECPoint> {

    @Override
    public String type() {
        return "java.security.spec.ECPoint";
    }

    @Override
    public YAPIONAnyType serialize(SerializeData<ECPoint> serializeData) {
        YAPIONObject yapionObject = new YAPIONObject();
        yapionObject.add(TYPE_IDENTIFIER, type());
        yapionObject.add("x", serializeData.object.getAffineX());
        yapionObject.add("y", serializeData.object.getAffineY());
        return yapionObject;
    }

    @Override
    public ECPoint deserialize(DeserializeData<? extends YAPIONAnyType> deserializeData) {
        YAPIONObject yapionObject = (YAPIONObject) deserializeData.object;
        BigInteger x = yapionObject.getValue("x", BigInteger.class).get();
        BigInteger y = yapionObject.getValue("y", BigInteger.class).get();
        return new ECPoint(x, y);
    }

}