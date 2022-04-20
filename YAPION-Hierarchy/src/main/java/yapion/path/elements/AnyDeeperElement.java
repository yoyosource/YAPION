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
import yapion.hierarchy.types.YAPIONObject;
import yapion.path.PathContext;
import yapion.path.PathElement;

import java.util.ArrayList;
import java.util.List;

public class AnyDeeperElement implements PathElement {

    @Override
    public PathContext apply(PathContext pathContext) {
        if (pathContext.noPossibleNext()) {
            pathContext.getCurrent().clear();
            return pathContext;
        }
        List<YAPIONAnyType> finalResult = new ArrayList<>();
        while (pathContext.isNotEmpty()) {
            YAPIONAnyType element = pathContext.remove(0);
            if (pathContext.check(element)) {
                finalResult.add(element);
            }
            if (element instanceof YAPIONObject yapionObject) {
                pathContext.addAll(yapionObject.getAllValues());
            } else if (element instanceof YAPIONArray yapionArray) {
                pathContext.addAll(yapionArray.getAllValues());
            }
        }
        pathContext.setCurrent(finalResult);
        return pathContext;
    }
}
