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
import yapion.hierarchy.api.groups.YAPIONAnyType;
import yapion.hierarchy.types.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class YAPIONDiff {

    @Getter
    private List<DiffBase> diffs = new ArrayList<>();

    protected YAPIONDiff() {

    }

    public YAPIONDiff(YAPIONObject first, YAPIONObject second) {
        for (String key : first.allKeys()) {
            if (second.containsKey(key)) {
                diff(first.getYAPIONAnyType(key), second.getYAPIONAnyType(key));
            } else {
                YAPIONAnyType deleted = first.getYAPIONAnyType(key);
                diffs.add(new DiffDelete(deleted.getPath(), deleted));
            }
        }

        for (String key : second.allKeys()) {
            if (first.containsKey(key)) {
                continue;
            }
            YAPIONAnyType inserted = second.getYAPIONAnyType(key);
            diffs.add(new DiffInsert(inserted.getPath(), inserted));
        }
        merge();
    }

    public YAPIONDiff(YAPIONMap first, YAPIONMap second) {
        for (YAPIONAnyType key : first.allKeys()) {
            if (second.containsKey(key)) {
                diff(first.getYAPIONAnyType(key), second.getYAPIONAnyType(key));
            } else {
                YAPIONAnyType deleted = first.getYAPIONAnyType(key);
                diffs.add(new DiffDelete(deleted.getPath(), deleted));
            }
        }

        for (YAPIONAnyType key : second.allKeys()) {
            if (first.containsKey(key)) {
                continue;
            }
            YAPIONAnyType inserted = second.getYAPIONAnyType(key);
            diffs.add(new DiffInsert(inserted.getPath(), inserted));
        }
        merge();
    }

    public YAPIONDiff(YAPIONArray first, YAPIONArray second) {
        for (Integer index : first.allKeys()) {
            if (second.containsKey(index)) {
                diff(first.getYAPIONAnyType(index), second.getYAPIONAnyType(index));
            } else {
                YAPIONAnyType deleted = first.getYAPIONAnyType(index);
                diffs.add(new DiffDelete(deleted.getPath(), deleted));
            }
        }

        for (Integer index : second.allKeys()) {
            if (first.containsKey(index)) {
                continue;
            }
            YAPIONAnyType inserted = second.getYAPIONAnyType(index);
            diffs.add(new DiffInsert(inserted.getPath(), inserted));
        }
        merge();
    }

    private void merge() {
        List<DiffInsert> diffInserts = diffs.stream()
                .filter(d -> d instanceof DiffInsert)
                .map(d -> (DiffInsert) d)
                .collect(Collectors.toList());

        for (DiffInsert diffInsert : diffInserts) {
            Optional<DiffDelete> diffDeleteOptional = diffs.stream()
                    .filter(d -> d instanceof DiffDelete)
                    .map(d -> (DiffDelete) d)
                    .filter(d -> d.getDeleted().equals(diffInsert.getInserted()))
                    .findFirst();
            if (!diffDeleteOptional.isPresent()) {
                continue;
            }
            DiffDelete diffDelete = diffDeleteOptional.get();

            diffs = diffs.stream()
                    .filter(d -> d != diffInsert && d != diffDelete)
                    .collect(Collectors.toList());
            diffs.add(new DiffMove(diffDelete.getPath(), diffInsert.getPath()));
        }
    }

    private void diff(YAPIONAnyType first, YAPIONAnyType second) {
        if (first.getType() != second.getType()) {
            diffs.add(new DiffChange(first.getPath(), first, second));
            return;
        }
        if (first instanceof YAPIONValue || first instanceof YAPIONPointer) {
            if (first.toString().equals(second.toString())) {
                return;
            }
            diffs.add(new DiffChange(first.getPath(), first, second));
        } else if (first instanceof YAPIONObject) {
            YAPIONObject firstObject = (YAPIONObject) first;
            YAPIONObject secondObject = (YAPIONObject) second;
            if (firstObject.equals(secondObject)) {
                return;
            }
            diffs.addAll(new YAPIONDiff(firstObject, secondObject).diffs);
        } else if (first instanceof YAPIONArray) {
            YAPIONArray firstArray = (YAPIONArray) first;
            YAPIONArray secondArray = (YAPIONArray) second;
            if (firstArray.equals(secondArray)) {
                return;
            }
            diffs.addAll(new YAPIONDiff(firstArray, secondArray).diffs);
        } else if (first instanceof YAPIONMap) {
            YAPIONMap firstMap = (YAPIONMap) first;
            YAPIONMap secondMap = (YAPIONMap) second;
            if (firstMap.equals(secondMap)) {
                return;
            }
            diffs.addAll(new YAPIONDiff(firstMap, secondMap).diffs);
        }
    }

}
