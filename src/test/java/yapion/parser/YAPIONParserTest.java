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

package yapion.parser;

import org.junit.Test;
import yapion.exceptions.parser.YAPIONParserException;
import yapion.hierarchy.output.StringOutput;
import yapion.hierarchy.types.YAPIONObject;

import java.io.InputStream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static yapion.YAPIONAssertion.isYAPION;

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

    @Test(expected = YAPIONParserException.class)
    public void testEmptyString() {
        YAPIONParser.parse("");
    }

    @Test
    public void testCharArray() {
        char[] chars = "{a()b{}c[]d<>}".toCharArray();
        YAPIONObject yapionObject = new YAPIONParser(chars).parse().result();
        assertThat(yapionObject, isYAPION("{a()b{}c[]d<>}"));
    }

    @Test
    public void testByteArray() {
        byte[] bytes = "{a()b{}c[]d<>}".getBytes();
        YAPIONObject yapionObject = new YAPIONParser(bytes).parse().result();
        assertThat(yapionObject, isYAPION("{a()b{}c[]d<>}"));
    }

    @Test
    public void testEmpty() {
        YAPIONObject yapionObject = YAPIONParser.parse("{}");
        assertThat(yapionObject, isYAPION("{}"));
    }

    @Test
    public void testVariable() {
        YAPIONObject yapionObject = YAPIONParser.parse("{a()b(\"Hello\")c(true)d(\"true\")}");
        assertThat(yapionObject, isYAPION("{a()b(Hello)c(true)d(\"true\")}"));
    }

    @Test
    public void testBlank() {
        YAPIONObject yapionObject = YAPIONParser.parse("{   a()\\   b()()}");
        assertThat(yapionObject, isYAPION("{a()\\   b()()}"));
    }

    @Test
    public void testArray() {
        YAPIONObject yapionObject = YAPIONParser.parse("{a[]b[[null,null],[],[]]}");
        assertThat(yapionObject, isYAPION("{a[]b[[null,null],[],[]]}"));
    }

    @Test
    public void testMap() {
        YAPIONObject yapionObject = YAPIONParser.parse("{a<>}");
        assertThat(yapionObject, isYAPION("{a<>}"));
    }

    @Test
    public void testPointer() {
        YAPIONObject yapionObject = YAPIONParser.parse("{a->0000000000000000}");
        assertThat(yapionObject, isYAPION("{a->0000000000000000}"));
    }

    @Test
    public void testBackslash() {
        YAPIONObject yapionObject = YAPIONParser.parse("{a(\\\\)}");
        assertThat(yapionObject, isYAPION("{a(\\\\)}"));
    }

    @Test
    public void testUnicode() {
        YAPIONObject yapionObject = YAPIONParser.parse("{a(\\u0020)}");
        assertThat(yapionObject, isYAPION("{a( )}"));
    }

    @Test
    public void testUnicodeKey() {
        YAPIONObject yapionObject = YAPIONParser.parse("{\\u0020(a)}");
        assertThat(yapionObject, isYAPION("{\\ (a)}"));
    }

    @Test
    public void testUnicodeKeyReparse() {
        YAPIONObject yapionObject = new YAPIONObject();
        yapionObject.add("\u0000", "a");
        assertThat(yapionObject, isYAPION("{\\u0000(a)}"));
        yapionObject = YAPIONParser.parse(yapionObject.toYAPION(new StringOutput()).getResult());
        assertThat(yapionObject, isYAPION("{\\u0000(a)}"));
    }

    @Test
    public void testCharacters() {
        YAPIONObject yapionObject = YAPIONParser.parse("{\\ !\"#$%&'\\(\\)*+,-./0123456789:;\\<=\\>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ\\[\\\\]^_`abcdefghijklmnopqrstuvwxyz\\{|\\}~( !\"#$%&'\\(\\)*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~)}");
        assertThat(yapionObject, isYAPION("{\\ !\"#$%&'\\(\\)*+,-./0123456789:;\\<=\\>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ\\[\\\\]^_`abcdefghijklmnopqrstuvwxyz\\{|\\}~( !\"#$%&'\\(\\)*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~)}"));
    }

}
