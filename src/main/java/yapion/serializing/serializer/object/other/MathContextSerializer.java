// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020,2021 yoyosource

package yapion.serializing.serializer.object.other;

import yapion.annotations.deserialize.YAPIONLoadExclude;
import yapion.annotations.serialize.YAPIONSaveExclude;
import yapion.hierarchy.typegroups.YAPIONAnyType;
import yapion.hierarchy.types.YAPIONObject;
import yapion.serializing.InternalSerializer;
import yapion.serializing.data.DeserializeData;
import yapion.serializing.data.SerializeData;
import yapion.serializing.serializer.SerializerImplementation;

import java.math.MathContext;
import java.math.RoundingMode;

import static yapion.utils.IdentifierUtils.TYPE_IDENTIFIER;

@YAPIONSaveExclude(context = "*")
@YAPIONLoadExclude(context = "*")
@SerializerImplementation(since = "0.13.1")
public class MathContextSerializer implements InternalSerializer<MathContext> {

    @Override
    public String type() {
        return "java.math.MathContext";
    }

    @Override
    public YAPIONAnyType serialize(SerializeData<MathContext> serializeData) {
        YAPIONObject yapionObject = new YAPIONObject();
        yapionObject.add(TYPE_IDENTIFIER, type());
        yapionObject.add("precision", serializeData.object.getPrecision());
        yapionObject.add("roundMode", serializeData.serialize(serializeData.object.getRoundingMode()));
        return yapionObject;
    }

    @Override
    public MathContext deserialize(DeserializeData<? extends YAPIONAnyType> deserializeData) {
        YAPIONObject yapionObject = (YAPIONObject) deserializeData.object;
        int precision = yapionObject.getValue("precision", 0).get();
        RoundingMode roundingMode = (RoundingMode) deserializeData.deserialize(yapionObject.getObject("roundMode"));
        return new MathContext(precision, roundingMode);
    }

}