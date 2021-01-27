// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020 yoyosource

package yapion.hierarchy.types.value;

import java.util.Optional;

import static yapion.utils.ReferenceIDUtils.calc;

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
    public long referenceValue() {
        return calc("java.lang.Long");
    }

}