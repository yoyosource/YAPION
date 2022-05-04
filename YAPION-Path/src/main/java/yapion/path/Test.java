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

package yapion.path;

import yapion.hierarchy.types.YAPIONArray;
import yapion.hierarchy.types.YAPIONObject;
import yapion.hierarchy.types.YAPIONValue;

public class Test {

    public static void main(String[] args) {
        YAPIONObject yapionObject = new YAPIONObject();
        for (int i = 0; i < 100; i++) {
            YAPIONArray yapionArray = new YAPIONArray();
            for (int j = 0; j < 100; j++) {
                yapionArray.add(new YAPIONValue<>(j));
            }
            yapionObject.add("test" + i, yapionArray);
        }

        YAPIONPath yapionPath = new Selector().array().select("test0").select("test1").done().array().select(0).select(1).select(2).done().build();
        System.out.println(yapionPath.apply(yapionObject));
    }
}
