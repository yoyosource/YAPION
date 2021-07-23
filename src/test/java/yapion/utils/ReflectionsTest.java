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

package yapion.utils;

import org.junit.Test;
import yapion.exceptions.YAPIONException;
import yapion.exceptions.serializing.YAPIONDeserializerException;
import yapion.exceptions.utils.YAPIONReflectionException;
import yapion.hierarchy.api.groups.YAPIONAnyType;
import yapion.hierarchy.types.YAPIONMap;
import yapion.hierarchy.types.YAPIONObject;
import yapion.serializing.InternalSerializer;
import yapion.serializing.data.DeserializeData;
import yapion.serializing.data.SerializeData;
import yapion.serializing.utils.SerializingUtils;

import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Function;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static yapion.utils.IdentifierUtils.ENUM_TYPE_IDENTIFIER;
import static yapion.utils.IdentifierUtils.TYPE_IDENTIFIER;

public class ReflectionsTest {

    private static class MapSerializer implements InternalSerializer<Map<?, ?>> {
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

        private Map<?, ?> createDefaultImplementation() {
            return new HashMap<>();
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
                Map<?, ?> map = SerializingUtils.deserializeMap(deserializeData, yapionMap, (Map<Object, Object>) createDefaultImplementation());
                return wrapper.getOrDefault(ClassUtils.getClass(((YAPIONObject) deserializeData.object).getPlainValue(TYPE_IDENTIFIER)), objects -> objects).apply(map);
            } catch (YAPIONException e) {
                throw e;
            } catch (Exception e) {
                throw yapionDeserializerException;
            }
        }
    }

    @Test(expected = YAPIONReflectionException.class)
    public void testYAPIONConstructionInvalidTest() {
        ReflectionsUtils.constructObject(new YAPIONObject(), new MapSerializer(), false);
    }

    @Test
    public void testYAPIONConstructionValidTest() {
        Object object = ReflectionsUtils.constructObject(new YAPIONObject().add(TYPE_IDENTIFIER, "java.util.HashMap"), new MapSerializer(), false);
        assertThat(object, is(new HashMap<>()));
    }

    @Test
    public void testYAPIONConstructionValidRemapTest() {
        Object object = ReflectionsUtils.constructObject(new YAPIONObject().add(TYPE_IDENTIFIER, "java.util.Map"), new MapSerializer(), false);
        assertThat(object, is(new HashMap<>()));
    }

    @Test
    public void testGetFields() {
        List<Field> fieldList = ReflectionsUtils.getFields(UtilsTestObjects.UtilReflectionsTest.class);
        assertThat(fieldList.size(), is(1));
    }

    @Test
    public void testGetFieldsExtends() {
        List<Field> fieldList = ReflectionsUtils.getFields(UtilsTestObjects.UtilReflectionsTestExtends.class);
        assertThat(fieldList.size(), is(4));
    }

    @Test
    public void testGetFieldsExtendsTwo() {
        List<Field> fieldList = ReflectionsUtils.getFields(UtilsTestObjects.UtilReflectionsTestExtendsTwo.class);
        assertThat(fieldList.size(), is(4));
    }

}
