// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020,2021 yoyosource

package yapion.serializing.serializer.object.time;

import yapion.annotations.deserialize.YAPIONLoadExclude;
import yapion.annotations.serialize.YAPIONSaveExclude;
import yapion.hierarchy.typegroups.YAPIONAnyType;
import yapion.hierarchy.types.YAPIONObject;
import yapion.serializing.InternalSerializer;
import yapion.serializing.data.DeserializeData;
import yapion.serializing.data.SerializeData;
import yapion.serializing.serializer.SerializerImplementation;

import java.time.Month;
import java.time.MonthDay;

import static yapion.utils.IdentifierUtils.TYPE_IDENTIFIER;

@YAPIONSaveExclude(context = "*")
@YAPIONLoadExclude(context = "*")
@SerializerImplementation(since = "0.18.0")
public class MonthDaySerializer implements InternalSerializer<MonthDay> {

    @Override
    public String type() {
        return "java.time.MonthDay";
    }

    @Override
    public YAPIONAnyType serialize(SerializeData<MonthDay> serializeData) {
        YAPIONObject yapionObject = new YAPIONObject();
        yapionObject.add(TYPE_IDENTIFIER, type());
        yapionObject.add("month", serializeData.serialize(serializeData.object.getMonth()));
        yapionObject.add("dayOfMonth", serializeData.object.getDayOfMonth());
        return yapionObject;
    }

    @Override
    public MonthDay deserialize(DeserializeData<? extends YAPIONAnyType> deserializeData) {
        YAPIONObject yapionObject = (YAPIONObject) deserializeData.object;
        Month month = (Month) deserializeData.deserialize(yapionObject.getObject("month"));
        int dayOfMonth = yapionObject.getValue("dayOfMonth", 0).get();
        return MonthDay.of(month, dayOfMonth);
    }
}