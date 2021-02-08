// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020,2021 yoyosource

package yapion.hierarchy.types.value;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.function.ToLongFunction;

import static yapion.utils.ReferenceIDUtils.reference;
import static yapion.utils.ReferenceIDUtils.referenceOld;

public class BigDecimalHandler implements ValueHandler<BigDecimal> {

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
    public long referenceValue(ToLongFunction<String> referenceFunction) {
        return referenceFunction.applyAsLong("java.math.BigDecimal");
    }

}