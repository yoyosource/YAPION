package yapion.parser;

import org.junit.Test;
import yapion.hierarchy.types.YAPIONObject;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class YAPIONParserTest {

    @Test
    public void testEmpty() {
        YAPIONObject yapionObject = YAPIONParser.parse("{}");
        assertThat(yapionObject.toString(), is("{}"));
    }

    @Test
    public void testVariable() {
        YAPIONObject yapionObject = YAPIONParser.parse("{a()b(\"Hello\")c(true)d(\"true\")}");
        assertThat(yapionObject.toString(), is("{a()b(Hello)c(true)d(\"true\")}"));
    }

    @Test
    public void testBlank() {
        YAPIONObject yapionObject = YAPIONParser.parse("{   a()\\   b()()}");
        assertThat(yapionObject.toString(), is("{a()\\   b()()}"));
    }

    @Test
    public void testArray() {
        YAPIONObject yapionObject = YAPIONParser.parse("{a[]b[[null,null],[],[]]}");
        assertThat(yapionObject.toString(), is("{a[]b[[null,null],[],[]]}"));
    }

    @Test
    public void testMap() {
        YAPIONObject yapionObject = YAPIONParser.parse("{a<>}");
        assertThat(yapionObject.toString(), is("{a<>}"));
    }

    @Test
    public void testPointer() {
        YAPIONObject yapionObject = YAPIONParser.parse("{a->0000000000000000}");
        assertThat(yapionObject.toString(), is("{a->0000000000000000}"));
    }

}
