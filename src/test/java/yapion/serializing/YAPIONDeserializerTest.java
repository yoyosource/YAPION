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

package yapion.serializing;

import org.junit.Test;
import yapion.exceptions.serializing.YAPIONDeserializerException;
import yapion.hierarchy.output.StringOutput;
import yapion.hierarchy.types.YAPIONObject;
import yapion.parser.YAPIONParser;

import java.io.File;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static yapion.serializing.YAPIONTestObjects.*;

public class YAPIONDeserializerTest {

    @Test
    public void testObject() {
        Object object = YAPIONDeserializer.deserialize(YAPIONParser.parse("{@type(yapion.serializing.YAPIONTestObjects$TestObject)}"));
        assertThat(object, is(new TestObject()));
    }

    @Test
    public void testMap() {
        Object object = YAPIONDeserializer.deserialize(YAPIONParser.parse("{@type(yapion.serializing.YAPIONTestObjects$TestMap)stringStringMap{@type(java.util.HashMap)values<0:1#0(Hello1)#1(Hello1)2:3#2(Hello)#3(Hello)4:5#4(Hello2)#5(Hello2)>}hugoStringMap{@type(java.util.HashMap)values<>}hashMap{@type(java.util.HashMap)values<>}linkedHashMap{@type(java.util.LinkedHashMap)values<>}}"));
        assertThat(object, is(new TestMap()));
    }

    @Test
    public void testArray() {
        Object object = YAPIONDeserializer.deserialize(YAPIONParser.parse("{@type(yapion.serializing.YAPIONTestObjects$TestArray)strings[Hello World,null]strings1[[Hello World,null,null],[null,Hello World2,null],[null,null,Hello World3]]strings2{@type(java.util.ArrayList)values[Hello World,Hello World1,Hello World2,Hello World3]}strings3{@type(java.util.LinkedList)values[Hello World,Hello World1,Hello World2,Hello World3]}}"));
        assertThat(object, is(new TestArray()));
    }

    @Test
    public void testInnerArray() {
        Object object = YAPIONDeserializer.deserialize(YAPIONParser.parse("{@type(yapion.serializing.YAPIONTestObjects$TestInnerArray)intList{@type(java.util.ArrayList)values[[[0L],[1L],[2L]],[[1L],[2L],[3L]],[[2L],[3L],[4L]]]}}"));
        assertThat(object, is(new TestInnerArray()));
    }

    @Test
    public void testPrimitive() {
        Object object = YAPIONDeserializer.deserialize(YAPIONParser.parse("{@type(yapion.serializing.YAPIONTestObjects$TestPrimitive)t(false)T(true)b(0B)B(0B)s(0S)S(0S)i(0)I(0)l(0L)L(0L)d(0.0)D(0.0)f(0.0F)F(0.0F)bi(0BI)bd(0BD)c(' ')C(' ')string()stringB{@type(java.lang.StringBuilder)string()}stringb{@type(java.lang.StringBuffer)string()}file{@type(java.io.File)path(" + getUserHome() + File.separator + "YAPI)}}"));
        TestPrimitive toEqual = new TestPrimitive();
        assertThat(object, is(toEqual));
        TestPrimitive primitive = (TestPrimitive) object;
        assertThat(primitive.stringB.toString(), is(toEqual.stringB.toString()));
        assertThat(primitive.stringb.toString(), is(toEqual.stringb.toString()));
    }

    @Test
    public void testEnum() {
        Object object = YAPIONDeserializer.deserialize(YAPIONParser.parse("{@type(yapion.serializing.YAPIONTestObjects$TestEnum)test1{@type(java.lang.Enum)@enum(yapion.serializing.YAPIONTestObjects$EnumerationTest)value(Hello)ordinal(0)}test2{@type(java.lang.Enum)@enum(yapion.serializing.YAPIONTestObjects$EnumerationTest)value(World)ordinal(1)}test3{@type(java.lang.Enum)@enum(yapion.serializing.YAPIONTestObjects$EnumerationTest)value(Hugo)ordinal(2)}test4{@type(java.lang.Enum)@enum(yapion.serializing.YAPIONTestObjects$EnumerationTest)value(My)ordinal(3)}test5{@type(java.lang.Enum)@enum(yapion.serializing.YAPIONTestObjects$EnumerationTest)value(Name)ordinal(4)}}"));
        assertThat(object, is(new TestEnum()));
    }

    @Test(expected = YAPIONDeserializerException.class)
    public void testEnumClassNotFound() {
        YAPIONDeserializer.deserialize(YAPIONParser.parse("{@type(yapion.serializing.YAPIONTestObjects$TestEnum)test1{@type(java.lang.Enum)@enum(yapion.serializing.YAPIONTestObjects$Unknown)value(Hello)ordinal(0)}}"));
    }

    @Test(expected = YAPIONDeserializerException.class)
    public void testEnumClassNoEnum() {
        YAPIONDeserializer.deserialize(YAPIONParser.parse("{@type(yapion.serializing.YAPIONTestObjects$TestEnum)test1{@type(java.lang.Enum)@enum(yapion.serializing.YAPIONTestObjects$TestPrimitive)value(Hello)ordinal(0)}}"));
    }

    @Test(expected = YAPIONDeserializerException.class)
    public void testEnumElementNotFound() {
        YAPIONDeserializer.deserialize(YAPIONParser.parse("{@type(yapion.serializing.YAPIONTestObjects$TestEnum)test1{@type(java.lang.Enum)@enum(yapion.serializing.YAPIONTestObjects$EnumerationTest)value(NotHello)ordinal(0)}}"));
    }

    @Test(expected = NullPointerException.class)
    public void testNPECheck() {
        YAPIONDeserializer.deserialize(null);
    }

    @Test(expected = NullPointerException.class)
    public void testNPECheckState() {
        YAPIONDeserializer.deserialize(null, "some state");
    }

    @Test(expected = YAPIONDeserializerException.class)
    public void testDeserializerMissingLoadAnnotation() {
        // Resolved class has no Load annotation
        Object object = YAPIONDeserializer.deserialize(YAPIONParser.parse("{@type(yapion.serializing.YAPIONTestObjects$NoAnnotations)}"));
    }

    @Test
    public void testReducedTypes() {
        YAPIONObject yapionObject = new YAPIONSerializer(new TestReduced()).parse().getReducedYAPIONObject();
        Object object = new YAPIONDeserializer(yapionObject).reducedMode(true).parse().getObject();
        TestReduced toEqual = new TestReduced();
        assertThat(object, is(toEqual));
        TestReduced primitive = (TestReduced) object;
        assertThat(primitive.stringB.toString(), is(toEqual.stringB.toString()));
        assertThat(primitive.stringb.toString(), is(toEqual.stringb.toString()));
    }

}
