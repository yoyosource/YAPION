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

package yapion.pathn.impl.value;

import yapion.hierarchy.api.groups.YAPIONAnyType;
import yapion.hierarchy.types.YAPIONValue;
import yapion.pathn.PathContext;
import yapion.pathn.PathElement;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class ValueAnd implements ValueElement {

    private ValueElement[] valueElements;

    public ValueAnd(ValueElement... valueElements) {
        this.valueElements = valueElements;
    }

    public ValueAnd(List<ValueElement> valueElements) {
        this.valueElements = valueElements.toArray(new ValueElement[valueElements.size()]);
    }

    @Override
    public boolean check(YAPIONAnyType yapionAnyType) {
        if (!(yapionAnyType instanceof YAPIONValue yapionValue)) {
            return true;
        }
        for (ValueElement valueElement : valueElements) {
            if (!valueElement.check(yapionAnyType)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public PathContext apply(PathContext pathContext, Optional<PathElement> possibleNextPathElement) {
        return pathContext.removeIf(yapionAnyType -> {
            if (!(yapionAnyType instanceof YAPIONValue yapionValue)) {
                return false;
            }
            PathContext innerPathContext = pathContext.with(Arrays.asList(yapionValue));
            for (ValueElement valueElement : valueElements) {
                innerPathContext = valueElement.apply(innerPathContext, possibleNextPathElement);
            }
            return innerPathContext.eval().isEmpty();
        });
    }
}
