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

package yapion.reference;

import org.junit.Test;
import yapion.hierarchy.types.YAPIONArray;
import yapion.hierarchy.types.YAPIONMap;
import yapion.hierarchy.types.YAPIONObject;
import yapion.hierarchy.types.YAPIONValue;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class ReferenceValueCacheTest {

    @Test
    public void testObjectReferenceValue() {
        assertThat(new YAPIONObject().referenceValue(), is(9163003314768395257L));
    }

    @Test
    public void testMapReferenceValue() {
        assertThat(new YAPIONMap().referenceValue(), is(2978161325094671632L));
    }

    @Test
    public void testArrayReferenceValue() {
        assertThat(new YAPIONArray().referenceValue(), is(8428319342632167454L));
    }

    @Test
    public void testObjectReferenceValueDiscardOnAdd() {
        YAPIONObject yapionObject = new YAPIONObject();
        assertThat(yapionObject.referenceValue(), is(9163003314768395257L));
        yapionObject.add("TEST", new YAPIONValue<>(0));
        assertThat(yapionObject.referenceValue(), not(9163003314768395257L));
    }

    @Test
    public void testObjectReferenceValueDiscardOnAddOrPointer() {
        YAPIONObject yapionObject = new YAPIONObject();
        assertThat(yapionObject.referenceValue(), is(9163003314768395257L));
        yapionObject.putOrPointerAndGetItself("TEST", new YAPIONValue<>(0));
        assertThat(yapionObject.referenceValue(), not(9163003314768395257L));
    }

    @Test
    public void testObjectReferenceValueDiscardOnRemove() {
        YAPIONObject yapionObject = new YAPIONObject();
        yapionObject.add("TEST", new YAPIONValue<>(0));
        assertThat(yapionObject.referenceValue(), not(9163003314768395257L));
        yapionObject.remove("TEST");
        assertThat(yapionObject.referenceValue(), is(9163003314768395257L));
    }

    @Test
    public void testObjectReferenceValueDiscardOnRemoveAndGet() {
        YAPIONObject yapionObject = new YAPIONObject();
        yapionObject.add("TEST", new YAPIONValue<>(0));
        assertThat(yapionObject.referenceValue(), not(9163003314768395257L));
        yapionObject.removeAndGet("TEST");
        assertThat(yapionObject.referenceValue(), is(9163003314768395257L));
    }

    @Test
    public void testMapReferenceValueDiscardOnAdd() {
        YAPIONMap yapionMap = new YAPIONMap();
        assertThat(yapionMap.referenceValue(), is(2978161325094671632L));
        yapionMap.add(new YAPIONValue<>("TEST"), new YAPIONValue<>(0));
        assertThat(yapionMap.referenceValue(), not(2978161325094671632L));
    }

    @Test
    public void testMapReferenceValueDiscardOnAddOrPointer() {
        YAPIONMap yapionMap = new YAPIONMap();
        assertThat(yapionMap.referenceValue(), is(2978161325094671632L));
        yapionMap.putOrPointerAndGetItself(new YAPIONValue<>("TEST"), new YAPIONValue<>(0));
        assertThat(yapionMap.referenceValue(), not(2978161325094671632L));
    }

    @Test
    public void testMapReferenceValueDiscardOnRemove() {
        YAPIONMap yapionMap = new YAPIONMap();
        yapionMap.add(new YAPIONValue<>("TEST"), new YAPIONValue<>(0));
        assertThat(yapionMap.referenceValue(), not(2978161325094671632L));
        yapionMap.remove(new YAPIONValue<>("TEST"));
        assertThat(yapionMap.referenceValue(), is(2978161325094671632L));
    }

    @Test
    public void testMapReferenceValueDiscardOnRemoveAndGet() {
        YAPIONMap yapionMap = new YAPIONMap();
        yapionMap.add(new YAPIONValue<>("TEST"), new YAPIONValue<>(0));
        assertThat(yapionMap.referenceValue(), not(2978161325094671632L));
        yapionMap.removeAndGet(new YAPIONValue<>("TEST"));
        assertThat(yapionMap.referenceValue(), is(2978161325094671632L));
    }

    @Test
    public void testArrayReferenceValueDiscardOnAdd() {
        YAPIONArray yapionArray = new YAPIONArray();
        assertThat(yapionArray.referenceValue(), is(8428319342632167454L));
        yapionArray.add(new YAPIONValue<>(0));
        assertThat(yapionArray.referenceValue(), not(8428319342632167454L));
    }

    @Test
    public void testArrayReferenceValueDiscardOnAddOrPointer() {
        YAPIONArray yapionArray = new YAPIONArray();
        assertThat(yapionArray.referenceValue(), is(8428319342632167454L));
        yapionArray.addOrPointer(new YAPIONValue<>(0));
        assertThat(yapionArray.referenceValue(), not(8428319342632167454L));
    }

    @Test
    public void testArrayReferenceValueDiscardOnSet() {
        YAPIONArray yapionArray = new YAPIONArray();
        yapionArray.add(new YAPIONObject());
        assertThat(yapionArray.referenceValue(), is(855368639465640934L));
        yapionArray.set(0, new YAPIONValue<>(0));
        assertThat(yapionArray.referenceValue(), not(855368639465640934L));
    }

    @Test
    public void testArrayReferenceValueDiscardOnSetOrPointer() {
        YAPIONArray yapionArray = new YAPIONArray();
        yapionArray.add(new YAPIONObject());
        assertThat(yapionArray.referenceValue(), is(855368639465640934L));
        yapionArray.setOrPointer(0, new YAPIONValue<>(0));
        assertThat(yapionArray.referenceValue(), not(855368639465640934L));
    }

    @Test
    public void testArrayReferenceValueDiscardOnAddWithIndex() {
        YAPIONArray yapionArray = new YAPIONArray();
        yapionArray.add(new YAPIONObject());
        assertThat(yapionArray.referenceValue(), is(855368639465640934L));
        yapionArray.add(0, new YAPIONValue<>(0));
        assertThat(yapionArray.referenceValue(), not(855368639465640934L));
    }

    @Test
    public void testArrayReferenceValueDiscardOnAddWithIndexOrPointer() {
        YAPIONArray yapionArray = new YAPIONArray();
        yapionArray.add(new YAPIONObject());
        assertThat(yapionArray.referenceValue(), is(855368639465640934L));
        yapionArray.putOrPointerAndGetItself(0, new YAPIONValue<>(0));
        assertThat(yapionArray.referenceValue(), not(855368639465640934L));
    }

    @Test
    public void testArrayReferenceValueDiscardOnRemove() {
        YAPIONArray yapionArray = new YAPIONArray();
        yapionArray.add(new YAPIONValue<>(0));
        assertThat(yapionArray.referenceValue(), not(8428319342632167454L));
        yapionArray.remove(0);
        assertThat(yapionArray.referenceValue(), is(8428319342632167454L));
    }

    @Test
    public void testArrayReferenceValueDiscardOnRemoveAndGet() {
        YAPIONArray yapionArray = new YAPIONArray();
        yapionArray.add(new YAPIONValue<>(0));
        assertThat(yapionArray.referenceValue(), not(8428319342632167454L));
        yapionArray.removeAndGet(0);
        assertThat(yapionArray.referenceValue(), is(8428319342632167454L));
    }

}
