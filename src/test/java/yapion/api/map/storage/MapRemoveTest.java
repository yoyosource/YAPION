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

package yapion.api.map.storage;

import org.junit.Test;
import yapion.hierarchy.types.YAPIONMap;
import yapion.hierarchy.types.YAPIONValue;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

public class MapRemoveTest {

    @Test
    public void testRemoveNotSetValue() {
        YAPIONMap yapionMap = new YAPIONMap();
        try {
            yapionMap.remove("");
        } catch (Exception e) {
            throw new AssertionError(e.getMessage(), e);
        }
    }

    @Test
    public void testRemoveValue() {
        YAPIONMap yapionMap = new YAPIONMap();
        yapionMap.add("", "");
        try {
            yapionMap.remove("");
        } catch (Exception e) {
            throw new AssertionError(e.getMessage(), e);
        }
    }

    @Test
    public void testRemoveAndGetNotSetValue() {
        YAPIONMap yapionMap = new YAPIONMap();
        assertThat(yapionMap.removeAndGet(""), nullValue());
    }

    @Test
    public void testRemoveAndGetValue() {
        YAPIONMap yapionMap = new YAPIONMap();
        yapionMap.add("", "");
        assertThat(yapionMap.removeAndGet(""), is(new YAPIONValue<>("")));
    }

}
