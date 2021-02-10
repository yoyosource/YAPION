package yapion.reference;

import org.junit.Test;
import yapion.hierarchy.types.YAPIONArray;
import yapion.hierarchy.types.YAPIONMap;
import yapion.hierarchy.types.YAPIONObject;
import yapion.hierarchy.types.YAPIONValue;

import java.awt.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class ReferenceValueCacheTest {

    @Test
    public void testObjectReferenceValue() {
        assertThat(new YAPIONObject().referenceValue(), is(-60368722086380551L));
    }

    @Test
    public void testMapReferenceValue() {
        assertThat(new YAPIONMap().referenceValue(), is(2978161325094671632L));
    }

    @Test
    public void testArrayReferenceValue() {
        assertThat(new YAPIONArray().referenceValue(), is(-795052694222608354L));
    }

    @Test
    public void testObjectReferenceValueDiscardOnAdd() {
        YAPIONObject yapionObject = new YAPIONObject();
        assertThat(yapionObject.referenceValue(), is(-60368722086380551L));
        yapionObject.add("TEST", new YAPIONValue<>(0));
        assertThat(yapionObject.referenceValue(), not(-60368722086380551L));
    }

    @Test
    public void testObjectReferenceValueDiscardOnAddOrPointer() {
        YAPIONObject yapionObject = new YAPIONObject();
        assertThat(yapionObject.referenceValue(), is(-60368722086380551L));
        yapionObject.addOrPointer("TEST", new YAPIONValue<>(0));
        assertThat(yapionObject.referenceValue(), not(-60368722086380551L));
    }

    @Test
    public void testObjectReferenceValueDiscardOnRemove() {
        YAPIONObject yapionObject = new YAPIONObject();
        yapionObject.add("TEST", new YAPIONValue<>(0));
        assertThat(yapionObject.referenceValue(), not(-60368722086380551L));
        yapionObject.remove("TEST");
        assertThat(yapionObject.referenceValue(), is(-60368722086380551L));
    }

    @Test
    public void testObjectReferenceValueDiscardOnRemoveAndGet() {
        YAPIONObject yapionObject = new YAPIONObject();
        yapionObject.add("TEST", new YAPIONValue<>(0));
        assertThat(yapionObject.referenceValue(), not(-60368722086380551L));
        yapionObject.removeAndGet("TEST");
        assertThat(yapionObject.referenceValue(), is(-60368722086380551L));
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
        yapionMap.addOrPointer(new YAPIONValue<>("TEST"), new YAPIONValue<>(0));
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
        assertThat(yapionArray.referenceValue(), is(-795052694222608354L));
        yapionArray.add(new YAPIONValue<>(0));
        assertThat(yapionArray.referenceValue(), not(-795052694222608354L));
    }

    @Test
    public void testArrayReferenceValueDiscardOnAddOrPointer() {
        YAPIONArray yapionArray = new YAPIONArray();
        assertThat(yapionArray.referenceValue(), is(-795052694222608354L));
        yapionArray.addOrPointer(new YAPIONValue<>(0));
        assertThat(yapionArray.referenceValue(), not(-795052694222608354L));
    }

    @Test
    public void testArrayReferenceValueDiscardOnSet() {
        YAPIONArray yapionArray = new YAPIONArray();
        yapionArray.add(new YAPIONObject());
        assertThat(yapionArray.referenceValue(), is(-8368003397389134874L));
        yapionArray.set(0, new YAPIONValue<>(0));
        assertThat(yapionArray.referenceValue(), not(-8368003397389134874L));
    }

    @Test
    public void testArrayReferenceValueDiscardOnSetOrPointer() {
        YAPIONArray yapionArray = new YAPIONArray();
        yapionArray.add(new YAPIONObject());
        assertThat(yapionArray.referenceValue(), is(-8368003397389134874L));
        yapionArray.setOrPointer(0, new YAPIONValue<>(0));
        assertThat(yapionArray.referenceValue(), not(-8368003397389134874L));
    }

    @Test
    public void testArrayReferenceValueDiscardOnAddWithIndex() {
        YAPIONArray yapionArray = new YAPIONArray();
        yapionArray.add(new YAPIONObject());
        assertThat(yapionArray.referenceValue(), is(-8368003397389134874L));
        yapionArray.add(0, new YAPIONValue<>(0));
        assertThat(yapionArray.referenceValue(), not(-8368003397389134874L));
    }

    @Test
    public void testArrayReferenceValueDiscardOnAddWithIndexOrPointer() {
        YAPIONArray yapionArray = new YAPIONArray();
        yapionArray.add(new YAPIONObject());
        assertThat(yapionArray.referenceValue(), is(-8368003397389134874L));
        yapionArray.addOrPointer(0, new YAPIONValue<>(0));
        assertThat(yapionArray.referenceValue(), not(-8368003397389134874L));
    }

    @Test
    public void testArrayReferenceValueDiscardOnRemove() {
        YAPIONArray yapionArray = new YAPIONArray();
        yapionArray.add(new YAPIONValue<>(0));
        assertThat(yapionArray.referenceValue(), not(-795052694222608354L));
        yapionArray.remove(0);
        assertThat(yapionArray.referenceValue(), is(-795052694222608354L));
    }

    @Test
    public void testArrayReferenceValueDiscardOnRemoveAndGet() {
        YAPIONArray yapionArray = new YAPIONArray();
        yapionArray.add(new YAPIONValue<>(0));
        assertThat(yapionArray.referenceValue(), not(-795052694222608354L));
        yapionArray.removeAndGet(0);
        assertThat(yapionArray.referenceValue(), is(-795052694222608354L));
    }

}
