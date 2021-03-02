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
import yapion.hierarchy.types.YAPIONObject;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static yapion.YAPIONAssertion.isYAPION;

public class JSONMapperTest {

    @Test
    public void testMapperByte() {
        YAPIONObject yapionObject = YAPIONParser.mapJSON("{\"byte\":{\"@byte\":0}}");
        assertThat(yapionObject, isYAPION("{byte(0B)}"));
    }

    @Test
    public void testMapperShort() {
        YAPIONObject yapionObject = YAPIONParser.mapJSON("{\"short\":{\"@short\":0}}");
        assertThat(yapionObject, isYAPION("{short(0S)}"));
    }

    @Test
    public void testMapperInt() {
        YAPIONObject yapionObject = YAPIONParser.mapJSON("{\"int\":{\"@int\":0}}");
        assertThat(yapionObject, isYAPION("{int(0)}"));
    }

    @Test
    public void testMapperLong() {
        YAPIONObject yapionObject = YAPIONParser.mapJSON("{\"long\":{\"@long\":0}}");
        assertThat(yapionObject, isYAPION("{long(0L)}"));
    }

    @Test
    public void testMapperBInt() {
        YAPIONObject yapionObject = YAPIONParser.mapJSON("{\"bint\":{\"@bint\":\"0\"}}");
        assertThat(yapionObject, isYAPION("{bint(0BI)}"));
    }

    @Test
    public void testMapperFloat() {
        YAPIONObject yapionObject = YAPIONParser.mapJSON("{\"float\":{\"@float\":0.0}}");
        assertThat(yapionObject, isYAPION("{float(0.0F)}"));
    }

    @Test
    public void testMapperDouble() {
        YAPIONObject yapionObject = YAPIONParser.mapJSON("{\"double\":{\"@double\":0.0}}");
        assertThat(yapionObject, isYAPION("{double(0.0)}"));
    }

    @Test
    public void testMapperBDouble() {
        YAPIONObject yapionObject = YAPIONParser.mapJSON("{\"bdecimal\":{\"@bdecimal\":\"0.0\"}}");
        assertThat(yapionObject, isYAPION("{bdecimal(0.0BD)}"));
    }

    @Test
    public void testMapperChar() {
        YAPIONObject yapionObject = YAPIONParser.mapJSON("{\"char\":{\"@char\":\"0\"}}");
        assertThat(yapionObject, isYAPION("{char('0')}"));
    }

    @Test
    public void testMapperPointer() {
        YAPIONObject yapionObject = YAPIONParser.mapJSON("{\"pointer\":{\"@pointer\":\"0000000000000000\"}}");
        assertThat(yapionObject, isYAPION("{pointer->0000000000000000}"));
    }

    @Test
    public void testMapperMap() {
        YAPIONObject yapionObject = YAPIONParser.mapJSON("{\"map\":{\"@mapping\":[\"0:1\"],\"#0\":\"string1\",\"#1\":\"string2\"}}");
        assertThat(yapionObject, isYAPION("{map<(string1):(string2)>}"));
    }

}
