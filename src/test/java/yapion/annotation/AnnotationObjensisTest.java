package yapion.annotation;

import org.junit.Test;
import yapion.hierarchy.types.YAPIONObject;
import yapion.serializing.YAPIONDeserializer;
import yapion.serializing.YAPIONSerializer;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static yapion.annotation.AnnotationTestObjects.*;

public class AnnotationObjensisTest {

    @Test
    public void testObjenesis() {
        YAPIONObject yapionObject = YAPIONSerializer.serialize(new ObjenesisTest());
        assertThat(yapionObject.toYAPIONString(), is("{@type(yapion.annotation.AnnotationTestObjects$ObjenesisTest)s(objenesis)}"));
        yapionObject = YAPIONSerializer.serialize(YAPIONDeserializer.deserialize(yapionObject, "noObjenesis"));
        assertThat(yapionObject.toYAPIONString(), is("{@type(yapion.annotation.AnnotationTestObjects$ObjenesisTest)s(null)}"));
    }

}
