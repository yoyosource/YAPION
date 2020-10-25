// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020 yoyosource

package yapion.serializing.serializer.other;

import yapion.annotations.deserialize.YAPIONLoadExclude;
import yapion.annotations.serialize.YAPIONSaveExclude;
import yapion.hierarchy.typegroups.YAPIONAnyType;
import yapion.hierarchy.types.YAPIONObject;
import yapion.hierarchy.types.YAPIONValue;
import yapion.hierarchy.types.YAPIONVariable;
import yapion.serializing.InternalSerializer;
import yapion.serializing.data.DeserializeData;
import yapion.serializing.data.SerializeData;
import yapion.serializing.serializer.SerializerImplementation;

import java.math.MathContext;
import java.math.RoundingMode;

import static yapion.utils.IdentifierUtils.TYPE_IDENTIFIER;

@YAPIONSaveExclude(context = "*")
@YAPIONLoadExclude(context = "*")
@SerializerImplementation
public class MathContextSerializer implements InternalSerializer<MathContext> {

    @Override
    public String type() {
        return "java.math.MathContext";
    }

    @Override
    public YAPIONAnyType serialize(SerializeData<MathContext> serializeData) {
        YAPIONObject yapionObject = new YAPIONObject();
        yapionObject.add(new YAPIONVariable(TYPE_IDENTIFIER, new YAPIONValue<>(type())));
        yapionObject.add(new YAPIONVariable("precision", new YAPIONValue<>(serializeData.object.getPrecision())));
        yapionObject.add(new YAPIONVariable("roundMode", serializeData.serialize(serializeData.object.getRoundingMode())));
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