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

package yapion.utils;

import org.junit.Test;
import yapion.utils.UtilsTestObjects.UtilModifierTest;

import java.lang.reflect.Field;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class ModifierTest {

    @Test
    public void testModifiers() {
        List<Field> fieldList = ReflectionsUtils.getFields(UtilModifierTest.class);
        boolean[] booleans = new boolean[fieldList.size()];
        for (int i = 0; i < fieldList.size(); i++) {
            booleans[i] = ModifierUtils.removed(fieldList.get(i));
        }
        assertThat(booleans, is(new boolean[]{true, true, false}));
    }

}
