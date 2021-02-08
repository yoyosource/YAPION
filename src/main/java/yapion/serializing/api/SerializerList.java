// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020,2021 yoyosource

package yapion.serializing.api;

import yapion.serializing.SerializeManager;

import java.util.List;

@SuppressWarnings({"java:S1610"})
public abstract class SerializerList<T extends List<?>> implements SerializerListInterface<T> {

    /**
     * Add this ListSerializer to the SerializeManager by calling
     * {@link SerializeManager#add(SerializerListInterface)}.
     */
    public final void add() {
        SerializeManager.add(this);
    }

}