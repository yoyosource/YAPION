// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020 yoyosource

package yapion.serializing.serializer.object;

import yapion.annotations.YAPIONLoadExclude;
import yapion.annotations.YAPIONSaveExclude;
import yapion.hierarchy.YAPIONAny;
import yapion.hierarchy.types.YAPIONMap;
import yapion.serializing.Serializer;
import yapion.serializing.YAPIONDeserializer;
import yapion.serializing.YAPIONSerializer;

import java.util.LinkedHashMap;
import java.util.Map;

@YAPIONSaveExclude(context = "*")
@YAPIONLoadExclude(context = "*")
public class MapSerializerLinkedHash implements Serializer<LinkedHashMap> {

    @Override
    public String type() {
        return "java.util.LinkedHashMap";
    }

    @Override
    public YAPIONAny serialize(LinkedHashMap object, YAPIONSerializer yapionSerializer) {
        YAPIONMap yapionMap = new YAPIONMap();
        for (Object obj : object.entrySet()) {
            Map.Entry entry = (Map.Entry)obj;
            yapionMap.add(yapionSerializer.parse(entry.getKey(), yapionSerializer), yapionSerializer.parse(entry.getValue(), yapionSerializer));
        }
        return yapionMap;
    }

    @Override
    public LinkedHashMap deserialize(YAPIONAny yapionAny, YAPIONDeserializer yapionDeserializer) {
        return null;
    }

}