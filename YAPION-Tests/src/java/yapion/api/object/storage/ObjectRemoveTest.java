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

package yapion.api.object.storage;

import org.junit.Test;
import yapion.hierarchy.types.YAPIONObject;
import yapion.hierarchy.types.YAPIONValue;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

public class ObjectRemoveTest {

    @Test
    public void testRemoveNotSetValue() {
        YAPIONObject yapionObject = new YAPIONObject();
        try {
            yapionObject.remove("");
        } catch (Exception e) {
            throw new AssertionError(e.getMessage(), e);
        }
    }

    @Test
    public void testRemoveValue() {
        YAPIONObject yapionObject = new YAPIONObject();
        yapionObject.add("", "");
        try {
            yapionObject.remove("");
        } catch (Exception e) {
            throw new AssertionError(e.getMessage(), e);
        }
    }

    @Test
    public void testRemoveAndGetNotSetValue() {
        YAPIONObject yapionObject = new YAPIONObject();
        assertThat(yapionObject.removeAndGet(""), nullValue());
    }

    @Test
    public void testRemoveAndGetValue() {
        YAPIONObject yapionObject = new YAPIONObject();
        yapionObject.add("", "");
        assertThat(yapionObject.removeAndGet(""), is(new YAPIONValue<>("")));
    }

}
