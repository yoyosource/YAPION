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

package yapion.api.array;

import org.junit.Test;
import yapion.hierarchy.types.YAPIONArray;
import yapion.hierarchy.types.YAPIONObject;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class ArrayPathTest {

    @Test
    public void testGetPathByValue() {
        YAPIONArray yapionArray = new YAPIONArray();
        YAPIONArray yapionArray1 = new YAPIONArray();
        yapionArray.add(yapionArray1);
        assertThat(yapionArray.getPath(yapionArray1), is("0"));
    }

    @Test
    public void testGetPath() {
        YAPIONArray yapionArray = new YAPIONArray();
        YAPIONArray yapionArray1 = new YAPIONArray();
        yapionArray.add(yapionArray1);
        assertThat(yapionArray1.getPath().getPath(), is(new String[]{"0"}));
    }

    @Test
    public void testGetDepth() {
        YAPIONArray yapionArray = new YAPIONArray();
        YAPIONArray yapionArray1 = new YAPIONArray();
        yapionArray.add(yapionArray1);
        assertThat(yapionArray1.getDepth(), is(1));
    }

}
