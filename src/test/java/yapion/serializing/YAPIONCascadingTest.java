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

package yapion.serializing;

import org.junit.Test;
import yapion.exceptions.serializing.YAPIONSerializerException;
import yapion.hierarchy.types.YAPIONObject;
import yapion.serializing.YAPIONTestObjects.Cascading;
import yapion.serializing.YAPIONTestObjects.NonCascading;
import yapion.serializing.YAPIONTestObjects.NonSaved;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static yapion.YAPIONAssertion.isYAPION;

// TODO: add more tests
public class YAPIONCascadingTest {

    @Test(expected = YAPIONSerializerException.class)
    public void testNonSavedInNonCascading() {
        NonCascading nonCascading = new NonCascading(new NonSaved(null));
        YAPIONSerializer.serialize(nonCascading, "empty");
    }

    @Test
    public void testNonCascadingInCascading() {
        Cascading cascading = new Cascading(new NonSaved(null));
        YAPIONObject yapionObject = YAPIONSerializer.serialize(cascading, "empty");
        assertThat(yapionObject, isYAPION("{@type(yapion.serializing.YAPIONTestObjects$Cascading)cnc1{@type(yapion.serializing.YAPIONTestObjects$NonSaved)cnc1(null)cnc2(null)}cnc2(null)}"));
    }

    @Test
    public void testNotUpCascading() {
        NonCascading nonCascading = new NonCascading(new Cascading(null), new NonSaved(null));
        YAPIONObject yapionObject = YAPIONSerializer.serialize(nonCascading, "empty");
        assertThat(yapionObject, isYAPION("{@type(yapion.serializing.YAPIONTestObjects$NonCascading)cnc1{@type(yapion.serializing.YAPIONTestObjects$Cascading)cnc1(null)cnc2(null)}}"));
    }

    @Test
    public void testNotUpCascadingOther() {
        NonCascading nonCascading = new NonCascading(new Cascading(null), new NonSaved(null));
        YAPIONObject yapionObject = YAPIONSerializer.serialize(nonCascading);
        assertThat(yapionObject, isYAPION("{@type(yapion.serializing.YAPIONTestObjects$NonCascading)cnc1{@type(yapion.serializing.YAPIONTestObjects$Cascading)cnc1(null)cnc2(null)}}"));
    }

    @Test
    public void testNonSavedInNonSavedInCascading() {
        Cascading cascading = new Cascading(new NonSaved(new NonSaved(null)));
        YAPIONObject yapionObject = YAPIONSerializer.serialize(cascading, "empty");
        assertThat(yapionObject, isYAPION("{@type(yapion.serializing.YAPIONTestObjects$Cascading)cnc1{@type(yapion.serializing.YAPIONTestObjects$NonSaved)cnc1{@type(yapion.serializing.YAPIONTestObjects$NonSaved)cnc1(null)cnc2(null)}cnc2(null)}cnc2(null)}"));
    }

}
