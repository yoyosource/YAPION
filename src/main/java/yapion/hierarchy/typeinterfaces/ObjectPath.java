// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020 yoyosource

package yapion.hierarchy.typeinterfaces;

import yapion.hierarchy.typegroups.YAPIONAnyType;

public interface ObjectPath {

    String getPath(YAPIONAnyType yapionAnyType);

    String[] getPath();

    int getDepth();

}