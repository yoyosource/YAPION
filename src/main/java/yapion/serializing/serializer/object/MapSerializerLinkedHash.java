// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020 yoyosource

package yapion.serializing.serializer.object;

import yapion.annotations.deserialize.YAPIONLoadExclude;
import yapion.annotations.serialize.YAPIONSaveExclude;
import yapion.hierarchy.YAPIONAny;
import yapion.hierarchy.YAPIONVariable;
import yapion.hierarchy.types.YAPIONMap;
import yapion.hierarchy.types.YAPIONObject;
import yapion.hierarchy.types.YAPIONValue;
import yapion.serializing.InternalSerializer;
import yapion.serializing.YAPIONDeserializer;
import yapion.serializing.YAPIONSerializer;

import java.util.LinkedHashMap;
import java.util.Map;

@YAPIONSaveExclude(context = "*")
@YAPIONLoadExclude(context = "*")
@SuppressWarnings({"java:S3740"})
public class MapSerializerLinkedHash implements InternalSerializer<LinkedHashMap> {

    @Override
    public String type() {
        return "java.util.LinkedHashMap";
    }

    @Override
    public YAPIONAny serialize(LinkedHashMap object, YAPIONSerializer yapionSerializer) {
        YAPIONObject yapionObject = new YAPIONObject();
        yapionObject.add(new YAPIONVariable("@type", new YAPIONValue<>("java.util.LinkedHashMap")));
        YAPIONMap yapionMap = new YAPIONMap();
        yapionObject.add(new YAPIONVariable("values", yapionMap));
        for (Object obj : object.entrySet()) {
            Map.Entry entry = (Map.Entry)obj;
            yapionMap.add(yapionSerializer.parse(entry.getKey(), yapionSerializer), yapionSerializer.parse(entry.getValue(), yapionSerializer));
        }
        return yapionObject;
    }

    @Override
    public LinkedHashMap deserialize(YAPIONAny yapionAny, YAPIONDeserializer yapionDeserializer) {
        YAPIONMap yapionMap = ((YAPIONObject) yapionAny).getMap("values");
        LinkedHashMap<Object, Object> map = new LinkedHashMap<>();
        for (YAPIONAny key : yapionMap.getKeys()) {
            map.put(yapionDeserializer.parse(key, yapionDeserializer), yapionDeserializer.parse(yapionMap.get(key), yapionDeserializer));
        }
        return map;
    }

}