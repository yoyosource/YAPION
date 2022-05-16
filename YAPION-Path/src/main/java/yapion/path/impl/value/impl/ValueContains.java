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

package yapion.path.impl.value.impl;

import yapion.path.PathContext;
import yapion.path.PathElement;
import yapion.path.impl.value.ValueElement;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

public class ValueContains implements ValueElement {

    private Map<Object, Boolean> cache = new LinkedHashMap<>() {
        @Override
        protected boolean removeEldestEntry(Map.Entry<Object, Boolean> eldest) {
            return size() > 100;
        }
    };

    private String value;

    public ValueContains(String value) {
        this.value = value;
    }

    @Override
    public boolean check(Object object) {
        String stringValue = object == null ? "null" : object.toString();
        return stringValue.contains(this.value);
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
