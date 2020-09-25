// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020 yoyosource

package yapion.packet;

import yapion.annotations.deserialize.YAPIONLoadExclude;
import yapion.annotations.serialize.YAPIONSaveExclude;

@YAPIONLoadExclude(context = "*")
@YAPIONSaveExclude(context = "*")
@FunctionalInterface
public interface YAPIONPacketHandler {

    /**
     * Handle a specific {@link YAPIONPacket}.
     *
     * @param yapionPacket the packet to handle.
     */
    void handlePacket(YAPIONPacket yapionPacket);

    /**
     * Specifies if this handler should be run in an Thread.
     *
     * @return {@see true} if it should run in a thread, {@see false} otherwise
     */
    default boolean runThread() {
        return false;
    }

    /**
     * Specifies if exceptions should be ignores and not processes by the
     * '@exception' handler.
     *
     * @return {@see true} if exceptions should be ignored, {@see false} otherwise
     */
    default boolean ignoreException() {
        return false;
    }

}