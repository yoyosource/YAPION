// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020,2021 yoyosource

package yapion.serializing.utils;

import yapion.hierarchy.types.YAPIONArray;
import yapion.hierarchy.types.YAPIONMap;
import yapion.hierarchy.types.YAPIONObject;
import yapion.serializing.data.SerializeData;

import java.util.*;

public class SerializeUtils {

    private SerializeUtils() {
        throw new IllegalStateException("Utility Class");
    }

    public static <T extends Map<?, ?>> YAPIONObject serializeMap(SerializeData<T> serializeData, YAPIONObject yapionObject) {
        YAPIONMap yapionMap = new YAPIONMap();
        yapionObject.add("values", yapionMap);
        for (Map.Entry<?, ?> entry : serializeData.object.entrySet()) {
            yapionMap.add(serializeData.serialize(entry.getKey()), serializeData.serialize(entry.getValue()));
        }
        return yapionObject;
    }

    public static <T extends Set<?>> YAPIONObject serializeSet(SerializeData<T> serializeData, YAPIONObject yapionObject) {
        YAPIONArray yapionArray = new YAPIONArray();
        yapionObject.add("values", yapionArray);
        for (Object o : serializeData.object) {
            yapionArray.add(serializeData.serialize(o));
        }
        return yapionObject;
    }

    public static <T extends List<?>> YAPIONObject serializeList(SerializeData<T> serializeData, YAPIONObject yapionObject) {
        YAPIONArray yapionArray = new YAPIONArray();
        yapionObject.add("values", yapionArray);
        for (Object o : serializeData.object) {
            yapionArray.add(serializeData.serialize(o));
        }
        return yapionObject;
    }

    public static <T extends Queue<?>> YAPIONObject serializeQueue(SerializeData<T> serializeData, YAPIONObject yapionObject) {
        YAPIONArray yapionArray = new YAPIONArray();
        yapionObject.add("values", yapionArray);
        for (Object o : serializeData.object) {
            yapionArray.add(serializeData.serialize(o));
        }
        return yapionObject;
    }

    public static <T extends Deque<?>> YAPIONObject serializeDeque(SerializeData<T> serializeData, YAPIONObject yapionObject) {
        YAPIONArray yapionArray = new YAPIONArray();
        yapionObject.add("values", yapionArray);
        for (Object o : serializeData.object) {
            yapionArray.add(serializeData.serialize(o));
        }
        return yapionObject;
    }

}