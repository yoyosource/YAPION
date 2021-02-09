// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020,2021 yoyosource

package yapion.hierarchy.types.value;

import yapion.utils.ReferenceFunction;

import java.util.Optional;

public class ShortHandler implements ValueHandler<Short> {

    @Override
    public String output(Short aShort) {
        return aShort + NumberSuffix.SHORT.getSuffix();
    }

    @Override
    public Optional<Short> preParse(String s) {
        return NumberSuffix.tryValueParse(s, NumberSuffix.SHORT);
    }

    @Override
    public Optional<Short> parse(String s) {
        return Optional.empty();
    }

    @Override
    public long referenceValue(ReferenceFunction referenceFunction) {
        return referenceFunction.stringToReferenceValue("java.lang.Short");
    }

}