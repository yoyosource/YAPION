// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020,2021 yoyosource

package yapion.hierarchy.types.value;

import yapion.utils.ReferenceFunction;

import java.util.Optional;

public class ByteHandler implements ValueHandler<Byte> {

    @Override
    public String output(Byte aByte) {
        return aByte + NumberSuffix.BYTE.getSuffix();
    }

    @Override
    public Optional<Byte> preParse(String s) {
        return NumberSuffix.tryValueParse(s, NumberSuffix.BYTE);
    }

    @Override
    public Optional<Byte> parse(String s) {
        return Optional.empty();
    }

    @Override
    public long referenceValue(ReferenceFunction referenceFunction) {
        return referenceFunction.stringToReferenceValue("java.lang.Byte");
    }

}