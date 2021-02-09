// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020,2021 yoyosource

package yapion.hierarchy.types.value;

import yapion.utils.ReferenceFunction;

import java.math.BigInteger;
import java.util.Optional;

public class BigIntegerHandler implements ValueHandler<BigInteger> {

    @Override
    public String output(BigInteger bigInteger) {
        return bigInteger + NumberSuffix.BIG_INTEGER.getSuffix();
    }

    @Override
    public Optional<BigInteger> preParse(String s) {
        return NumberSuffix.tryValueParse(s, NumberSuffix.BIG_INTEGER);
    }

    @Override
    public Optional<BigInteger> parse(String s) {
        return NumberSuffix.trySuffixLessValueParse(s, NumberSuffix.BIG_INTEGER);
    }

    @Override
    public long referenceValue(ReferenceFunction referenceFunction) {
        return referenceFunction.stringToReferenceValue("java.math.BigInteger");
    }

}