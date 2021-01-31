package yapion.notation;

import org.junit.Test;
import yapion.hierarchy.output.StringOutput;
import yapion.hierarchy.types.YAPIONArray;
import yapion.hierarchy.types.YAPIONMap;
import yapion.hierarchy.types.YAPIONObject;
import yapion.parser.YAPIONParser;
import yapion.utils.ReflectionsUtils;

import java.nio.charset.StandardCharsets;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
// import static yapion.annotation.AnnotationTestObjects.*;

public class NotationObjectsTest {

    @Test
    public void testObjectYAPION() {
        assertThat(new YAPIONObject().toYAPION(new StringOutput()).getResult(), is("{}"));
    }

    @Test
    public void testObjectJSON() {
        assertThat(new YAPIONObject().toJSONLossy(new StringOutput()).getResult(), is("{}"));
    }

    @Test
    public void testBackslash() {
        YAPIONObject yapionObject = new YAPIONObject();
        yapionObject.add("a", "\\\\");
        assertThat(yapionObject.toYAPION(new StringOutput()).getResult(), is("{a(\\\\)}"));
    }

    /*
    @Test
    public void testUnicode() {
        YAPIONObject yapionObject = new YAPIONObject();
        yapionObject.add("a", "ß");
        assertThat(yapionObject.toYAPION(new StringOutput()).getResult(), is("{a(ß)}"));
    }

    @Test
    public void testUnicodeKey() {
        YAPIONObject yapionObject = new YAPIONObject();
        yapionObject.add("ß", "a");
        assertThat(yapionObject.toYAPION(new StringOutput()).getResult(), is("{ß(a)}"));
    }
    */

    @Test
    public void testKeyCharacters() {
        YAPIONObject yapionObject = new YAPIONObject();
        yapionObject.add(" !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~", " !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~");
        assertThat(yapionObject.toYAPION(new StringOutput()).getResult(), is("{\\ !\"#$%&'\\(\\)*+,-./0123456789:;\\<=\\>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ\\[\\\\]^_`abcdefghijklmnopqrstuvwxyz\\{|\\}~( !\"#$%&'\\(\\)*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~)}"));
    }

    @Test
    public void testUmlauts() {
        YAPIONObject yapionObject = new YAPIONObject();
        yapionObject.add("äöü", "äöü");
        assertThat(yapionObject.toYAPION(new StringOutput()).getResult(), is("{äöü(äöü)}"));
    }

    @Test
    public void testOutsideLatin1() {
        YAPIONObject yapionObject = new YAPIONObject();
        yapionObject.add("\u0000\u001F\u007E\u007F\u00A0\u00A1\u0100", "\u0000\u001F\u007E\u007F\u00A0\u00A1\u0100");
        assertThat(yapionObject.toYAPION(new StringOutput()).getResult(), is("{\\u0000\\u001F~\\u007F\\u00A0¡\\u0100(\\u0000\\u001F~\\u007F\\u00A0¡\\u0100)}"));
    }

    @Test
    public void testMapYAPION() {
        assertThat(new YAPIONMap().toYAPION(new StringOutput()).getResult(), is("<>"));
    }

    @Test
    public void testMapJSON() {
        assertThat(new YAPIONMap().toJSONLossy(new StringOutput()).getResult(), is("{\"@mapping\":[]}"));
    }

    @Test
    public void testArrayYAPION() {
        assertThat(new YAPIONArray().toYAPION(new StringOutput()).getResult(), is("[]"));
    }

    @Test
    public void testArrayJSON() {
        assertThat(new YAPIONArray().toJSONLossy(new StringOutput()).getResult(), is("[]"));
    }

}
