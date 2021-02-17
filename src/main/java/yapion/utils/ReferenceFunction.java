// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020,2021 yoyosource

package yapion.utils;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.util.function.ToLongFunction;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class ReferenceFunction {

    private final ToLongFunction<String> referenceFunction;

    public long stringToReferenceValue(String s) {
        return referenceFunction.applyAsLong(s);
    }

}