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
import yapion.hierarchy.types.*;

import java.io.InputStream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static yapion.YAPIONAssertion.isYAPION;

public class YAPIONParserTest {

    public static void main(String[] args) {
        System.out.println(YAPIONParser.parse("{test(), \"test2\":{}}"));
        System.out.println(YAPIONParser.parse("{\"contributor\":[{\"name\":\"yoyosource\",\"owner\":true},{\"name\":\"chaoscaot444\",\"owner\":\"false\"}]}"));
        System.out.println(YAPIONParser.parse("{\n" +
                "  \"id\": 1,\n" +
                "  \"first_name\": \"Jeanette\",\n" +
                "  \"last_name\": \"Penddreth\",\n" +
                "  \"email\": \"jpenddreth0@census.gov\",\n" +
                "  \"gender\": \"Female\",\n" +
                "  \"ip_address\": \"26.58.193.2\"\n" +
                "}"));
    }

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
        System.out.println(YAPIONParser.parse("{]"));
    }

    @Test
    public void testInitialType() {
        assertThat(YAPIONParser.parse("[]"), isYAPION("{[]}"));
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

    @Test
    public void testStringEscape() {
        YAPIONObject yapionObject = YAPIONParser.parse("{(\"\"\")}");
        assertThat(yapionObject.getPlainValue(""), is("\""));
        assertThat(yapionObject, isYAPION("{(\"\"\")}"));
    }

    @Test
    public void testStringStartsAndEndsWith() {
        YAPIONObject yapionObject = YAPIONParser.parse("{(\"\"\"\")}");
        assertThat(yapionObject.getPlainValue(""), is("\"\""));
        assertThat(yapionObject, isYAPION("{(\"\"\"\")}"));
    }

    @Test
    public void testStringStartsWith() {
        YAPIONObject yapionObject = YAPIONParser.parse("{(\"\"Hello world\")}");
        assertThat(yapionObject.getPlainValue(""), is("\"Hello world"));
        assertThat(yapionObject, isYAPION("{(\"\"Hello world\")}"));
    }

    @Test
    public void testStringEndsWith() {
        YAPIONObject yapionObject = YAPIONParser.parse("{(\"Hello world\"\")}");
        assertThat(yapionObject.getPlainValue(""), is("Hello world\""));
        assertThat(yapionObject, isYAPION("{(\"Hello world\"\")}"));
    }

    @Test
    public void testArrayStringWithPointer() {
        YAPIONObject yapionObject = YAPIONParser.parse("{[a->b,b->c]}");
        assertThat(yapionObject.getArray("").getYAPIONAnyType(0), instanceOf(YAPIONValue.class));
        assertThat(yapionObject.getArray("").getYAPIONAnyType(1), instanceOf(YAPIONValue.class));
        assertThat(yapionObject, isYAPION("{[a->b,b->c]}"));
    }

    @Test
    public void testArrayWithAnyType() {
        YAPIONObject yapionObject = YAPIONParser.parse("{[{},[],(),->0000000000000000,<>]}");
        assertThat(yapionObject.getArray("").getYAPIONAnyType(0), instanceOf(YAPIONObject.class));
        assertThat(yapionObject.getArray("").getYAPIONAnyType(1), instanceOf(YAPIONArray.class));
        assertThat(yapionObject.getArray("").getYAPIONAnyType(2), instanceOf(YAPIONValue.class));
        assertThat(yapionObject.getArray("").getYAPIONAnyType(3), instanceOf(YAPIONPointer.class));
        assertThat(yapionObject.getArray("").getYAPIONAnyType(4), instanceOf(YAPIONMap.class));
        assertThat(yapionObject, isYAPION("{[{},[],(),->0000000000000000,<>]}"));
    }

    @Test
    public void testFileInputStream() {
        YAPIONParser.parse(YAPIONParserTest.class.getResourceAsStream("/test.yapion"));
    }

    @Test
    public void testYAPIONStartEnd() {
        assertThat(YAPIONParser.parse("hello()").toYAPION(new StringOutput()).getResult(), is("{hello()}"));
    }

    @Test
    public void testArraySeparatorSupport() {
        YAPIONObject yapionObject = new YAPIONObject();
        YAPIONArray yapionArray = new YAPIONArray();
        yapionObject.add("", yapionArray);
        yapionArray.add("Hello, World");
        assertThat(yapionObject, is(YAPIONParser.parse(yapionObject.toString())));
    }

    @Test
    public void testArraySupportWithoutSeparator() {
        YAPIONObject yapionObject = YAPIONParser.parse("{[{}{}]}");
        assertThat(yapionObject, isYAPION("{[{},{}]}"));
    }

    @Test
    public void testArrayStringWithoutSeparator() {
        YAPIONObject yapionObject = YAPIONParser.parse("{[{}{}HelloWorld,->0000000000000000]}");
        assertThat(yapionObject.getArray("").getYAPIONAnyType(2), instanceOf(YAPIONValue.class));
        assertThat(yapionObject.getArray("").getYAPIONAnyType(3), instanceOf(YAPIONPointer.class));
        assertThat(yapionObject.toString(), is("{[{},{},HelloWorld,->0000000000000000]}"));
    }

    @Test
    public void testArrayStringPointerStartInValue() {
        YAPIONObject yapionObject = YAPIONParser.parse("{[\\->0000000000000000]}");
        assertThat(yapionObject.getArray("").getYAPIONAnyType(0), instanceOf(YAPIONValue.class));
        assertThat(yapionObject.toString(), is("{[\\->0000000000000000]}"));
    }

    @Test
    public void testArrayWithStringValue() {
        YAPIONObject yapionObject = new YAPIONObject().add("", new YAPIONArray().add(new YAPIONValue<>("{Hello World}")));
        yapionObject = YAPIONParser.parse(yapionObject.toYAPION(new StringOutput()).getResult());
        assertThat(yapionObject.getArray("").getValue(0), notNullValue());
        assertThat(yapionObject.getArray("").getValue(0), isYAPION(new YAPIONValue<>("{Hello World}")));
    }

    @Test
    public void testArrayStringWhitespaceInValue() {
        YAPIONObject yapionObject = YAPIONParser.parse("{[ Hugo Hello World]}");
        assertThat(yapionObject.getArray("").getYAPIONAnyType(0), instanceOf(YAPIONValue.class));
        assertThat(yapionObject.getArray("").getValue(0).get(), instanceOf(String.class));
        assertThat(yapionObject.getArray("").getValue(0).get(), is("Hugo Hello World"));
    }

    @Test
    public void testNewLine() {
        YAPIONObject yapionObject = YAPIONParser.parse("{\r   n()}");
        assertThat(yapionObject.getPlainValue("n"), notNullValue());

        yapionObject = YAPIONParser.parse("{[\r   Hello World]}");
        assertThat(yapionObject.getArray("").getValue(0).get(), is("Hello World"));
    }

    @Test
    public void testEscapedNewLine() {
        YAPIONObject yapionObject = YAPIONParser.parse("{\\\r   n()}");
        assertThat(yapionObject.getPlainValue("\r   n"), notNullValue());

        yapionObject = YAPIONParser.parse("{[\\\r   Hello World]}");
        assertThat(yapionObject.getArray("").getValue(0).get(), is("\\\r   Hello World"));
    }

    @Test
    public void testNormalEscape() {
        YAPIONObject yapionObject = YAPIONParser.parse("{\\r\\n\\t()}");
        assertThat(yapionObject.getPlainValue("\r\n\t"), notNullValue());
        assertThat(yapionObject.toYAPION(new StringOutput()).getResult(), is("{\\r\\n\\t()}"));
    }

    @Test
    public void testComaKey() {
        assertThat(YAPIONParser.parse("{\\,hello()}").toYAPION(new StringOutput()).getResult(), is("{\\,hello()}"));
    }

    @Test
    public void testComaSeparator() {
        assertThat(YAPIONParser.parse("{,hello()}").toYAPION(new StringOutput()).getResult(), is("{hello()}"));
    }

}
