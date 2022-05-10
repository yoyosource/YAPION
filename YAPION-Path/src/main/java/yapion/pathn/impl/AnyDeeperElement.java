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

package yapion.pathn.impl;

import yapion.hierarchy.api.groups.YAPIONAnyType;
import yapion.hierarchy.api.groups.YAPIONDataType;
import yapion.pathn.PathContext;
import yapion.pathn.PathElement;
import yapion.utils.YAPIONTreeIterator;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AnyDeeperElement implements PathElement {

    @Override
    public PathContext apply(PathContext pathContext, Optional<PathElement> possibleNextPathElement) {
        if (!possibleNextPathElement.isPresent()) {
            return pathContext.empty();
        }
        return pathContext.map(yapionAnyType -> {
            long time = System.nanoTime();
            try {
                List<YAPIONAnyType> result = new ArrayList<>();
                if (yapionAnyType instanceof YAPIONDataType yapionDataType) {
                    new YAPIONTreeIterator(yapionDataType).forEachRemaining(element -> {
                        if (possibleNextPathElement.get().check(element)) {
                            result.add(element);
                        }
                    });
                }
                return result;
            } finally {
                pathContext.addTiming(this, System.nanoTime() - time);
            }
        });
    }
}
