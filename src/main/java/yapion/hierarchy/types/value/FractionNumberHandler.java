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
import yapion.utils.IdentifierUtils;
import yapion.utils.MethodReturnValue;
import yapion.utils.ReferenceFunction;

import java.math.BigDecimal;

@UtilityClass
public class FractionNumberHandler {

    public static class FloatHandler implements ValueHandler<Float> {

        @Override
        public String type() {
            return Float.class.getTypeName();
        }

        @Override
        public String typeIdentifier() {
            return IdentifierUtils.FLOAT_IDENTIFIER;
        }

        @Override
        public int index() {
            return 11;
        }

        @Override
        public boolean allowed(char c, int length) {
            if (length > 20) {
                return false;
            }
            if (length == 0 && c == 'F') {
                return false;
            }
            if (length == 1 && c == '.') {
                return true;
            }
            return (c >= '0' && c <= '9') || c == '.' || c == 'F' || c == '+' || c == '-' || c == 'E';
        }

        @Override
        public String output(Float aFloat) {
            return aFloat + NumberSuffix.FLOAT.getSuffix();
        }

        @Override
        public MethodReturnValue<Float> preParse(String s) {
            return NumberSuffix.tryValueParse(s, NumberSuffix.FLOAT);
        }

        @Override
        public MethodReturnValue<Float> parse(String s) {
            return MethodReturnValue.empty();
        }

        @Override
        public long referenceValue(ReferenceFunction referenceFunction) {
            return referenceFunction.stringToReferenceValue("java.lang.Float");
        }

    }

    public static class DoubleHandler implements ValueHandler<Double> {

        @Override
        public String type() {
            return Double.class.getTypeName();
        }

        @Override
        public String typeIdentifier() {
            return IdentifierUtils.DOUBLE_IDENTIFIER;
        }

        @Override
        public int index() {
            return 12;
        }

        @Override
        public boolean allowed(char c, int length) {
            if (length > 40) {
                return false;
            }
            if (length == 0 && c == 'D') {
                return false;
            }
            if (length == 1 && c == '.') {
                return true;
            }
            return (c >= '0' && c <= '9') || c == '.' || c == 'D' || c == '+' || c == '-' || c == 'E';
        }

        @Override
        public String output(Double aDouble) {
            return aDouble + "";
        }

        @Override
        public MethodReturnValue<Double> preParse(String s) {
            return NumberSuffix.tryValueParse(s, NumberSuffix.DOUBLE);
        }

        @Override
        public MethodReturnValue<Double> parse(String s) {
            return NumberSuffix.trySuffixLessValueParse(s, NumberSuffix.DOUBLE);
        }

        @Override
        public long referenceValue(ReferenceFunction referenceFunction) {
            return referenceFunction.stringToReferenceValue("java.lang.Double");
        }

    }

    public static class BigDecimalHandler implements ValueHandler<BigDecimal> {

        @Override
        public String typeIdentifier() {
            return IdentifierUtils.BIG_DECIMAL_IDENTIFIER;
        }

        @Override
        public String type() {
            return BigDecimal.class.getTypeName();
        }

        @Override
        public int index() {
            return 13;
        }

        @Override
        public boolean allowed(char c, int length) {
            if (length == 0 && c == 'B') {
                return false;
            }
            if (length == 1 && c == 'D') {
                return false;
            }
            if (length == 1 && c == '.') {
                return true;
            }
            return (c >= '0' && c <= '9') || c == '.' || c == 'B' || c == 'D' || c == '+' || c == '-' || c == 'E';
        }

        @Override
        public String output(BigDecimal bigDecimal) {
            return bigDecimal + NumberSuffix.BIG_DECIMAL.getSuffix();
        }

        @Override
        public MethodReturnValue<BigDecimal> preParse(String s) {
            return NumberSuffix.tryValueParse(s, NumberSuffix.BIG_DECIMAL);
        }

        @Override
        public MethodReturnValue<BigDecimal> parse(String s) {
            return NumberSuffix.trySuffixLessValueParse(s, NumberSuffix.BIG_DECIMAL);
        }

        @Override
        public long referenceValue(ReferenceFunction referenceFunction) {
            return referenceFunction.stringToReferenceValue("java.math.BigDecimal");
        }

    }

}
