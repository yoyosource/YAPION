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

package yapion.path.impl;

import yapion.hierarchy.api.groups.YAPIONAnyType;
import yapion.path.PathContext;
import yapion.path.PathElement;

import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Optional;

public class IdentityDistinctElements implements PathElement {

    @Override
    public PathContext apply(PathContext pathContext, Optional<PathElement> possibleNextPathElement) {
        Map<YAPIONAnyType, Void> set = new IdentityHashMap<>();
        return pathContext.removeIf(yapionAnyType -> {
            long time = System.nanoTime();
            try {
                if (set.containsKey(yapionAnyType)) return true;
                set.put(yapionAnyType, null);
                return false;
            } finally {
                pathContext.addTiming(this, System.nanoTime() - time);
            }
        });
    }
}
