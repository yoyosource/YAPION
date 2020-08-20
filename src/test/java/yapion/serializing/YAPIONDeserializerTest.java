package yapion.serializing;

import org.junit.Test;
import yapion.parser.YAPIONParser;

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
        Object object = YAPIONDeserializer.deserialize(YAPIONParser.parse("{@type(yapion.serializing.YAPIONTestObjects$TestMap)stringStringMap<0:1#0(Hello1)#1(Hello1)2:3#2(Hello)#3(Hello)4:5#4(Hello2)#5(Hello2)>hugoStringMap<>hashMap<>linkedHashMap<>}"));
        assertThat(object, is(new TestMap()));
    }

    @Test
    public void testArray() {
        Object object = YAPIONDeserializer.deserialize(YAPIONParser.parse("{@type(yapion.serializing.YAPIONTestObjects$TestArray)strings[Hello World,null]strings1[[Hello World,null,null],[null,Hello World2,null],[null,null,Hello World3]]strings2[Hello World,Hello World1,Hello World2,Hello World3]strings3[Hello World,Hello World1,Hello World2,Hello World3]}"));
        assertThat(object, is(new TestArray()));
    }

    @Test
    public void testPrimitive() {
        Object object = YAPIONDeserializer.deserialize(YAPIONParser.parse("{@type(yapion.serializing.YAPIONTestObjects$TestPrimitive)b(0B)B(0B)s(0S)S(0S)i(0)I(0)l(0L)L(0L)d(0.0)D(0.0)f(0.0F)F(0.0F)bi(0BI)bd(0BD)c(' ')C(' ')string()stringB()stringb()file{@type(java.io.File)absolutePath(" + getUserHome() + "/YAPI)}}"));
        assertThat(object, is(new TestPrimitive()));
    }

}
