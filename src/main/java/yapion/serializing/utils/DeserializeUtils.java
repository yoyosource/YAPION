// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020,2021 yoyosource

package yapion.serializing.utils;

import yapion.hierarchy.api.groups.YAPIONAnyType;
import yapion.hierarchy.types.YAPIONArray;
import yapion.hierarchy.types.YAPIONMap;
import yapion.serializing.data.DeserializeData;

import java.util.*;

public class DeserializeUtils {

    private DeserializeUtils() {
        throw new IllegalStateException("Utility Class");
    }

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