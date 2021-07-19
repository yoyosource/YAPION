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

import java.math.BigDecimal;
import java.math.BigInteger;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class ReferenceValueTest {

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
    public void testVStringReferenceValue() {
        assertThat(new YAPIONValue<>("").referenceValue(), is(4439669832890617833L));
    }

    @Test
    public void testVCharacterReferenceValue() {
        assertThat(new YAPIONValue<>(' ').referenceValue(), is(2414206669692766399L));
    }

    @Test
    public void testVBooleanReferenceValue() {
        assertThat(new YAPIONValue<>(false).referenceValue(), is(624264244984273665L));
    }

    @Test
    public void testVByteReferenceValue() {
        assertThat(new YAPIONValue<>((byte)0).referenceValue(), is(985391121155152084L));
    }

    @Test
    public void testVShortReferenceValue() {
        assertThat(new YAPIONValue<>((short)0).referenceValue(), is(3933871441497133323L));
    }

    @Test
    public void testVIntReferenceValue() {
        assertThat(new YAPIONValue<>(0).referenceValue(), is(4078542956719737349L));
    }

    @Test
    public void testVLongReferenceValue() {
        assertThat(new YAPIONValue<>(0L).referenceValue(), is(985391121155152080L));
    }

    @Test
    public void testVBIntReferenceValue() {
        assertThat(new YAPIONValue<>(BigInteger.ZERO).referenceValue(), is(262015884133445292L));
    }

    @Test
    public void testVFloatReferenceValue() {
        assertThat(new YAPIONValue<>(0F).referenceValue(), is(3933871441497133357L));
    }

    @Test
    public void testVDoubleReferenceValue() {
        assertThat(new YAPIONValue<>(0D).referenceValue(), is(3571614250193028107L));
    }

    @Test
    public void testVBDoubleReferenceValue() {
        assertThat(new YAPIONValue<>(BigDecimal.ZERO).referenceValue(), is(2287479047331297305L));
    }

}
