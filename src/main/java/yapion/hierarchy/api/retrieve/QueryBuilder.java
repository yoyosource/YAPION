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

package yapion.hierarchy.api.retrieve;

import yapion.hierarchy.types.YAPIONArray;
import yapion.hierarchy.types.YAPIONMap;
import yapion.hierarchy.types.YAPIONObject;

public class QueryBuilder {

    private QueryBuilder() {
        throw new IllegalStateException("Utility Class");
    }

    public static Query<YAPIONObject, YAPIONObject> ObjectQuery() {
        return input -> input;
    }

    public static Query<YAPIONMap, YAPIONMap> MapQuery() {
        return input -> input;
    }

    public static Query<YAPIONArray, YAPIONArray> ArrayQuery() {
        return input -> input;
    }

}
