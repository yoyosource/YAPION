// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020 yoyosource

package yapion.hierarchy.types.value;

import java.util.Optional;

import static yapion.utils.ReferenceIDUtils.calc;

public class NullHandler implements ValueHandler<Object> {

    @Override
    public String output(Object o) {
        return "null";
    }

    @Override
    public Optional<Object> preParse(String s) {
        if (s.equals("null")) {
            return null;
        }
        return Optional.empty();
    }

    @Override
    public Optional<Object> parse(String s) {
        return Optional.empty();
    }

    @Override
    public long referenceValue() {
        return calc("null");
    }

}