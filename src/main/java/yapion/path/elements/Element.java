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

package yapion.path.elements;

import yapion.hierarchy.api.groups.YAPIONAnyType;
import yapion.hierarchy.types.YAPIONArray;
import yapion.hierarchy.types.YAPIONObject;
import yapion.path.PathElement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Element implements PathElement {

    private String[] elements;
    private List<Integer> integers = new ArrayList<>();

    public Element(String... elements) {
        this.elements = elements;
        for (String s : elements) {
            try {
                integers.add(Integer.parseInt(s));
            } catch (NumberFormatException e) {
                // Ignored
            }
        }
    }

    @Override
    public boolean check(YAPIONAnyType element) {
        if (element instanceof YAPIONObject yapionObject) {
            for (String s : elements) {
                if (yapionObject.containsKey(s)) {
                    return true;
                }
            }
            return false;
        }
        if (element instanceof YAPIONArray yapionArray) {
            for (int i : integers) {
                if (i < 0) {
                    if (yapionArray.containsKey(yapionArray.length() + i)) {
                        return true;
                    }
                }
                if (yapionArray.containsKey(i)) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    @Override
    public List<YAPIONAnyType> apply(List<YAPIONAnyType> current, PathElement possibleNext) {
        List<YAPIONAnyType> result = new ArrayList<>();
        for (YAPIONAnyType element : current) {
            if (element instanceof YAPIONObject yapionObject) {
                for (String s : elements) {
                    if (yapionObject.containsKey(s)) {
                        result.add(yapionObject.getAny(s));
                    }
                }
            }
            if (element instanceof YAPIONArray yapionArray) {
                for (int i : integers) {
                    if (yapionArray.containsKey(i)) {
                        result.add(yapionArray.getAny(i));
                    }
                }
            }
        }
        return result;
    }
}
