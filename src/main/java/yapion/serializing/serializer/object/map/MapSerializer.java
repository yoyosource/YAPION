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
import yapion.exceptions.YAPIONException;
import yapion.exceptions.serializing.YAPIONDeserializerException;
import yapion.hierarchy.api.groups.YAPIONAnyType;
import yapion.hierarchy.types.YAPIONMap;
import yapion.hierarchy.types.YAPIONObject;
import yapion.serializing.data.DeserializeData;
import yapion.serializing.data.SerializeData;
import yapion.serializing.serializer.FinalInternalSerializer;
import yapion.serializing.utils.SerializingUtils;
import yapion.utils.ClassUtils;
import yapion.utils.ReflectionsUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.function.Function;

import static yapion.utils.IdentifierUtils.ENUM_TYPE_IDENTIFIER;
import static yapion.utils.IdentifierUtils.TYPE_IDENTIFIER;

@SerializerImplementation(since = "0.26.0", initialSince = "0.3.0, 0.7.0, 0.12.0, 0.23.0, 0.24.0", standsFor = {Map.class, HashMap.class, IdentityHashMap.class, LinkedHashMap.class, TreeMap.class, WeakHashMap.class, ConcurrentHashMap.class, ConcurrentSkipListMap.class, EnumMap.class})
public class MapSerializer implements FinalInternalSerializer<Map<?, ?>> {

    private Map<Class<?>, Function<Map<?, ?>, Map<?, ?>>> wrapper = new HashMap<>();

    @Override
    public void init() {
        wrapper = new HashMap<>();
        wrapper.put(Collections.emptyMap().getClass(), map -> Collections.emptyMap());
        wrapper.put(Collections.emptyNavigableMap().getClass(), map -> Collections.emptyNavigableMap());
        wrapper.put(Collections.emptySortedMap().getClass(), map -> Collections.emptySortedMap());
        wrapper.put(Collections.singletonMap(null, null).getClass(), map -> {
            Map.Entry<?, ?> entry = map.entrySet().iterator().next();
            return Collections.singletonMap(entry.getKey(), entry.getValue());
        });
        wrapper.put(Collections.unmodifiableMap(new HashMap<>()).getClass(), map -> Collections.unmodifiableMap(map));
        wrapper.put(Collections.unmodifiableNavigableMap(new TreeMap<>()).getClass(), map -> Collections.unmodifiableNavigableMap((NavigableMap<? extends Object, ?>) map));
        wrapper.put(Collections.unmodifiableSortedMap(new TreeMap<>()).getClass(), map -> Collections.unmodifiableSortedMap((SortedMap<? extends Object, ?>) map));

        ReflectionsUtils.addSpecialCreator(EnumMap.class, new Function<YAPIONObject, EnumMap>() {
            @Override
            public EnumMap apply(YAPIONObject yapionObject) {
                return get(yapionObject);
            }

            private <E extends Enum<E>> EnumMap<?, ?> get(YAPIONObject yapionObject) {
                try {
                    Class<E> clazz = (Class<E>) Class.forName(yapionObject.getPlainValue(ENUM_TYPE_IDENTIFIER));
                    return new EnumMap(clazz);
                } catch (ClassNotFoundException | ClassCastException e) {
                    throw new YAPIONException(e.getMessage(), e);
                }
            }
        });
    }

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
        YAPIONObject yapionObject = new YAPIONObject(serializeData.object.getClass());
        if (serializeData.object instanceof EnumMap) {
            yapionObject.add(ENUM_TYPE_IDENTIFIER, (Class<?>) serializeData.getField("keyType"));
        }
        return SerializingUtils.serializeMap(serializeData, yapionObject);
    }

    @SuppressWarnings({"unchecked"})
    @Override
    public Map<?, ?> deserialize(DeserializeData<? extends YAPIONAnyType> deserializeData) {
        YAPIONDeserializerException yapionDeserializerException;
        try {
            Object object = ReflectionsUtils.constructObject((YAPIONObject) deserializeData.object, this, false, deserializeData.typeReMapper);
            YAPIONMap yapionMap = ((YAPIONObject) deserializeData.object).getMap("values");
            return SerializingUtils.deserializeMap(deserializeData, yapionMap, (Map<Object, Object>) object);
        } catch (Exception e) {
            yapionDeserializerException = new YAPIONDeserializerException(e.getMessage(), e);
        }
        try {
            YAPIONMap yapionMap = ((YAPIONObject) deserializeData.object).getMap("values");
            Map<?, ?> map = SerializingUtils.deserializeMap(deserializeData, yapionMap, (Map<Object, Object>) defaultImplementation().newInstance());
            return wrapper.getOrDefault(ClassUtils.getClass(((YAPIONObject) deserializeData.object).getPlainValue(TYPE_IDENTIFIER)), objects -> objects).apply(map);
        } catch (InstantiationException e) {
            throw new YAPIONDeserializerException(e.getMessage(), e);
        } catch (YAPIONException e) {
            throw e;
        } catch (Exception e) {
            throw yapionDeserializerException;
        }
    }
}
