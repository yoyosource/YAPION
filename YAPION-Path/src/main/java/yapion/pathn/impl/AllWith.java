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
import yapion.pathn.PathContext;
import yapion.pathn.PathElement;

import java.util.List;
import java.util.Optional;

public class AllWith implements PathElement {

    private PathElement[] pathElements;

    public AllWith(PathElement... pathElements) {
        this.pathElements = pathElements;
    }

    public AllWith(List<PathElement> pathElementList) {
        this.pathElements = pathElementList.toArray(new PathElement[0]);
    }

    @Override
    public boolean check(YAPIONAnyType yapionAnyType) {
        for (PathElement pathElement : pathElements) {
            if (!pathElement.check(yapionAnyType)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public PathContext apply(PathContext pathContext, Optional<PathElement> possibleNextPathElement) {
        return pathContext.stream(yapionAnyTypeStream -> {
            for (PathElement pathElement : pathElements) {
                yapionAnyTypeStream = yapionAnyTypeStream.filter(pathElement::check);
            }
            return yapionAnyTypeStream;
        });
    }
}