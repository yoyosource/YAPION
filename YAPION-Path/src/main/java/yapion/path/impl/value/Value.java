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

package yapion.path.impl.value;

import yapion.hierarchy.api.groups.YAPIONAnyType;
import yapion.path.PathContext;
import yapion.path.PathElement;

import java.util.List;
import java.util.Optional;

public class Value implements PathElement {

    private ValueAnd valueAnd;

    public Value(ValueElement... valueElements) {
        this.valueAnd = new ValueAnd(valueElements);
    }

    public Value(List<ValueElement> valueElements) {
        this.valueAnd = new ValueAnd(valueElements);
    }

    @Override
    public boolean check(YAPIONAnyType yapionAnyType) {
        return valueAnd.check(yapionAnyType);
    }

    @Override
    public PathContext apply(PathContext pathContext, Optional<PathElement> possibleNextPathElement) {
        return valueAnd.apply(pathContext, possibleNextPathElement);
    }
}
