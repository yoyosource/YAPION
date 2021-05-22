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

package yapion.serializing.serializer.object.collection;

import yapion.annotations.api.SerializerImplementation;
import yapion.exceptions.YAPIONException;
import yapion.exceptions.serializing.YAPIONDeserializerException;
import yapion.hierarchy.api.groups.YAPIONAnyType;
import yapion.hierarchy.types.YAPIONArray;
import yapion.hierarchy.types.YAPIONObject;
import yapion.serializing.InternalSerializer;
import yapion.serializing.data.DeserializeData;
import yapion.serializing.data.SerializeData;
import yapion.serializing.utils.SerializingUtils;
import yapion.utils.ClassUtils;
import yapion.utils.ReflectionsUtils;

import java.util.*;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.function.Function;

import static yapion.utils.IdentifierUtils.ENUM_TYPE_IDENTIFIER;
import static yapion.utils.IdentifierUtils.TYPE_IDENTIFIER;

@SerializerImplementation(since = "0.26.0", initialSince = "0.3.0, 0.7.0, 0.12.0, 0.23.0, 0.24.0", standsFor = {Set.class, HashSet.class, LinkedHashSet.class, TreeSet.class, ConcurrentSkipListSet.class, CopyOnWriteArraySet.class, EnumSet.class})
public class SetSerializer implements InternalSerializer<Set<?>> {

    private Map<Class<?>, Function<Set<?>, Set<?>>> wrapper = new HashMap<>();

    {
        init();
    }

    @Override
    public void init() {
        wrapper = new HashMap<>();
        wrapper.put(Collections.emptySet().getClass(), objects -> Collections.emptySet());
        wrapper.put(Collections.emptyNavigableSet().getClass(), objects -> Collections.emptyNavigableSet());
        wrapper.put(Collections.emptySortedSet().getClass(), objects -> Collections.emptySortedSet());
        wrapper.put(Collections.singleton(null).getClass(), objects -> Collections.singleton(objects.iterator().next()));
        wrapper.put(Collections.unmodifiableSet(new HashSet<>()).getClass(), objects -> Collections.unmodifiableSet(objects));
        wrapper.put(Collections.unmodifiableSortedSet(new TreeSet<>()).getClass(), objects -> Collections.unmodifiableSortedSet((SortedSet<? extends Object>) objects));
        wrapper.put(Collections.unmodifiableNavigableSet(new TreeSet<>()).getClass(), objects -> Collections.unmodifiableNavigableSet((NavigableSet<? extends Object>) objects));

        ReflectionsUtils.addSpecialCreator(EnumSet.class, new Function<YAPIONObject, EnumSet>() {
            @Override
            public EnumSet apply(YAPIONObject yapionObject) {
                return get(yapionObject);
            }

            private <E extends Enum<E>> EnumSet<?> get(YAPIONObject yapionObject) {
                try {
                    Class<E> clazz = (Class<E>) Class.forName(yapionObject.getPlainValue(ENUM_TYPE_IDENTIFIER));
                    return EnumSet.noneOf(clazz);
                } catch (ClassNotFoundException | ClassCastException e) {
                    throw new YAPIONException(e.getMessage(), e);
                }
            }
        });
    }

    @Override
    public Class<?> type() {
        return Set.class;
    }

    @Override
    public Class<?> defaultImplementation() {
        return HashSet.class;
    }

    @Override
    public Class<?> interfaceType() {
        return Set.class;
    }

    @Override
    public YAPIONAnyType serialize(SerializeData<Set<?>> serializeData) {
        YAPIONObject yapionObject = new YAPIONObject();
        yapionObject.add(TYPE_IDENTIFIER, serializeData.object.getClass().getTypeName());
        if (serializeData.object instanceof EnumSet) {
            yapionObject.add(ENUM_TYPE_IDENTIFIER, (Class<?>) serializeData.getField("elementType"));
        }
        return SerializingUtils.serializeCollection(serializeData, yapionObject);
    }

    @SuppressWarnings({"unchecked"})
    @Override
    public Set<?> deserialize(DeserializeData<? extends YAPIONAnyType> deserializeData) {
        YAPIONDeserializerException yapionDeserializerException;
        try {
            Object object = ReflectionsUtils.constructObject((YAPIONObject) deserializeData.object, this, false, deserializeData.typeReMapper);
            YAPIONArray yapionArray = ((YAPIONObject) deserializeData.object).getArray("values");
            return SerializingUtils.deserializeCollection(deserializeData, yapionArray, (Set<Object>) object);
        } catch (Exception e) {
            yapionDeserializerException = new YAPIONDeserializerException(e.getMessage(), e);
        }
        try {
            YAPIONArray yapionArray = ((YAPIONObject) deserializeData.object).getArray("values");
            Set<?> list = SerializingUtils.deserializeCollection(deserializeData, yapionArray, (Set<Object>) defaultImplementation().newInstance());
            return wrapper.getOrDefault(ClassUtils.getClass(((YAPIONObject) deserializeData.object).getPlainValue(TYPE_IDENTIFIER)), objects -> objects).apply(list);
        } catch (InstantiationException e) {
            throw new YAPIONDeserializerException(e.getMessage(), e);
        } catch (Exception e) {
            throw yapionDeserializerException;
        }
    }

}
