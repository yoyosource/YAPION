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

package yapion.serializing;

import yapion.annotations.object.YAPIONData;
import yapion.annotations.registration.YAPIONSerializing;

@YAPIONSerializing(serializationStep = false, deserializationStep = false)
@YAPIONData
public class Test {

    public static void main(String[] args) {
        GeneratedSerializerLoader.addClass(Test.class);
        Test test = new Test();
        YAPIONSerializer.serialize(test);
        long time = System.currentTimeMillis();
        // 54.5ms - without serializer loading
        // 9.39ms - with serializer loading
        for (int j = 0; j < 100; j++) {
            for (int i = 0; i < 10000; i++) {
                YAPIONSerializer.serialize(test);
            }
        }
        System.out.println((System.currentTimeMillis() - time) / 100.0 + "ms");
    }

    private int i = 0;
    private int j = 0;
}
