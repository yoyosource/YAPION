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
import yapion.hierarchy.types.YAPIONObject;
import yapion.serializing.YAPIONDeserializer;
import yapion.serializing.YAPIONSerializer;

import static org.hamcrest.MatcherAssert.assertThat;
import static yapion.annotation.AnnotationTestObjects.ObjenesisTest;
import static yapion.YAPIONAssertion.isYAPION;

public class AnnotationObjensisTest {

    @Test
    public void testObjenesis() {
        YAPIONObject yapionObject = YAPIONSerializer.serialize(new ObjenesisTest());
        assertThat(yapionObject, isYAPION("{@type(yapion.annotation.AnnotationTestObjects$ObjenesisTest)s(objenesis)}"));
        yapionObject = YAPIONSerializer.serialize(YAPIONDeserializer.deserialize(yapionObject, "noObjenesis"));
        assertThat(yapionObject, isYAPION("{@type(yapion.annotation.AnnotationTestObjects$ObjenesisTest)s(null)}"));
    }

}
