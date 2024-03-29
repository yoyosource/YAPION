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
import yapion.hierarchy.api.ObjectSearch;
import yapion.hierarchy.types.YAPIONMap;
import yapion.hierarchy.types.YAPIONValue;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class MapSearchTest {

    @Test
    public void getOne() {
        YAPIONMap yapionMap = new YAPIONMap();
        assertThat(yapionMap.getViaPath(""), is(Optional.empty()));
    }

    @Test
    public void getOneValid() {
        YAPIONMap yapionMap = new YAPIONMap();
        yapionMap.add("", new YAPIONMap());
        assertThat(yapionMap.getViaPath("()"), is(Optional.of(new ObjectSearch.YAPIONSearchResult(new YAPIONMap()))));
    }

    @Test
    public void getDepth() {
        YAPIONMap yapionMap = new YAPIONMap();
        assertThat(yapionMap.getViaPath("", ""), is(Optional.empty()));
    }

    @Test
    public void getDepthValid() {
        YAPIONMap yapionMap = new YAPIONMap();
        yapionMap.add("", new YAPIONMap().add(new YAPIONValue<>(""), new YAPIONMap()));
        assertThat(yapionMap.getViaPath("()", "()"), is(Optional.of(new ObjectSearch.YAPIONSearchResult(new YAPIONMap()))));
    }

}
