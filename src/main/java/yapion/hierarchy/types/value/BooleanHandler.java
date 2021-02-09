// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020,2021 yoyosource

package yapion.hierarchy.types.value;

import yapion.utils.ReferenceFunction;

import java.util.Optional;

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
    public long referenceValue(ReferenceFunction referenceFunction) {
        return referenceFunction.stringToReferenceValue("java.lang.Boolean");
    }

}