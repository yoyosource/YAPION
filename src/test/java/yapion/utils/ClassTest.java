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

import java.util.HashMap;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.both;
import static org.hamcrest.Matchers.is;

public class ClassTest {

    @Test
    public void testStringToClass() {
        Class<?> clazz = ClassUtils.getClass("java.util.HashMap");
        assertThat(clazz, is(HashMap.class));
    }

    @Test
    public void testStringToClassForPrimitives() {
        Class<?> clazz = ClassUtils.getClass("boolean");
        assertThat(clazz, is(boolean.class));
    }

    @Test
    public void testConversionToBoxed() {
        assertThat(ClassUtils.getBoxed("boolean"), is("java.lang.Boolean"));
    }

    @Test
    public void testConversionToPrimitive() {
        assertThat(ClassUtils.getPrimitive("java.lang.Boolean"), is("boolean"));
    }

    @Test
    public void testIfPrimitive() {
        assertThat(ClassUtils.isPrimitive(boolean.class), is(true));
        assertThat(ClassUtils.isPrimitive(Boolean.class), is(false));
    }

    @Test
    public void testClassConversionToBoxed() {
        assertThat(ClassUtils.getBoxed(boolean.class), is(Boolean.class));
        assertThat(ClassUtils.getBoxed(Boolean.class), is(Boolean.class));
    }

}
