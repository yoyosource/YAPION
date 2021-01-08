// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020 yoyosource

package yapion.serializing.api;

import yapion.serializing.SerializeManager;

@SuppressWarnings({"java:S1610"})
public abstract class InstanceFactory<T> implements InstanceFactoryInterface<T> {

    /**
     * Add this InstanceFactory to the SerializeManager by calling
     * {@link SerializeManager#add(InstanceFactoryInterface)}.
     */
    public final void add() {
        SerializeManager.add(this);
    }

}