package yapion.annotation;

import org.junit.Test;
import yapion.hierarchy.types.YAPIONObject;
import yapion.serializing.YAPIONDeserializer;
import yapion.serializing.YAPIONSerializer;

import static org.hamcrest.MatcherAssert.assertThat;
import static yapion.annotation.AnnotationTestObjects.ObjenesisTest;
import static yapion.utils.YAPIONAssertion.isYAPION;

public class AnnotationObjensisTest {

    @Test
    public void testObjenesis() {
        YAPIONObject yapionObject = YAPIONSerializer.serialize(new ObjenesisTest());
        assertThat(yapionObject, isYAPION("{@type(yapion.annotation.AnnotationTestObjects$ObjenesisTest)s(objenesis)}"));
        yapionObject = YAPIONSerializer.serialize(YAPIONDeserializer.deserialize(yapionObject, "noObjenesis"));
        assertThat(yapionObject, isYAPION("{@type(yapion.annotation.AnnotationTestObjects$ObjenesisTest)s(null)}"));
    }

}
