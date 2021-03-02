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
import yapion.exceptions.utils.YAPIONReflectionException;
import yapion.hierarchy.types.YAPIONObject;
import yapion.serializing.serializer.object.map.MapSerializer;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static yapion.utils.IdentifierUtils.TYPE_IDENTIFIER;

public class ReflectionsTest {

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
