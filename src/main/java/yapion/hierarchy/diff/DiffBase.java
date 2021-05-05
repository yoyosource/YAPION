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

package yapion.hierarchy.diff;

import lombok.Getter;
import lombok.ToString;
import yapion.hierarchy.api.groups.YAPIONAnyType;
import yapion.hierarchy.types.YAPIONPath;

import java.util.Arrays;

public interface DiffBase {
    DiffType type();

    @ToString
    @Getter
    class DiffChange implements DiffBase {

        private String[] path;
        private YAPIONAnyType from;
        private YAPIONAnyType to;

        public DiffChange(YAPIONPath path, YAPIONAnyType from, YAPIONAnyType to) {
            this.path = Arrays.copyOf(path.getPath(), path.depth());
            this.from = from;
            this.to = to;
        }

        @Override
        public DiffType type() {
            return DiffType.CHANGE;
        }
    }

    @ToString
    @Getter
    class DiffDelete implements DiffBase {

        private String[] path;
        private YAPIONAnyType deleted;

        public DiffDelete(YAPIONPath path, YAPIONAnyType deleted) {
            this.path = Arrays.copyOf(path.getPath(), path.depth());
            this.deleted = deleted;
        }

        @Override
        public DiffType type() {
            return DiffType.DELETE;
        }

    }

    @ToString
    @Getter
    class DiffInsert implements DiffBase {

        private String[] path;
        private YAPIONAnyType inserted;

        public DiffInsert(YAPIONPath path, YAPIONAnyType inserted) {
            this.path = Arrays.copyOf(path.getPath(), path.depth());
            this.inserted = inserted;
        }

        @Override
        public DiffType type() {
            return DiffType.INSERT;
        }
    }

    @ToString
    @Getter
    class DiffMove implements DiffBase {

        private String[] fromPath;
        private String[] toPath;

        public DiffMove(YAPIONPath fromPath, YAPIONPath toPath) {
            this.fromPath = Arrays.copyOf(fromPath.getPath(), fromPath.depth());
            this.toPath = Arrays.copyOf(toPath.getPath(), toPath.depth());
        }

        @Override
        public DiffType type() {
            return DiffType.MOVE;
        }
    }
}
