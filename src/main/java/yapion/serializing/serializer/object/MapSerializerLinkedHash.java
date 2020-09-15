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

import java.util.LinkedHashMap;
import java.util.Map;

@YAPIONSaveExclude(context = "*")
@YAPIONLoadExclude(context = "*")
@SerializerImplementation
public class MapSerializerLinkedHash implements InternalSerializer<LinkedHashMap<?, ?>> {

    @Override
    public String type() {
        return "java.util.LinkedHashMap";
    }

    @Override
    public YAPIONAnyType serialize(LinkedHashMap<?, ?> object, YAPIONSerializer yapionSerializer) {
        YAPIONObject yapionObject = new YAPIONObject();
        yapionObject.add(new YAPIONVariable(SerializeManager.TYPE_IDENTIFIER, new YAPIONValue<>(type())));
        YAPIONMap yapionMap = new YAPIONMap();
        yapionObject.add(new YAPIONVariable("values", yapionMap));
        for (Map.Entry<?, ?> entry : object.entrySet()) {
            yapionMap.add(yapionSerializer.parse(entry.getKey()), yapionSerializer.parse(entry.getValue()));
        }
        return yapionObject;
    }

    @Override
    public LinkedHashMap<?, ?> deserialize(YAPIONAnyType yapionAnyType, YAPIONDeserializer yapionDeserializer) {
        YAPIONMap yapionMap = ((YAPIONObject) yapionAnyType).getMap("values");
        LinkedHashMap<Object, Object> map = new LinkedHashMap<>();
        for (YAPIONAnyType key : yapionMap.getKeys()) {
            map.put(yapionDeserializer.parse(key), yapionDeserializer.parse(yapionMap.get(key)));
        }
        return map;
    }

}