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

import lombok.AllArgsConstructor;
import yapion.annotations.object.YAPIONData;
import yapion.hierarchy.types.YAPIONObject;
import yapion.serializing.TypeReMapper;
import yapion.serializing.YAPIONDeserializer;
import yapion.serializing.YAPIONSerializer;

public class WeirdDeserializer {

    @AllArgsConstructor
    @YAPIONData
    private static class Test {
        private String s;
    }

    @AllArgsConstructor
    @YAPIONData
    private static class TestRemapped {
        private String s;
    }

    public static void main(String[] args) {
        TypeReMapper typeReMapper = new TypeReMapper();
        typeReMapper.addClassMapping(Test.class, TestRemapped.class);

        YAPIONObject yapionObject = YAPIONSerializer.serialize(new Test("Hello World!"));
        System.out.println(yapionObject);
        System.out.println((TestRemapped) YAPIONDeserializer.deserialize(yapionObject, typeReMapper));
        System.out.println(yapionObject);
    }
}
