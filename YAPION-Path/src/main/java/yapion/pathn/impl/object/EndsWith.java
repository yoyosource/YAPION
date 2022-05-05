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

package yapion.pathn.impl.object;

import yapion.hierarchy.api.groups.YAPIONAnyType;
import yapion.hierarchy.types.YAPIONObject;
import yapion.pathn.PathContext;
import yapion.pathn.PathElement;

import java.util.Optional;
import java.util.stream.Collectors;

public class EndsWith implements PathElement {

    private String check;

    public EndsWith(String check) {
        this.check = check;
    }

    @Override
    public boolean check(YAPIONAnyType yapionAnyType) {
        if (yapionAnyType instanceof YAPIONObject yapionObject) {
            return yapionObject.getKeys().stream().anyMatch(key -> key.endsWith(check));
        }
        return false;
    }

    @Override
    public PathContext apply(PathContext pathContext, Optional<PathElement> possibleNextPathElement) {
        return pathContext.map(yapionAnyType -> {
            if (yapionAnyType instanceof YAPIONObject yapionObject) {
                return yapionObject.getKeys().stream().filter(s -> s.endsWith(check)).map(yapionObject::get).collect(Collectors.toList());
            }
            return null;
        });
    }
}
