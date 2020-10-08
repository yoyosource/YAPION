package yapion.annotation;

import org.junit.Test;
import yapion.exceptions.YAPIONException;
import yapion.hierarchy.types.YAPIONObject;
import yapion.serializing.YAPIONDeserializer;
import yapion.serializing.YAPIONSerializer;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static yapion.annotation.AnnotationTestObjects.*;

public class AnnotationDeserializationTest {

    @Test(expected = YAPIONException.class)
    public void testPreException() {
        YAPIONDeserializer.deserialize(YAPIONSerializer.serialize(new PreTest()), "exception");
    }

    @Test
    public void testPre() {
        YAPIONDeserializer.deserialize(YAPIONSerializer.serialize(new PreTest()), "noException");
    }

    @Test(expected = YAPIONException.class)
    public void testPostException() {
        YAPIONDeserializer.deserialize(YAPIONSerializer.serialize(new PostTest()), "exception");
    }

    @Test
    public void testPost() {
        YAPIONDeserializer.deserialize(YAPIONSerializer.serialize(new PostTest()), "noException");
    }

    @Test
    public void testOptimizeNull() {
        YAPIONObject yapionObject = YAPIONSerializer.serialize(new OptimizeTest(null));
        assertThat(yapionObject.toYAPIONString(), is("{@type(yapion.annotation.AnnotationTestObjects$OptimizeTest)}"));
    }

    @Test
    public void testOptimizeNonNull() {
        YAPIONObject yapionObject = YAPIONSerializer.serialize(new OptimizeTest("yoyosource"));
        assertThat(yapionObject.toYAPIONString(), is("{@type(yapion.annotation.AnnotationTestObjects$OptimizeTest)s(yoyosource)}"));
    }

}
