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

package yapion.notation;

import org.junit.Test;
import yapion.hierarchy.output.StringOutput;
import yapion.hierarchy.types.YAPIONArray;
import yapion.hierarchy.types.YAPIONMap;
import yapion.hierarchy.types.YAPIONObject;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static yapion.YAPIONAssertion.isYAPION;
// import static yapion.annotation.AnnotationTestObjects.*;

public class NotationObjectsTest {

    @Test
    public void testObjectYAPION() {
        assertThat(new YAPIONObject(), isYAPION("{}"));
    }

    @Test
    public void testObjectJSON() {
        assertThat(new YAPIONObject().toJSONLossy(new StringOutput()).getResult(), is("{}"));
    }

    @Test
    public void testBackslash() {
        YAPIONObject yapionObject = new YAPIONObject();
        yapionObject.add("a", "\\\\");
        assertThat(yapionObject, isYAPION("{a(\\\\)}"));
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
        assertThat(yapionObject, isYAPION("{\\ !\"#$%&'\\(\\)*+,-./0123456789:;\\<=\\>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ\\[\\\\]^_`abcdefghijklmnopqrstuvwxyz\\{|\\}~( !\"#$%&'\\(\\)*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~)}"));
    }

    @Test
    public void testUmlauts() {
        YAPIONObject yapionObject = new YAPIONObject();
        yapionObject.add("äöü", "äöü");
        assertThat(yapionObject, isYAPION("{äöü(äöü)}"));
    }

    @Test
    public void testOutsideLatin1() {
        YAPIONObject yapionObject = new YAPIONObject();
        yapionObject.add("\u0000\u001F\u007E\u007F\u00A0\u00A1\u0100", "\u0000\u001F\u007E\u007F\u00A0\u00A1\u0100");
        assertThat(yapionObject, isYAPION("{\\u0000\\u001F~\\u007F\\u00A0¡\\u0100(\\u0000\\u001F~\\u007F\\u00A0¡\\u0100)}"));
    }

    @Test
    public void testMapYAPION() {
        assertThat(new YAPIONMap(), isYAPION("<>"));
    }

    @Test
    public void testMapJSON() {
        assertThat(new YAPIONMap().toJSONLossy(new StringOutput()).getResult(), is("{\"@mapping\":[]}"));
    }

    @Test
    public void testArrayYAPION() {
        assertThat(new YAPIONArray(), isYAPION("[]"));
    }

    @Test
    public void testArrayJSON() {
        assertThat(new YAPIONArray().toJSONLossy(new StringOutput()).getResult(), is("[]"));
    }

    @Test
    public void testPrettifiedYAPIONArrayOptimizationWithLastValue() {
        YAPIONArray yapionArray = new YAPIONArray();
        yapionArray.add(new YAPIONObject());
        assertThat(yapionArray.toYAPION(new StringOutput(true)).getResult(), is("[\n  {}\n]"));
    }

}
