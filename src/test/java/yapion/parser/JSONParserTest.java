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

    @Test
    public void testJSONStartEnd() {
        assertThat(YAPIONParser.parse("\"hello\": \"\"").toYAPION(new StringOutput()).getResult(), is("{hello()}"));
    }

}
