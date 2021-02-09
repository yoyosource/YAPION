// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020,2021 yoyosource

package yapion.hierarchy.types.value;

import yapion.utils.ReferenceFunction;

import java.util.Optional;

public class LongHandler implements ValueHandler<Long> {

    @Override
    public String output(Long aLong) {
        return aLong + NumberSuffix.LONG.getSuffix();
    }

    @Override
    public Optional<Long> preParse(String s) {
        return NumberSuffix.tryValueParse(s, NumberSuffix.LONG);
    }

    @Override
    public Optional<Long> parse(String s) {
        return NumberSuffix.trySuffixLessValueParse(s, NumberSuffix.LONG);
    }

    @Override
    public long referenceValue(ReferenceFunction referenceFunction) {
        return referenceFunction.stringToReferenceValue("java.lang.Long");
    }

}