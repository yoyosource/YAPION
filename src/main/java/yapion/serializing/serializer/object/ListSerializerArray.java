// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020 yoyosource

package yapion.serializing.serializer.object;

import yapion.annotations.deserialize.YAPIONLoadExclude;
import yapion.annotations.serialize.YAPIONSaveExclude;
import yapion.hierarchy.YAPIONAny;
import yapion.hierarchy.types.YAPIONArray;
import yapion.serializing.Serializer;
import yapion.serializing.YAPIONDeserializer;
import yapion.serializing.YAPIONSerializer;

import java.lang.reflect.Field;
import java.util.ArrayList;

@YAPIONSaveExclude(context = "*")
@YAPIONLoadExclude(context = "*")
@SuppressWarnings({"java:S3740"})
public class ListSerializerArray implements Serializer<ArrayList> {

    @Override
    public String type() {
        return "java.util.ArrayList";
    }

    @Override
    public YAPIONAny serialize(ArrayList object, YAPIONSerializer yapionSerializer) {
        YAPIONArray yapionArray = new YAPIONArray();
        for (int i = 0; i < object.size(); i++) {
            yapionArray.add(yapionSerializer.parse(object.get(i), yapionSerializer));
        }
        return yapionArray;
    }

    @Override
    public ArrayList deserialize(YAPIONAny yapionAny, YAPIONDeserializer yapionDeserializer, Field field) {
        YAPIONArray yapionArray = (YAPIONArray)yapionAny;
        ArrayList<Object> list = new ArrayList<>(yapionArray.length());
        for (int i = 0; i < yapionArray.length(); i++) {
            list.add(yapionDeserializer.parse(yapionArray.get(i), yapionDeserializer, field));
        }
        return list;
    }

}