package yapion.annotation;

import org.junit.Test;
import yapion.hierarchy.types.YAPIONObject;
import yapion.serializing.YAPIONSerializer;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static yapion.annotation.AnnotationTestObjects.*;

public class AnnotationFieldTypeTest {

    @Test
    public void testField() {
        YAPIONObject yapionObject = YAPIONSerializer.serialize(new FieldTypeTest(), "field");
        assertThat(yapionObject, is(nullValue()));
    }

    @Test
    public void testFieldOther() {
        YAPIONObject yapionObject = YAPIONSerializer.serialize(new FieldTypeTest(), "fieldOther");
        assertThat(yapionObject.toYAPIONString(), is("{@type(yapion.annotation.AnnotationTestObjects$FieldTypeTest)s(FieldType)}"));
    }

    @Test
    public void testType() {
        YAPIONObject yapionObject = YAPIONSerializer.serialize(new FieldTypeTest(), "type");
        assertThat(yapionObject.toYAPIONString(), is("{@type(yapion.annotation.AnnotationTestObjects$FieldTypeTest)s(FieldType)}"));
    }

}
