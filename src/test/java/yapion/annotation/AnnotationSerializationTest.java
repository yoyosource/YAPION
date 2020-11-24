package yapion.annotation;

import org.junit.Test;
import yapion.exceptions.YAPIONException;
import yapion.serializing.YAPIONSerializer;

import static yapion.annotation.AnnotationTestObjects.PostTest;
import static yapion.annotation.AnnotationTestObjects.PreTest;

public class AnnotationSerializationTest {

    @Test(expected = YAPIONException.class)
    public void testPreException() {
        YAPIONSerializer.serialize(new PreTest(), "exception");
    }

    @Test
    public void testPre() {
        YAPIONSerializer.serialize(new PreTest(), "noException");
    }

    @Test(expected = YAPIONException.class)
    public void testPostException() {
        YAPIONSerializer.serialize(new PostTest(), "exception");
    }

    @Test
    public void testPost() {
        YAPIONSerializer.serialize(new PostTest(), "noException");
    }

}
