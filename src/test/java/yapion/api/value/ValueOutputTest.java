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

package yapion.api.value;

import org.junit.Test;
import yapion.hierarchy.output.StringOutput;
import yapion.hierarchy.types.YAPIONValue;

import java.math.BigDecimal;
import java.math.BigInteger;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class ValueOutputTest {

    @Test
    public void testYAPIONString() {
        assertThat(new YAPIONValue<>("").toYAPION(new StringOutput()).getResult(), is("()"));
    }

    @Test
    public void testJSONString() {
        assertThat(new YAPIONValue<>("").toJSON(new StringOutput()).getResult(), is("\"\""));
    }

    @Test
    public void testLossyJSONString() {
        assertThat(new YAPIONValue<>("").toJSONLossy(new StringOutput()).getResult(), is("\"\""));
    }

    @Test
    public void testYAPIONStringNumber() {
        assertThat(new YAPIONValue<>("0").toYAPION(new StringOutput()).getResult(), is("(\"0\")"));
    }

    @Test
    public void testJSONStringNumber() {
        assertThat(new YAPIONValue<>("0").toJSON(new StringOutput()).getResult(), is("\"0\""));
    }

    @Test
    public void testLossyJSONStringNumber() {
        assertThat(new YAPIONValue<>("0").toJSONLossy(new StringOutput()).getResult(), is("\"0\""));
    }

    @Test
    public void testYAPIONStringBoolean() {
        assertThat(new YAPIONValue<>("true").toYAPION(new StringOutput()).getResult(), is("(\"true\")"));
    }

    @Test
    public void testJSONStringBoolean() {
        assertThat(new YAPIONValue<>("true").toJSON(new StringOutput()).getResult(), is("\"true\""));
    }

    @Test
    public void testLossyJSONStringBoolean() {
        assertThat(new YAPIONValue<>("true").toJSONLossy(new StringOutput()).getResult(), is("\"true\""));
    }

    @Test
    public void testYAPIONStringParenthesis() {
        assertThat(new YAPIONValue<>("()[]{}<>").toYAPION(new StringOutput()).getResult(), is("(\\(\\)[]{}<>)"));
    }

    @Test
    public void testJSONStringParenthesis() {
        assertThat(new YAPIONValue<>("()[]{}<>").toJSON(new StringOutput()).getResult(), is("\"()[]{}<>\""));
    }

    @Test
    public void testLossyJSONStringParenthesis() {
        assertThat(new YAPIONValue<>("()[]{}<>").toJSONLossy(new StringOutput()).getResult(), is("\"()[]{}<>\""));
    }

    @Test
    public void testYAPIONStringQuotes() {
        assertThat(new YAPIONValue<>("\"").toYAPION(new StringOutput()).getResult(), is("(\"\"\")"));
    }

    @Test
    public void testJSONStringQuotes() {
        assertThat(new YAPIONValue<>("\"").toJSON(new StringOutput()).getResult(), is("\"\\\"\""));
    }

    @Test
    public void testLossyJSONStringQuotes() {
        assertThat(new YAPIONValue<>("\"").toJSONLossy(new StringOutput()).getResult(), is("\"\\\"\""));
    }

    @Test
    public void testYAPIONStringMoreQuotes() {
        assertThat(new YAPIONValue<>("\"\"\"").toYAPION(new StringOutput()).getResult(), is("(\"\"\"\"\")"));
    }

    @Test
    public void testJSONStringMoreQuotes() {
        assertThat(new YAPIONValue<>("\"\"\"").toJSON(new StringOutput()).getResult(), is("\"\\\"\\\"\\\"\""));
    }

    @Test
    public void testLossyJSONStringMoreQuotes() {
        assertThat(new YAPIONValue<>("\"\"\"").toJSONLossy(new StringOutput()).getResult(), is("\"\\\"\\\"\\\"\""));
    }

    @Test
    public void testYAPIONByte() {
        assertThat(new YAPIONValue<>((byte) 0).toYAPION(new StringOutput()).getResult(), is("(0B)"));
    }

    @Test
    public void testJSONByte() {
        assertThat(new YAPIONValue<>((byte) 0).toJSON(new StringOutput()).getResult(), is("{\"@byte\":0}"));
    }

    @Test
    public void testLossyJSONByte() {
        assertThat(new YAPIONValue<>((byte) 0).toJSONLossy(new StringOutput()).getResult(), is("0"));
    }

    @Test
    public void testYAPIONShort() {
        assertThat(new YAPIONValue<>((short) 0).toYAPION(new StringOutput()).getResult(), is("(0S)"));
    }

    @Test
    public void testJSONShort() {
        assertThat(new YAPIONValue<>((short) 0).toJSON(new StringOutput()).getResult(), is("{\"@short\":0}"));
    }

    @Test
    public void testLossyJSONShort() {
        assertThat(new YAPIONValue<>((short) 0).toJSONLossy(new StringOutput()).getResult(), is("0"));
    }

    @Test
    public void testYAPIONInteger() {
        assertThat(new YAPIONValue<>(0).toYAPION(new StringOutput()).getResult(), is("(0)"));
    }

    @Test
    public void testJSONInteger() {
        assertThat(new YAPIONValue<>(0).toJSON(new StringOutput()).getResult(), is("{\"@int\":0}"));
    }

    @Test
    public void testLossyJSONInteger() {
        assertThat(new YAPIONValue<>(0).toJSONLossy(new StringOutput()).getResult(), is("0"));
    }

    @Test
    public void testYAPIONLong() {
        assertThat(new YAPIONValue<>((long) 0).toYAPION(new StringOutput()).getResult(), is("(0L)"));
    }

    @Test
    public void testJSONLong() {
        assertThat(new YAPIONValue<>((long) 0).toJSON(new StringOutput()).getResult(), is("{\"@long\":0}"));
    }

    @Test
    public void testLossyJSONLong() {
        assertThat(new YAPIONValue<>((long) 0).toJSONLossy(new StringOutput()).getResult(), is("0"));
    }

    @Test
    public void testYAPIONBigInteger() {
        assertThat(new YAPIONValue<>(BigInteger.ZERO).toYAPION(new StringOutput()).getResult(), is("(0BI)"));
    }

    @Test
    public void testJSONBigInteger() {
        assertThat(new YAPIONValue<>(BigInteger.ZERO).toJSON(new StringOutput()).getResult(), is("{\"@bint\":\"0\"}"));
    }

    @Test
    public void testLossyJSONBigInteger() {
        assertThat(new YAPIONValue<>(BigInteger.ZERO).toJSONLossy(new StringOutput()).getResult(), is("0"));
    }

    @Test
    public void testYAPIONFloat() {
        assertThat(new YAPIONValue<>((float) 0).toYAPION(new StringOutput()).getResult(), is("(0.0F)"));
    }

    @Test
    public void testJSONFloat() {
        assertThat(new YAPIONValue<>((float) 0).toJSON(new StringOutput()).getResult(), is("{\"@float\":0.0}"));
    }

    @Test
    public void testLossyJSONFloat() {
        assertThat(new YAPIONValue<>((float) 0).toJSONLossy(new StringOutput()).getResult(), is("0.0"));
    }

    @Test
    public void testYAPIONDouble() {
        assertThat(new YAPIONValue<>((double) 0).toYAPION(new StringOutput()).getResult(), is("(0.0)"));
    }

    @Test
    public void testJSONDouble() {
        assertThat(new YAPIONValue<>((double) 0).toJSON(new StringOutput()).getResult(), is("{\"@double\":0.0}"));
    }

    @Test
    public void testLossyJSONDouble() {
        assertThat(new YAPIONValue<>((double) 0).toJSONLossy(new StringOutput()).getResult(), is("0.0"));
    }

    @Test
    public void testYAPIONBigDecimal() {
        assertThat(new YAPIONValue<>(BigDecimal.ZERO).toYAPION(new StringOutput()).getResult(), is("(0BD)"));
    }

    @Test
    public void testJSONBigDecimal() {
        assertThat(new YAPIONValue<>(BigDecimal.ZERO).toJSON(new StringOutput()).getResult(), is("{\"@bdecimal\":\"0\"}"));
    }

    @Test
    public void testLossyJSONBigDecimal() {
        assertThat(new YAPIONValue<>(BigDecimal.ZERO).toJSONLossy(new StringOutput()).getResult(), is("0"));
    }

    @Test
    public void testYAPIONCharacter() {
        assertThat(new YAPIONValue<>(' ').toYAPION(new StringOutput()).getResult(), is("(' ')"));
    }

    @Test
    public void testJSONCharacter() {
        assertThat(new YAPIONValue<>(' ').toJSON(new StringOutput()).getResult(), is("{\"@char\":\" \"}"));
    }

    @Test
    public void testLossyJSONCharacter() {
        assertThat(new YAPIONValue<>(' ').toJSONLossy(new StringOutput()).getResult(), is("\" \""));
    }

    @Test
    public void testYAPIONNull() {
        assertThat(new YAPIONValue<>(null).toYAPION(new StringOutput()).getResult(), is("(null)"));
    }

    @Test
    public void testJSONNull() {
        assertThat(new YAPIONValue<>(null).toJSON(new StringOutput()).getResult(), is("null"));
    }

    @Test
    public void testLossyJSONNull() {
        assertThat(new YAPIONValue<>(null).toJSONLossy(new StringOutput()).getResult(), is("null"));
    }

    @Test
    public void testYAPIONBoolean() {
        assertThat(new YAPIONValue<>(true).toYAPION(new StringOutput()).getResult(), is("(true)"));
    }

    @Test
    public void testJSONBoolean() {
        assertThat(new YAPIONValue<>(true).toJSON(new StringOutput()).getResult(), is("true"));
    }

    @Test
    public void testLossyJSONBoolean() {
        assertThat(new YAPIONValue<>(true).toJSONLossy(new StringOutput()).getResult(), is("true"));
    }

}
