// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020 yoyosource

package test;

import yapion.annotations.YAPIONOptimize;
import yapion.annotations.YAPIONSave;

import java.util.*;

@YAPIONSave
public class Test {

    public transient volatile double i = 10.0;

    private List<String> strings = new ArrayList<>();
    //private String[] strings2 = new String[4];

    private static int d = 0;

    private Map<String, String> stringStringMap = new HashMap<>();
    private Map<Hugo, String> hugoStringMap = new HashMap<>();
    private HashMap<String, String> hashMap = new HashMap<>();
    private LinkedHashMap<Integer, String> linkedHashMap = new LinkedHashMap<>();

    {
        strings.add("Hello WOrld");
        strings.add("Hello WOrld!");
        strings.add("Hello WOrld!!");
        strings.add("Hello WOrld!!!");
        strings.add("Hello WOrld!!!!");

        stringStringMap.put("Hello", "Hello2");
        stringStringMap.put("Hello2", "Hello2");
        stringStringMap.put("Hello3", "Hello2");
        stringStringMap.put("Hello4", "Hello2");
        stringStringMap.put("Hello5", "Hello2");
    }

    public Hugo hugo = new Hugo(this);

    private HashMap<Hugo, String> hugoStringHashMap = new HashMap<>();

    {
        hugoStringHashMap.put(hugo, "");
        hugoStringHashMap.put(new Hugo(), "EMPTY");
    }

    public String tru = "true";

    public Hugo hugo2 = hugo;

    private Double test = 0.0;

    @YAPIONSave
    private class Hugo {

        public int i = 0;

        @YAPIONOptimize
        public Test t;

        public Hugo2 hugo2 = new Hugo2();

        private Hugo() {

        }

        public Hugo(Test t) {
            this.t = t;
        }

        public int getI() {
            return i;
        }

        @YAPIONSave
        private class Hugo2 {

            private int i = 0;

        }

    }

}