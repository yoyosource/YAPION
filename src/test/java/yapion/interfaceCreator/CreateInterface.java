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

import java.util.ArrayList;
import java.util.List;

public class CreateInterface {

    private static List<String> stringList = new ArrayList<>();

    public static void main(String[] args) {
        stringList.add("@NonNull String");
        stringList.add("char");
        stringList.add("boolean");
        stringList.add("byte");
        stringList.add("short");
        stringList.add("int");
        stringList.add("long");
        stringList.add("@NonNull BigInteger");
        stringList.add("float");
        stringList.add("double");
        stringList.add("@NonNull BigDecimal");

        fillTemplate("default boolean hasValue($ key) {\nreturn hasValue(new YAPIONValue<>(key));\n}");
        fillTemplate("default boolean hasValue($ key, YAPIONType yapionType) {\nreturn hasValue(new YAPIONValue<>(key), yapionType);\n}");
        fillTemplate("default <T> boolean hasValue($ key, Class<T> type) {\nreturn hasValue(new YAPIONValue<>(key), type);\n}");
        fillTemplate("default YAPIONAnyType getYAPIONAnyType($ key) {\nreturn getYAPIONAnyType(new YAPIONValue<>(key));\n}");
        fillTemplate("default YAPIONObject getObject($ key) {\nreturn getObject(new YAPIONValue<>(key));\n}");
        fillTemplate("default void getObject($ key, Consumer<YAPIONObject> valueConsumer, Runnable noValue) {\ngetObject(new YAPIONValue<>(key), valueConsumer, noValue);\n}");
        fillTemplate("default YAPIONArray getArray($ key) {\nreturn getArray(new YAPIONValue<>(key));\n}");
        fillTemplate("default void getArray($ key, Consumer<YAPIONArray> valueConsumer, Runnable noValue) {\ngetArray(new YAPIONValue<>(key), valueConsumer, noValue);\n}");
        fillTemplate("default YAPIONMap getMap($ key) {\nreturn getMap(new YAPIONValue<>(key));\n}");
        fillTemplate("default void getMap($ key, Consumer<YAPIONMap> valueConsumer, Runnable noValue) {\ngetMap(new YAPIONValue<>(key), valueConsumer, noValue);\n}");
        fillTemplate("default YAPIONPointer getPointer($ key) {\nreturn getPointer(new YAPIONValue<>(key));\n}");
        fillTemplate("default void getPointer($ key, Consumer<YAPIONPointer> valueConsumer, Runnable noValue) {\ngetPointer(new YAPIONValue<>(key), valueConsumer, noValue);\n}");
        fillTemplate("default YAPIONValue getValue($ key) {\nreturn getValue(new YAPIONValue<>(key));\n}");
        fillTemplate("default void getValue($ key, Consumer<YAPIONValue> valueConsumer, Runnable noValue) {\ngetValue(new YAPIONValue<>(key), valueConsumer, noValue);\n}");
        fillTemplate("default <T> YAPIONValue<T> getValue($ key, Class<T> type) {\nreturn getValue(new YAPIONValue<>(key), type);\n}");
        fillTemplate("default <T> YAPIONValue<T> getValueOrDefault($ key, Class<T> type, T defaultValue) {\nreturn getValueOrDefault(new YAPIONValue<>(key), type, defaultValue);\n}");
        fillTemplate("default <T> void getValue($ key, Class<T> type, Consumer<YAPIONValue<T>> valueConsumer, Runnable noValue) {\ngetValue(new YAPIONValue<>(key), type, valueConsumer, noValue);\n}");
        fillTemplate("default <T> YAPIONValue<T> getValue($ key, T type) {\nreturn getValue(new YAPIONValue<>(key), type);\n}");
        fillTemplate("default <T> YAPIONValue<T> getValueOrDefault($ key, T defaultValue) {\nreturn getValueOrDefault(new YAPIONValue<>(key), defaultValue);\n}");
        fillTemplate("default <T> T getPlainValue($ key) {\nreturn getPlainValue(new YAPIONValue<>(key));\n}");
        fillTemplate("default <T> T getPlainValueOrDefault($ key, T defaultValue) {\nreturn getPlainValueOrDefault(new YAPIONValue<>(key), defaultValue);\n}");
        fillTemplate("default <T> void getPlainValue($ key, Consumer<T> valueConsumer, Runnable noValue) {\ngetPlainValue(new YAPIONValue<>(key), valueConsumer, noValue);\n}");
    }

    private static void fillTemplate(String template) {
        if (!template.contains("$")) return;
        for (String s : stringList) {
            System.out.println();
            System.out.println(template.replace("$", s));
        }
    }

}
