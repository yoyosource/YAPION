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
import yapion.serializing.data.DeserializeData;

import java.util.*;

@UtilityClass
public class DeserializeUtils {

    public static <T extends Map<Object, Object>> T deserializeMap(DeserializeData<?> deserializeData, YAPIONMap yapionMap, T map) {
        for (YAPIONAnyType key : yapionMap.getKeys()) {
            map.put(deserializeData.deserialize(key), deserializeData.deserialize(yapionMap.get(key)));
        }
        return map;
    }

    public static <T extends Set<Object>> T deserializeSet(DeserializeData<?> deserializeData, YAPIONArray yapionArray, T set) {
        for (int i = 0; i < yapionArray.length(); i++) {
            set.add(deserializeData.deserialize(yapionArray.getYAPIONAnyType(i)));
        }
        return set;
    }

    public static <T extends List<Object>> T deserializeList(DeserializeData<?> deserializeData, YAPIONArray yapionArray, T list) {
        for (int i = 0; i < yapionArray.length(); i++) {
            list.add(deserializeData.deserialize(yapionArray.getYAPIONAnyType(i)));
        }
        return list;
    }

    public static <T extends Queue<Object>> T deserializeQueue(DeserializeData<?> deserializeData, YAPIONArray yapionArray, T queue) {
        for (int i = 0; i < yapionArray.length(); i++) {
            queue.add(deserializeData.deserialize(yapionArray.getYAPIONAnyType(i)));
        }
        return queue;
    }

    public static <T extends Deque<Object>> T deserializeDeque(DeserializeData<?> deserializeData, YAPIONArray yapionArray, T deque) {
        for (int i = 0; i < yapionArray.length(); i++) {
            deque.add(deserializeData.deserialize(yapionArray.getYAPIONAnyType(i)));
        }
        return deque;
    }

}
