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
import yapion.hierarchy.types.YAPIONMap;
import yapion.hierarchy.types.YAPIONType;
import yapion.utils.ReferenceIDUtils;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class MapTypeTest {

    @Test
    public void testTypeValue() {
        assertThat(new YAPIONMap().getType(), is(YAPIONType.MAP));
    }

    @Test
    public void testReferenceValue() {
        assertThat(new YAPIONMap().referenceValue(), is(2978161325094671632L));
    }

    @Test
    public void testReferenceValueSpecificFunction() {
        assertThat(new YAPIONMap().referenceValue(ReferenceIDUtils.REFERENCE_FUNCTION), is(2978161325094671632L));
    }

}
