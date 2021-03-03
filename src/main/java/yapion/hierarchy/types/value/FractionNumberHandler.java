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

import yapion.utils.ReferenceFunction;

import java.math.BigDecimal;
import java.util.Optional;

public class FractionNumberHandler {

    private FractionNumberHandler() {
        throw new IllegalStateException("Utility Class");
    }

    public static class FloatHandler implements ValueHandler<Float> {

        @Override
        public String output(Float aFloat) {
            return aFloat + NumberSuffix.FLOAT.getSuffix();
        }

        @Override
        public Optional<Float> preParse(String s) {
            return NumberSuffix.tryValueParse(s, NumberSuffix.FLOAT);
        }

        @Override
        public Optional<Float> parse(String s) {
            return Optional.empty();
        }

        @Override
        public long referenceValue(ReferenceFunction referenceFunction) {
            return referenceFunction.stringToReferenceValue("java.lang.Float");
        }

    }

    public static class DoubleHandler implements ValueHandler<Double> {

        @Override
        public String output(Double aDouble) {
            return aDouble + "";
        }

        @Override
        public Optional<Double> preParse(String s) {
            return NumberSuffix.tryValueParse(s, NumberSuffix.DOUBLE);
        }

        @Override
        public Optional<Double> parse(String s) {
            return NumberSuffix.trySuffixLessValueParse(s, NumberSuffix.DOUBLE);
        }

        @Override
        public long referenceValue(ReferenceFunction referenceFunction) {
            return referenceFunction.stringToReferenceValue("java.lang.Double");
        }

    }

    public static class BigDecimalHandler implements ValueHandler<BigDecimal> {

        @Override
        public String output(BigDecimal bigDecimal) {
            return bigDecimal + NumberSuffix.BIG_DECIMAL.getSuffix();
        }

        @Override
        public Optional<BigDecimal> preParse(String s) {
            return NumberSuffix.tryValueParse(s, NumberSuffix.BIG_DECIMAL);
        }

        @Override
        public Optional<BigDecimal> parse(String s) {
            return NumberSuffix.trySuffixLessValueParse(s, NumberSuffix.BIG_DECIMAL);
        }

        @Override
        public long referenceValue(ReferenceFunction referenceFunction) {
            return referenceFunction.stringToReferenceValue("java.math.BigDecimal");
        }

    }

}
