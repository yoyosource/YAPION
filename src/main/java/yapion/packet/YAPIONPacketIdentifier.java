// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020 yoyosource

package yapion.packet;

import yapion.annotations.deserialize.YAPIONLoadExclude;
import yapion.annotations.serialize.YAPIONSaveExclude;

@YAPIONSaveExclude(context = "*")
@YAPIONLoadExclude(context = "*")
public class YAPIONPacketIdentifier<T> {

    private final T identifier;

    /**
     * Creates an YAPIONPacketIdentifier with the specified
     * value.
     *
     * @param identifier the Value
     */
    public YAPIONPacketIdentifier(T identifier) {
        this.identifier = identifier;
    }

    /**
     * Returns the specified value.
     *
     * @return the Value
     */
    public T get() {
        return identifier;
    }

}