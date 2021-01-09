package yapion.annotation;

import org.junit.Test;
import yapion.annotation.AnnotationTestObjects.ClassB;
import yapion.exceptions.utils.YAPIONReflectionInvocationException;
import yapion.serializing.YAPIONSerializer;

public class AnnotationSuperClassTest {

    @Test(expected = YAPIONReflectionInvocationException.class)
    public void testSuperClassCall() {
        ClassB classB = new ClassB();
        YAPIONSerializer.serialize(classB);
    }

}
