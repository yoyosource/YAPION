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

package yapion.api.array.storage;

import org.junit.Test;
import yapion.exceptions.utils.YAPIONArrayIndexOutOfBoundsException;
import yapion.hierarchy.types.YAPIONArray;
import yapion.hierarchy.types.YAPIONMap;
import yapion.hierarchy.types.YAPIONValue;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

public class ArrayRemoveTest {

    @Test(expected = YAPIONArrayIndexOutOfBoundsException.class)
    public void testRemoveNotSetValue() {
        YAPIONArray yapionArray = new YAPIONArray();
        yapionArray.remove(0);
    }

    @Test
    public void testRemoveValue() {
        YAPIONArray yapionArray = new YAPIONArray();
        yapionArray.add("");
        try {
            yapionArray.remove(0);
        } catch (Exception e) {
            throw new AssertionError(e.getMessage(), e);
        }
    }

    @Test(expected = YAPIONArrayIndexOutOfBoundsException.class)
    public void testRemoveAndGetNotSetValue() {
        YAPIONArray yapionArray = new YAPIONArray();
        assertThat(yapionArray.removeAndGet(0), nullValue());
    }

    @Test
    public void testRemoveAndGetValue() {
        YAPIONArray yapionArray = new YAPIONArray();
        yapionArray.add("");
        assertThat(yapionArray.removeAndGet(0), is(new YAPIONValue<>("")));
    }

}
