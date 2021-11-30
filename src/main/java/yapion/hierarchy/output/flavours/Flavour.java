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
import yapion.hierarchy.types.YAPIONValue;

public interface Flavour {

    enum PrettifyBehaviour {
        CHOOSEABLE,
        ALWAYS,
        NEVER
    }

    PrettifyBehaviour getPrettifyBehaviour();
    default boolean removeRootObject() {
        return false;
    }

    default String header() {
        return null;
    }

    String beginObject();
    String objectKeyPairStart(String key);
    default String objectKeyPairEnd(String key) {
        return null;
    }
    default String objectFullKeyPairSeparator() {
        return null;
    }
    String endObject();

    String beginArray();
    default String arrayElementBegin(int index) {
        return null;
    }
    default String arrayElementEnd(int index) {
        return null;
    }
    default String arraySeparator() {
        return null;
    }
    default String arrayLastElementSeparatorIfPrettified() {
        return null;
    }
    String endArray();

    default String beginMap() {
        throw new UnsupportedOperationException();
    }
    default String mapSeparator() {
        throw new UnsupportedOperationException();
    }
    default String endMap() {
        throw new UnsupportedOperationException();
    }

    String beginValue();
    <T> String value(YAPIONValue<T> value);
    String endValue();

    default String beginArrayValue() {
        return beginValue();
    }
    default <T> String arrayValue(YAPIONValue<T> value) {
        return value(value);
    }
    default String endArrayValue() {
        return endValue();
    }

    default String beginPointer() {
        throw new UnsupportedOperationException();
    }
    default String pointer(YAPIONPointer yapionPointer) {
        throw new UnsupportedOperationException();
    }
    default String endPointer() {
        throw new UnsupportedOperationException();
    }

    default String beginComment() {
        return null;
    }
    default String comment(String comment) {
        return null;
    }
    default String endComment() {
        return null;
    }

    default String footer() {
        return null;
    }
}
