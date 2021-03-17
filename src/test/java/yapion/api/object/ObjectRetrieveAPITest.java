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
import yapion.hierarchy.types.YAPIONObject;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class ObjectRetrieveAPITest {

    @Test
    public void testGetValueByBoxed() {
        YAPIONObject yapionObject = new YAPIONObject();
        yapionObject.add("test", 0);
        assertThat(yapionObject.getValue("test", Integer.class), notNullValue());
    }

    @Test
    public void testGetValueByPrimitive() {
        YAPIONObject yapionObject = new YAPIONObject();
        yapionObject.add("test", 0);
        assertThat(yapionObject.getValue("test", int.class), notNullValue());
    }

}
