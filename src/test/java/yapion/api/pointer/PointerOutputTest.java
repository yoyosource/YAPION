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

package yapion.api.pointer;

import org.junit.Test;
import yapion.hierarchy.output.StringOutput;
import yapion.hierarchy.types.YAPIONPointer;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class PointerOutputTest {

    @Test
    public void testYAPION() {
        assertThat(new YAPIONPointer("0000000000000000").toYAPION(new StringOutput()).getResult(), is("->0000000000000000"));
    }

    @Test
    public void testJSON() {
        assertThat(new YAPIONPointer("0000000000000000").toJSON(new StringOutput()).getResult(), is("{\"@pointer\":\"0000000000000000\"}"));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testLossyJSON() {
        new YAPIONPointer("0000000000000000").toJSONLossy(new StringOutput()).getResult();
    }

}
