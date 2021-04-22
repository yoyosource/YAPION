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

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import yapion.annotations.api.DeprecationInfo;
import yapion.exceptions.YAPIONException;
import yapion.exceptions.utils.YAPIONPacketException;
import yapion.utils.IdentifierUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

@Slf4j
public class YAPIONPacketReceiver {

    private final Map<String, YAPIONPacketHandler> handlerMap = new HashMap<>();

    @RequiredArgsConstructor
    public enum SpecialHandler {

        /**
         * Used when any {@link SpecialHandler} except {@link #HEART_BEAT} or {@link #LOST_HEART_BEAT} threw an exception.
         */
        ERROR("@error"),

        /**
         * Used when any user handlers threw an exception.
         */
        EXCEPTION("@exception"),

        /**
         * Used when a packet has an unknown {@link IdentifierUtils#TYPE_IDENTIFIER}.
         */
        UNKNOWN_PACKET("@unknown"),

        /**
         * Used when some data was dropped.
         */
        DROP("@drop"),

        /**
         * Used when deserialization to an YAPIONPacket failed with an exception.
         */
        DESERIALIZE_EXCEPTION("@deserialize"),

        /**
         * Used when anything while checking the incoming data failed.
         */
        HANDLE_FAILED("@handle"),

        /**
         * Used when an heartbeat packet arrived.
         */
        HEART_BEAT("@heartbeat"),

        /**
         * Used when the heartbeat packet was not received in the given amount of time.
         */
        LOST_HEART_BEAT("@lost_heartbeat");

        private static final SpecialHandler[] specialHandlers = values();

        private final String identifier;
    }

    /**
     * Creates an YAPIONPacketReceiver
     */
    public YAPIONPacketReceiver() {
        for (SpecialHandler specialHandler : SpecialHandler.specialHandlers) {
            handlerMap.put(specialHandler.identifier, yapionPacket -> {

            });
        }
    }

    /**
     * Add an YAPIONPacketHandler for a specified packetType to handle.
     * If either the packetType or yapionPacketHandler is {@code null}
     * this method throws an YAPIONPacketException.
     *
     * @param packetType the packet to handle
     * @param yapionPacketHandler the handler which handles the specified packet
     */
    public YAPIONPacketReceiver add(Class<? extends YAPIONPacket> packetType, YAPIONPacketHandler yapionPacketHandler) {
        if (yapionPacketHandler == null || packetType == null) {
            throw new YAPIONPacketException();
        }
        handlerMap.put(packetType.getTypeName(), yapionPacketHandler);
        return this;
    }

    /**
     * Add an YAPIONPacketHandler for the specified packetTypes to handle.
     * If either the yapionPacketHandler or the packetTypes itself is
     * {@code null} this method throws an YAPIONPacketException. If any
     * value in the packetTypes array is {@code null} it will just be
     * skipped over.
     *
     * @param packetTypes the packets to handle
     * @param yapionPacketHandler the handler which handles the specified packets
     * @deprecated since 0.25.0
     */
    @Deprecated
    @DeprecationInfo(since = "0.25.0")
    public YAPIONPacketReceiver add(Class<? extends YAPIONPacket>[] packetTypes, YAPIONPacketHandler yapionPacketHandler) {
        if (packetTypes == null) {
            throw new YAPIONPacketException();
        }
        for (Class<? extends YAPIONPacket> s : packetTypes) {
            if (s == null) continue;
            add(s, yapionPacketHandler);
        }
        return this;
    }

    /**
     * A wrapper function to {@link #add(Class, YAPIONPacketHandler)}
     */
    public YAPIONPacketReceiver add(YAPIONPacketHandler yapionPacketHandler, Class<? extends YAPIONPacket> packetType) {
        return add(packetType, yapionPacketHandler);
    }

    /**
     * A wrapper function to {@link #add(Class[], YAPIONPacketHandler)}
     *
     * @deprecated since 0.25.0
     */
    @Deprecated
    @DeprecationInfo(since = "0.25.0")
    public YAPIONPacketReceiver add(YAPIONPacketHandler yapionPacketHandler, Class<? extends YAPIONPacket>... packetTypes) {
        return add(packetTypes, yapionPacketHandler);
    }

    /**
     * Set the SpecialHandler {@link YAPIONPacketHandler} to do something.
     *
     * @param yapionPacketHandler the {@link YAPIONPacketHandler} to set
     */
    public YAPIONPacketReceiver setHandler(SpecialHandler specialHandler, YAPIONPacketHandler yapionPacketHandler) {
        if (yapionPacketHandler == null) {
            throw new YAPIONException();
        }
        handlerMap.put(specialHandler.identifier, yapionPacketHandler);
        return this;
    }

    /**
     * Handles an YAPIONPacket by calling the specified yapionPacketHandler
     * for the type of the packet. If the packet type is not found the
     * "@error" handler gets called. If any exception gets thrown the
     * special "@exception" handler is called. If this also throws an
     * exception this method will log the exception as a warning.
     *
     * @param yapionPacket to handle
     */
    public void handle(YAPIONPacket yapionPacket) {
        String type = yapionPacket.getType();
        if (!handlerMap.containsKey(type)) {
            handleUnknown(yapionPacket);
            return;
        }
        handlePacket(yapionPacket, type, handlerMap.get(type), this::handleException);
    }

    private void handlePacket(YAPIONPacket yapionPacket, String type, YAPIONPacketHandler handler, Consumer<YAPIONPacket> yapionPacketConsumer) {
        Runnable runnable = () -> {
            try {
                handler.handlePacket(yapionPacket);
            } catch (Exception e) {
                log.warn(String.format("The packet handler with type '%s' threw an exception.", type), e.getCause());
                yapionPacket.setException(e);
                if (handler.closeOnException() && yapionPacket.getYAPIONPacketStream() != null) {
                    try {
                        yapionPacket.getYAPIONPacketStream().close();
                    } catch (IOException ex) {
                        // Ignored
                    }
                    return;
                }
                if (!handler.ignoreException()) yapionPacketConsumer.accept(yapionPacket);
            }
        };
        if (handler.runThread()) {
            Thread thread = new Thread(runnable);
            thread.setName("Packet handler Thread (" + type + ")");
            thread.setDaemon(handler.daemonThread());
            thread.start();
        } else {
            runnable.run();
        }
    }

    void handleHeartBeat(YAPIONPacket yapionPacket) {
        handlePacket(yapionPacket, SpecialHandler.HEART_BEAT.identifier, handlerMap.get(SpecialHandler.HEART_BEAT.identifier), this::handleException);
    }

    void handleLostHeartBeat(YAPIONPacket yapionPacket) {
        handlePacket(yapionPacket, SpecialHandler.LOST_HEART_BEAT.identifier, handlerMap.get(SpecialHandler.LOST_HEART_BEAT.identifier), this::handleException);
    }

    void handleDrop(YAPIONPacket yapionPacket) {
        handlePacket(yapionPacket, SpecialHandler.DROP.identifier, handlerMap.get(SpecialHandler.DROP.identifier), this::handleError);
    }

    void handleDeserializationException(YAPIONPacket yapionPacket) {
        handlePacket(yapionPacket, SpecialHandler.DESERIALIZE_EXCEPTION.identifier, handlerMap.get(SpecialHandler.DESERIALIZE_EXCEPTION.identifier), this::handleError);
    }

    void handleHandleFailed(YAPIONPacket yapionPacket) {
        handlePacket(yapionPacket, SpecialHandler.HANDLE_FAILED.identifier, handlerMap.get(SpecialHandler.HANDLE_FAILED.identifier), this::handleError);
    }

    private void handleUnknown(YAPIONPacket yapionPacket) {
        handlePacket(yapionPacket, SpecialHandler.UNKNOWN_PACKET.identifier, handlerMap.get(SpecialHandler.UNKNOWN_PACKET.identifier), this::handleError);
    }

    private void handleException(YAPIONPacket yapionPacket) {
        handlePacket(yapionPacket, SpecialHandler.EXCEPTION.identifier, handlerMap.get(SpecialHandler.EXCEPTION.identifier), this::handleError);
    }

    private void handleError(YAPIONPacket yapionPacket) {
        handlePacket(yapionPacket, SpecialHandler.ERROR.identifier, handlerMap.get(SpecialHandler.ERROR.identifier), packet -> {
            throw new SecurityException(packet.getException().getMessage(), packet.getException());
        });
    }

}
