// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020,2021 yoyosource

package yapion.hierarchy.types.value;

import yapion.utils.ReferenceFunction;

import java.util.Optional;

public class DoubleHandler implements ValueHandler<Double> {

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