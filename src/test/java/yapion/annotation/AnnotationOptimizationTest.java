package yapion.annotation;

import org.junit.Test;
import yapion.hierarchy.types.YAPIONObject;
import yapion.serializing.YAPIONSerializer;

import static org.hamcrest.MatcherAssert.assertThat;
import static yapion.annotation.AnnotationTestObjects.OptimizeTest;
import static yapion.utils.YAPIONAssertion.isYAPION;

public class AnnotationOptimizationTest {

    @Test
    public void testOptimizeNull() {
        YAPIONObject yapionObject = YAPIONSerializer.serialize(new OptimizeTest(null));
        assertThat(yapionObject, isYAPION("{@type(yapion.annotation.AnnotationTestObjects$OptimizeTest)}"));
    }

    @Test
    public void testOptimizeNonNull() {
        YAPIONObject yapionObject = YAPIONSerializer.serialize(new OptimizeTest("yoyosource"));
        assertThat(yapionObject, isYAPION("{@type(yapion.annotation.AnnotationTestObjects$OptimizeTest)s(yoyosource)}"));
    }

}
