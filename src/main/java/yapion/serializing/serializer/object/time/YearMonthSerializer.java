// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020,2021 yoyosource

package yapion.serializing.serializer.object.time;

import yapion.annotations.deserialize.YAPIONLoadExclude;
import yapion.annotations.serialize.YAPIONSaveExclude;
import yapion.hierarchy.api.groups.YAPIONAnyType;
import yapion.hierarchy.types.YAPIONObject;
import yapion.serializing.InternalSerializer;
import yapion.serializing.data.DeserializeData;
import yapion.serializing.data.SerializeData;
import yapion.serializing.serializer.SerializerImplementation;

import java.time.Month;
import java.time.YearMonth;

import static yapion.utils.IdentifierUtils.TYPE_IDENTIFIER;

@YAPIONSaveExclude(context = "*")
@YAPIONLoadExclude(context = "*")
@SerializerImplementation(since = "0.18.0")
public class YearMonthSerializer implements InternalSerializer<YearMonth> {

    @Override
    public String type() {
        return "java.time.YearMonth";
    }

    @Override
    public YAPIONAnyType serialize(SerializeData<YearMonth> serializeData) {
        YAPIONObject yapionObject = new YAPIONObject();
        yapionObject.add(TYPE_IDENTIFIER, type());
        yapionObject.add("year", serializeData.object.getYear());
        yapionObject.add("month", serializeData.serialize(serializeData.object.getMonth()));
        return yapionObject;
    }

    @Override
    public YearMonth deserialize(DeserializeData<? extends YAPIONAnyType> deserializeData) {
        YAPIONObject yapionObject = (YAPIONObject) deserializeData.object;
        int year = yapionObject.getValue("year", 0).get();
        Month month = (Month) deserializeData.deserialize(yapionObject.getObject("month"));
        return YearMonth.of(year, month);
    }
}