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
import yapion.serializing.api.InstanceFactory;

import java.util.ArrayList;
import java.util.List;

public class YAPIONDiff {

    public static void main(String[] args) {
        YAPIONObject first = new YAPIONObject();
        first.add("test", "Hello World");
        first.add("test4", "Hello World");
        first.add("test2", "Hello World");
        first.add("object", new YAPIONObject());
        first.add("objectInternal", new YAPIONObject().add("test", "test").add("test2", "test2").add("test3", ""));

        YAPIONObject second = new YAPIONObject();
        second.add("test", "Hello World2");
        second.add("test3", "Hello World");
        second.add("test4", "Hello World");
        second.add("object", new YAPIONObject());
        second.add("objectInternal", new YAPIONObject().add("test", "test").add("test2", "test3").add("test4", ""));

        YAPIONDiff yapionDiff = new YAPIONDiff(first, second);
        System.out.println(yapionDiff.diffs);

        System.out.println(first);
        System.out.println(second);
        YAPIONDiffApplier.diffApplierObject(first).apply(yapionDiff);
        System.out.println(first.equals(second));
        YAPIONDiffApplier.diffApplierObject(first).reverseApply(yapionDiff);
        System.out.println(first);
        System.out.println(second);
        System.out.println(first.equals(second));
    }

    @Getter
    private List<DiffBase> diffs = new ArrayList<>();

    private YAPIONDiff() {

    }

    public static class YAPIONDiffFactory extends InstanceFactory<YAPIONDiff> {
        @Override
        public Class<YAPIONDiff> type() {
            return YAPIONDiff.class;
        }

        @Override
        public YAPIONDiff instance() {
            return new YAPIONDiff();
        }
    }

    public YAPIONDiff(YAPIONObject first, YAPIONObject second) {
        for (String key : first.allKeys()) {
            if (second.hasValue(key)) {
                diff(first.getYAPIONAnyType(key), second.getYAPIONAnyType(key));
            } else {
                YAPIONAnyType deleted = first.getYAPIONAnyType(key);
                diffs.add(new DiffDelete(deleted.getPath(), deleted));
            }
        }

        for (String key : second.allKeys()) {
            if (first.hasValue(key)) {
                continue;
            }
            YAPIONAnyType inserted = second.getYAPIONAnyType(key);
            diffs.add(new DiffInsert(inserted.getPath(), inserted));
        }
    }

    public YAPIONDiff(YAPIONMap first, YAPIONMap second) {
        for (YAPIONAnyType key : first.allKeys()) {
            if (second.hasValue(key)) {
                diff(first.getYAPIONAnyType(key), second.getYAPIONAnyType(key));
            } else {
                YAPIONAnyType deleted = first.getYAPIONAnyType(key);
                diffs.add(new DiffDelete(deleted.getPath(), deleted));
            }
        }

        for (YAPIONAnyType key : second.allKeys()) {
            if (first.hasValue(key)) {
                continue;
            }
            YAPIONAnyType inserted = second.getYAPIONAnyType(key);
            diffs.add(new DiffInsert(inserted.getPath(), inserted));
        }
    }

    public YAPIONDiff(YAPIONArray first, YAPIONArray second) {
        for (Integer index : first.allKeys()) {
            if (second.hasValue(index)) {
                diff(first.getYAPIONAnyType(index), second.getYAPIONAnyType(index));
            } else {
                YAPIONAnyType deleted = first.getYAPIONAnyType(index);
                diffs.add(new DiffDelete(deleted.getPath(), deleted));
            }
        }

        for (Integer index : second.allKeys()) {
            if (first.hasValue(index)) {
                continue;
            }
            YAPIONAnyType inserted = second.getYAPIONAnyType(index);
            diffs.add(new DiffInsert(inserted.getPath(), inserted));
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
