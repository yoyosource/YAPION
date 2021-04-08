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
import yapion.utils.MethodReturnValue;
import yapion.utils.ReferenceFunction;

import java.math.BigInteger;

@UtilityClass
public class WholeNumberHandler {

    public static class ByteHandler implements ValueHandler<Byte> {

        @Override
        public boolean allowed(char c, int length) {
            switch (length) {
                case 0:
                    if (c == '-') return true;
                case 1:
                    if (c == 'x' || c == 'X' || c == '#') return true;
                default:
                    if (length > 5) return false;
                    return (c >= '0' && c <= '9') || (c >= 'A' && c <= 'F') || (c >= 'a' && c <= 'f');
            }
        }

        @Override
        public String output(Byte aByte) {
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
        public boolean allowed(char c, int length) {
            switch (length) {
                case 0:
                    if (c == '-') return true;
                case 1:
                    if (c == 'x' || c == 'X' || c == '#') return true;
                default:
                    if (length > 8) return false;
                    return (c >= '0' && c <= '9') || (c >= 'A' && c <= 'F') || (c >= 'a' && c <= 'f') || c == 'S';
            }
        }

        @Override
        public String output(Short aShort) {
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
        public boolean allowed(char c, int length) {
            switch (length) {
                case 0:
                    if (c == '-') return true;
                case 1:
                    if (c == 'x' || c == 'X' || c == '#') return true;
                default:
                    if (length > 12) return false;
                    return (c >= '0' && c <= '9') || (c >= 'A' && c <= 'F') || (c >= 'a' && c <= 'f') || c == 'I';
            }
        }

        @Override
        public String output(Integer integer) {
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
        public boolean allowed(char c, int length) {
            switch (length) {
                case 0:
                    if (c == '-') return true;
                case 1:
                    if (c == 'x' || c == 'X' || c == '#') return true;
                default:
                    if (length > 20) return false;
                    return (c >= '0' && c <= '9') || (c >= 'A' && c <= 'F') || (c >= 'a' && c <= 'f') || c == 'L';
            }
        }

        @Override
        public String output(Long aLong) {
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
        public boolean allowed(char c, int length) {
            switch (length) {
                case 0:
                    if (c == '-') return true;
                case 1:
                    if (c == 'x' || c == 'X' || c == '#') return true;
                default:
                    return (c >= '0' && c <= '9') || (c >= 'A' && c <= 'F') || (c >= 'a' && c <= 'f') || c == 'I';
            }
        }

        @Override
        public String output(BigInteger bigInteger) {
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
