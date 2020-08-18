// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020 yoyosource

package yapion.serializing.serializer.object;

import yapion.annotations.deserialize.YAPIONLoadExclude;
import yapion.annotations.serialize.YAPIONSaveExclude;
import yapion.hierarchy.YAPIONAny;
import yapion.hierarchy.types.YAPIONMap;
import yapion.serializing.Serializer;
import yapion.serializing.YAPIONDeserializer;
import yapion.serializing.YAPIONSerializer;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.TreeMap;

@YAPIONSaveExclude(context = "*")
@YAPIONLoadExclude(context = "*")
public class MapSerializerTree implements Serializer<TreeMap> {

    @Override
    public String type() {
        return "java.util.TreeMap";
    }

    @Override
    public YAPIONAny serialize(TreeMap object, YAPIONSerializer yapionSerializer) {
        YAPIONMap yapionMap = new YAPIONMap();
        for (Object obj : object.entrySet()) {
            Map.Entry entry = (Map.Entry)obj;
            yapionMap.add(yapionSerializer.parse(entry.getKey(), yapionSerializer), yapionSerializer.parse(entry.getValue(), yapionSerializer));
        }
        return yapionMap;
    }

    @Override
    public TreeMap deserialize(YAPIONAny yapionAny, YAPIONDeserializer yapionDeserializer, Field field) {
        YAPIONMap yapionMap = (YAPIONMap)yapionAny;
        TreeMap<Object, Object> map = new TreeMap<>();
        for (YAPIONAny key : yapionMap.getKeys()) {
            map.put(yapionDeserializer.parse(key, yapionDeserializer, field), yapionDeserializer.parse(yapionMap.get(key), yapionDeserializer, field));
        }
        return map;
    }

}