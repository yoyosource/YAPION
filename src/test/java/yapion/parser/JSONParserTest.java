package yapion.parser;

import org.junit.Test;
import yapion.hierarchy.output.StringOutput;
import yapion.hierarchy.types.YAPIONObject;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class JSONParserTest {

    @Test
    public void testParseObject() {
        YAPIONObject yapionObject = YAPIONParser.parseJSON("{}");
        assertThat(yapionObject.toYAPION(new StringOutput()).getResult(), is("{}"));
    }

    @Test
    public void testParseObjectValue() {
        YAPIONObject yapionObject = YAPIONParser.parseJSON("{\"test\":{}}");
        assertThat(yapionObject.toYAPION(new StringOutput()).getResult(), is("{test{}}"));
    }

    @Test
    public void testParseArray() {
        YAPIONObject yapionObject = YAPIONParser.parseJSON("{\"test\":[]}");
        assertThat(yapionObject.toYAPION(new StringOutput()).getResult(), is("{test[]}"));
    }

    @Test
    public void testParseMap() {
        YAPIONObject yapionObject = YAPIONParser.parseJSON("{\"test\":{\"@mapping\":[]}}");
        assertThat(yapionObject.toYAPION(new StringOutput()).getResult(), is("{test{@mapping[]}}"));
    }

}
