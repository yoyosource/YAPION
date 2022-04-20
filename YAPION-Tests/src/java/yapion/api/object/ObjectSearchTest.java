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
import yapion.hierarchy.api.ObjectSearch;
import yapion.hierarchy.types.YAPIONObject;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class ObjectSearchTest {

    @Test
    public void getOne() {
        YAPIONObject yapionObject = new YAPIONObject();
        assertThat(yapionObject.getViaPath(""), is(Optional.empty()));
    }

    @Test
    public void getOneValid() {
        YAPIONObject yapionObject = new YAPIONObject();
        yapionObject.add("", new YAPIONObject());
        assertThat(yapionObject.getViaPath(""), is(Optional.of(new ObjectSearch.YAPIONSearchResult(new YAPIONObject()))));
    }

    @Test
    public void getDepth() {
        YAPIONObject yapionObject = new YAPIONObject();
        assertThat(yapionObject.getViaPath("", ""), is(Optional.empty()));
    }

    @Test
    public void getDepthValid() {
        YAPIONObject yapionObject = new YAPIONObject();
        yapionObject.add("", new YAPIONObject().add("", new YAPIONObject()));
        assertThat(yapionObject.getViaPath("", ""), is(Optional.of(new ObjectSearch.YAPIONSearchResult(new YAPIONObject()))));
    }

}
