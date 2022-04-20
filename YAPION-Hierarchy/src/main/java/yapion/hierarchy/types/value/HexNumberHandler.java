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
import yapion.utils.MethodReturnValue;
import yapion.utils.ReferenceFunction;

@UtilityClass
public class HexNumberHandler {

    public static class ByteHexHandler implements ValueHandler<Byte> {

        @Override
        public String type() {
            return "";
        }

        @Override
        public String typeIdentifier() {
            return null;
        }

        @Override
        public int index() {
            return 0;
        }

        @Override
        public boolean allowed(char c, int length) {
            switch (length) {
                case 0:
                    return c == '-' || c == '0' || c == '#';
                case 1:
                    if (c == '0' || c == 'x' || c == 'X' || c == '#') return true;
                case 2:
                    if (c == 'x' || c == 'X') return true;
                default:
                    if (length > 5) return false;
                    return (c >= '0' && c <= '9') || (c >= 'A' && c <= 'F') || (c >= 'a' && c <= 'f');
            }
        }

        @Override
        public String output(Byte aByte, YAPIONType parent) {
            return aByte + NumberSuffix.BYTE_HEX.getSuffix();
        }

        @Override
        public String outputJSON(Byte aByte, YAPIONType parent) {
            return aByte + "";
        }

        @Override
        public MethodReturnValue<Byte> preParse(String s) {
            return NumberSuffix.tryValueParse(s, NumberSuffix.BYTE_HEX);
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

    public static class ShortHexHandler implements ValueHandler<Short> {

        @Override
        public String type() {
            return "";
        }

        @Override
        public String typeIdentifier() {
            return null;
        }

        @Override
        public int index() {
            return 1;
        }

        @Override
        public boolean allowed(char c, int length) {
            switch (length) {
                case 0:
                    return c == '-' || c == '0' || c == '#';
                case 1:
                    if (c == '0' || c == 'x' || c == 'X' || c == '#') return true;
                case 2:
                    if (c == 'x' || c == 'X') return true;
                default:
                    if (length > 8) return false;
                    return (c >= '0' && c <= '9') || (c >= 'A' && c <= 'F') || (c >= 'a' && c <= 'f') || c == 'S';
            }
        }

        @Override
        public String output(Short aShort, YAPIONType parent) {
            return aShort + NumberSuffix.SHORT_HEX.getSuffix();
        }

        @Override
        public String outputJSON(Short aShort, YAPIONType parent) {
            return aShort + "";
        }

        @Override
        public MethodReturnValue<Short> preParse(String s) {
            return NumberSuffix.tryValueParse(s, NumberSuffix.SHORT_HEX);
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

    public static class IntegerHexHandler implements ValueHandler<Integer> {

        @Override
        public String type() {
            return "";
        }

        @Override
        public String typeIdentifier() {
            return null;
        }

        @Override
        public int index() {
            return 2;
        }

        @Override
        public boolean allowed(char c, int length) {
            switch (length) {
                case 0:
                    return c == '-' || c == '0' || c == '#';
                case 1:
                    if (c == '0' || c == 'x' || c == 'X' || c == '#') return true;
                case 2:
                    if (c == 'x' || c == 'X') return true;
                default:
                    if (length > 12) return false;
                    return (c >= '0' && c <= '9') || (c >= 'A' && c <= 'F') || (c >= 'a' && c <= 'f') || c == 'I';
            }
        }

        @Override
        public String output(Integer integer, YAPIONType parent) {
            return integer + "";
        }

        @Override
        public String outputJSON(Integer integer, YAPIONType parent) {
            return integer + "";
        }

        @Override
        public MethodReturnValue<Integer> preParse(String s) {
            return NumberSuffix.tryValueParse(s, NumberSuffix.INTEGER_HEX);
        }

        @Override
        public MethodReturnValue<Integer> parse(String s) {
            return NumberSuffix.trySuffixLessValueParse(s, NumberSuffix.INTEGER_HEX);
        }

        @Override
        public long referenceValue(ReferenceFunction referenceFunction) {
            return referenceFunction.stringToReferenceValue("java.lang.Integer");
        }
    }

    public static class LongHexHandler implements ValueHandler<Long> {

        @Override
        public String type() {
            return "";
        }

        @Override
        public String typeIdentifier() {
            return null;
        }

        @Override
        public int index() {
            return 3;
        }

        @Override
        public boolean allowed(char c, int length) {
            switch (length) {
                case 0:
                    return c == '-' || c == '0' || c == '#';
                case 1:
                    if (c == '0' || c == 'x' || c == 'X' || c == '#') return true;
                case 2:
                    if (c == 'x' || c == 'X') return true;
                default:
                    if (length > 20) return false;
                    return (c >= '0' && c <= '9') || (c >= 'A' && c <= 'F') || (c >= 'a' && c <= 'f') || c == 'L';
            }
        }

        @Override
        public String output(Long aLong, YAPIONType parent) {
            return aLong + NumberSuffix.LONG_HEX.getSuffix();
        }

        @Override
        public String outputJSON(Long aLong, YAPIONType parent) {
            return aLong + "";
        }

        @Override
        public MethodReturnValue<Long> preParse(String s) {
            return NumberSuffix.tryValueParse(s, NumberSuffix.LONG_HEX);
        }

        @Override
        public MethodReturnValue<Long> parse(String s) {
            return NumberSuffix.trySuffixLessValueParse(s, NumberSuffix.LONG_HEX);
        }

        @Override
        public long referenceValue(ReferenceFunction referenceFunction) {
            return referenceFunction.stringToReferenceValue("java.lang.Long");
        }
    }
}
