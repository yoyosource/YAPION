package yapion.reference;

import org.junit.Test;
import yapion.hierarchy.types.YAPIONArray;
import yapion.hierarchy.types.YAPIONObject;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class ReferenceValueCacheTest {

    @Test
    public void testObjectReferenceValue() {
        YAPIONObject yapionObject = new YAPIONObject();

        long time = System.nanoTime();
        long referenceValue = yapionObject.referenceValue();
        time = System.nanoTime() - time;

        assertThat(referenceValue, is(-60368722086380551L));
        assertThat(time, lessThan(100000000L));
    }

    @Test
    public void testObjectSecondReferenceValue() {
        YAPIONObject yapionObject = new YAPIONObject();
        yapionObject.referenceValue();

        long time = System.nanoTime();
        long referenceValue = yapionObject.referenceValue();
        time = System.nanoTime() - time;

        assertThat(referenceValue, is(-60368722086380551L));
        assertThat(time, lessThan(100000L));
    }

    @Test
    public void testObjectDiscardReferenceValue() {
        YAPIONObject yapionObject = new YAPIONObject();
        yapionObject.referenceValue();
        yapionObject.add("Hello", new YAPIONObject());

        long time = System.nanoTime();
        long referenceValue = yapionObject.referenceValue();
        time = System.nanoTime() - time;

        assertThat(referenceValue, is(-110L));
        assertThat(time, greaterThan(10000L));
    }

    @Test
    public void testObjectDiscardReferenceValueBack() {
        YAPIONObject yapionObject = new YAPIONObject();
        YAPIONArray yapionArray = new YAPIONArray();
        yapionObject.add("array", yapionArray);

        long referenceValue = yapionObject.referenceValue();
        yapionArray.add(new YAPIONObject());
        assertThat(referenceValue, not(yapionObject.referenceValue()));
        assertThat(referenceValue, is(-855368639465640912L));
        assertThat(yapionArray.referenceValue(), is(-8368003397389134876L));
    }

}
