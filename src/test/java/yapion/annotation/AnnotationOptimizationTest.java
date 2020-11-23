package yapion.annotation;

import org.junit.Test;
import yapion.hierarchy.output.StringOutput;
import yapion.hierarchy.types.YAPIONObject;
import yapion.serializing.YAPIONSerializer;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static yapion.annotation.AnnotationTestObjects.*;

public class AnnotationOptimizationTest {

    @Test
    public void testOptimizeNull() {
        YAPIONObject yapionObject = YAPIONSerializer.serialize(new OptimizeTest(null));
        assertThat(yapionObject.toYAPION(new StringOutput()).getResult(), is("{@type(yapion.annotation.AnnotationTestObjects$OptimizeTest)}"));
    }

    @Test
    public void testOptimizeNonNull() {
        YAPIONObject yapionObject = YAPIONSerializer.serialize(new OptimizeTest("yoyosource"));
        assertThat(yapionObject.toYAPION(new StringOutput()).getResult(), is("{@type(yapion.annotation.AnnotationTestObjects$OptimizeTest)s(yoyosource)}"));
    }

}
