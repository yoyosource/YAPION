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
import yapion.hierarchy.types.YAPIONArray;
import yapion.hierarchy.types.YAPIONObject;
import yapion.pathn.PathContext;
import yapion.pathn.PathElement;
import yapion.pathn.impl.value.ValueElement;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public class KeySelection implements PathElement {

    private ValueElement valueElement;

    public KeySelection(ValueElement valueElement) {
        this.valueElement = valueElement;
    }

    @Override
    public boolean check(YAPIONAnyType yapionAnyType) {
        return true;
    }

    @Override
    public PathContext apply(PathContext pathContext, Optional<PathElement> possibleNextPathElement) {
        return pathContext.singleMap(yapionAnyType -> {
            long time = System.nanoTime();
            try {
                YAPIONObject result = new YAPIONObject();
                if (yapionAnyType instanceof YAPIONArray yapionArray) {
                    AtomicInteger atomicInteger = new AtomicInteger();
                    for (YAPIONAnyType element : yapionArray.getAllValues()) {
                        int i = atomicInteger.getAndIncrement();
                        if (valueElement.check(i)) {
                            result.unsafe().put(i + "", element);
                        }
                    }
                } else if (yapionAnyType instanceof YAPIONObject yapionObject) {
                    for (String key : yapionObject.unsafe().keySet()) {
                        if (valueElement.check(key)) {
                            result.unsafe().put(key, yapionObject.get(key));
                        }
                    }
                }
                if (result.isEmpty()) {
                    return null;
                }
                return result;
            } finally {
                pathContext.addTiming(this, System.nanoTime() - time);
            }
        });
    }
}
