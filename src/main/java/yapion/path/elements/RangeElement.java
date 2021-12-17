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

package yapion.path.elements;

import yapion.hierarchy.api.groups.YAPIONAnyType;
import yapion.hierarchy.types.YAPIONArray;
import yapion.path.PathElement;

import java.util.ArrayList;
import java.util.List;

public class RangeElement implements PathElement {

    public static RangeElement min(int min) {
        return new RangeElement(min, Integer.MAX_VALUE);
    }

    public static RangeElement max(int max) {
        return new RangeElement(Integer.MIN_VALUE, max);
    }

    public static RangeElement range(int min, int max) {
        return new RangeElement(min, max);
    }

    private final int min;
    private final int max;

    private RangeElement(int min, int max) {
        this.min = min;
        this.max = max;
    }

    @Override
    public boolean check(YAPIONAnyType current) {
        if (current instanceof YAPIONArray yapionArray) {
            return min < yapionArray.size() && max > 0;
        }
        return false;
    }

    @Override
    public List<YAPIONAnyType> apply(List<YAPIONAnyType> current, PathElement possibleNext) {
        List<YAPIONAnyType> result = new ArrayList<>();
        int elementMin = Math.max(0, min);
        for (YAPIONAnyType element : current) {
            if (element instanceof YAPIONArray yapionArray) {
                int elementMax = Math.min(yapionArray.size(), max);
                for (int i = elementMin; i < elementMax; i++) {
                    result.add(yapionArray.getAny(i));
                }
            }
        }
        return result;
    }
}
