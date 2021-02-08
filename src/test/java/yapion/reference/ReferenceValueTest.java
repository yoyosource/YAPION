package yapion.reference;

import org.junit.Test;
import yapion.hierarchy.types.YAPIONArray;
import yapion.hierarchy.types.YAPIONMap;
import yapion.hierarchy.types.YAPIONObject;
import yapion.hierarchy.types.YAPIONValue;

import java.math.BigDecimal;
import java.math.BigInteger;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class ReferenceValueTest {

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
    public void testVStringReferenceValue() {
        assertThat(new YAPIONValue<>("").referenceValue(), is(-8852853582040250046L));
    }

    @Test
    public void testVCharacterReferenceValue() {
        assertThat(new YAPIONValue<>(' ').referenceValue(), is(-8852853577507325300L));
    }

    @Test
    public void testVBooleanReferenceValue() {
        assertThat(new YAPIONValue<>(false).referenceValue(), is(-8852853582040250046L));
    }

    @Test
    public void testVByteReferenceValue() {
        assertThat(new YAPIONValue<>((byte)0).referenceValue(), is(-8852853567501013466L));
    }

    @Test
    public void testVShortReferenceValue() {
        assertThat(new YAPIONValue<>((short)0).referenceValue(), is(-8852853567501013466L));
    }

    @Test
    public void testVIntReferenceValue() {
        assertThat(new YAPIONValue<>(0).referenceValue(), is(-8852853582040250046L));
    }

    @Test
    public void testVLongReferenceValue() {
        assertThat(new YAPIONValue<>(0L).referenceValue(), is(-8852853567501013466L));
    }

    @Test
    public void testVBIntReferenceValue() {
        assertThat(new YAPIONValue<>(BigInteger.ZERO).referenceValue(), is(-8852853576490904008L));
    }

    @Test
    public void testVFloatReferenceValue() {
        assertThat(new YAPIONValue<>(0F).referenceValue(), is(-8852853567501013466L));
    }

    @Test
    public void testVDoubleReferenceValue() {
        assertThat(new YAPIONValue<>(0D).referenceValue(), is(-8852853582040250046L));
    }

    @Test
    public void testVBDoubleReferenceValue() {
        assertThat(new YAPIONValue<>(BigDecimal.ZERO).referenceValue(), is(-8852853576490904008L));
    }

}
