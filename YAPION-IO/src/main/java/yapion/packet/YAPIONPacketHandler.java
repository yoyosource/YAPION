/*
 * Copyright 2019,2020,2021 yoyosource
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package yapion.packet;

import lombok.NonNull;

import java.io.IOException;
import java.util.function.Consumer;

@FunctionalInterface
public interface YAPIONPacketHandler<T extends YAPIONPacket> {

    /**
     * Handle a specific {@link YAPIONPacket}.
     *
     * @param yapionPacket the packet to handle.
     */
    void handlePacket(T yapionPacket);

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
     * Specifies if exceptions should be ignored and not processed by the
     * '@exception' handler.
     *
     * @return {@code true} if exceptions should be ignored, {@code false} otherwise
     */
    default boolean ignoreException() {
        return false;
    }

    /**
     * Specifies if the OutputStream and InputStream should be closed if an exception gets thrown.
     * This will be evaluated before {@link #ignoreException()} is evaluated and will only take effect
     * if used in conjunction with YAPIONSocket.
     *
     * @return {@code true} if it should get closed on exception, {@code false} otherwise
     */
    default boolean closeOnException() {
        return false;
    }

    /**
     * Close a PacketStream associated with this YAPIONPacket.
     *
     * @param yapionPacket the packet to use
     * @return {@code true} if and only if the close was successful, {@code false} otherwise
     */
    static boolean close(@NonNull YAPIONPacket yapionPacket) {
        if (yapionPacket.getYAPIONPacketStream() != null) {
            try {
                yapionPacket.getYAPIONPacketStream().close();
                return true;
            } catch (IOException e) {
                return false;
            }
        }
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
    static <T extends YAPIONPacket> YAPIONPacketHandler<T> createInstance(Consumer<T> yapionPacketConsumer, boolean runThread, boolean daemonThread, boolean ignoreException) {
        return new YAPIONPacketHandler<>() {
            @Override
            public void handlePacket(T yapionPacket) {
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
    static <T extends YAPIONPacket> YAPIONPacketHandler<T> createInstance(Consumer<T> yapionPacketConsumer, boolean runThread, boolean ignoreException) {
        return new YAPIONPacketHandler<>() {
            @Override
            public void handlePacket(T yapionPacket) {
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
