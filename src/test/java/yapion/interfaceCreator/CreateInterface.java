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

package yapion.interfaceCreator;

import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class CreateInterface {

    private static List<String> typeList = new ArrayList<>();
    private static int count = 0;

    public static void main(String[] args) {
        typeList.add("@NonNull String");
        typeList.add("char");
        typeList.add("boolean");
        typeList.add("byte");
        typeList.add("short");
        typeList.add("int");
        typeList.add("long");
        typeList.add("@NonNull BigInteger");
        typeList.add("float");
        typeList.add("double");
        typeList.add("@NonNull BigDecimal");

        evalTemplate("default boolean hasValue($0 key) {\nreturn hasValue(new YAPIONValue<>(key));\n}");
        evalTemplate("default boolean hasValue($0 key, YAPIONType yapionType) {\nreturn hasValue(new YAPIONValue<>(key), yapionType);\n}");
        evalTemplate("default <T> boolean hasValue($0 key, Class<T> type) {\nreturn hasValue(new YAPIONValue<>(key), type);\n}");
        evalTemplate("default YAPIONAnyType getYAPIONAnyType($0 key) {\nreturn getYAPIONAnyType(new YAPIONValue<>(key));\n}");
        evalTemplate("default YAPIONObject getObject($0 key) {\nreturn getObject(new YAPIONValue<>(key));\n}");
        evalTemplate("default void getObject($0 key, Consumer<YAPIONObject> valueConsumer, Runnable noValue) {\ngetObject(new YAPIONValue<>(key), valueConsumer, noValue);\n}");
        evalTemplate("default YAPIONArray getArray($0 key) {\nreturn getArray(new YAPIONValue<>(key));\n}");
        evalTemplate("default void getArray($0 key, Consumer<YAPIONArray> valueConsumer, Runnable noValue) {\ngetArray(new YAPIONValue<>(key), valueConsumer, noValue);\n}");
        evalTemplate("default YAPIONMap getMap($0 key) {\nreturn getMap(new YAPIONValue<>(key));\n}");
        evalTemplate("default void getMap($0 key, Consumer<YAPIONMap> valueConsumer, Runnable noValue) {\ngetMap(new YAPIONValue<>(key), valueConsumer, noValue);\n}");
        evalTemplate("default YAPIONPointer getPointer($0 key) {\nreturn getPointer(new YAPIONValue<>(key));\n}");
        evalTemplate("default void getPointer($0 key, Consumer<YAPIONPointer> valueConsumer, Runnable noValue) {\ngetPointer(new YAPIONValue<>(key), valueConsumer, noValue);\n}");
        evalTemplate("default YAPIONValue getValue($0 key) {\nreturn getValue(new YAPIONValue<>(key));\n}");
        evalTemplate("default void getValue($0 key, Consumer<YAPIONValue> valueConsumer, Runnable noValue) {\ngetValue(new YAPIONValue<>(key), valueConsumer, noValue);\n}");
        evalTemplate("default <T> YAPIONValue<T> getValue($0 key, Class<T> type) {\nreturn getValue(new YAPIONValue<>(key), type);\n}");
        evalTemplate("default <T> YAPIONValue<T> getValueOrDefault($0 key, Class<T> type, T defaultValue) {\nreturn getValueOrDefault(new YAPIONValue<>(key), type, defaultValue);\n}");
        evalTemplate("default <T> void getValue($0 key, Class<T> type, Consumer<YAPIONValue<T>> valueConsumer, Runnable noValue) {\ngetValue(new YAPIONValue<>(key), type, valueConsumer, noValue);\n}");
        evalTemplate("default <T> YAPIONValue<T> getValue($0 key, T type) {\nreturn getValue(new YAPIONValue<>(key), type);\n}");
        evalTemplate("default <T> YAPIONValue<T> getValueOrDefault($0 key, T defaultValue) {\nreturn getValueOrDefault(new YAPIONValue<>(key), defaultValue);\n}");
        evalTemplate("default <T> T getPlainValue($0 key) {\nreturn getPlainValue(new YAPIONValue<>(key));\n}");
        evalTemplate("default <T> T getPlainValueOrDefault($0 key, T defaultValue) {\nreturn getPlainValueOrDefault(new YAPIONValue<>(key), defaultValue);\n}");
        evalTemplate("default <T> void getPlainValue($0 key, Consumer<T> valueConsumer, Runnable noValue) {\ngetPlainValue(new YAPIONValue<>(key), valueConsumer, noValue);\n}");
        System.out.println();
        System.out.println(count);
    }

    private static class Template {

        private String template;
        private int depth = -1;
        private List<List<String>> templateReplaceList = new ArrayList<>();
        private List<String> results = new ArrayList<>();

        private Template(String template) {
            this.template = template;
            while (true) {
                if (!template.contains("$" + (depth + 1))) break;
                depth++;
            }
        }

        public void add(@NonNull List<String> templateReplacements) {
            templateReplaceList.add(templateReplacements.stream().filter(Objects::nonNull).collect(Collectors.toList()));
        }

        public void replace() {
            if (templateReplaceList.size() != depth + 1) return;
            results.clear();
            replace(template, 0);
        }

        private void replace(String current, int index) {
            if (index > depth) {
                results.add(current);
                return;
            }
            for (String s : templateReplaceList.get(index)) {
                replace(current.replace("$" + index, s), index + 1);
            }
        }

        public List<String> getResults() {
            return new ArrayList<>(results);
        }

    }

    private static void evalTemplate(String templateString) {
        Template template = new Template(templateString);
        template.add(typeList);
        template.replace();
        template.getResults().forEach(s -> {
            System.out.println();
            System.out.println(s);
            count++;
        });
    }

}
