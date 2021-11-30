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

public class XMLFlavour implements Flavour {

    @Override
    public PrettifyBehaviour getPrettifyBehaviour() {
        return PrettifyBehaviour.CHOOSEABLE;
    }

    @Override
    public String header() {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>";
    }

    @Override
    public String beginObject() {
        return null;
    }

    @Override
    public String objectKeyPairStart(String key) {
        return "<" + key + ">";
    }

    @Override
    public String objectKeyPairEnd(String key) {
        return "</" + key + ">";
    }

    @Override
    public String endObject() {
        return null;
    }

    @Override
    public String beginArray() {
        return null;
    }

    @Override
    public String arrayElementBegin(int index) {
        return "<element>";
    }

    @Override
    public String arrayElementEnd(int index) {
        return "</element>";
    }

    @Override
    public String endArray() {
        return null;
    }

    @Override
    public String beginValue() {
        return null;
    }

    @Override
    public <T> String value(YAPIONValue<T> value) {
        if (value.get() == null) {
            return "null";
        }
        return value.get().toString();
    }

    @Override
    public String endValue() {
        return null;
    }

    @Override
    public String footer() {
        return "</root>";
    }
}
