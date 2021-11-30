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

import yapion.hierarchy.types.YAPIONPointer;
import yapion.hierarchy.types.YAPIONType;
import yapion.hierarchy.types.YAPIONValue;
import yapion.hierarchy.types.value.ValueUtils;

public class YAPIONFlavour implements Flavour {

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
        if (key.startsWith(" ") || key.startsWith(",")) {
            return "\\" + key;
        }
        return key;
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
    public String arrayLastElementSeparatorIfPrettified() {
        return ",";
    }

    @Override
    public String endArray() {
        return "]";
    }

    @Override
    public String beginMap() {
        return "<";
    }

    @Override
    public String mapSeparator() {
        return ":";
    }

    @Override
    public String endMap() {
        return ">";
    }

    @Override
    public String beginValue() {
        return "(";
    }

    @Override
    public <T> String value(YAPIONValue<T> value) {
        return value.getValueHandler().output(value.get(), YAPIONType.VALUE);
    }

    @Override
    public String endValue() {
        return ")";
    }

    @Override
    public String beginArrayValue() {
        return null;
    }

    @Override
    public <T> String arrayValue(YAPIONValue<T> value) {
        String string = value.getValueHandler().output(value.get(), YAPIONType.ARRAY);
        if (string.startsWith(" ") || string.startsWith("-")) {
            string = "\\" + string;
        }
        if (ValueUtils.startsWith(string, ValueUtils.EscapeCharacters.KEY) || string.isEmpty()) {
            return beginValue() + string + endValue();
        } else {
            return string;
        }
    }

    @Override
    public String endArrayValue() {
        return null;
    }

    @Override
    public String beginPointer() {
        return "->";
    }

    @Override
    public String pointer(YAPIONPointer yapionPointer) {
        return yapionPointer.getPointerIDString();
    }

    @Override
    public String endPointer() {
        return null;
    }

    @Override
    public String beginComment() {
        return "/*";
    }

    @Override
    public String comment(String comment) {
        return comment;
    }

    @Override
    public String endComment() {
        return "*/";
    }
}
