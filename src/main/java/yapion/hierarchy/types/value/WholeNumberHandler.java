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

package yapion.hierarchy.types.value;

import lombok.experimental.UtilityClass;
import yapion.hierarchy.types.YAPIONType;
import yapion.utils.IdentifierUtils;
import yapion.utils.MethodReturnValue;
import yapion.utils.ReferenceFunction;

import java.math.BigInteger;

@UtilityClass
public class WholeNumberHandler {

    public static class ByteHandler implements ValueHandler<Byte> {

        @Override
        public String type() {
            return Byte.class.getTypeName();
        }

        @Override
        public String typeIdentifier() {
            return IdentifierUtils.BYTE_IDENTIFIER;
        }

        @Override
        public int index() {
            return 6;
        }

        @Override
        public boolean allowed(char c, int length) {
            switch (length) {
                case 0:
                    if (c == '-') return true;
                default:
                    if (length > 5) return false;
                    return (c >= '0' && c <= '9') || c == 'B';
            }
        }

        @Override
        public String output(Byte aByte, YAPIONType parent) {
            return aByte + NumberSuffix.BYTE.getSuffix();
        }

        @Override
        public MethodReturnValue<Byte> preParse(String s) {
            return NumberSuffix.tryValueParse(s, NumberSuffix.BYTE);
        }

        @Override
        public MethodReturnValue<Byte> parse(String s) {
            return MethodReturnValue.empty();
        }

        @Override
        public long referenceValue(ReferenceFunction referenceFunction) {
            return referenceFunction.stringToReferenceValue("java.lang.Byte");
        }
    }

    public static class ShortHandler implements ValueHandler<Short> {

        @Override
        public String type() {
            return Short.class.getTypeName();
        }

        @Override
        public String typeIdentifier() {
            return IdentifierUtils.SHORT_IDENTIFIER;
        }

        @Override
        public int index() {
            return 7;
        }

        @Override
        public boolean allowed(char c, int length) {
            switch (length) {
                case 0:
                    if (c == '-') return true;
                default:
                    if (length > 8) return false;
                    return (c >= '0' && c <= '9') || c == 'S';
            }
        }

        @Override
        public String output(Short aShort, YAPIONType parent) {
            return aShort + NumberSuffix.SHORT.getSuffix();
        }

        @Override
        public MethodReturnValue<Short> preParse(String s) {
            return NumberSuffix.tryValueParse(s, NumberSuffix.SHORT);
        }

        @Override
        public MethodReturnValue<Short> parse(String s) {
            return MethodReturnValue.empty();
        }

        @Override
        public long referenceValue(ReferenceFunction referenceFunction) {
            return referenceFunction.stringToReferenceValue("java.lang.Short");
        }
    }

    public static class IntegerHandler implements ValueHandler<Integer> {

        @Override
        public String type() {
            return Integer.class.getTypeName();
        }

        @Override
        public String typeIdentifier() {
            return IdentifierUtils.INT_IDENTIFIER;
        }

        @Override
        public int index() {
            return 8;
        }

        @Override
        public boolean allowed(char c, int length) {
            switch (length) {
                case 0:
                    if (c == '-') return true;
                default:
                    if (length > 12) return false;
                    return (c >= '0' && c <= '9') || c == 'I';
            }
        }

        @Override
        public String output(Integer integer, YAPIONType parent) {
            return integer + "";
        }

        @Override
        public MethodReturnValue<Integer> preParse(String s) {
            return NumberSuffix.tryValueParse(s, NumberSuffix.INTEGER);
        }

        @Override
        public MethodReturnValue<Integer> parse(String s) {
            return NumberSuffix.trySuffixLessValueParse(s, NumberSuffix.INTEGER);
        }

        @Override
        public long referenceValue(ReferenceFunction referenceFunction) {
            return referenceFunction.stringToReferenceValue("java.lang.Integer");
        }
    }

    public static class LongHandler implements ValueHandler<Long> {

        @Override
        public String typeIdentifier() {
            return IdentifierUtils.LONG_IDENTIFIER;
        }

        @Override
        public String type() {
            return Long.class.getTypeName();
        }

        @Override
        public int index() {
            return 9;
        }

        @Override
        public boolean allowed(char c, int length) {
            switch (length) {
                case 0:
                    if (c == '-') return true;
                default:
                    if (length > 20) return false;
                    return (c >= '0' && c <= '9') || c == 'L';
            }
        }

        @Override
        public String output(Long aLong, YAPIONType parent) {
            return aLong + NumberSuffix.LONG.getSuffix();
        }

        @Override
        public MethodReturnValue<Long> preParse(String s) {
            return NumberSuffix.tryValueParse(s, NumberSuffix.LONG);
        }

        @Override
        public MethodReturnValue<Long> parse(String s) {
            return NumberSuffix.trySuffixLessValueParse(s, NumberSuffix.LONG);
        }

        @Override
        public long referenceValue(ReferenceFunction referenceFunction) {
            return referenceFunction.stringToReferenceValue("java.lang.Long");
        }
    }

    public static class BigIntegerHandler implements ValueHandler<BigInteger> {

        @Override
        public String typeIdentifier() {
            return IdentifierUtils.BIG_INTEGER_IDENTIFIER;
        }

        @Override
        public String type() {
            return BigInteger.class.getTypeName();
        }

        @Override
        public int index() {
            return 10;
        }

        @Override
        public boolean allowed(char c, int length) {
            switch (length) {
                case 0:
                    if (c == '-') return true;
                default:
                    return (c >= '0' && c <= '9') || c == 'B' || c == 'I';
            }
        }

        @Override
        public String output(BigInteger bigInteger, YAPIONType parent) {
            return bigInteger + NumberSuffix.BIG_INTEGER.getSuffix();
        }

        @Override
        public MethodReturnValue<BigInteger> preParse(String s) {
            return NumberSuffix.tryValueParse(s, NumberSuffix.BIG_INTEGER);
        }

        @Override
        public MethodReturnValue<BigInteger> parse(String s) {
            return NumberSuffix.trySuffixLessValueParse(s, NumberSuffix.BIG_INTEGER);
        }

        @Override
        public long referenceValue(ReferenceFunction referenceFunction) {
            return referenceFunction.stringToReferenceValue("java.math.BigInteger");
        }
    }
}
