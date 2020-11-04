// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020 yoyosource

package yapion.hierarchy.typegroups;

import yapion.hierarchy.typeinterfaces.ObjectOutput;

@SuppressWarnings({"java:S1610"})
public abstract class YAPIONAnyClosure implements ObjectOutput {

    public abstract long referenceValue();

}