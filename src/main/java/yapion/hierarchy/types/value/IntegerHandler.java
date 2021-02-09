// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020,2021 yoyosource

package yapion.hierarchy.types.value;

import yapion.utils.ReferenceFunction;

import java.util.Optional;

public class IntegerHandler implements ValueHandler<Integer> {

    @Override
    public String output(Integer integer) {
        return integer + "";
    }

    @Override
    public Optional<Integer> preParse(String s) {
        return NumberSuffix.tryValueParse(s, NumberSuffix.INTEGER);
    }

    @Override
    public Optional<Integer> parse(String s) {
        return NumberSuffix.trySuffixLessValueParse(s, NumberSuffix.INTEGER);
    }

    @Override
    public long referenceValue(ReferenceFunction referenceFunction) {
        return referenceFunction.stringToReferenceValue("java.lang.Integer");
    }

}