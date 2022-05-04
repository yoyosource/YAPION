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

package yapion;

import java.util.function.Consumer;

public class YAPIONGenerator {

    public static void main(String[] args) {
        YAPIONGenerator yapionGenerator = new YAPIONGenerator(System.out::println);
        yapionGenerator.startObject()
                .add("name", "yoyosource")
                .add("age", "18")
                .endObject();
    }

    private Consumer<String> sink;

    public YAPIONGenerator(Consumer<String> sink) {
        this.sink = sink;
    }

    public YAPIONGenerator startObject() {
        sink.accept("{");
        return this;
    }

    public YAPIONGenerator endObject() {
        sink.accept("}");
        return this;
    }

    public YAPIONGenerator startArray() {
        sink.accept("[");
        return this;
    }

    public YAPIONGenerator endArray() {
        sink.accept("]");
        return this;
    }

    public YAPIONGenerator add(String key, String value) {
        sink.accept(key + "(" + value + ")");
        return this;
    }

    public YAPIONGenerator add(String value) {
        sink.accept(value);
        return this;
    }
}
