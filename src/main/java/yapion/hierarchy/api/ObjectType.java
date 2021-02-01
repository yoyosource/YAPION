// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020,2021 yoyosource

package yapion.hierarchy.api;

import yapion.hierarchy.types.YAPIONType;

public interface ObjectType {

    YAPIONType getType();

    long referenceValue();

}