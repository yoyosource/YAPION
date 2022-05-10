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

package yapion.pathn;

import yapion.hierarchy.api.groups.YAPIONAnyType;
import yapion.hierarchy.types.YAPIONArray;
import yapion.hierarchy.types.YAPIONObject;
import yapion.hierarchy.types.YAPIONValue;
import yapion.pathn.builder.Selector;
import yapion.pathn.impl.*;
import yapion.pathn.impl.object.Contains;
import yapion.pathn.impl.object.Regex;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Test {

    public static void main(String[] args) {
        YAPIONObject yapionObject = new YAPIONObject();
        yapionObject.add("element", 0);
        for (int i = 0; i < 100; i++) {
            YAPIONArray yapionArray = new YAPIONArray();
            for (int j = 0; j < 100; j++) {
                yapionArray.add(new YAPIONValue<>(j));
            }
            yapionObject.add("test" + i, yapionArray);
        }
        // yapionObject.remove("element");
        yapionObject.put("element", 1);

        if (false) {
            YAPIONPath yapionPath = new YAPIONPath(new AnyDeeperElement(), new AnyElement()); // 7376ms
            // YAPIONPath yapionPath = new YAPIONPath(new AnyElement(), new AnyElement()); // 846ms
            long timeTotal = 0;
            for (int i = 0; i < 10000; i++) {
                PathResult result = yapionPath.apply(yapionObject);
                timeTotal += result.nanoTime;
            }
            System.out.println(timeTotal / 1000000 + "ms");
        }

        if (false) {
            YAPIONPath yapionPath = new YAPIONPath(new AnyOf(new Element("test0"), new Element("test1")), new AnyOf(new Element(0), new Element(1), new Element(0)));
            output(yapionPath.apply(yapionObject));
        }

        if (false) {
            YAPIONPath yapionPath = new YAPIONPath(new Contains("0"), new Element(0));
            output(yapionPath.apply(yapionObject));
        }

        if (false) {
            YAPIONPath yapionPath = new YAPIONPath(new AnyOf(new Contains("0"), new Contains("1")), new Element(0));
            output(yapionPath.apply(yapionObject));
        }
        if (false) {
            YAPIONPath yapionPath = new YAPIONPath(new AnyOf(new Contains("0"), new Contains("1")), new IdentityDistinctElements(), new Element(0));
            output(yapionPath.apply(yapionObject));
        }
        if (false) {
            YAPIONPath yapionPath = new YAPIONPath(new AnyOf(new Contains("0"), new Contains("1")), new DistinctElements(), new Element(0));
            output(yapionPath.apply(yapionObject));
        }

        if (false) {
            YAPIONPath yapionPath = new YAPIONPath(new AnyOf(new Contains("0"), new Contains("1")), new ElementType(YAPIONObject.class), new Element(0));
            output(yapionPath.apply(yapionObject));
        }

        if (false) {
            YAPIONPath yapionPath = new YAPIONPath(new Regex(Pattern.compile("test1.*")), new Element(0));
            output(yapionPath.apply(yapionObject));
        }

        if (false) {
            YAPIONPath yapionPath = new YAPIONPath(new AnyOf(new YAPIONPath(new Element("test0"), new Element(0)), new Contains("1")));
            output(yapionPath.apply(yapionObject));
        }

        if (false) {
            YAPIONPath yapionPath = new YAPIONPath(new AllWith(new Contains("0"), new Contains("1")));
            output(yapionPath.apply(yapionObject));
        }

        if (false) {
            YAPIONPath yapionPath = new YAPIONPath(new Spread(), new AllWith(new Contains("0"), new Contains("1")));
            output(yapionPath.apply(yapionObject));
        }

        if (false) {
            YAPIONPath yapionPath = new Selector()
                    .spread()
                    .allWith()
                        .contains("0")
                        .contains("1")
                    .build()
                    .any()
                    .spread()
                    .anyOf()
                        .allWith()
                            .contains("0")
                            .contains("1")
                        .build()
                        .allWith()
                            .contains("0")
                            .contains("2")
                        .build()
                    .build()
                    .any()
                    .build();
            output(yapionPath.apply(yapionObject));
        }

        if (false) {
            YAPIONPath yapionPath = new Selector()
                    .spread()
                    .anyWith()
                        .contains("0")
                        .contains("1")
                    .build()
                    .build();
            output(yapionPath.apply(yapionObject));
        }

        if (false) {
            YAPIONPath yapionPath = new Selector()
                    .select("test0")
                    .range(10, 20)
                    .build();
            output(yapionPath.apply(yapionObject));
        }

        if (false) {
            // yapionObject.remove("element");
            YAPIONPath yapionPath = new Selector()
                    .spread()
                    .allWith()
                        .select("10")
                        .subselect()
                            .root()
                            .select("element")
                        .build()
                    .build()
                    .build();
            output(yapionPath.apply(yapionObject));
        }

        if (false) {
            YAPIONPath yapionPath = new Selector()
                    .spread()
                    .when()
                        .subselect()
                            .root()
                            .select("element")
                        .build()
                    .build()
                    .build();
            output(yapionPath.apply(yapionObject));
        }

        if (false) {
            YAPIONPath yapionPath = new Selector()
                    .spread()
                    .anyOf()
                        .subselect()
                            .when()
                                .subselect()
                                    .root()
                                    .select("element")
                                .build()
                            .build()
                            .select("test0")
                        .build()
                        .subselect()
                            .when()
                                .subselect()
                                    .root()
                                    .select("element")
                                .build()
                            .build()
                            .select("test1")
                        .build()
                    .build()
                    .build();
            output(yapionPath.apply(yapionObject));
        }

        if (false) {
            YAPIONPath yapionPath = new Selector()
                    .spread()
                    .anyOf()
                        .subselect()
                            .when()
                                .subselect()
                                    .root()
                                    .select("element")
                                    .value()
                                        .min(1)
                                    .build()
                                .build()
                            .build()
                            .anyOf()
                                .select("test0")
                                .select("test1")
                            .build()
                        .build()
                    .build()
                    .build();
            output(yapionPath.apply(yapionObject));
        }

        if (false) {
            YAPIONPath yapionPath = new Selector()
                    .spread()
                    .when()
                        .allOf()
                            .subselect()
                                .root()
                                .select("element")
                                .value()
                                    .min(1)
                                .build()
                            .build()
                            .subselect()
                                .current()
                                .contains("0")
                            .build()
                        .build()
                    .build()
                    .build();
            output(yapionPath.apply(yapionObject));
        }

        if (false) {
            YAPIONPath yapionPath = new Selector()
                    .spread()
                    .when()
                        .allOf()
                            .subselect()
                                .root()
                                .select("element")
                                .value()
                                    .min(1)
                                .build()
                            .build()
                            .subselect()
                                .current()
                                .contains("0")
                            .build()
                        .build()
                    .build()
                    .flatten()
                    .build();
            output(yapionPath.apply(yapionObject));
        }

        if (true) {
            YAPIONPath yapionPath = new Selector()
                    .any()
                    .spread()
                    .when()
                        .contains("0")
                    .build()
                    .flatten()
                    .build();
            output(yapionPath.apply(yapionObject));
        }

        if (true) {
            yapionObject.remove("element");
            YAPIONPath yapionPath = new Selector()
                    .any()
                    .add((pathContext, possibleNextPathElement) -> pathContext.map(yapionAnyType -> {
                        YAPIONArray yapionArray = (YAPIONArray) yapionAnyType;
                        YAPIONObject result = new YAPIONObject();
                        for (int i = 0; i < yapionArray.size(); i++) {
                            String key = i + "";
                            if (key.contains("0")) {
                                result.add(key, yapionArray.get(i));
                            }
                        }
                        if (result.isEmpty()) {
                            return null;
                        }
                        return Arrays.asList(result);
                    }))
                    .build();
            output(yapionPath.apply(yapionObject));
            long total = 0;
            for (int i = 0; i < 10000; i++) {
                total += yapionPath.apply(yapionObject).nanoTime;
            }
            System.out.println((total / 1000 / 1000.0 / 10000) + "ms/iteration");
        }

        if (true) {
            YAPIONPath yapionPath = new Selector()
                    .any()
                    .keySelector()
                        .contains("0")
                    .build()
                    .build();
            output(yapionPath.apply(yapionObject));
            long total = 0;
            for (int i = 0; i < 10000; i++) {
                total += yapionPath.apply(yapionObject).nanoTime;
            }
            System.out.println((total / 1000 / 1000.0 / 10000) + "ms/iteration");
        }
    }

    private static void output(PathResult result) {
        System.out.println(result.size());
        System.out.println(result.nanoTime / 10000 / 100.0 + "ms");
        StringBuilder st = new StringBuilder();
        for (Map.Entry<PathElement, Long> entry : result.timingMap.entrySet()) {
            if (st.length() != 0) {
                st.append(", ");
            }
            st.append(entry.getKey().toString()).append("=").append(entry.getValue() / 1000000.0).append("ms");
        }
        System.out.println(st);
        System.out.println(result.yapionAnyTypeList);
    }
}
