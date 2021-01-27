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

import java.time.LocalDate;

import static yapion.utils.IdentifierUtils.TYPE_IDENTIFIER;

@YAPIONSaveExclude(context = "*")
@YAPIONLoadExclude(context = "*")
@SerializerImplementation(since = "0.18.0")
public class LocalDateSerializer implements InternalSerializer<LocalDate> {

    @Override
    public String type() {
        return "java.time.LocalDate";
    }

    @Override
    public YAPIONAnyType serialize(SerializeData<LocalDate> serializeData) {
        YAPIONObject yapionObject = new YAPIONObject();
        yapionObject.add(TYPE_IDENTIFIER, type());
        yapionObject.add("year", serializeData.object.getYear());
        yapionObject.add("dayOfYear", serializeData.object.getDayOfYear());
        return yapionObject;
    }

    @Override
    public LocalDate deserialize(DeserializeData<? extends YAPIONAnyType> deserializeData) {
        YAPIONObject yapionObject = (YAPIONObject) deserializeData.object;
        int year = yapionObject.getValue("year", 0).get();
        int dayOfYear = yapionObject.getValue("dayOfYear", 0).get();
        return LocalDate.ofYearDay(year, dayOfYear);
    }
}