package yapion.parser;

import org.junit.Test;
import yapion.hierarchy.output.StringOutput;
import yapion.hierarchy.types.YAPIONObject;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class JSONMapperTest {

    @Test
    public void testMapperByte() {
        YAPIONObject yapionObject = YAPIONParser.mapJSON("{\"byte\":{\"@byte\":0}}");
        assertThat(yapionObject.toYAPION(new StringOutput()).getResult(), is("{byte(0B)}"));
    }

    @Test
    public void testMapperShort() {
        YAPIONObject yapionObject = YAPIONParser.mapJSON("{\"short\":{\"@short\":0}}");
        assertThat(yapionObject.toYAPION(new StringOutput()).getResult(), is("{short(0S)}"));
    }

    @Test
    public void testMapperInt() {
        YAPIONObject yapionObject = YAPIONParser.mapJSON("{\"int\":{\"@int\":0}}");
        assertThat(yapionObject.toYAPION(new StringOutput()).getResult(), is("{int(0)}"));
    }

    @Test
    public void testMapperLong() {
        YAPIONObject yapionObject = YAPIONParser.mapJSON("{\"long\":{\"@long\":0}}");
        assertThat(yapionObject.toYAPION(new StringOutput()).getResult(), is("{long(0L)}"));
    }

    @Test
    public void testMapperBInt() {
        YAPIONObject yapionObject = YAPIONParser.mapJSON("{\"bint\":{\"@bint\":\"0\"}}");
        assertThat(yapionObject.toYAPION(new StringOutput()).getResult(), is("{bint(0BI)}"));
    }

    @Test
    public void testMapperFloat() {
        YAPIONObject yapionObject = YAPIONParser.mapJSON("{\"float\":{\"@float\":0.0}}");
        assertThat(yapionObject.toYAPION(new StringOutput()).getResult(), is("{float(0.0F)}"));
    }

    @Test
    public void testMapperDouble() {
        YAPIONObject yapionObject = YAPIONParser.mapJSON("{\"double\":{\"@double\":0.0}}");
        assertThat(yapionObject.toYAPION(new StringOutput()).getResult(), is("{double(0.0)}"));
    }

    @Test
    public void testMapperBDouble() {
        YAPIONObject yapionObject = YAPIONParser.mapJSON("{\"bdecimal\":{\"@bdecimal\":\"0.0\"}}");
        assertThat(yapionObject.toYAPION(new StringOutput()).getResult(), is("{bdecimal(0.0BD)}"));
    }

    @Test
    public void testMapperChar() {
        YAPIONObject yapionObject = YAPIONParser.mapJSON("{\"char\":{\"@char\":\"0\"}}");
        assertThat(yapionObject.toYAPION(new StringOutput()).getResult(), is("{char('0')}"));
    }

    @Test
    public void testMapperPointer() {
        YAPIONObject yapionObject = YAPIONParser.mapJSON("{\"pointer\":{\"@pointer\":\"0000000000000000\"}}");
        assertThat(yapionObject.toYAPION(new StringOutput()).getResult(), is("{pointer->0000000000000000}"));
    }

    @Test
    public void testMapperMap() {
        YAPIONObject yapionObject = YAPIONParser.mapJSON("{\"map\":{\"@mapping\":[\"0:1\"],\"#0\":\"string1\",\"#1\":\"string2\"}}");
        assertThat(yapionObject.toYAPION(new StringOutput()).getResult(), is("{map<(string1):(string2)>}"));
    }

}
