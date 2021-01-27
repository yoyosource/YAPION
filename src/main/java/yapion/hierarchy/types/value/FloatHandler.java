// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020 yoyosource

package yapion.hierarchy.types.value;

import java.util.Optional;

import static yapion.utils.ReferenceIDUtils.calc;

public class FloatHandler implements ValueHandler<Float> {

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
    public long referenceValue() {
        return calc("java.lang.Float");
    }

}