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

import yapion.hierarchy.api.groups.YAPIONAnyType;
import yapion.hierarchy.types.YAPIONArray;
import yapion.hierarchy.types.YAPIONObject;
import yapion.path.elements.*;
import yapion.path.filters.ContainsFilter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

public class YAPIONPathParser {

    private String s;
    private int index = 0;

    public YAPIONPathParser(String s) {
        this.s = s;
    }

    private boolean escaped = false;
    private boolean regex = false;
    private StringBuilder current = new StringBuilder();
    private YAPIONAnyType elements = new YAPIONArray();
    private boolean array = false;

    public YAPIONPath parse() {
        while (index < s.length()) {
            char c = s.charAt(index);
            if (c == '\\' && !escaped) {
                escaped = true;
                index++;
                current.append(c);
                continue;
            }
            if (c == '[' && !escaped && !regex) {
                if (array) {
                    throw new IllegalArgumentException("Array can't be nested!");
                }
                array = true;
                current.append(c);
                index++;
                continue;
            }
            if (c == ']' && !escaped && !regex) {
                if (!array) {
                    throw new IllegalArgumentException("Array can't be nested!");
                }
                array = false;
                current.append(c);
                index++;
                continue;
            }
            if (c == '.' && !escaped && !regex && !array) {
                ((YAPIONArray) elements).add(new YAPIONObject().add("", current.toString()));
                current = new StringBuilder();
                index++;
                continue;
            }
            if (c == '{' && !escaped && !regex && !array) {
                YAPIONArray array = new YAPIONArray();
                YAPIONObject yapionObject = new YAPIONObject().add("", current.toString())
                        .add("filter", new YAPIONArray()
                                .add(new YAPIONObject()
                                        .add("", array)
                                        .add("operation", "&")
                                )
                        );
                ((YAPIONArray) elements).add(yapionObject);
                elements = array;
                current = new StringBuilder();
                index++;
                continue;
            }
            if (c == '}' && !escaped && !regex && !array) {
                ((YAPIONArray) elements).add(new YAPIONObject().add("", current.toString()));
                elements = elements.getParent().getParent().getParent().getParent();
                current = new StringBuilder();
                index++;
                continue;
            }
            if (elements.hasParent()) {
                if ((c == '|' || c == '&') && !escaped && !regex) {
                    ((YAPIONArray) elements).add(new YAPIONObject().add("", current.toString()));
                    YAPIONArray yapionArray = (YAPIONArray) elements.getParent().getParent();
                    YAPIONArray array = new YAPIONArray();
                    yapionArray.add(new YAPIONObject().add("operation", c + "").add("", array));
                    elements = array;
                    current = new StringBuilder();
                    index++;
                    continue;
                }
            }
            if (c == '/' && !escaped && !array) {
                regex = !regex;
                index++;
                current.append(c);
                continue;
            }
            current.append(c);
            escaped = false;
            index++;
        }
        if (s.length() > 0 && s.charAt(s.length() - 1) == '.' || current.length() > 0) {
            ((YAPIONArray) elements).add(new YAPIONObject().add("", current.toString()));
        }

        List<PathElement> pathElements = parseElements((YAPIONArray) elements);
        return new YAPIONPath(pathElements);
    }

    private List<PathElement> parseElements(YAPIONArray yapionArray) {
        List<PathElement> pathElements = new ArrayList<>();
        for (int i = 0; i < yapionArray.size(); i++) {
            YAPIONObject yapionObject = yapionArray.getObject(i);
            String identifier = yapionObject.getPlainValue("");
            pathElements.add(parseIdentifier(identifier));
            if (yapionObject.containsKey("filter")) {
                YAPIONArray filter = yapionObject.getArray("filter");
                pathElements.add(parseFilter(filter));
            }
        }
        return pathElements;
    }

    private PathElement parseIdentifier(String identifier) {
        if (identifier.equals("*")) {
            return new AnyElement();
        }
        if (identifier.equals("**")) {
            return new AnyDeeperElement();
        }
        if (identifier.startsWith("/") && identifier.endsWith("/")) {
            return new RegexElement(identifier.substring(1, identifier.length() - 1));
        }
        if (identifier.matches("\\[((-?\\d+\\.\\.\\.-?\\d+)|(\\.\\.\\.-?\\d+)|(-?\\d+\\.\\.\\.))\\]")) {
            String[] split = identifier.split("\\.\\.\\.");
            split[0] = split[0].substring(1);
            split[1] = split[1].substring(0, split[1].length() - 1);
            if (split[0].length() == 0 && split[1].length() != 0) {
                return RangeElement.max(Integer.parseInt(split[1]));
            } else if (split[0].length() != 0 && split[1].length() == 0) {
                return RangeElement.min(Integer.parseInt(split[0]));
            } else {
                return RangeElement.range(Integer.parseInt(split[0]), Integer.parseInt(split[1]));
            }
        }
        if (identifier.startsWith("[") && identifier.endsWith("]")) {
            String[] split = identifier.split("(?<=[^\\\\]),");
            split[0] = split[0].substring(1);
            split[split.length - 1] = split[split.length - 1].substring(0, split[split.length - 1].length() - 1);
            return new Element(split);
        }
        // TODO: StartsWith and EndsWith, contains!
        return new Element(identifier);
    }

    private ElementFilter parseFilter(YAPIONArray yapionArray) {
        Predicate<YAPIONAnyType> predicate = current -> true;
        for (YAPIONAnyType yapionAnyType : yapionArray) {
            YAPIONObject yapionObject = (YAPIONObject) yapionAnyType;
            String operation = yapionObject.getPlainValue("operation");
            // TODO: Size Filter
            List<PathElement> pathElements = parseElements(yapionObject.getArray(""));
            ContainsFilter containsFilter = new ContainsFilter(pathElements.toArray(new PathElement[0]));
            if (operation.equals("&")) {
                predicate = predicate.and(containsFilter);
            } else if (operation.equals("|")) {
                predicate = predicate.or(containsFilter);
            }
        }
        return new ElementFilter(predicate);
    }
}
