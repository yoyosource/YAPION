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

import java.util.Optional;

public class Spread implements PathElement {

    @Override
    public boolean check(YAPIONAnyType yapionAnyType) {
        return true;
    }

    @Override
    public PathContext apply(PathContext pathContext, Optional<PathElement> possibleNextPathElement) {
        return pathContext.streamMap(yapionAnyType -> {
            if (yapionAnyType instanceof YAPIONArray yapionArray) {
                return yapionArray.allKeys().stream().map(integer -> {
                    return new YAPIONObject().putAndGetItself(integer + "", yapionArray.get(integer));
                });
            } else if (yapionAnyType instanceof YAPIONObject yapionObject) {
                return yapionObject.allKeys().stream().map(string -> {
                    return new YAPIONObject().putAndGetItself(string, yapionObject.get(string));
                });
            }
            return null;
        });
    }
}
