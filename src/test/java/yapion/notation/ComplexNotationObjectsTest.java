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
import yapion.hierarchy.types.YAPIONValue;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static yapion.YAPIONAssertion.isYAPION;
// import static yapion.annotation.AnnotationTestObjects.*;

public class ComplexNotationObjectsTest {

    @Test
    public void testObjectObjectYAPION() {
        assertThat(new YAPIONObject().add("object", new YAPIONObject()), isYAPION("{object{}}"));
    }

    @Test
    public void testObjectObjectJSON() {
        assertThat(new YAPIONObject().add("object", new YAPIONObject()).toJSONLossy(new StringOutput()).getResult(), is("{\"object\": {}}"));
    }

    @Test
    public void testObjectArrayYAPION() {
        assertThat(new YAPIONObject().add("array", new YAPIONArray()), isYAPION("{array[]}"));
    }

    @Test
    public void testObjectArrayJSON() {
        assertThat(new YAPIONObject().add("array", new YAPIONArray()).toJSONLossy(new StringOutput()).getResult(), is("{\"array\": []}"));
    }

    @Test
    public void testObjectMapYAPION() {
        assertThat(new YAPIONObject().add("map", new YAPIONMap()), isYAPION("{map<>}"));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testObjectMapJSON() {
        new YAPIONObject().add("map", new YAPIONMap()).toJSONLossy(new StringOutput()).getResult();
    }

    @Test
    public void testArrayObjectYAPION() {
        assertThat(new YAPIONArray().add(new YAPIONObject()), isYAPION("[{}]"));
    }

    @Test
    public void testArrayObjectJSON() {
        assertThat(new YAPIONArray().add(new YAPIONObject()).toJSONLossy(new StringOutput()).getResult(), is("[{}]"));
    }

    @Test
    public void testArrayArrayYAPION() {
        assertThat(new YAPIONArray().add(new YAPIONArray()), isYAPION("[[]]"));
    }

    @Test
    public void testArrayArrayJSON() {
        assertThat(new YAPIONArray().add(new YAPIONArray()).toJSONLossy(new StringOutput()).getResult(), is("[[]]"));
    }

    @Test
    public void testArrayMapYAPION() {
        assertThat(new YAPIONArray().add(new YAPIONMap()), isYAPION("[<>]"));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testArrayMapJSON() {
        new YAPIONArray().add(new YAPIONMap()).toJSONLossy(new StringOutput()).getResult();
    }

    @Test
    public void testMapObjectYAPION() {
        assertThat(new YAPIONMap().add(new YAPIONValue<>("object"), new YAPIONObject()), isYAPION("<(object):{}>"));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testMapObjectJSON() {
        new YAPIONMap().add(new YAPIONValue<>("object"), new YAPIONObject()).toJSONLossy(new StringOutput()).getResult();
    }

    @Test
    public void testMapArrayYAPION() {
        assertThat(new YAPIONMap().add(new YAPIONValue<>("array"), new YAPIONArray()), isYAPION("<(array):[]>"));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testMapArrayJSON() {
        new YAPIONMap().add(new YAPIONValue<>("array"), new YAPIONArray()).toJSONLossy(new StringOutput()).getResult();
    }

    @Test
    public void testMapMapYAPION() {
        assertThat(new YAPIONMap().add(new YAPIONValue<>("map"), new YAPIONMap()), isYAPION("<(map):<>>"));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testMapMapJSON() {
        new YAPIONMap().add(new YAPIONValue<>("map"), new YAPIONMap()).toJSONLossy(new StringOutput()).getResult();
    }

}
