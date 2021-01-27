// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020,2021 yoyosource

package yapion.serializing.serializer.object.map;

import yapion.annotations.deserialize.YAPIONLoadExclude;
import yapion.annotations.serialize.YAPIONSaveExclude;
import yapion.hierarchy.typegroups.YAPIONAnyType;
import yapion.hierarchy.types.YAPIONMap;
import yapion.hierarchy.types.YAPIONObject;
import yapion.hierarchy.types.YAPIONVariable;
import yapion.serializing.InternalSerializer;
import yapion.serializing.data.DeserializeData;
import yapion.serializing.data.SerializeData;
import yapion.serializing.serializer.SerializerImplementation;

import java.util.IdentityHashMap;
import java.util.Map;

import static yapion.utils.IdentifierUtils.TYPE_IDENTIFIER;

@YAPIONSaveExclude(context = "*")
@YAPIONLoadExclude(context = "*")
@SerializerImplementation(since = "0.7.0")
public class IdentityHashMapSerializer implements InternalSerializer<IdentityHashMap<?, ?>> {

    @Override
    public String type() {
        return "java.util.IdentityHashMap";
    }

    @Override
    public YAPIONAnyType serialize(SerializeData<IdentityHashMap<?, ?>> serializeData) {
        YAPIONObject yapionObject = new YAPIONObject();
        yapionObject.add(TYPE_IDENTIFIER, type());
        YAPIONMap yapionMap = new YAPIONMap();
        yapionObject.add(new YAPIONVariable("values", yapionMap));
        for (Map.Entry<?, ?> entry : serializeData.object.entrySet()) {
            yapionMap.add(serializeData.serialize(entry.getKey()), serializeData.serialize(entry.getValue()));
        }
        return yapionObject;
    }

    @Override
    public IdentityHashMap<?, ?> deserialize(DeserializeData<? extends YAPIONAnyType> deserializeData) {
        YAPIONMap yapionMap = ((YAPIONObject) deserializeData.object).getMap("values");
        IdentityHashMap<Object, Object> map = new IdentityHashMap<>();
        for (YAPIONAnyType key : yapionMap.getKeys()) {
            map.put(deserializeData.deserialize(key), deserializeData.deserialize(yapionMap.get(key)));
        }
        return map;
    }

}