// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020,2021 yoyosource

package yapion.hierarchy.typegroups;

import java.util.List;

public abstract class YAPIONDataType extends YAPIONAnyType {

    public abstract int size();

    public abstract long deepSize();

    public abstract int length();

    public abstract boolean isEmpty();

    public abstract List<YAPIONAnyType> getAllValues();

}