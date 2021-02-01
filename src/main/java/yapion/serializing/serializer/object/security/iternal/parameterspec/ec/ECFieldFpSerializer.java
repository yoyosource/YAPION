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
import java.security.spec.ECFieldFp;

import static yapion.utils.IdentifierUtils.TYPE_IDENTIFIER;

@YAPIONSaveExclude
@YAPIONLoadExclude
@SerializerImplementation(since = "0.23.0")
public class ECFieldFpSerializer implements InternalSerializer<ECFieldFp> {

    @Override
    public String type() {
        return "java.security.spec.ECFieldFp";
    }

    @Override
    public YAPIONAnyType serialize(SerializeData<ECFieldFp> serializeData) {
        YAPIONObject yapionObject = new YAPIONObject();
        yapionObject.add(TYPE_IDENTIFIER, type());
        yapionObject.add("p", serializeData.object.getP());
        return yapionObject;
    }

    @Override
    public ECFieldFp deserialize(DeserializeData<? extends YAPIONAnyType> deserializeData) {
        YAPIONObject yapionObject = (YAPIONObject) deserializeData.object;
        BigInteger p = yapionObject.getValue("p", BigInteger.class).get();
        return new ECFieldFp(p);
    }

}