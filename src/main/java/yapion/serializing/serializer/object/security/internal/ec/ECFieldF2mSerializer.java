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
import java.security.spec.ECFieldF2m;

import static yapion.utils.IdentifierUtils.TYPE_IDENTIFIER;

@YAPIONSaveExclude
@YAPIONLoadExclude
@SerializerImplementation(since = "0.23.0")
public class ECFieldF2mSerializer implements InternalSerializer<ECFieldF2m> {

    @Override
    public String type() {
        return "java.security.spec.ECFieldF2m";
    }

    @Override
    public YAPIONAnyType serialize(SerializeData<ECFieldF2m> serializeData) {
        YAPIONObject yapionObject = new YAPIONObject();
        yapionObject.add(TYPE_IDENTIFIER, type());
        yapionObject.add("m", serializeData.object.getM());
        yapionObject.add("ks", serializeData.serialize(serializeData.object.getMidTermsOfReductionPolynomial()));
        yapionObject.add("rp", serializeData.object.getReductionPolynomial());
        return yapionObject;
    }

    @Override
    public ECFieldF2m deserialize(DeserializeData<? extends YAPIONAnyType> deserializeData) {
        YAPIONObject yapionObject = (YAPIONObject) deserializeData.object;
        int m = yapionObject.getValue("m", Integer.class).get();
        YAPIONAnyType ksAny = yapionObject.getYAPIONAnyType("ks");
        YAPIONAnyType rpAny = yapionObject.getYAPIONAnyType("rp");
        if (ksAny == null && rpAny == null) {
            return new ECFieldF2m(m);
        }
        if (ksAny == null) {
            BigInteger rp = yapionObject.getValue("rp", BigInteger.class).get();
            return new ECFieldF2m(m, rp);
        } else {
            int[] ks = (int[]) deserializeData.deserialize(ksAny);
            return new ECFieldF2m(m, ks);
        }
    }

}