package yapion.parser;

import org.junit.Test;
import yapion.exceptions.parser.YAPIONParserException;
import yapion.hierarchy.output.StringOutput;
import yapion.hierarchy.types.YAPIONObject;

import java.io.InputStream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class YAPIONParserTest {

    @Test(expected = NullPointerException.class)
    public void testNullSafety() {
        YAPIONParser.parse((InputStream) null);
    }

    @Test(expected = YAPIONParserException.class)
    public void testStringError() {
        YAPIONParser.parse("{{}");
    }

    @Test(expected = YAPIONParserException.class)
    public void testTypeMismatch() {
        YAPIONParser.parse("{]");
    }

    @Test(expected = YAPIONParserException.class)
    public void testInitialType() {
        YAPIONParser.parse("[]");
    }

    @Test
    public void testEmpty() {
        YAPIONObject yapionObject = YAPIONParser.parse("{}");
        assertThat(yapionObject.toYAPION(new StringOutput()).getResult(), is("{}"));
    }

    @Test
    public void testVariable() {
        YAPIONObject yapionObject = YAPIONParser.parse("{a()b(\"Hello\")c(true)d(\"true\")}");
        assertThat(yapionObject.toYAPION(new StringOutput()).getResult(), is("{a()b(Hello)c(true)d(\"true\")}"));
    }

    @Test
    public void testBlank() {
        YAPIONObject yapionObject = YAPIONParser.parse("{   a()\\   b()()}");
        assertThat(yapionObject.toYAPION(new StringOutput()).getResult(), is("{a()\\   b()()}"));
    }

    @Test
    public void testArray() {
        YAPIONObject yapionObject = YAPIONParser.parse("{a[]b[[null,null],[],[]]}");
        assertThat(yapionObject.toYAPION(new StringOutput()).getResult(), is("{a[]b[[null,null],[],[]]}"));
    }

    @Test
    public void testMap() {
        YAPIONObject yapionObject = YAPIONParser.parse("{a<>}");
        assertThat(yapionObject.toYAPION(new StringOutput()).getResult(), is("{a<>}"));
    }

    @Test
    public void testPointer() {
        YAPIONObject yapionObject = YAPIONParser.parse("{a->0000000000000000}");
        assertThat(yapionObject.toYAPION(new StringOutput()).getResult(), is("{a->0000000000000000}"));
    }

    @Test
    public void testBackslash() {
        YAPIONObject yapionObject = YAPIONParser.parse("{a(\\\\)}");
        assertThat(yapionObject.toYAPION(new StringOutput()).getResult(), is("{a(\\\\)}"));
    }

    @Test
    public void testUnicode() {
        YAPIONObject yapionObject = YAPIONParser.parse("{a(\\u0020)}");
        assertThat(yapionObject.toYAPION(new StringOutput()).getResult(), is("{a( )}"));
    }

    @Test
    public void testUnicodeKey() {
        YAPIONObject yapionObject = YAPIONParser.parse("{\\u0020(a)}");
        assertThat(yapionObject.toYAPION(new StringOutput()).getResult(), is("{\\ (a)}"));
    }

    @Test
    public void testUnicodeKeyReparse() {
        YAPIONObject yapionObject = new YAPIONObject();
        yapionObject.add("§", "a");
        assertThat(yapionObject.toYAPION(new StringOutput()).getResult(), is("{§(a)}"));
        yapionObject = YAPIONParser.parse(yapionObject.toYAPION(new StringOutput()).getResult());
        assertThat(yapionObject.toYAPION(new StringOutput()).getResult(), is("{§(a)}"));
    }

    @Test
    public void testCharacters() {
        YAPIONObject yapionObject = YAPIONParser.parse("{\\ !\"#$%&'\\(\\)*+,-./0123456789:;\\<=\\>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ\\[\\\\]^_`abcdefghijklmnopqrstuvwxyz\\{|\\}~( !\"#$%&'\\(\\)*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~)}");
        assertThat(yapionObject.toYAPION(new StringOutput()).getResult(), is("{\\ !\"#$%&'\\(\\)*+,-./0123456789:;\\<=\\>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ\\[\\\\]^_`abcdefghijklmnopqrstuvwxyz\\{|\\}~( !\"#$%&'\\(\\)*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~)}"));
    }

}
