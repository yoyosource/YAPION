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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class JSONParserTest {

    @Test
    public void testParseObject() {
        YAPIONObject yapionObject = YAPIONParser.parse("{}");
        assertThat(yapionObject.toYAPION(new StringOutput()).getResult(), is("{}"));
    }

    @Test
    public void testParseObjectValue() {
        YAPIONObject yapionObject = YAPIONParser.parse("{\"test\":{}}");
        assertThat(yapionObject.toYAPION(new StringOutput()).getResult(), is("{test{}}"));
    }

    @Test
    public void testParseArray() {
        YAPIONObject yapionObject = YAPIONParser.parse("{\"test\":[]}");
        assertThat(yapionObject.toYAPION(new StringOutput()).getResult(), is("{test[]}"));
    }

    @Test
    public void testParseMap() {
        YAPIONObject yapionObject = YAPIONParser.parse("{\"test\":{\"@mapping\":[]}}");
        assertThat(yapionObject.toYAPION(new StringOutput()).getResult(), is("{test{@mapping[]}}"));
    }

    @Test(expected = YAPIONParserException.class)
    public void testJSONStartEnd() {
        YAPIONParser.parse("\"hello\": \"\"");
    }

    @Test
    public void testJSONValueWithComa() {
        assertThat(YAPIONParser.parse("{\n" +
                "\t\"text\": \"ipse amor, admisso sequitur vestigia passu.\",\n" +
                "\t\"author\": \"Ovid\"\n" +
                "}").toYAPION(new StringOutput()).getResult(), is("{text(ipse amor, admisso sequitur vestigia passu.)author(Ovid)}"));
    }

    @Test
    public void testJSONParseWithEscapes() {
        assertThat(YAPIONParser.parse("{\"test\":\"\\\"Fer pater\\\", inquit, \\\"opem! tellus\\\", ait, \\\"hisce vel istam,\"}").toYAPION(new StringOutput()).getResult(), is("{test(\"Fer pater\", inquit, \"opem! tellus\", ait, \"hisce vel istam,)}"));
    }

    @Test
    public void testNullValue() {
        assertThat(YAPIONParser.parse("{\n" +
                "\t\"text\": true\n" +
                "}").toYAPION(new StringOutput()).getResult(), is("{text(true)}"));
    }

    @Test
    public void testNegativeNumbers() {
        assertThat(YAPIONParser.parse("{\"test\":-1}").toYAPION(), is("{test(-1)}"));
    }

}
