package yapion.annotation;

import org.junit.Test;
import yapion.exceptions.utils.YAPIONReflectionException;
import yapion.serializing.YAPIONDeserializer;
import yapion.serializing.YAPIONSerializer;

import static yapion.annotation.AnnotationTestObjects.PostTest;
import static yapion.annotation.AnnotationTestObjects.PreTest;

public class AnnotationDeserializationTest {

    @Test(expected = YAPIONReflectionException.class)
    public void testPreException() {
        YAPIONDeserializer.deserialize(YAPIONSerializer.serialize(new PreTest()), "exception");
    }

    @Test
    public void testPre() {
        YAPIONDeserializer.deserialize(YAPIONSerializer.serialize(new PreTest()), "noException");
    }

    @Test(expected = YAPIONReflectionException.class)
    public void testPostException() {
        YAPIONDeserializer.deserialize(YAPIONSerializer.serialize(new PostTest()), "exception");
    }

    @Test
    public void testPost() {
        YAPIONDeserializer.deserialize(YAPIONSerializer.serialize(new PostTest()), "noException");
    }

}
