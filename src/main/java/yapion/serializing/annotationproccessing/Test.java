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

package yapion.serializing.annotationproccessing;

import yapion.annotations.registration.YAPIONSerializing;
import yapion.annotations.serialize.YAPIONOptimize;
import yapion.annotations.serialize.YAPIONSaveExclude;

@YAPIONSerializing
public class Test {
    private int test = 0;
    @YAPIONOptimize(context = {"Hello"})
    private Integer hugo = null;
    @YAPIONSaveExclude(context = {"Hello"})
    protected final int k = 0;

    public Test(int test, Integer hugo) {
        this.test = test;
        this.hugo = hugo;
    }

    public Test(int test) {
        this.test = test;
    }

    public Test(Integer hugo) {
        this.hugo = hugo;
    }
}
