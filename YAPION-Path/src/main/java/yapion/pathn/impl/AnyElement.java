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

import yapion.hierarchy.types.YAPIONArray;
import yapion.hierarchy.types.YAPIONObject;
import yapion.pathn.PathContext;
import yapion.pathn.PathElement;

import java.util.Map;
import java.util.Optional;

public class AnyElement implements PathElement {

    @Override
    public PathContext apply(PathContext pathContext, Optional<PathElement> possibleNextPathElement) {
        return pathContext.streamMap(yapionAnyType -> {
            long time = System.nanoTime();
            try {
                if (yapionAnyType instanceof YAPIONObject yapionObject) {
                    return yapionObject.unsafe().values().stream();
                } else if (yapionAnyType instanceof YAPIONArray yapionArray) {
                    return yapionArray.unsafe().stream();
                }
                return null;
            } finally {
                pathContext.addTiming(this, System.nanoTime() - time);
            }
        });
    }
}
