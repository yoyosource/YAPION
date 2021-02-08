// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020,2021 yoyosource

package yapion.hierarchy.api;

import yapion.hierarchy.types.YAPIONType;
import yapion.utils.ReferenceIDUtils;

import java.util.function.ToLongFunction;

public interface ObjectType {

    YAPIONType getType();

    default long referenceValue() {
        return referenceValue(ReferenceIDUtils::reference);
    }

    long referenceValue(ToLongFunction<String> referenceFunction);

}