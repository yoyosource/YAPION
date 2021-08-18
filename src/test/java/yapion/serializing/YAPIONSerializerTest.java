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
import yapion.exceptions.serializing.YAPIONSerializerException;
import yapion.hierarchy.output.StringOutput;
import yapion.hierarchy.types.YAPIONObject;
import yapion.hierarchy.types.value.ValueUtils;
import yapion.parser.YAPIONParser;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static yapion.YAPIONAssertion.isYAPION;
import static yapion.serializing.YAPIONTestObjects.*;

public class YAPIONSerializerTest {

    @Test
    public void testObject() {
        YAPIONObject yapionObject = YAPIONSerializer.serialize(new TestObject());
        assertThat(yapionObject, isYAPION("{@type(yapion.serializing.YAPIONTestObjects$TestObject)}"));
    }

    @Test
    public void testObjectReparse() {
        YAPIONObject yapionObject = YAPIONSerializer.serialize(new TestObject());
        assertThat(YAPIONParser.parse(yapionObject.toYAPION(new StringOutput()).getResult()), isYAPION(yapionObject));
    }

    @Test
    public void testMap() {
        YAPIONObject yapionObject = YAPIONSerializer.serialize(new TestMap());
        assertThat(yapionObject, isYAPION("{@type(yapion.serializing.YAPIONTestObjects$TestMap)stringStringMap{@type(java.util.HashMap)values<(Hello1):(Hello1)(Hello):(Hello)(Hello2):(Hello2)>}hugoStringMap{@type(java.util.HashMap)values<>}hashMap{@type(java.util.HashMap)values<>}linkedHashMap{@type(java.util.LinkedHashMap)values<>}}"));
    }

    @Test
    public void testMapReparse() {
        YAPIONObject yapionObject = YAPIONSerializer.serialize(new TestMap());
        assertThat(YAPIONParser.parse(yapionObject.toYAPION(new StringOutput()).getResult()), isYAPION(yapionObject));
    }

    @Test
    public void testArray() {
        YAPIONObject yapionObject = YAPIONSerializer.serialize(new TestArray());
        assertThat(yapionObject, isYAPION("{@type(yapion.serializing.YAPIONTestObjects$TestArray)strings[Hello World,null]strings1[[Hello World,null,null],[null,Hello World2,null],[null,null,Hello World3]]strings2{@type(java.util.ArrayList)values[Hello World,Hello World1,Hello World2,Hello World3]}strings3{@type(java.util.LinkedList)values[Hello World,Hello World1,Hello World2,Hello World3]}}"));
    }

    @Test
    public void testArrayReparse() {
        YAPIONObject yapionObject = YAPIONSerializer.serialize(new TestArray());
        assertThat(YAPIONParser.parse(yapionObject.toYAPION(new StringOutput()).getResult()), isYAPION(yapionObject));
    }

    @Test
    public void testPrimitive() {
        YAPIONObject yapionObject = YAPIONSerializer.serialize(new TestPrimitive());
        assertThat(yapionObject, isYAPION("{@type(yapion.serializing.YAPIONTestObjects$TestPrimitive)t(false)T(true)b(0B)B(0B)s(0S)S(0S)i(0)I(0)l(0L)L(0L)d(0.0)D(0.0)f(0.0F)F(0.0F)bi(0BI)bd(0BD)c(' ')C(' ')string()stringB{@type(java.lang.StringBuilder)string()}stringb{@type(java.lang.StringBuffer)string()}file{@type(java.io.File)path(" + ValueUtils.stringToUTFEscapedString(getUserHome(), ValueUtils.EscapeCharacters.VALUE) + File.separator + "YAPI)}}"));
    }

    @Test
    public void testPrimitiveReparse() {
        YAPIONObject yapionObject = YAPIONSerializer.serialize(new TestPrimitive());
        assertThat(YAPIONParser.parse(yapionObject.toYAPION(new StringOutput()).getResult()), isYAPION(yapionObject));
    }

    @Test
    public void testEnum() {
        YAPIONObject yapionObject = YAPIONSerializer.serialize(new TestEnum());
        assertThat(yapionObject, isYAPION("{@type(yapion.serializing.YAPIONTestObjects$TestEnum)test1{@type(yapion.serializing.YAPIONTestObjects$EnumerationTest)value(Hello)ordinal(0)}test2{@type(yapion.serializing.YAPIONTestObjects$EnumerationTest)value(World)ordinal(1)}test3{@type(yapion.serializing.YAPIONTestObjects$EnumerationTest)value(Hugo)ordinal(2)}test4{@type(yapion.serializing.YAPIONTestObjects$EnumerationTest)value(My)ordinal(3)}test5{@type(yapion.serializing.YAPIONTestObjects$EnumerationTest)value(Name)ordinal(4)}}"));
    }

    @Test
    public void testEnumReparse() {
        YAPIONObject yapionObject = YAPIONSerializer.serialize(new TestEnum());
        assertThat(YAPIONParser.parse(yapionObject.toYAPION(new StringOutput()).getResult()), isYAPION(yapionObject));
    }

    @Test(expected = NullPointerException.class)
    public void testNPECheck() {
        YAPIONSerializer.serialize(null);
    }

    @Test(expected = NullPointerException.class)
    public void testNPECheckState() {
        YAPIONSerializer.serialize(null, "some state");
    }

    @Test
    public void testInternalSerializer() {
        // It should be possible to use objects as root objects for which
        // an internal serializer exists which returns a YAPIONObject.
        YAPIONObject yapionObject = YAPIONSerializer.serialize(new ArrayList<String>());
        assertThat(yapionObject, isYAPION("{@type(java.util.ArrayList)values[]}"));
    }

    @Test
    public void testStringListWithLongs() {
        List<String> stringList = new ArrayList<>();
        stringList.add("333276742632472577");

        YAPIONObject yapionObject = YAPIONSerializer.serialize(stringList);
        assertThat(yapionObject, isYAPION("{@type(java.util.ArrayList)values[\"333276742632472577\"]}"));
    }

    @Test
    public void testYAPIONAnyTypeSerialization() {
        YAPIONObject yapionObject = YAPIONSerializer.serialize(new TestYAPIONAnyType());
        assertThat(yapionObject, isYAPION("{@type(yapion.serializing.YAPIONTestObjects$TestYAPIONAnyType)yapionObject{Test(Test)}yapionMap<(Test):(Test)>yapionArray[Test]}"));
    }

    @Test
    public void testYAPIONMultiContextAnnotation() {
        YAPIONObject yapionObject = YAPIONSerializer.serialize(new TestMultiContextAnnotation());
        assertThat(yapionObject, isYAPION("{@type(yapion.serializing.YAPIONTestObjects$TestMultiContextAnnotation)}"));
    }

    @Test(expected = YAPIONSerializerException.class)
    public void testYAPIONMultiContextAnnotationWrongContext() {
        YAPIONSerializer.serialize(new TestMultiContextAnnotation(), "Hugo");
    }

    @Test
    public void testYAPIONMultiContextAnnotationCorrectContext() {
        YAPIONObject yapionObject = YAPIONSerializer.serialize(new TestMultiContextAnnotation(), "Hello World");
        assertThat(yapionObject, isYAPION("{@type(yapion.serializing.YAPIONTestObjects$TestMultiContextAnnotation)}"));
    }

    @Test
    public void testYAPIONMultiContextAnnotationCorrectSecondContext() {
        YAPIONObject yapionObject = YAPIONSerializer.serialize(new TestMultiContextAnnotation(), "Hello");
        assertThat(yapionObject, isYAPION("{@type(yapion.serializing.YAPIONTestObjects$TestMultiContextAnnotation)}"));
    }
}
