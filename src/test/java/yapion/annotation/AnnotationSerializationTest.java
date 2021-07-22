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
import yapion.annotation.AnnotationTestObjects.TestPreAndPostAnnotation;
import yapion.exceptions.YAPIONException;
import yapion.hierarchy.types.YAPIONObject;
import yapion.serializing.YAPIONSerializer;

import static org.hamcrest.MatcherAssert.assertThat;
import static yapion.YAPIONAssertion.isYAPION;
import static yapion.annotation.AnnotationTestObjects.PostTest;
import static yapion.annotation.AnnotationTestObjects.PreTest;

public class AnnotationSerializationTest {

    @Test(expected = YAPIONException.class)
    public void testPreException() {
        YAPIONSerializer.serialize(new PreTest(), "exception");
    }

    @Test
    public void testPre() {
        YAPIONSerializer.serialize(new PreTest(), "noException");
    }

    @Test(expected = YAPIONException.class)
    public void testPostException() {
        YAPIONSerializer.serialize(new PostTest(), "exception");
    }

    @Test
    public void testPost() {
        YAPIONSerializer.serialize(new PostTest(), "noException");
    }

    @Test
    public void testYAPIONSerDeAnnotations() {
        YAPIONObject yapionObject = YAPIONSerializer.serialize(new TestPreAndPostAnnotation());
        assertThat(yapionObject, isYAPION("{@type(yapion.serializing.YAPIONTestObjects$TestPreAndPostAnnotation)pre(1)post(1)}"));
    }

}
