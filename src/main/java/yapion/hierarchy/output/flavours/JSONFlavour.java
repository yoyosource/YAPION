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

package yapion.hierarchy.output.flavours;

import yapion.hierarchy.types.YAPIONValue;

public class JSONFlavour implements Flavour {

    @Override
    public PrettifyBehaviour getPrettifyBehaviour() {
        return PrettifyBehaviour.CHOOSEABLE;
    }

    @Override
    public String beginObject() {
        return "{";
    }

    @Override
    public String objectKeyPairStart(String key) {
        return "\"" + key + "\": ";
    }

    @Override
    public String objectFullKeyPairSeparator() {
        return ",";
    }

    @Override
    public String endObject() {
        return "}";
    }

    @Override
    public String beginArray() {
        return "[";
    }

    @Override
    public String arraySeparator() {
        return ",";
    }

    @Override
    public String endArray() {
        return "]";
    }

    @Override
    public String beginValue() {
        return null;
    }

    @Override
    public <T> String value(YAPIONValue<T> input) {
        T value = input.get();
        if (value == null) {
            return "null";
        }
        if (value instanceof String || value instanceof Character) {
            return "\"" + value.toString().replace("\"", "\\\"").replace("\n", "\\n").replace("\r", "\\r").replace("\t", "\\t") + "\"";
        }
        return value.toString();
    }

    @Override
    public String endValue() {
        return null;
    }
}
