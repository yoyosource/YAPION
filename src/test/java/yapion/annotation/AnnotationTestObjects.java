package yapion.annotation;

import yapion.annotations.deserialize.YAPIONLoad;
import yapion.annotations.object.*;
import yapion.annotations.serialize.YAPIONOptimize;
import yapion.annotations.serialize.YAPIONSave;

public class AnnotationTestObjects {

    @YAPIONData
    public static class PreTest {

        @YAPIONPreDeserialization(context = {"exception"})
        private void pre() {
            throw new IllegalArgumentException();
        }

        @YAPIONPreDeserialization(context = {"noException"})
        private void preOther() {

        }

    }

    @YAPIONData
    public static class PostTest {

        @YAPIONPostDeserialization(context = {"exception"})
        private void post() {
            throw new IllegalArgumentException();
        }

        @YAPIONPostDeserialization(context = "noException")
        private void postOther() {

        }

    }

    @YAPIONData
    public static class OptimizeTest {

        @YAPIONOptimize
        private String s;

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
        private String s = "objenesis";

    }

    @YAPIONData(context = "type")
    @YAPIONSave(context = "fieldOther")
    public static class FieldTypeTest {

        @YAPIONField(context = {"field", "fieldOther"})
        private String s = "FieldType";

    }

}
