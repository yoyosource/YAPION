// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020 yoyosource

package yapion.packet;

import yapion.annotations.deserialize.YAPIONLoadExclude;
import yapion.annotations.serialize.YAPIONSaveExclude;

import java.util.function.Consumer;

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
     * Specifies if this handler should be run in a Thread.
     *
     * @return {@code true} if it should run in a thread, {@code false} otherwise
     */
    default boolean runThread() {
        return false;
    }

    /**
     * Specifies if the Thread should be a Daemon or not.
     * This only takes effect when {@link #runThread()} returns {@code true}.
     * If not implemented this method returns {@code true} by default.
     *
     * @return {@code true} if it should run as a daemon, {@code false} otherwise.
     */
    default boolean daemonThread() {
        return true;
    }

    /**
     * Specifies if exceptions should be ignores and not processes by the
     * '@exception' handler.
     *
     * @return {@code true} if exceptions should be ignored, {@code false} otherwise
     */
    default boolean ignoreException() {
        return false;
    }

    /**
     * Create a {@link YAPIONPacketHandler} by the different parameters
     *
     * @param yapionPacketConsumer the {@link Consumer} to handle the {@link YAPIONPacket}
     * @param runThread {@code true} if it should run in a thread, {@code false} otherwise
     * @param daemonThread {@code true} if it should run as a daemon, {@code false} otherwise.
     * @param ignoreException {@code true} if exceptions should be ignored, {@code false} otherwise
     * @return {@link YAPIONPacketHandler} instance
     */
    static YAPIONPacketHandler createInstance(Consumer<YAPIONPacket> yapionPacketConsumer, boolean runThread, boolean daemonThread, boolean ignoreException) {
        return new YAPIONPacketHandler() {
            @Override
            public void handlePacket(YAPIONPacket yapionPacket) {
                yapionPacketConsumer.accept(yapionPacket);
            }

            @Override
            public boolean runThread() {
                return runThread;
            }

            @Override
            public boolean daemonThread() {
                return daemonThread;
            }

            @Override
            public boolean ignoreException() {
                return ignoreException;
            }
        };
    }

    /**
     * Create a {@link YAPIONPacketHandler} by the different parameters
     *
     * @param yapionPacketConsumer the {@link Consumer} to handle the {@link YAPIONPacket}
     * @param runThread {@code true} if it should run in a thread, {@code false} otherwise
     * @param ignoreException {@code true} if exceptions should be ignored, {@code false} otherwise
     * @return {@link YAPIONPacketHandler} instance
     */
    static YAPIONPacketHandler createInstance(Consumer<YAPIONPacket> yapionPacketConsumer, boolean runThread, boolean ignoreException) {
        return new YAPIONPacketHandler() {
            @Override
            public void handlePacket(YAPIONPacket yapionPacket) {
                yapionPacketConsumer.accept(yapionPacket);
            }

            @Override
            public boolean runThread() {
                return runThread;
            }

            @Override
            public boolean ignoreException() {
                return ignoreException;
            }
        };
    }

}