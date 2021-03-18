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

package yapion.api.map;

import org.junit.Test;
import yapion.hierarchy.output.StringOutput;
import yapion.hierarchy.output.StringPrettifiedOutput;
import yapion.hierarchy.types.YAPIONMap;
import yapion.hierarchy.types.YAPIONValue;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class MapOutputTest {

    @Test
    public void testYAPION() {
        assertThat(new YAPIONMap().toYAPION(new StringOutput()).getResult(), is("<>"));
    }

    @Test
    public void testJSON() {
        assertThat(new YAPIONMap().toJSON(new StringOutput()).getResult(), is("{\"@mapping\":[]}"));
    }

    @Test
    public void testLossyJSON() {
        assertThat(new YAPIONMap().toJSONLossy(new StringOutput()).getResult(), is("{\"@mapping\":[]}"));
    }

    @Test
    public void testPrettifiedYAPION() {
        assertThat(new YAPIONMap().toYAPION(new StringPrettifiedOutput()).getResult(), is("<>"));
    }

    @Test
    public void testPrettifiedJSON() {
        assertThat(new YAPIONMap().toJSON(new StringPrettifiedOutput()).getResult(), is("{\n    \"@mapping\":[]\n  }"));
    }

    @Test
    public void testPrettifiedLossyJSON() {
        assertThat(new YAPIONMap().toJSONLossy(new StringPrettifiedOutput()).getResult(), is("{\n    \"@mapping\":[]\n  }"));
    }

    @Test
    public void testPrettifiedYAPIONDepth() {
        assertThat(new YAPIONMap().add(new YAPIONValue<>(""), new YAPIONMap()).toYAPION(new StringPrettifiedOutput()).getResult(), is("<\n  ():<>\n>"));
    }

    @Test
    public void testPrettifiedJSONDepth() {
        assertThat(new YAPIONMap().add(new YAPIONValue<>(""), new YAPIONMap()).toJSON(new StringPrettifiedOutput()).getResult(), is("{\n    \"@mapping\":[\n      \"0:1\"\n    ],\n    \"#0\":\"\",\n    \"#1\":{\n      \"@mapping\":[]\n    }\n  }"));
    }

    @Test
    public void testPrettifiedLossyJSONDepth() {
        assertThat(new YAPIONMap().add(new YAPIONValue<>(""), new YAPIONMap()).toJSONLossy(new StringPrettifiedOutput()).getResult(), is("{\n    \"@mapping\":[\n      \"0:1\"\n    ],\n    \"#0\":\"\",\n    \"#1\":{\n      \"@mapping\":[]\n    }\n  }"));
    }

}
