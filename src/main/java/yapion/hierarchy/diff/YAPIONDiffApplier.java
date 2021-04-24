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
import yapion.exceptions.YAPIONException;
import yapion.hierarchy.api.groups.YAPIONAnyType;
import yapion.hierarchy.api.groups.YAPIONDataType;
import yapion.hierarchy.types.YAPIONArray;
import yapion.hierarchy.types.YAPIONMap;
import yapion.hierarchy.types.YAPIONObject;
import yapion.parser.YAPIONParser;

public class YAPIONDiffApplier<I, K, T extends YAPIONDataType<I, K>> {

    public static YAPIONDiffApplier<YAPIONObject, String, YAPIONObject> diffApplierObject(YAPIONObject yapionObject) {
        return new YAPIONDiffApplier<>(yapionObject);
    }

    public static YAPIONDiffApplier<YAPIONMap, YAPIONAnyType, YAPIONMap> diffApplierMap(YAPIONMap yapionMap) {
        return new YAPIONDiffApplier<>(yapionMap);
    }

    public static YAPIONDiffApplier<YAPIONArray, Integer, YAPIONArray> diffApplierArray(YAPIONArray yapionArray) {
        return new YAPIONDiffApplier<>(yapionArray);
    }

    @Getter
    private T value;

    private YAPIONDiffApplier(T value) {
        this.value = value;
    }

    public YAPIONDiffApplier<I, K, T> apply(YAPIONDiff yapionDiff) {
        yapionDiff.getDiffs().forEach(this::apply);
        return this;
    }

    public YAPIONDiffApplier<I, K, T> reverseApply(YAPIONDiff yapionDiff) {
        yapionDiff.getDiffs().forEach(this::reverseApply);
        return this;
    }

    public <B extends DiffBase> YAPIONDiffApplier<I, K, T> apply(B diff) {
        switch (diff.type()) {
            case DELETE:
                DiffBase.DiffDelete diffDelete = (DiffBase.DiffDelete) diff;
                applyDelete(diffDelete.getPath());
                break;
            case INSERT:
                DiffBase.DiffInsert diffInsert = (DiffBase.DiffInsert) diff;
                applyInsert(diffInsert.getPath(), diffInsert.getInserted());
                break;
            case CHANGE:
                DiffBase.DiffChange diffChange = (DiffBase.DiffChange) diff;
                applyInsert(diffChange.getPath(), diffChange.getTo());
                break;
        }
        return this;
    }

    public <B extends DiffBase> YAPIONDiffApplier<I, K, T> reverseApply(B diff) {
        switch (diff.type()) {
            case DELETE:
                DiffBase.DiffDelete diffDelete = (DiffBase.DiffDelete) diff;
                reverseApplyDelete(diffDelete.getPath(), diffDelete.getDeleted());
                break;
            case INSERT:
                DiffBase.DiffInsert diffInsert = (DiffBase.DiffInsert) diff;
                reverseApplyInsert(diffInsert.getPath());
                break;
            case CHANGE:
                DiffBase.DiffChange diffChange = (DiffBase.DiffChange) diff;
                reverseApplyDelete(diffChange.getPath(), diffChange.getFrom());
                break;
        }
        return this;
    }

    private void applyDelete(String[] path) {
        if (path.length == 0) {
            return;
        }
        YAPIONAnyType yapionAnyType = resolvePath(path);
        if (yapionAnyType instanceof YAPIONObject) {
            ((YAPIONObject) yapionAnyType).remove(path[path.length - 1]);
        }
        if (yapionAnyType instanceof YAPIONMap) {
            ((YAPIONMap) yapionAnyType).remove(YAPIONParser.parse(path[path.length - 1]));
        }
        if (yapionAnyType instanceof YAPIONArray) {
            ((YAPIONArray) yapionAnyType).remove(Integer.parseInt(path[path.length - 1]));
        }
    }

    private void applyInsert(String[] path, YAPIONAnyType toInsert) {
        if (path.length == 0) {
            return;
        }
        YAPIONAnyType yapionAnyType = resolvePath(path);
        if (yapionAnyType instanceof YAPIONObject) {
            ((YAPIONObject) yapionAnyType).add(path[path.length - 1], toInsert);
        }
        if (yapionAnyType instanceof YAPIONMap) {
            ((YAPIONMap) yapionAnyType).add(YAPIONParser.parse(path[path.length - 1]), toInsert);
        }
        if (yapionAnyType instanceof YAPIONArray) {
            ((YAPIONArray) yapionAnyType).add(Integer.parseInt(path[path.length - 1]), toInsert);
        }
    }

    private void reverseApplyDelete(String[] path, YAPIONAnyType deleted) {
        if (path.length == 0) {
            return;
        }
        YAPIONAnyType yapionAnyType = resolvePath(path);
        if (yapionAnyType instanceof YAPIONObject) {
            ((YAPIONObject) yapionAnyType).add(path[path.length - 1], deleted);
        }
        if (yapionAnyType instanceof YAPIONMap) {
            ((YAPIONMap) yapionAnyType).add(YAPIONParser.parse(path[path.length - 1]), deleted);
        }
        if (yapionAnyType instanceof YAPIONArray) {
            ((YAPIONArray) yapionAnyType).add(Integer.parseInt(path[path.length - 1]), deleted);
        }
    }

    private void reverseApplyInsert(String[] path) {
        if (path.length == 0) {
            return;
        }
        YAPIONAnyType yapionAnyType = resolvePath(path);
        if (yapionAnyType instanceof YAPIONObject) {
            ((YAPIONObject) yapionAnyType).remove(path[path.length - 1]);
        }
        if (yapionAnyType instanceof YAPIONMap) {
            ((YAPIONMap) yapionAnyType).remove(YAPIONParser.parse(path[path.length - 1]));
        }
        if (yapionAnyType instanceof YAPIONArray) {
            ((YAPIONArray) yapionAnyType).remove(Integer.parseInt(path[path.length - 1]));
        }
    }

    @SuppressWarnings({"unchecked", "java:S125"})
    private YAPIONAnyType resolvePath(String[] path) {
        if (path.length <= 1) {
            return value;
        }
        YAPIONAnyType current = value;
        for (int i = 0; i < path.length - 1; i++) {
            String s = path[i];
            if (current instanceof YAPIONDataType) {
                current = ((YAPIONDataType) current).internalGetYAPIONAnyType(s);
            } else {
                throw new YAPIONException();
            }
        }
        return current;
    }

}
