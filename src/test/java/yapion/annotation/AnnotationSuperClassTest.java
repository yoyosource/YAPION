package yapion.annotation;

import org.junit.Test;
import yapion.annotation.AnnotationTestObjects.*;
import yapion.exceptions.utils.YAPIONReflectionInvocationException;
import yapion.serializing.YAPIONSerializer;

public class AnnotationSuperClassTest {

    @Test(expected = YAPIONReflectionInvocationException.class)
    public void testSuperClassCall() {
        ClassB classB = new ClassB();
        YAPIONSerializer.serialize(classB);
    }

    @Test(expected = YAPIONReflectionInvocationException.class)
    public void testSuperClassCallDefaultContext() {
        ClassC classC = new ClassC();
        YAPIONSerializer.serialize(classC);
    }

    @Test
    public void testSuperClassCallOtherContext() {
        ClassC classC = new ClassC();
        YAPIONSerializer.serialize(classC, "other");
    }

}
