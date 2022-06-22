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

import yapion.exceptions.YAPIONException;
import yapion.path.impl.*;
import yapion.path.impl.array.Range;
import yapion.path.impl.object.Contains;
import yapion.path.impl.object.EndsWith;
import yapion.path.impl.object.Regex;
import yapion.path.impl.object.StartsWith;
import yapion.path.impl.value.Value;
import yapion.path.impl.value.ValueAnd;
import yapion.path.impl.value.ValueElement;
import yapion.path.impl.value.ValueOr;
import yapion.path.impl.value.impl.ValueContains;
import yapion.path.impl.value.impl.ValueRange;
import yapion.path.impl.value.impl.ValueRegex;
import yapion.path.impl.value.impl.ValueStartsWith;

import java.util.*;
import java.util.stream.Collectors;

public class YAPIONPathParser {

    public static void main(String[] args) {
        System.out.println(parse("*.**.(*).&().|().$.@.#().?().!().!*.:.test*.*test.*test*./test/.{}.=.@=.(test.hugo)[0-].[-0].[0-10]"));
        System.out.println();
        System.out.println(parse("*.!*.?(*0*).:"));
        System.out.println();
        System.out.println(parse("*.!(*0*)"));
    }

    public static YAPIONPath parse(String path) {
        YAPIONPathParser parser = new YAPIONPathParser(path);
        Collection<Object> objects = parser.parse(0, new ArrayList<>());
        // System.out.println(objects);
        return (YAPIONPath) parser.parse(objects, false);
    }

    private Iterator<Character> characterIterator;

    public YAPIONPathParser(String s) {
        characterIterator = s.chars().mapToObj(c -> (char) c).iterator();
    }

    private Collection<Object> parse(int depth, Collection<Object> objects) {
        boolean regex = false;
        StringBuilder current = new StringBuilder();
        while (characterIterator.hasNext()) {
            char c = characterIterator.next();
            if (c == '/') {
                regex = !regex;
                if (!regex) {
                    current.append(c);
                    objects.add(current.toString());
                    current = new StringBuilder();
                    continue;
                }
            }
            if (regex) {
                current.append(c);
                continue;
            }
            if (depth > 0 && c == ',') {
                if (current.length() > 0) {
                    objects.add(current.toString());
                    current = new StringBuilder();
                }
                objects.add(null);
                continue;
            }
            if (c == '(' || c == '{') {
                String type = current.toString();
                boolean defaultMatched = c == '{';
                if (c == '(') {
                    switch (type) {
                        case "u&":
                            objects.add(parse(depth + 1, new AllOf<>()));
                            break;
                        case "v&":
                            objects.add(parse(depth + 1, new AnyOf<>()));
                            break;
                        case "&":
                            objects.add(parse(depth + 1, new AndGroup<>()));
                            break;
                        case "u|":
                            objects.add(parse(depth + 1, new AllWith<>()));
                            break;
                        case "v|":
                            objects.add(parse(depth + 1, new AnyWith<>()));
                            break;
                        case "|":
                            objects.add(parse(depth + 1, new OrGroup<>()));
                            break;
                        case "#":
                            objects.add(parse(depth + 1, new ValueGroup<>()));
                            break;
                        case "?":
                            objects.add(parse(depth + 1, new WhenGroup<>()));
                            break;
                        case "!":
                            objects.add(parse(depth + 1, new KeySelectionGroup<>()));
                            break;
                        default:
                            defaultMatched = true;
                    }
                }
                if (defaultMatched) {
                    if (current.length() > 0) {
                        objects.add(type);
                    }
                    if (c == '{') {
                        objects.add(parse(depth + 1, new HashSet<>()));
                    } else {
                        objects.add(parse(depth + 1, new ArrayList<>()));
                    }
                }
                current = new StringBuilder();
                continue;
            }
            
            if (objects instanceof List && c == ')' && depth > 0) break;
            if (objects instanceof Set && c == '}' && depth > 0) break;
            if (c == '.') {
                if (current.length() > 0) {
                    objects.add(current.toString());
                }
                current = new StringBuilder();
            } else {
                current.append(c);
            }
        }
        if (current.length() > 0) {
            objects.add(current.toString());
        }
        return objects;
    }

    private static class AndGroup<T> extends ArrayList<T> {}
    private static class OrGroup<T> extends ArrayList<T> {}
    private static class ValueGroup<T> extends ArrayList<T> {}
    private static class WhenGroup<T> extends ArrayList<T> {}
    private static class KeySelectionGroup<T> extends ArrayList<T> {}

    private static class AllOf<T> extends ArrayList<T> {}
    private static class AllWith<T> extends ArrayList<T> {}
    private static class AnyOf<T> extends ArrayList<T> {}
    private static class AnyWith<T> extends ArrayList<T> {}

    private PathElement parse(Object element, boolean insideOfValue) {
        if (element instanceof String string) {
            if (!insideOfValue) {
                switch (string) {
                    case "*":
                        return new AnyElement();
                    case "**":
                        return new AnyDeeperElement();
                    case "$":
                        return new RootElement();
                    case "@":
                        return new CurrentElement();
                    case "!*":
                        return new Spread();
                    case ":":
                        return new Flatten();
                    case "=":
                        return new DistinctElements();
                    case "@=":
                        return new IdentityDistinctElements();
                }
            }

            boolean startsWith = string.startsWith("*");
            boolean endsWith = string.endsWith("*");
            if (startsWith && endsWith) {
                if (insideOfValue) {
                    return new ValueContains(string.substring(1, string.length() - 1));
                }
                return new Contains(string.substring(1, string.length() - 1));
            } else if (startsWith) {
                if (insideOfValue) {
                    return new ValueStartsWith(string.substring(1));
                }
                return new StartsWith(string.substring(1));
            } else if (endsWith) {
                if (insideOfValue) {
                    return new ValueContains(string.substring(0, string.length() - 1));
                }
                return new EndsWith(string.substring(0, string.length() - 1));
            }

            if (string.matches("(\\[\\d+-\\d+])|(\\[\\d+-])|(\\[-\\d+])")) {
                String[] strings = string.split("-");
                strings[0] = strings[0].substring(1);
                strings[1] = strings[1].substring(0, strings[1].length() - 1);
                if (strings[0].length() != 0 && strings[1].length() != 0) {
                    if (insideOfValue) {
                        return ValueRange.range(Integer.parseInt(strings[0]), Integer.parseInt(strings[1]));
                    }
                    return Range.range(Integer.parseInt(strings[0]), Integer.parseInt(strings[1]));
                } else if (strings[0].length() != 0) {
                    if (insideOfValue) {
                        return ValueRange.min(Integer.parseInt(strings[0]));
                    }
                    return Range.min(Integer.parseInt(strings[0]));
                } else {
                    if (insideOfValue) {
                        return ValueRange.max(Integer.parseInt(strings[1]));
                    }
                    return Range.max(Integer.parseInt(strings[1]));
                }
            }

            if (string.startsWith("/") && string.endsWith("/")) {
                if (insideOfValue) {
                    return new ValueRegex(string.substring(1, string.length() - 1));
                }
                return new Regex(string.substring(1, string.length() - 1));
            }

            if (insideOfValue) {
                throw new YAPIONException("Invalid value path element: " + string);
            }
            return new Element(string.replaceAll("\\\\(.)", "$1"));
        }
        if (element instanceof Collection) {
            Collection<Object> collections = (Collection<Object>) element;
            boolean isValue = insideOfValue;
            boolean check = collections instanceof ValueGroup || collections instanceof AndGroup || collections instanceof OrGroup || collections instanceof KeySelectionGroup;
            if (isValue && !check) {
                throw new YAPIONException("Invalid value path element: " + collections);
            }
            isValue |= check;

            boolean finalIsValue = isValue;
            List<List<PathElement>> splittedAndParsed = split(collections).stream()
                    .map(objects -> objects.stream().map(o -> parse(o, finalIsValue)).collect(Collectors.toList()))
                    .collect(Collectors.toList());
            // System.out.println(splittedAndParsed);

            if (check) {
                for (List<PathElement> pathElements : splittedAndParsed) {
                    if (pathElements.size() != 1) {
                        throw new YAPIONException("Invalid value path element: " + pathElements);
                    }
                    if (!(pathElements.get(0) instanceof ValueElement)) {
                        throw new YAPIONException("Invalid value path element: " + pathElements.get(0));
                    }
                }
                ValueElement[] elements = new ValueElement[splittedAndParsed.size()];
                for (int i = 0; i < elements.length; i++) {
                    elements[i] = (ValueElement) splittedAndParsed.get(i).get(0);
                }
                if (collections instanceof ValueGroup) {
                    return new Value(elements);
                } else if (collections instanceof AndGroup) {
                    return new ValueAnd(elements);
                } else if (collections instanceof OrGroup) {
                    return new ValueOr(elements);
                } else if (collections instanceof KeySelectionGroup) {
                    return new KeySelection(new ValueAnd(elements));
                } else {
                    throw new YAPIONException("Invalid value path element: " + collections);
                }
            } else if (collections instanceof AllOf || collections instanceof AllWith || collections instanceof AnyOf || collections instanceof AnyWith) {
                for (List<PathElement> pathElements : splittedAndParsed) {
                    if (pathElements.size() != 1) {
                        throw new YAPIONException("Invalid path element: " + pathElements);
                    }
                }
                PathElement[] elements = new PathElement[splittedAndParsed.size()];
                for (int i = 0; i < elements.length; i++) {
                    elements[i] = splittedAndParsed.get(i).get(0);
                }
                if (collections instanceof AllOf) {
                    return new yapion.path.impl.AllOf(elements);
                } else if (collections instanceof AllWith) {
                    return new yapion.path.impl.AllWith(elements);
                } else if (collections instanceof AnyOf) {
                    return new yapion.path.impl.AnyOf(elements);
                } else if (collections instanceof AnyWith) {
                    return new yapion.path.impl.AnyWith(elements);
                }
            } else {
                if (splittedAndParsed.isEmpty()) {
                    return new YAPIONPath();
                }
                if (splittedAndParsed.size() != 1) {
                    throw new YAPIONException("Invalid path element: " + collections);
                }
                if (collections instanceof WhenGroup) {
                    return new When(new YAPIONPath(splittedAndParsed.get(0).toArray(new PathElement[0])));
                }
                return new YAPIONPath(splittedAndParsed.get(0).toArray(new PathElement[0]));
            }
        }
        throw new SecurityException();
    }

    private List<List<Object>> split(Collection<Object> current) {
        List<List<Object>> elements = new ArrayList<>();
        List<Object> currentElement = new ArrayList<>();
        for (Object o : current) {
            if (o == null) {
                elements.add(currentElement);
                currentElement = new ArrayList<>();
            } else {
                currentElement.add(o);
            }
        }
        if (!currentElement.isEmpty()) {
            elements.add(currentElement);
        }
        return elements;
    }
}
