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
import yapion.hierarchy.types.YAPIONObject;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class RecursionTest {

    @Test
    public void checkNoneRecursion() {
        YAPIONObject root = new YAPIONObject();
        YAPIONObject toTest = new YAPIONObject();
        RecursionUtils.RecursionResult recursionResult = RecursionUtils.checkRecursion(root, toTest);
        assertThat(recursionResult.getRecursionType(), is(RecursionUtils.RecursionType.NONE));
    }

    @Test
    public void checkDirectRecursion() {
        YAPIONObject root = new YAPIONObject();
        RecursionUtils.RecursionResult recursionResult = RecursionUtils.checkRecursion(root, root);
        assertThat(recursionResult.getRecursionType(), is(RecursionUtils.RecursionType.DIRECT));
    }

    @Test
    public void checkBackReferenceRecursion() {
        YAPIONObject root = new YAPIONObject();
        YAPIONObject toTest = new YAPIONObject();
        root.add("test", toTest);
        RecursionUtils.RecursionResult recursionResult = RecursionUtils.checkRecursion(root, toTest);
        assertThat(recursionResult.getRecursionType(), is(RecursionUtils.RecursionType.BACK_REFERENCE));
    }

}
