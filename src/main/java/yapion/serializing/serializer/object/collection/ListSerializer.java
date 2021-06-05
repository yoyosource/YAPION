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
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Function;

import static yapion.utils.IdentifierUtils.TYPE_IDENTIFIER;

@SerializerImplementation(since = "0.26.0", initialSince = "0.3.0, 0.12.0, 0.23.0", standsFor = {List.class, ArrayList.class, LinkedList.class, CopyOnWriteArrayList.class})
public class ListSerializer implements InternalSerializer<List<?>> {

    private Map<Class<?>, Function<List<?>, List<?>>> wrapper = new HashMap<>();

    {
        init();
    }

    @Override
    public void init() {
        wrapper = new HashMap<>();
        wrapper.put(Arrays.asList().getClass(), objects -> Arrays.asList(objects.toArray()));
        wrapper.put(Collections.emptyList().getClass(), objects -> Collections.emptyList());
        wrapper.put(Collections.singletonList(null).getClass(), objects -> Collections.singletonList(objects.get(0)));
        wrapper.put(Collections.unmodifiableList(new ArrayList<>()).getClass(), objects -> Collections.unmodifiableList(objects));
    }

    @Override
    public Class<?> type() {
        return List.class;
    }

    @Override
    public Class<?> defaultImplementation() {
        return ArrayList.class;
    }

    @Override
    public Class<?> interfaceType() {
        return List.class;
    }

    @Override
    public YAPIONAnyType serialize(SerializeData<List<?>> serializeData) {
        YAPIONObject yapionObject = new YAPIONObject();
        yapionObject.add(TYPE_IDENTIFIER, serializeData.object.getClass().getTypeName());
        return SerializingUtils.serializeCollection(serializeData, yapionObject);
    }

    @SuppressWarnings({"unchecked"})
    @Override
    public List<?> deserialize(DeserializeData<? extends YAPIONAnyType> deserializeData) {
        YAPIONDeserializerException yapionDeserializerException;
        try {
            Object object = ReflectionsUtils.constructObject((YAPIONObject) deserializeData.object, this, false, deserializeData.typeReMapper);
            YAPIONArray yapionArray = ((YAPIONObject) deserializeData.object).getArray("values");
            return SerializingUtils.deserializeCollection(deserializeData, yapionArray, (List<Object>) object);
        } catch (Exception e) {
            yapionDeserializerException = new YAPIONDeserializerException(e.getMessage(), e);
        }
        try {
            YAPIONArray yapionArray = ((YAPIONObject) deserializeData.object).getArray("values");
            List<?> list = SerializingUtils.deserializeCollection(deserializeData, yapionArray, (List<Object>) defaultImplementation().newInstance());
            return wrapper.getOrDefault(ClassUtils.getClass(((YAPIONObject) deserializeData.object).getPlainValue(TYPE_IDENTIFIER)), objects -> objects).apply(list);
        } catch (InstantiationException e) {
            throw new YAPIONDeserializerException(e.getMessage(), e);
        } catch (YAPIONException e) {
            throw e;
        } catch (Exception e) {
            throw yapionDeserializerException;
        }
    }
}
