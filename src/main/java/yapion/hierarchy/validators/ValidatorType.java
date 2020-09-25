// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020 yoyosource

package yapion.hierarchy.validators;

import yapion.hierarchy.types.YAPIONType;

public enum ValidatorType {

    ANY(YAPIONType.ANY),
    OBJECT(YAPIONType.OBJECT),
    ARRAY(YAPIONType.ARRAY),
    MAP(YAPIONType.MAP),
    POINTER(YAPIONType.POINTER),
    VALUE(YAPIONType.VALUE);

    private YAPIONType type;

    ValidatorType(YAPIONType type) {
        this.type = type;
    }

    YAPIONType getType() {
        return type;
    }

}