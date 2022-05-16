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
import yapion.hierarchy.types.YAPIONArray;
import yapion.hierarchy.types.YAPIONObject;
import yapion.path.PathContext;
import yapion.path.PathElement;

import java.util.Arrays;
import java.util.Optional;

public class Element implements PathElement {

    private String key;
    private Optional<Integer> index;

    public Element(String key) {
        this.key = key;
        try {
            index = Optional.of(Integer.parseInt(key));
        } catch (NumberFormatException e) {
            index = Optional.empty();
        }
    }

    public Element(int index) {
        this.key = "" + index;
        this.index = Optional.of(index);
    }

    @Override
    public boolean check(YAPIONAnyType yapionAnyType) {
        if (index.isPresent() && yapionAnyType instanceof YAPIONArray yapionArray) {
            int index = this.index.get();
            if (index < 0) index = yapionArray.size() + index;
            return index >= 0 && index < yapionArray.size();
        }
        if (yapionAnyType instanceof YAPIONObject yapionObject) {
            return yapionObject.containsKey(key);
        }
        return false;
    }

    @Override
    public PathContext apply(PathContext pathContext, Optional<PathElement> possibleNextPathElement) {
        return pathContext.map(yapionAnyType -> {
            long time = System.nanoTime();
            try {
                if (index.isPresent() && yapionAnyType instanceof YAPIONArray yapionArray) {
                    int index = this.index.get();
                    if (index < 0) index = yapionArray.size() + index;
                    return Arrays.asList(yapionArray.get(index));
                }
                if (yapionAnyType instanceof YAPIONObject yapionObject) {
                    return Arrays.asList(yapionObject.get(key));
                }
                return null;
            } finally {
                pathContext.addTiming(this, System.nanoTime() - time);
            }
        });
    }
}
