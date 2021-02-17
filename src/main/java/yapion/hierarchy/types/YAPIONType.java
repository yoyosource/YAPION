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

package yapion.hierarchy.types;

public enum YAPIONType {

    ANY(0x21b7bcb508370b96L, "ANY"),

    OBJECT(0xff2986f7947f3bf9L, "OBJ"),
    ARRAY(0xf4f767c97724e81eL, "ARR"),
    MAP(0x29548df23a131510L, "MAP"),
    POINTER(0x4e958af3539fb57cL, "PNR"),
    VALUE(0xf86bfba6285c9be2L, "VAL");

    private final long referenceValue;
    private final String name;

    YAPIONType(long referenceValue, String name) {
        this.referenceValue = referenceValue;
        this.name = name;
    }

    public long getReferenceValue() {
        return referenceValue;
    }

    public String getName() {
        return name;
    }

}
