// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020,2021 yoyosource

package yapion.serializing.api;

import yapion.serializing.SerializeManager;

import java.util.Queue;

@SuppressWarnings({"java:S1610"})
public abstract class SerializerQueue<T extends Queue<?>> implements SerializerQueueInterface<T> {

    /**
     * Add this ListSerializer to the SerializeManager by calling
     * {@link SerializeManager#add(SerializerQueueInterface)}.
     */
    public final void add() {
        SerializeManager.add(this);
    }

}