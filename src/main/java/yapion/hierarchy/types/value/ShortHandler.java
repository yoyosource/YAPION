// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020,2021 yoyosource

package yapion.hierarchy.types.value;

import java.util.Optional;
import java.util.function.ToLongFunction;

import static yapion.utils.ReferenceIDUtils.referenceOld;

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
    public long referenceValue(ToLongFunction<String> referenceFunction) {
        return referenceFunction.applyAsLong("java.lang.Short");
    }

}