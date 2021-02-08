// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020,2021 yoyosource

package yapion.hierarchy.types.value;

import java.util.Optional;
import java.util.function.ToLongFunction;

public class BooleanHandler implements ValueHandler<Boolean> {

    @Override
    public String output(Boolean aBoolean) {
        if (aBoolean) {
            return "true";
        } else {
            return "false";
        }
    }

    @Override
    public Optional<Boolean> preParse(String s) {
        return Optional.empty();
    }

    @Override
    public Optional<Boolean> parse(String s) {
        if (s.equals("true")) {
            return Optional.of(true);
        }
        if (s.equals("false")) {
            return Optional.of(false);
        }
        return Optional.empty();
    }

    @Override
    public long referenceValue(ToLongFunction<String> referenceFunction) {
        return referenceFunction.applyAsLong("java.lang.Boolean");
    }

}