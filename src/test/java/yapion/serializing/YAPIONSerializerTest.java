package yapion.serializing;

import org.junit.Test;
import yapion.annotations.serialize.YAPIONSave;
import yapion.hierarchy.types.YAPIONObject;
import yapion.parser.YAPIONParser;

import java.io.File;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class YAPIONSerializerTest {

    @YAPIONSave
    private class TestObject {

    }

    @Test
    public void testObject() {
        YAPIONObject yapionObject = YAPIONSerializer.serialize(new TestObject());
        assertThat(yapionObject.toString(), is("{@type(yapion.serializing.YAPIONSerializerTest$TestObject)}"));
    }

    @Test
    public void testObjectReparse() {
        YAPIONObject yapionObject = YAPIONSerializer.serialize(new TestObject());
        assertThat(YAPIONParser.parse(yapionObject.toString()).toString(), is(yapionObject.toString()));
    }

    @YAPIONSave
    private class TestMap {

        private final Map<String, String> stringStringMap = new HashMap<>();
        private final Map<String, String> hugoStringMap = new HashMap<>();
        private final HashMap<String, String> hashMap = new HashMap<>();
        private final LinkedHashMap<Integer, String> linkedHashMap = new LinkedHashMap<>();
        {
            stringStringMap.put("Hello", "Hello");
            stringStringMap.put("Hello1", "Hello1");
            stringStringMap.put("Hello2", "Hello2");
        }
    }

    @Test
    public void testMap() {
        YAPIONObject yapionObject = YAPIONSerializer.serialize(new TestMap());
        assertThat(yapionObject.toString(), is("{@type(yapion.serializing.YAPIONSerializerTest$TestMap)stringStringMap<0:1,#0{(Hello1)},#1{(Hello1)},2:3,#2{(Hello)},#3{(Hello)},4:5,#4{(Hello2)},#5{(Hello2)}>hugoStringMap<>hashMap<>linkedHashMap<>}"));
    }

    @Test
    public void testMapReparse() {
        YAPIONObject yapionObject = YAPIONSerializer.serialize(new TestMap());
        assertThat(YAPIONParser.parse(yapionObject.toString()).toString(), is(yapionObject.toString()));
    }

    @YAPIONSave
    private class TestArray {

        private final String[] strings = new String[2];
        private final String[][] strings1 = new String[3][3];

        private final List<String> strings2 = new ArrayList<>();
        {
            strings[0] = "Hello World";
            strings1[0][0] = "Hello World";
            strings1[1][1] = "Hello World2";
            strings1[2][2] = "Hello World3";

            strings2.add("Hello World");
            strings2.add("Hello World1");
            strings2.add("Hello World2");
            strings2.add("Hello World3");
        }
    }

    @Test
    public void testArray() {
        YAPIONObject yapionObject = YAPIONSerializer.serialize(new TestArray());
        assertThat(yapionObject.toString(), is("{@type(yapion.serializing.YAPIONSerializerTest$TestArray)strings[Hello World,null]strings1[[Hello World,null,null],[null,Hello World2,null],[null,null,Hello World3]]strings2[Hello World,Hello World1,Hello World2,Hello World3]}"));
    }

    @Test
    public void testArrayReparse() {
        YAPIONObject yapionObject = YAPIONSerializer.serialize(new TestArray());
        assertThat(YAPIONParser.parse(yapionObject.toString()).toString(), is(yapionObject.toString()));
    }

    @YAPIONSave
    private class TestPrimitive {

        private final byte b = 0;
        private final Byte B = 0;
        private final short s = 0;
        private final Short S = 0;
        private final int i = 0;
        private final Integer I = 0;
        private final long l = 0;
        private final Long L = 0L;
        private final double d = 0;
        private final Double D = 0D;
        private final float f = 0;
        private final Float F = 0F;
        private final BigInteger bi = BigInteger.valueOf(0);
        private final BigDecimal bd = BigDecimal.valueOf(0);

        private final char c = ' ';
        private final Character C = ' ';

        private final String string = "";
        private final StringBuilder stringB = new StringBuilder();
        private final StringBuffer stringb = new StringBuffer();

        private final File file = new File("~");

    }

    @Test
    public void testPrimitive() {
        YAPIONObject yapionObject = YAPIONSerializer.serialize(new TestPrimitive());
        assertThat(yapionObject.toString(), is("{@type(yapion.serializing.YAPIONSerializerTest$TestPrimitive)b(0)B(0)s(0)S(0)i(0)I(0)l(0L)L(0L)d(0.0)D(0.0)f(0.0F)F(0.0F)bi(0BI)bd(0BD)c(' ')C(' ')string()stringB()stringb()file{@type(java.io.File)absolutePath(/Users/jojo/IdeaProjects/YAPION/~)}}"));
    }

    @Test
    public void testPrimitiveReparse() {
        YAPIONObject yapionObject = YAPIONSerializer.serialize(new TestPrimitive());
        assertThat(YAPIONParser.parse(yapionObject.toString()).toString(), is(yapionObject.toString()));
    }

}
