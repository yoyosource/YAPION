// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020 yoyosource

package yapion.serializing.serializer.object;

import yapion.annotations.deserialize.YAPIONLoadExclude;
import yapion.annotations.serialize.YAPIONSaveExclude;
import yapion.hierarchy.typegroups.YAPIONAnyType;
import yapion.hierarchy.YAPIONVariable;
import yapion.hierarchy.types.YAPIONMap;
import yapion.hierarchy.types.YAPIONObject;
import yapion.hierarchy.types.YAPIONValue;
import yapion.serializing.InternalSerializer;
import yapion.serializing.SerializeManager;
import yapion.serializing.YAPIONDeserializer;
import yapion.serializing.YAPIONSerializer;
import yapion.serializing.serializer.SerializerImplementation;

import java.util.HashMap;
import java.util.Map;

@YAPIONSaveExclude(context = "*")
@YAPIONLoadExclude(context = "*")
@SuppressWarnings({"java:S3740"})
@SerializerImplementation
public class MapSerializer implements InternalSerializer<Map> {

    @Override
    public String type() {
        return "java.util.Map";
    }

    @Override
    public YAPIONAnyType serialize(Map object, YAPIONSerializer yapionSerializer) {
        YAPIONObject yapionObject = new YAPIONObject();
        yapionObject.add(new YAPIONVariable(SerializeManager.TYPE_IDENTIFIER, new YAPIONValue<>(type())));
        YAPIONMap yapionMap = new YAPIONMap();
        yapionObject.add(new YAPIONVariable("values", yapionMap));
        for (Object obj : object.entrySet()) {
            Map.Entry entry = (Map.Entry)obj;
            yapionMap.add(yapionSerializer.parse(entry.getKey()), yapionSerializer.parse(entry.getValue()));
        }
        return yapionObject;
    }

    @Override
    public Map deserialize(YAPIONAnyType yapionAnyType, YAPIONDeserializer yapionDeserializer) {
        YAPIONMap yapionMap = ((YAPIONObject) yapionAnyType).getMap("values");
        HashMap<Object, Object> map = new HashMap();
        for (YAPIONAnyType key : yapionMap.getKeys()) {
            map.put(yapionDeserializer.parse(key), yapionDeserializer.parse(yapionMap.get(key)));
        }
        return map;
    }
}