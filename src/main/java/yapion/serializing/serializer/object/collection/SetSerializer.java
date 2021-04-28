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
import yapion.serializing.utils.DeserializeUtils;
import yapion.serializing.utils.SerializeUtils;
import yapion.utils.ReflectionsUtils;

import java.util.*;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.function.Function;

import static yapion.utils.IdentifierUtils.ENUM_TYPE_IDENTIFIER;
import static yapion.utils.IdentifierUtils.TYPE_IDENTIFIER;

@SerializerImplementation(since = "0.23.0", initialSince = "0.3.0, 0.7.0, 0.12.0, 0.24.0", standsFor = {Set.class, HashSet.class, LinkedHashSet.class, TreeSet.class, ConcurrentSkipListSet.class, CopyOnWriteArraySet.class, EnumSet.class})
public class SetSerializer implements InternalSerializer<Set<?>> {

    @Override
    public void init() {
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
        return SerializeUtils.serializeSet(serializeData, yapionObject);
    }

    @SuppressWarnings({"unchecked"})
    @Override
    public Set<?> deserialize(DeserializeData<? extends YAPIONAnyType> deserializeData) {
        try {
            Object object = ReflectionsUtils.constructObject((YAPIONObject) deserializeData.object, this, false);
            YAPIONArray yapionArray = ((YAPIONObject) deserializeData.object).getArray("values");
            return DeserializeUtils.deserializeSet(deserializeData, yapionArray, (Set<Object>) object);
        } catch (Exception e) {
            throw new YAPIONDeserializerException(e.getMessage(), e);
        }
    }

}
