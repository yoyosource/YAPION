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

package yapion.path;

import yapion.hierarchy.api.groups.YAPIONAnyType;

import java.util.ArrayList;
import java.util.List;

// TODO: Implement YAPIONPath like xPath or jsonPath
public class YAPIONPath {

    private PathElement[] pathElements;

    public YAPIONPath(List<PathElement> pathElements) {
        this(pathElements.toArray(new PathElement[0]));
    }

    public YAPIONPath(PathElement... pathElements) {
        this.pathElements = pathElements;
    }

    public List<YAPIONAnyType> apply(YAPIONAnyType element) {
        List<YAPIONAnyType> elements = new ArrayList<>();
        elements.add(element);
        return apply(elements);
    }

    public List<YAPIONAnyType> apply(List<YAPIONAnyType> elements) {
        for (int i = 0; i < pathElements.length; i++) {
            elements = pathElements[i].apply(elements, i < pathElements.length - 1 ? pathElements[i + 1] : null);
        }
        return elements;
    }

}
