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

import java.time.LocalTime;

import static yapion.utils.IdentifierUtils.TYPE_IDENTIFIER;

@SerializerImplementation(since = "0.18.0")
public class LocalTimeSerializer implements InternalSerializer<LocalTime> {

    @Override
    public String type() {
        return "java.time.LocalTime";
    }

    @Override
    public YAPIONAnyType serialize(SerializeData<LocalTime> serializeData) {
        YAPIONObject yapionObject = new YAPIONObject();
        yapionObject.add(TYPE_IDENTIFIER, type());
        yapionObject.add("hour", serializeData.object.getHour());
        yapionObject.add("minute", serializeData.object.getMinute());
        yapionObject.add("second", serializeData.object.getSecond());
        yapionObject.add("nano", serializeData.object.getNano());
        return yapionObject;
    }

    @Override
    public LocalTime deserialize(DeserializeData<? extends YAPIONAnyType> deserializeData) {
        YAPIONObject yapionObject = (YAPIONObject) deserializeData.object;
        int hour = yapionObject.getValue("hour", 0).get();
        int minute = yapionObject.getValue("minute", 0).get();
        int second = yapionObject.getValue("second", 0).get();
        int nano = yapionObject.getValue("nano", 0).get();
        return LocalTime.of(hour, minute, second, nano);
    }
}