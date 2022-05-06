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

package yapion.pathn;

import yapion.hierarchy.api.groups.YAPIONAnyType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class YAPIONPath implements PathElement {

    private PathElement[] pathElements;

    public YAPIONPath(List<PathElement> pathElements) {
        this(pathElements.toArray(new PathElement[0]));
    }

    public YAPIONPath(PathElement... pathElements) {
        this.pathElements = pathElements;
    }

    public PathResult apply(YAPIONAnyType element) {
        List<YAPIONAnyType> elements = new ArrayList<>();
        elements.add(element);
        return apply(elements);
    }

    public PathResult apply(List<YAPIONAnyType> elements) {
        return apply(elements, Optional.empty());
    }

    private PathResult apply(List<YAPIONAnyType> elements, Optional<PathElement> possibleNextPathElementOuter) {
        long time = System.nanoTime();
        PathContext pathContext = apply(PathContext.of(elements), possibleNextPathElementOuter);
        List<YAPIONAnyType> result = pathContext.eval();
        long time2 = System.nanoTime();
        return new PathResult(result, time2 - time);
    }

    private PathElement getPathElement(int index) {
        if (index >= pathElements.length) return null;
        return pathElements[index];
    }

    @Override
    public boolean check(YAPIONAnyType yapionAnyType) {
        if (pathElements.length == 0) return true;
        return pathElements[0].check(yapionAnyType);
    }

    @Override
    public PathContext apply(PathContext pathContext, Optional<PathElement> possibleNextPathElementOuter) {
        for (int i = 0; i < pathElements.length; i++) {
            Optional<PathElement> possibleNextPathElement = Optional.ofNullable(getPathElement(i));
            if (i == pathElements.length - 1) {
                possibleNextPathElement = possibleNextPathElementOuter;
            }
            pathContext = pathElements[i].apply(pathContext, possibleNextPathElement);
        }
        return pathContext;
    }
}
