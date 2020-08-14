// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020 yoyosource

package yapion.serializing.serializer.object;

import yapion.annotations.YAPIONLoadExclude;
import yapion.annotations.YAPIONSaveExclude;
import yapion.hierarchy.YAPIONAny;
import yapion.hierarchy.types.YAPIONArray;
import yapion.serializing.Serializer;
import yapion.serializing.YAPIONDeserializer;
import yapion.serializing.YAPIONSerializer;

import java.util.LinkedList;

@YAPIONSaveExclude(context = "*")
@YAPIONLoadExclude(context = "*")
public class ListSerializerLinked implements Serializer<LinkedList> {

    @Override
    public String type() {
        return "java.util.LinkedList";
    }

    @Override
    public YAPIONAny serialize(LinkedList object, YAPIONSerializer yapionSerializer) {
        YAPIONArray yapionArray = new YAPIONArray();
        for (int i = 0; i < object.size(); i++) {
            yapionArray.add(yapionSerializer.parse(object.get(i), yapionSerializer));
        }
        return yapionArray;
    }

    @Override
    public LinkedList deserialize(YAPIONAny yapionAny, YAPIONDeserializer yapionDeserializer) {
        return null;
    }

}