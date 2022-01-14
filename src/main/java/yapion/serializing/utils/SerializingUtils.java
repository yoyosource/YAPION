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

package yapion.serializing.utils;

import lombok.experimental.UtilityClass;
import yapion.hierarchy.api.groups.YAPIONAnyType;
import yapion.hierarchy.types.YAPIONArray;
import yapion.hierarchy.types.YAPIONMap;
import yapion.hierarchy.types.YAPIONObject;
import yapion.serializing.data.DeserializeData;
import yapion.serializing.data.SerializeData;

import java.util.Collection;
import java.util.Map;

@UtilityClass
public class SerializingUtils {

    public <T extends Map<?, ?>> YAPIONObject serializeMap(SerializeData<T> serializeData, YAPIONObject yapionObject) {
        YAPIONMap yapionMap = new YAPIONMap();
        yapionObject.add("values", yapionMap);
        for (Map.Entry<?, ?> entry : serializeData.object.entrySet()) {
            if (entry.getKey() instanceof YAPIONAnyType) {
                throw new IllegalArgumentException("Map key cannot be YAPIONAnyType. These are not allowed inside generic types.");
            }
            if (entry.getValue() instanceof YAPIONAnyType) {
                throw new IllegalArgumentException("Map value cannot be YAPIONAnyType. These are not allowed inside generic types.");
            }
            yapionMap.add(serializeData.serialize(entry.getKey()), serializeData.serialize(entry.getValue()));
        }
        return yapionObject;
    }

    public <T extends Collection<?>> YAPIONObject serializeCollection(SerializeData<T> serializeData, YAPIONObject yapionObject) {
        YAPIONArray yapionArray = new YAPIONArray();
        yapionObject.add("values", yapionArray);
        for (Object o : serializeData.object) {
            if (o instanceof YAPIONAnyType) {
                throw new IllegalArgumentException("Collection element cannot be YAPIONAnyType. These are not allowed inside generic types.");
            }
            yapionArray.add(serializeData.serialize(o));
        }
        return yapionObject;
    }

    public static <T extends Map<Object, Object>> T deserializeMap(DeserializeData<?> deserializeData, YAPIONMap yapionMap, T map) {
        for (YAPIONAnyType key : yapionMap.getKeys()) {
            map.put(deserializeData.deserialize(key), deserializeData.deserialize(yapionMap.get(key)));
        }
        return map;
    }

    public static <T extends Collection<Object>> T deserializeCollection(DeserializeData<?> deserializeData, YAPIONArray yapionArray, T collection) {
        for (int i = 0; i < yapionArray.length(); i++) {
            collection.add(deserializeData.deserialize(yapionArray.getAnyType(i)));
        }
        return collection;
    }

}
