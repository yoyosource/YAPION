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

package yapion.api.object;

import org.junit.Test;
import yapion.hierarchy.output.StringOutput;
import yapion.hierarchy.types.YAPIONObject;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class ObjectOutputTest {

    @Test
    public void testYAPION() {
        assertThat(new YAPIONObject().toYAPION(new StringOutput()).getResult(), is("{}"));
    }

    @Test
    public void testJSON() {
        assertThat(new YAPIONObject().toJSON(new StringOutput()).getResult(), is("{}"));
    }

    @Test
    public void testLossyJSON() {
        assertThat(new YAPIONObject().toJSONLossy(new StringOutput()).getResult(), is("{}"));
    }

    @Test
    public void testPrettifiedYAPION() {
        assertThat(new YAPIONObject().toYAPION(new StringOutput(true)).getResult(), is("{}"));
    }

    @Test
    public void testPrettifiedJSON() {
        assertThat(new YAPIONObject().toJSON(new StringOutput(true)).getResult(), is("{}"));
    }

    @Test
    public void testPrettifiedLossyJSON() {
        assertThat(new YAPIONObject().toJSONLossy(new StringOutput(true)).getResult(), is("{}"));
    }

    @Test
    public void testPrettifiedYAPIONDepth() {
        assertThat(new YAPIONObject().add("", new YAPIONObject()).toYAPION(new StringOutput(true)).getResult(), is("{\n  {}\n}"));
    }

    @Test
    public void testPrettifiedJSONDepth() {
        assertThat(new YAPIONObject().add("", new YAPIONObject()).toJSON(new StringOutput(true)).getResult(), is("{\n  \"\":{}\n}"));
    }

    @Test
    public void testPrettifiedLossyJSONDepth() {
        assertThat(new YAPIONObject().add("", new YAPIONObject()).toJSONLossy(new StringOutput(true)).getResult(), is("{\n  \"\":{}\n}"));
    }

}
