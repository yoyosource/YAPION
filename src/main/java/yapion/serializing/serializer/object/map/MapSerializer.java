/*
 * Copyright 2019,2020,2021 yoyosource
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package yapion.serializing.serializer.object.map;

import yapion.annotations.api.SerializerImplementation;
import yapion.exceptions.serializing.YAPIONDeserializerException;
import yapion.hierarchy.api.groups.YAPIONAnyType;
import yapion.hierarchy.types.YAPIONMap;
import yapion.hierarchy.types.YAPIONObject;
import yapion.serializing.InternalSerializer;
import yapion.serializing.data.DeserializeData;
import yapion.serializing.data.SerializeData;
import yapion.serializing.utils.DeserializeUtils;
import yapion.serializing.utils.SerializeUtils;
import yapion.utils.ReflectionsUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;

import static yapion.utils.IdentifierUtils.TYPE_IDENTIFIER;

@SerializerImplementation(since = "0.23.0", initialSince = "0.3.0, 0.7.0, 0.12.0", standsFor = {Map.class, HashMap.class, IdentityHashMap.class, LinkedHashMap.class, TreeMap.class, WeakHashMap.class, ConcurrentHashMap.class, ConcurrentSkipListMap.class})
public class MapSerializer implements InternalSerializer<Map<?, ?>> {

    @Override
    public Class<?> type() {
        return Map.class;
    }

    @Override
    public Class<?> defaultImplementation() {
        return HashMap.class;
    }

    @Override
    public Class<?> interfaceType() {
        return Map.class;
    }

    @Override
    public YAPIONAnyType serialize(SerializeData<Map<?, ?>> serializeData) {
        YAPIONObject yapionObject = new YAPIONObject();
        yapionObject.add(TYPE_IDENTIFIER, serializeData.object.getClass().getTypeName());
        return SerializeUtils.serializeMap(serializeData, yapionObject);
    }

    @SuppressWarnings({"unchecked"})
    @Override
    public Map<?, ?> deserialize(DeserializeData<? extends YAPIONAnyType> deserializeData) {
        try {
            Object object = ReflectionsUtils.constructObject((YAPIONObject) deserializeData.object, this, false);
            YAPIONMap yapionMap = ((YAPIONObject) deserializeData.object).getMap("values");
            return DeserializeUtils.deserializeMap(deserializeData, yapionMap, (Map<Object, Object>) object);
        } catch (Exception e) {
            throw new YAPIONDeserializerException(e.getMessage(), e);
        }
    }
}
