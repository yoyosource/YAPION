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

import java.util.Arrays;

public class YAPIONDiffApplier<I extends YAPIONDataType<I, K>, K, T extends YAPIONDataType<I, K>> {

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
                DiffDelete diffDelete = (DiffDelete) diff;
                applyDelete(diffDelete.getPath());
                break;
            case INSERT:
                DiffInsert diffInsert = (DiffInsert) diff;
                applyInsert(diffInsert.getPath(), diffInsert.getInserted());
                break;
            case CHANGE:
                DiffChange diffChange = (DiffChange) diff;
                applyInsert(diffChange.getPath(), diffChange.getTo());
                break;
            case MOVE:
                DiffMove diffMove = (DiffMove) diff;
                applyMove(diffMove.getFromPath(), diffMove.getToPath());
                break;
            default:
                throw new UnsupportedOperationException();
        }
        return this;
    }

    public <B extends DiffBase> YAPIONDiffApplier<I, K, T> reverseApply(B diff) {
        switch (diff.type()) {
            case DELETE:
                DiffDelete diffDelete = (DiffDelete) diff;
                applyInsert(diffDelete.getPath(), diffDelete.getDeleted());
                break;
            case INSERT:
                DiffInsert diffInsert = (DiffInsert) diff;
                applyDelete(diffInsert.getPath());
                break;
            case CHANGE:
                DiffChange diffChange = (DiffChange) diff;
                applyInsert(diffChange.getPath(), diffChange.getFrom());
                break;
            case MOVE:
                DiffMove diffMove = (DiffMove) diff;
                applyMove(diffMove.getToPath(), diffMove.getFromPath());
                break;
            default:
                throw new UnsupportedOperationException();
        }
        return this;
    }

    private void applyDelete(String[] path) {
        if (path.length == 0) {
            return;
        }
        YAPIONAnyType yapionAnyType = resolvePath(path);
        if (yapionAnyType instanceof YAPIONObject yapionObject) {
            yapionObject.remove(path[path.length - 1]);
        }
        if (yapionAnyType instanceof YAPIONMap yapionMap) {
            // yapionMap.remove(YAPIONParser.parse(path[path.length - 1]));
            throw new UnsupportedOperationException("Not implemented yet");
        }
        if (yapionAnyType instanceof YAPIONArray yapionArray) {
            yapionArray.remove(Integer.parseInt(path[path.length - 1]));
        }
    }

    private void applyInsert(String[] path, YAPIONAnyType toInsert) {
        if (path.length == 0) {
            return;
        }
        YAPIONAnyType yapionAnyType = resolvePath(path);
        if (yapionAnyType instanceof YAPIONObject yapionObject) {
            yapionObject.add(path[path.length - 1], toInsert);
        }
        if (yapionAnyType instanceof YAPIONMap yapionMap) {
            // yapionMap.add(YAPIONParser.parse(path[path.length - 1]), toInsert);
            throw new UnsupportedOperationException("Not implemented yet");
        }
        if (yapionAnyType instanceof YAPIONArray yapionArray) {
            int index = Integer.parseInt(path[path.length - 1]);
            if (index == yapionArray.size()) {
                yapionArray.add(toInsert);
            } else {
                yapionArray.add(index, toInsert);
            }
        }
    }

    private void applyMove(String[] from, String[] to) {
        if (from.length == 0 || to.length == 0) {
            return;
        }
        YAPIONAnyType toYapionAnyType = resolvePath(from);
        if (toYapionAnyType instanceof YAPIONObject yapionObject) {
            YAPIONAnyType yapionAnyType = yapionObject.removeAndGet(from[from.length - 1]);
            applyInsert(to, yapionAnyType);
        }
        if (toYapionAnyType instanceof YAPIONMap yapionMap) {
            // YAPIONAnyType yapionAnyType = yapionMap.removeAndGet(YAPIONParser.parse(from[from.length - 1]));
            // applyInsert(to, yapionAnyType);
            throw new UnsupportedOperationException("Not implemented yet");
        }
        if (toYapionAnyType instanceof YAPIONArray yapionArray) {
            YAPIONAnyType yapionAnyType = yapionArray.removeAndGet(Integer.parseInt(from[from.length - 1]));
            applyInsert(to, yapionAnyType);
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
            if (current instanceof YAPIONDataType yapionDataType) {
                current = yapionDataType.internalGetAnyType(s);
            } else {
                throw new YAPIONException("Resolution of the path '" + Arrays.toString(path) + "' failed as current value is not of type YAPIONDataType");
            }
        }
        return current;
    }
}
