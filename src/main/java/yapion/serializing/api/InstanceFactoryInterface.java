// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020,2021 yoyosource

package yapion.serializing.api;

import yapion.serializing.YAPIONDeserializer;

public interface InstanceFactoryInterface<T> {

    /**
     * Describes the Class type this Factory creates instances
     * of. This will be used by the {@link YAPIONDeserializer}.
     *
     * @return the Class Type this Factory is for
     */
    Class<T> type();

    /**
     * This should return an Instance of the desired class
     * described by {@link #type()}. Each time this method
     * is called it should return a completely new instance
     * because the {@link YAPIONDeserializer} would destroy
     * other instances otherwise.
     *
     * @return the Object instance of type {@link #type()}
     */
    T instance();

}