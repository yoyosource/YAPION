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

package yapion.pathn.impl.value.impl;

import yapion.hierarchy.api.groups.YAPIONAnyType;
import yapion.hierarchy.types.YAPIONValue;
import yapion.pathn.PathContext;
import yapion.pathn.PathElement;
import yapion.pathn.impl.value.ValueElement;

import java.util.Optional;

public class ValueEquals<T> implements ValueElement {

    private T value;

    public ValueEquals(T value) {
        this.value = value;
    }

    @Override
    public boolean check(YAPIONAnyType yapionAnyType) {
        if (!(yapionAnyType instanceof YAPIONValue yapionValue)) {
            return true;
        }
        if (yapionValue.get() == null && value == null) {
            return true;
        }
        if (yapionValue.get() == null) {
            return false;
        }
        if (value == null) {
            return false;
        }
        return yapionValue.get().equals(value);
    }

    @Override
    public PathContext apply(PathContext pathContext, Optional<PathElement> possibleNextPathElement) {
        return pathContext.retainIf(this::check);
    }
}