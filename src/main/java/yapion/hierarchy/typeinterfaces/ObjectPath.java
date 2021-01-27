// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020,2021 yoyosource

package yapion.hierarchy.typeinterfaces;

import yapion.hierarchy.typegroups.YAPIONAnyType;
import yapion.hierarchy.types.YAPIONPath;

public interface ObjectPath {

    String getPath(YAPIONAnyType yapionAnyType);

    YAPIONPath getPath();

    int getDepth();

}