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

package yapion.hierarchy.types;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import yapion.hierarchy.api.groups.YAPIONAnyType;

import java.util.Arrays;
import java.util.LinkedList;

@ToString
@EqualsAndHashCode
public final class YAPIONPath {

    private final String[] path;

    public YAPIONPath(String[] path) {
        this.path = path;
    }

    public YAPIONPath(YAPIONAnyType yapionAnyType) {
        LinkedList<String> path = new LinkedList<>();
        YAPIONAnyType last = yapionAnyType;
        while ((yapionAnyType = yapionAnyType.getParent()) != null) {
            path.addFirst(yapionAnyType.getPath(last));
            last = yapionAnyType;
        }
        this.path = path.toArray(new String[0]);
    }

    public String[] getPath() {
        return Arrays.copyOf(path, path.length);
    }

    public String join(String delimiter) {
        return String.join(delimiter, path);
    }

    public String join() {
        return join("");
    }

    public int depth() {
        return path.length;
    }

    public String get(int index) {
        return path[index];
    }

    public String getLast() {
        return get(path.length - 1);
    }

}
