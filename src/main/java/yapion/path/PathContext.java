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

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import yapion.hierarchy.api.groups.YAPIONAnyType;

import java.util.ArrayList;
import java.util.List;

public class PathContext {

    private final List<YAPIONAnyType> rootObject = new ArrayList<>();

    @Getter
    @Setter
    private List<YAPIONAnyType> current = new ArrayList<>();

    @Getter
    @Setter(AccessLevel.PACKAGE)
    private PathElement possibleNext;

    public PathContext(YAPIONAnyType rootObject) {
        this.rootObject.add(rootObject);
        current.add(rootObject);
    }

    public PathContext(List<YAPIONAnyType> current) {
        this.rootObject.addAll(current);
        this.current = new ArrayList<>(current);
    }

    public PathContext root() {
        return new PathContext(rootObject);
    }

    public boolean hasPossibleNext() {
        return possibleNext != null;
    }

    public boolean noPossibleNext() {
        return possibleNext == null;
    }

    public boolean isEmpty() {
        return current.isEmpty();
    }

    public boolean isNotEmpty() {
        return !current.isEmpty();
    }

    public YAPIONAnyType remove(int index) {
        return current.remove(index);
    }

    public boolean check(YAPIONAnyType yapionAnyType) {
        return possibleNext.check(yapionAnyType);
    }

    public boolean addAll(List<YAPIONAnyType> yapionAnyTypes) {
        return current.addAll(yapionAnyTypes);
    }
}
