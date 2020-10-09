package yapion.parser;

import org.junit.Test;
import yapion.hierarchy.types.YAPIONObject;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class JSONParserTest {

    @Test
    public void testParseObject() {
        YAPIONObject yapionObject = YAPIONParser.parseJSON("{}");
        assertThat(yapionObject.toYAPIONString(), is("{}"));
    }

    @Test
    public void testParseObjectValue() {
        YAPIONObject yapionObject = YAPIONParser.parseJSON("{\"test\":{}}");
        assertThat(yapionObject.toYAPIONString(), is("{test{}}"));
    }

    @Test
    public void testParseArray() {
        YAPIONObject yapionObject = YAPIONParser.parseJSON("{\"test\":[]}");
        assertThat(yapionObject.toYAPIONString(), is("{test[]}"));
    }

    @Test
    public void testParseMap() {
        YAPIONObject yapionObject = YAPIONParser.parseJSON("{\"test\":{\"@mapping\":[]}}");
        assertThat(yapionObject.toYAPIONString(), is("{test{@mapping[]}}"));
    }

}
