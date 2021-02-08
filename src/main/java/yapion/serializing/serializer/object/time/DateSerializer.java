// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020,2021 yoyosource

package yapion.serializing.serializer.object.time;

import yapion.hierarchy.api.groups.YAPIONAnyType;
import yapion.hierarchy.types.YAPIONObject;
import yapion.serializing.InternalSerializer;
import yapion.serializing.data.DeserializeData;
import yapion.serializing.data.SerializeData;
import yapion.serializing.serializer.SerializerImplementation;

import java.util.Date;

import static yapion.utils.IdentifierUtils.TYPE_IDENTIFIER;

@SerializerImplementation(since = "0.20.0")
public class DateSerializer implements InternalSerializer<Date> {

    @Override
    public String type() {
        return "java.util.Date";
    }

    @Override
    public YAPIONAnyType serialize(SerializeData<Date> serializeData) {
        YAPIONObject yapionObject = new YAPIONObject();
        yapionObject.add(TYPE_IDENTIFIER, type());
        yapionObject.add("date", serializeData.object.getTime());
        return yapionObject;
    }

    @Override
    public Date deserialize(DeserializeData<? extends YAPIONAnyType> deserializeData) {
        return new Date(((YAPIONObject) deserializeData.object).getValue("date", 0L).get());
    }
}