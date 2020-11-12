// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020 yoyosource

package yapion.serializing.serializer.object.time;

import yapion.annotations.deserialize.YAPIONLoadExclude;
import yapion.annotations.serialize.YAPIONSaveExclude;
import yapion.hierarchy.typegroups.YAPIONAnyType;
import yapion.hierarchy.types.YAPIONObject;
import yapion.serializing.InternalSerializer;
import yapion.serializing.data.DeserializeData;
import yapion.serializing.data.SerializeData;
import yapion.serializing.serializer.SerializerImplementation;

import java.time.Period;

import static yapion.utils.IdentifierUtils.TYPE_IDENTIFIER;

@YAPIONSaveExclude(context = "*")
@YAPIONLoadExclude(context = "*")
@SerializerImplementation
public class PeriodSerializer implements InternalSerializer<Period> {

    @Override
    public String type() {
        return "java.time.Period";
    }

    @Override
    public YAPIONAnyType serialize(SerializeData<Period> serializeData) {
        YAPIONObject yapionObject = new YAPIONObject();
        yapionObject.add(TYPE_IDENTIFIER, type());
        yapionObject.add("years", serializeData.object.getYears());
        yapionObject.add("months", serializeData.object.getMonths());
        yapionObject.add("days", serializeData.object.getDays());
        return yapionObject;
    }

    @Override
    public Period deserialize(DeserializeData<? extends YAPIONAnyType> deserializeData) {
        YAPIONObject yapionObject = (YAPIONObject) deserializeData.object;
        int years = yapionObject.getValue("years", 0).get();
        int months = yapionObject.getValue("months", 0).get();
        int days = yapionObject.getValue("days", 0).get();
        return Period.of(years, months, days);
    }
}