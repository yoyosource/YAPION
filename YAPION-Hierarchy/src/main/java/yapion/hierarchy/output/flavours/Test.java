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

import yapion.hierarchy.output.StringOutput;
import yapion.hierarchy.types.*;

public class Test {

    public static void main(String[] args) {
        YAPIONObject yapionObject = new YAPIONObject();
        yapionObject.add("test", "test");
        yapionObject.add("test2", "test2");
        yapionObject.add("test3", "test3");
        yapionObject.add("test4", new YAPIONArray()
                .add(new YAPIONObject())
                /*.add(new YAPIONMap()
                        .add("greetings", "hello")
                )
                */
                .add(new YAPIONPointer(yapionObject))
                // .addComment("Just a comment")
                .add("Hello World")
                .add('c')
                .add(0)
                .add(0.0)
                .add(true)
                .add(false)
                .add(new YAPIONValue<>(null))
                .add(0.0F)
                .add((byte) 0)
                .add((short) 0)
        );
        System.out.println(yapionObject.output(new StringOutput(true), new YAPIONConvertedJSONFlavour()).getResult());
    }
}
