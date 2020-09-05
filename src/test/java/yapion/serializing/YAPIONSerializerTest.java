package yapion.serializing;

import org.junit.Test;
import yapion.hierarchy.types.YAPIONObject;
import yapion.parser.YAPIONParser;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static yapion.serializing.YAPIONTestObjects.*;

public class YAPIONSerializerTest {

    @Test
    public void testObject() {
        YAPIONObject yapionObject = YAPIONSerializer.serialize(new TestObject());
        assertThat(yapionObject.toString(), is("{@type(yapion.serializing.YAPIONTestObjects$TestObject)}"));
    }

    @Test
    public void testObjectReparse() {
        YAPIONObject yapionObject = YAPIONSerializer.serialize(new TestObject());
        assertThat(YAPIONParser.parse(yapionObject.toString()).toString(), is(yapionObject.toString()));
    }

    @Test
    public void testMap() {
        YAPIONObject yapionObject = YAPIONSerializer.serialize(new TestMap());
        System.out.println(yapionObject);
        assertThat(yapionObject.toString(), is("{@type(yapion.serializing.YAPIONTestObjects$TestMap)stringStringMap{@type(java.util.HashMap)values<0:1#0(Hello1)#1(Hello1)2:3#2(Hello)#3(Hello)4:5#4(Hello2)#5(Hello2)>}hugoStringMap{@type(java.util.HashMap)values<>}hashMap{@type(java.util.HashMap)values<>}linkedHashMap{@type(java.util.LinkedHashMap)values<>}}"));
    }

    @Test
    public void testMapReparse() {
        YAPIONObject yapionObject = YAPIONSerializer.serialize(new TestMap());
        assertThat(YAPIONParser.parse(yapionObject.toString()).toString(), is(yapionObject.toString()));
    }

    @Test
    public void testArray() {
        YAPIONObject yapionObject = YAPIONSerializer.serialize(new TestArray());
        assertThat(yapionObject.toString(), is("{@type(yapion.serializing.YAPIONTestObjects$TestArray)strings[Hello World,null]strings1[[Hello World,null,null],[null,Hello World2,null],[null,null,Hello World3]]strings2{@type(java.util.ArrayList)values[Hello World,Hello World1,Hello World2,Hello World3]}strings3{@type(java.util.LinkedList)values[Hello World,Hello World1,Hello World2,Hello World3]}}"));
    }

    @Test
    public void testArrayReparse() {
        YAPIONObject yapionObject = YAPIONSerializer.serialize(new TestArray());
        assertThat(YAPIONParser.parse(yapionObject.toString()).toString(), is(yapionObject.toString()));
    }

    @Test
    public void testPrimitive() {
        YAPIONObject yapionObject = YAPIONSerializer.serialize(new TestPrimitive());
        assertThat(yapionObject.toString(), is("{@type(yapion.serializing.YAPIONTestObjects$TestPrimitive)t(false)T(true)b(0B)B(0B)s(0S)S(0S)i(0)I(0)l(0L)L(0L)d(0.0)D(0.0)f(0.0F)F(0.0F)bi(0BI)bd(0BD)c(' ')C(' ')string()stringB{@type(java.lang.StringBuilder)string()}stringb{@type(java.lang.StringBuffer)string()}file{@type(java.io.File)absolutePath(" + getUserHome() + "/YAPI)}}"));
    }

    @Test
    public void testPrimitiveReparse() {
        YAPIONObject yapionObject = YAPIONSerializer.serialize(new TestPrimitive());
        assertThat(YAPIONParser.parse(yapionObject.toString()).toString(), is(yapionObject.toString()));
    }

    @Test
    public void testEnum() {
        YAPIONObject yapionObject = YAPIONSerializer.serialize(new TestEnum());
        assertThat(yapionObject.toString(), is("{@type(yapion.serializing.YAPIONTestObjects$TestEnum)test1{@type(java.lang.Enum)@enum(yapion.serializing.YAPIONTestObjects$EnumerationTest)value(Hello)ordinal(0)}test2{@type(java.lang.Enum)@enum(yapion.serializing.YAPIONTestObjects$EnumerationTest)value(World)ordinal(1)}test3{@type(java.lang.Enum)@enum(yapion.serializing.YAPIONTestObjects$EnumerationTest)value(Hugo)ordinal(2)}test4{@type(java.lang.Enum)@enum(yapion.serializing.YAPIONTestObjects$EnumerationTest)value(My)ordinal(3)}test5{@type(java.lang.Enum)@enum(yapion.serializing.YAPIONTestObjects$EnumerationTest)value(Name)ordinal(4)}}"));
    }

    @Test
    public void testEnumReparse() {
        YAPIONObject yapionObject = YAPIONSerializer.serialize(new TestEnum());
        assertThat(YAPIONParser.parse(yapionObject.toString()).toString(), is(yapionObject.toString()));
    }

}
