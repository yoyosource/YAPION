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

import lombok.Getter;
import yapion.exceptions.parser.YAPIONParserException;
import yapion.hierarchy.api.groups.YAPIONAnyType;

@Getter
public enum YAPIONType {

    ANY(0x21b7bcb508370b96L, "ANY", (yapionAnyType, s, yapionAnyType2) -> {
        throw new YAPIONParserException("ANY is no YAPIONType to call add on");
    }),

    OBJECT(0xff2986f7947f3bf9L, "OBJ", (yapionAnyType, s, yapionAnyType2) -> {
        ((YAPIONObject) yapionAnyType).getBackedMap().put(s, yapionAnyType2);
        yapionAnyType2.setParent(yapionAnyType);
    }),
    ARRAY(0xf4f767c97724e81eL, "ARR", (yapionAnyType, s, yapionAnyType2) -> {
        ((YAPIONArray) yapionAnyType).getBackedArray().add(yapionAnyType2);
        yapionAnyType2.setParent(yapionAnyType);
    }),
    MAP(0x29548df23a131510L, "MAP", (yapionAnyType, s, yapionAnyType2) -> {
        ((YAPIONMap) yapionAnyType).unsafeAdd(yapionAnyType2);
    }),
    POINTER(0x4e958af3539fb57cL, "PNR", (yapionAnyType, s, yapionAnyType2) -> {
        throw new YAPIONParserException("POINTER is no YAPIONType to call add on");
    }),
    VALUE(0xf86bfba6285c9be2L, "VAL", (yapionAnyType, s, yapionAnyType2) -> {
        throw new YAPIONParserException("VALUE is no YAPIONType to call add on");
    }),

    COMMENT(0x0, "CMT", (yapionAnyType, s, yapionAnyType2) -> {
        throw new YAPIONParserException("COMMENT is no YAPIONType to call add on");
    });

    private final long referenceValue;
    private final String name;
    private final TriConsumer<YAPIONAnyType, String, YAPIONAnyType> addConsumer;

    YAPIONType(long referenceValue, String name, TriConsumer<YAPIONAnyType, String, YAPIONAnyType> addConsumer) {
        this.referenceValue = referenceValue;
        this.name = name;
        this.addConsumer = addConsumer;
    }

    public interface TriConsumer<T, R, K> {
        void accept(T t, R r, K k);
    }
}
