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
import yapion.path.PathContext;
import yapion.path.PathElement;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public class Element implements PathElement {

    private String[] elements;
    private Function<YAPIONAnyType, List<YAPIONAnyType>>[] functions;

    public Element(String... elements) {
        this.elements = elements;

        List<Function<YAPIONAnyType, List<YAPIONAnyType>>> functions = new ArrayList<>();
        for (String s : elements) {
            Integer integer = null;
            try {
                integer = Integer.parseInt(s);
            } catch (NumberFormatException ignored) {
                // Ignored
            }
            Predicate<String> elementPredicate = null;
            if (s.startsWith("*") && s.endsWith("*")) {
                String checkAgainst = s.substring(1, s.length() - 1);
                elementPredicate = current -> current.contains(checkAgainst);
            } else if (s.startsWith("*")) {
                String checkAgainst = s.substring(1);
                elementPredicate = current -> current.endsWith(checkAgainst);
            } else if (s.endsWith("*")) {
                String checkAgainst = s.substring(0, s.length() - 1);
                elementPredicate = current -> current.startsWith(checkAgainst);
            }
            Predicate<String> finalElementPredicate = elementPredicate;
            Integer finalInteger = integer;
            functions.add(element -> {
                List<YAPIONAnyType> result = new ArrayList<>();
                if (element instanceof YAPIONObject yapionObject) {
                    if (finalElementPredicate != null) {
                        for (String key : yapionObject.getKeys()) {
                            if (finalElementPredicate.test(key)) {
                                result.add(yapionObject.getAny(key));
                            }
                        }
                    } else if (yapionObject.containsKey(s)) {
                        result.add(yapionObject.getAny(s));
                    }
                }
                if (element instanceof YAPIONArray yapionArray) {
                    if (finalInteger != null) {
                        if (finalInteger < 0) {
                            if (yapionArray.containsKey(yapionArray.length() + finalInteger)) {
                                result.add(yapionArray.getAny(yapionArray.length() + finalInteger));
                            }
                        }
                        if (yapionArray.containsKey(finalInteger)) {
                            result.add(yapionArray.getAny(finalInteger));
                        }
                    }
                }
                return result;
            });
        }
        this.functions = functions.toArray(new Function[0]);
    }

    @Override
    public boolean check(YAPIONAnyType element) {
        for (Function<YAPIONAnyType, List<YAPIONAnyType>> function : functions) {
            if (!function.apply(element).isEmpty()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public PathContext apply(PathContext pathContext) {
        List<YAPIONAnyType> result = new ArrayList<>();
        for (YAPIONAnyType element : pathContext.getCurrent()) {
            for (Function<YAPIONAnyType, List<YAPIONAnyType>> function : functions) {
                result.addAll(function.apply(element));
            }
        }
        pathContext.setCurrent(result);
        return pathContext;
    }
}
