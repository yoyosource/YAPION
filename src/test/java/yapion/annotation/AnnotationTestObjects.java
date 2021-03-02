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

package yapion.annotation;

import yapion.annotations.deserialize.YAPIONLoad;
import yapion.annotations.object.*;
import yapion.annotations.serialize.YAPIONOptimize;
import yapion.annotations.serialize.YAPIONSave;

public class AnnotationTestObjects {

    @YAPIONData
    public static class PreTest {

        @YAPIONPreDeserialization(context = {"exception"})
        @YAPIONPreSerialization(context = {"exception"})
        private void pre() {
            throw new UnsupportedOperationException();
        }

        @YAPIONPreDeserialization(context = {"noException"})
        @YAPIONPreSerialization(context = {"noException"})
        private void preOther() {

        }

    }

    @YAPIONData
    public static class PostTest {

        @YAPIONPostDeserialization(context = {"exception"})
        @YAPIONPostSerialization(context = {"exception"})
        private void post() {
            throw new UnsupportedOperationException();
        }

        @YAPIONPostDeserialization(context = "noException")
        @YAPIONPostSerialization(context = {"noException"})
        private void postOther() {

        }

    }

    @YAPIONData
    public static class OptimizeTest {

        @YAPIONOptimize
        private final String s;

        public OptimizeTest(String s) {
            this.s = s;
        }

    }

    @YAPIONObjenesis
    @YAPIONSave
    @YAPIONLoad
    public static class ObjenesisTest {

        @YAPIONSave
        @YAPIONLoad(context = {"objenesis"})
        private final String s = "objenesis";

    }

    @YAPIONData(context = "type")
    @YAPIONSave(context = "fieldOther")
    public static class FieldTypeTest1 {

        @YAPIONField(context = {"fieldOther"})
        private final String s = "some-string";

    }

    @YAPIONSave(context = "other")
    public static class FieldTypeTest2 {

        @YAPIONField(context = {"fieldOther"})
        private final String s = "some-string";

    }

    @YAPIONData
    public static class ClassC extends ClassB {

        @YAPIONPreSerialization(context = "Test")
        private void test() {

        }

    }

    @YAPIONData
    public static class ClassB extends ClassA {

        @YAPIONPreSerialization
        private void test() {

        }

    }

    public static class ClassA {

        @YAPIONPreSerialization
        private void test2() {
            throw new UnsupportedOperationException();
        }

    }

}
