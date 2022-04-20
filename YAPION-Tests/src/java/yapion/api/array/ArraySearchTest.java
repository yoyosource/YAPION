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
import yapion.hierarchy.api.ObjectSearch;
import yapion.hierarchy.types.YAPIONArray;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class ArraySearchTest {

    @Test
    public void getOne() {
        YAPIONArray yapionArray = new YAPIONArray();
        assertThat(yapionArray.getViaPath("0"), is(Optional.empty()));
    }

    @Test
    public void getOneValid() {
        YAPIONArray yapionArray = new YAPIONArray();
        yapionArray.add(new YAPIONArray());
        assertThat(yapionArray.getViaPath("0"), is(Optional.of(new ObjectSearch.YAPIONSearchResult(new YAPIONArray()))));
    }

    @Test
    public void getDepth() {
        YAPIONArray yapionArray = new YAPIONArray();
        assertThat(yapionArray.getViaPath("0", "0"), is(Optional.empty()));
    }

    @Test
    public void getDepthValid() {
        YAPIONArray yapionArray = new YAPIONArray();
        yapionArray.add(new YAPIONArray().add(new YAPIONArray()));
        assertThat(yapionArray.getViaPath("0", "0"), is(Optional.of(new ObjectSearch.YAPIONSearchResult(new YAPIONArray()))));
    }

}
