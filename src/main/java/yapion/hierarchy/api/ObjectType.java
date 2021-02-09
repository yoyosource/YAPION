// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020,2021 yoyosource

package yapion.hierarchy.api;

import lombok.NonNull;
import yapion.hierarchy.types.YAPIONType;
import yapion.utils.ReferenceFunction;
import yapion.utils.ReferenceIDUtils;

public interface ObjectType {

    YAPIONType getType();

    default long referenceValue() {
        return referenceValue(ReferenceIDUtils.REFERENCE_FUNCTION);
    }

    long referenceValue(@NonNull ReferenceFunction referenceFunction);

}