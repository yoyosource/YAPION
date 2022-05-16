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

package yapion.path.impl.array;

import yapion.hierarchy.api.groups.YAPIONAnyType;
import yapion.hierarchy.types.YAPIONArray;
import yapion.path.PathContext;
import yapion.path.PathElement;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Range implements PathElement {

    public static Range min(int min) {
        return new Range(min, null);
    }

    public static Range max(int max) {
        return new Range(null, max);
    }

    public static Range range(int min, int max) {
        return new Range(min, max);
    }

    private Integer min;
    private Integer max;

    private Range(Integer min, Integer max) {
        this.min = min;
        this.max = max;
    }

    @Override
    public boolean check(YAPIONAnyType yapionAnyType) {
        if (yapionAnyType instanceof YAPIONArray yapionArray) {
            if (min != null && yapionArray.size() < min) {
                return false;
            }
            if (max != null && yapionArray.size() < max) {
                return false;
            }
            return true;
        }
        return false;
    }

    @Override
    public PathContext apply(PathContext pathContext, Optional<PathElement> possibleNextPathElement) {
        return pathContext.map(yapionAnyType -> {
            long time = System.nanoTime();
            try {
                if (!check(yapionAnyType)) {
                    return null;
                }
                if (yapionAnyType instanceof YAPIONArray yapionArray) {
                    int size = yapionArray.size();
                    int min = this.min == null ? 0 : this.min;
                    int max = this.max == null ? size : this.max;
                    List<YAPIONAnyType> elements = new ArrayList<>();
                    for (int i = min; i < max; i++) {
                        elements.add(yapionArray.get(i));
                    }
                    return elements;
                }
                return null;
            } finally {
                pathContext.addTiming(this, System.nanoTime() - time);
            }
        });
    }
}
