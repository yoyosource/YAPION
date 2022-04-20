/*
 * Copyright 2019,2020,2021 yoyosource
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package yapion.annotation;

import org.junit.Test;
import yapion.exceptions.serializing.YAPIONSerializerException;
import yapion.hierarchy.types.YAPIONObject;
import yapion.serializing.YAPIONSerializer;
import yapion.serializing.views.View;

import javax.swing.text.FieldView;

import static org.hamcrest.MatcherAssert.assertThat;
import static yapion.YAPIONAssertion.isYAPION;
import static yapion.annotation.AnnotationTestObjects.FieldTypeTest1;
import static yapion.annotation.AnnotationTestObjects.FieldTypeTest2;

public class AnnotationFieldTypeTest {

    @Test(expected = YAPIONSerializerException.class)
    public void testSerializeStateNotDefinedAsClassContext() {
        // No context with name "field" defined in @YAPIONData or @YAPIONSave
        YAPIONObject yapionObject = YAPIONSerializer.serialize(new FieldTypeTest1(), FieldView.class);
    }

    public static class FieldView implements View {
    }

    @Test
    public void testSerializeStateDefinedAsClassContextInSave() {
        // A context with name "fieldOther" defined in @YAPIONSave (but not in @YAPIONData, but this does not matter):
        // Field IS serialized
        YAPIONObject yapionObject = YAPIONSerializer.serialize(new FieldTypeTest1(), AnnotationTestObjects.FieldOtherView.class);
        assertThat(yapionObject, isYAPION("{@type(yapion.annotation.AnnotationTestObjects$FieldTypeTest1)s(some-string)}"));
    }

    @Test
    public void testSerializeStateDefinedAsClassContextInDataOverwriteFieldAnnotation() {
        // A context with name "type" defined in @YAPIONData (but not in @YAPIONSave, but this does not matter):
        // Field IS serialized
        YAPIONObject yapionObject = YAPIONSerializer.serialize(new FieldTypeTest1(), AnnotationTestObjects.TypeView.class);
        assertThat(yapionObject, isYAPION("{@type(yapion.annotation.AnnotationTestObjects$FieldTypeTest1)s(some-string)}"));
    }

    @Test
    public void testSerializeStateDefinedAsClassContextInSaveNotOverwritingFieldAnnotation() {
        // A context with name "other" defined in @YAPIONSave, but not in @YAPIONField:
        // Field is NOT serialized
        YAPIONObject yapionObject = YAPIONSerializer.serialize(new FieldTypeTest2(), AnnotationTestObjects.OtherView.class);
        assertThat(yapionObject, isYAPION("{@type(yapion.annotation.AnnotationTestObjects$FieldTypeTest2)}"));
    }

}
