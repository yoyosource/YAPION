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
import yapion.serializing.data.SerializationContext;
import yapion.serializing.views.View;

public class AnnotationTestObjects {

    @YAPIONData
    public static class PreTest {

        @YAPIONPreDeserialization(context = ExceptionView.class)
        @YAPIONPreSerialization(context = ExceptionView.class)
        private void pre() {
            throw new UnsupportedOperationException();
        }

        @YAPIONPreDeserialization(context = NoExceptionView.class)
        @YAPIONPreSerialization(context = NoExceptionView.class)
        private void preOther() {
            // Ignored
        }

    }

    @YAPIONData
    public static class PostTest {

        @YAPIONPostDeserialization(context = ExceptionView.class)
        @YAPIONPostSerialization(context = ExceptionView.class)
        private void post() {
            throw new UnsupportedOperationException();
        }

        @YAPIONPostDeserialization(context = NoExceptionView.class)
        @YAPIONPostSerialization(context = NoExceptionView.class)
        private void postOther() {
            // Ignored
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
        @YAPIONLoad(context = ObjenesisView.class)
        private final String s = "objenesis";

    }

    @YAPIONData(context = TypeView.class)
    @YAPIONSave(context = FieldOtherView.class)
    public static class FieldTypeTest1 {

        @YAPIONField(context = FieldOtherView.class)
        private final String s = "some-string";

    }

    @YAPIONSave(context = OtherView.class)
    public static class FieldTypeTest2 {

        @YAPIONField(context = FieldOtherView.class)
        private final String s = "some-string";

    }

    @YAPIONData
    public static class ClassC extends ClassB {

        @YAPIONPreSerialization(context = TestView.class)
        private void test() {
            // Ignored
        }

    }

    @YAPIONData
    public static class ClassB extends ClassA {

        @YAPIONPreSerialization
        private void test() {
            // Ignored
        }

    }

    public static class ClassA {

        @YAPIONPreSerialization
        private void test2() {
            throw new UnsupportedOperationException();
        }

    }

    @YAPIONData
    public static class TestPreAndPostAnnotation {

        @YAPIONPreSerialization
        private void preSerialization(SerializationContext serializationContext) {
            serializationContext.getYapionObject().add("pre", 1);
        }

        @YAPIONPostSerialization
        private void postSerialization(SerializationContext serializationContext) {
            serializationContext.getYapionObject().add("post", 1);
        }
    }

    public static class ExceptionView implements View {
    }

    public static class NoExceptionView implements View {
    }

    public static class ObjenesisView implements View {
    }

    public static class TypeView implements View {
    }

    public static class FieldOtherView implements View {
    }

    public static class OtherView implements View {
    }

    public static class TestView implements View {
    }
}
