package yapion.annotation;

import org.junit.Test;
import yapion.exceptions.serializing.YAPIONSerializerException;
import yapion.hierarchy.types.YAPIONObject;
import yapion.serializing.YAPIONSerializer;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static yapion.annotation.AnnotationTestObjects.FieldTypeTest1;
import static yapion.annotation.AnnotationTestObjects.FieldTypeTest2;

public class AnnotationFieldTypeTest {

    @Test(expected = YAPIONSerializerException.class)
    public void testSerializeStateNotDefinedAsClassContext() {
        // No context with name "field" defined in @YAPIONData or @YAPIONSave
        YAPIONObject yapionObject = YAPIONSerializer.serialize(new FieldTypeTest1(), "field");
    }

    @Test
    public void testSerializeStateDefinedAsClassContextInSave() {
        // A context with name "fieldOther" defined in @YAPIONSave (but not in @YAPIONData, but this does not matter):
        // Field IS serialized
        YAPIONObject yapionObject = YAPIONSerializer.serialize(new FieldTypeTest1(), "fieldOther");
        assertThat(yapionObject.toYAPIONString(), is("{@type(yapion.annotation.AnnotationTestObjects$FieldTypeTest1)s(some-string)}"));
    }

    @Test
    public void testSerializeStateDefinedAsClassContextInDataOverwriteFieldAnnotation() {
        // A context with name "type" defined in @YAPIONData (but not in @YAPIONSave, but this does not matter):
        // Field IS serialized
        YAPIONObject yapionObject = YAPIONSerializer.serialize(new FieldTypeTest1(), "type");
        assertThat(yapionObject.toYAPIONString(), is("{@type(yapion.annotation.AnnotationTestObjects$FieldTypeTest1)s(some-string)}"));
    }

    @Test
    public void testSerializeStateDefinedAsClassContextInSaveNotOverwritingFieldAnnotation() {
        // A context with name "other" defined in @YAPIONSave, but not in @YAPIONField:
        // Field is NOT serialized
        YAPIONObject yapionObject = YAPIONSerializer.serialize(new FieldTypeTest2(), "other");
        assertThat(yapionObject.toYAPIONString(), is("{@type(yapion.annotation.AnnotationTestObjects$FieldTypeTest2)}"));
    }

}
