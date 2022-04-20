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

package yapion.api.pointer;

import org.junit.Test;
import yapion.hierarchy.api.ObjectSearch;
import yapion.hierarchy.types.YAPIONObject;
import yapion.hierarchy.types.YAPIONPointer;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class PointerSearchTest {

    @Test
    public void getPointerReference() {
        YAPIONObject yapionObject = new YAPIONObject();
        yapionObject.putOrPointerAndGetItself("", yapionObject);
        YAPIONPointer yapionPointer = yapionObject.getPointer("");
        assertThat(yapionPointer.getViaPath("@reference"), is(Optional.of(new ObjectSearch.YAPIONSearchResult(yapionObject))));
    }

}
