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

import yapion.pathn.PathContext;
import yapion.pathn.PathElement;
import yapion.pathn.impl.value.ValueElement;

import java.util.*;

public class ValueSet<T> implements ValueElement {

    private Set<T> values = new HashSet<>();

    public ValueSet(T... values) {
        this.values.addAll(Arrays.asList(values));
    }

    public ValueSet(List<T> values) {
        this.values = new HashSet<>(values);
    }

    @Override
    public boolean check(Object object) {
        return values.contains((T) object);
    }

    @Override
    public PathContext apply(PathContext pathContext, Optional<PathElement> possibleNextPathElement) {
        return pathContext.retainIf(yapionAnyType -> {
            long time = System.nanoTime();
            try {
                return check(yapionAnyType);
            } finally {
                pathContext.addTiming(this, System.nanoTime() - time);
            }
        });
    }
}
