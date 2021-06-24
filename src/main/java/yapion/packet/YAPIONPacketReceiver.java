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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import yapion.annotations.api.InternalAPI;
import yapion.utils.IdentifierUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

@Slf4j
public class YAPIONPacketReceiver {

    private final Map<String, YAPIONPacketHandler> handlerMap = new HashMap<>();
    private final Map<String, Set<Predicate<YAPIONPacket>>> filterMap = new HashMap<>();
    private final Set<Predicate<YAPIONPacket>> packetFilters = new HashSet<>();

    @RequiredArgsConstructor
    public enum Handler {

        /**
         * Used when any {@link Handler} except {@link #HEART_BEAT} or {@link #LOST_HEART_BEAT} threw an exception.
         */
        ERROR("@error", null),

        /**
         * Used when any user handlers threw an exception.
         */
        EXCEPTION("@exception", ERROR),

        /**
         * Used when a packet has an unknown {@link IdentifierUtils#TYPE_IDENTIFIER}.
         */
        UNKNOWN_PACKET("@unknown", ERROR),

        /**
         * Used when a packet has been filtered by a {@link Predicate<YAPIONPacket>}
         */
        FILTERED("@filtered", ERROR),

        /**
         * Used when some data was dropped.
         */
        DROP("@drop", ERROR),

        /**
         * Used when deserialization to an YAPIONPacket failed with an exception.
         */
        DESERIALIZE_EXCEPTION("@deserialize", ERROR),

        /**
         * Used when anything while checking the incoming data failed.
         */
        HANDLE_FAILED("@handle", ERROR),

        /**
         * Used when an heartbeat packet arrived.
         */
        HEART_BEAT("@heartbeat", EXCEPTION),

        /**
         * Used when the heartbeat packet was not received in the given amount of time.
         */
        LOST_HEART_BEAT("@lost_heartbeat", EXCEPTION),

        @InternalAPI
        USER("", EXCEPTION);

        private static final Handler[] HANDLERS = values();

        private final String identifier;
        private final Handler onException;
    }

    /**
     * Creates an YAPIONPacketReceiver
     */
    public YAPIONPacketReceiver() {
        for (Handler handler : Handler.HANDLERS) {
            handlerMap.put(handler.identifier, yapionPacket -> {});
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
    public YAPIONPacketReceiver add(@NonNull Class<? extends YAPIONPacket> packetType, @NonNull YAPIONPacketHandler yapionPacketHandler) {
        handlerMap.put(packetType.getTypeName(), yapionPacketHandler);
        filterMap.put(packetType.getTypeName(), new HashSet<>());
        return this;
    }

    /**
     * A wrapper function to {@link #add(Class, YAPIONPacketHandler)}
     */
    public YAPIONPacketReceiver add(@NonNull YAPIONPacketHandler yapionPacketHandler, @NonNull Class<? extends YAPIONPacket> packetType) {
        return add(packetType, yapionPacketHandler);
    }

    /**
     * Set the SpecialHandler {@link YAPIONPacketHandler} to do something.
     *
     * @param yapionPacketHandler the {@link YAPIONPacketHandler} to set
     */
    public YAPIONPacketReceiver setHandler(@NonNull Handler handler, @NonNull YAPIONPacketHandler yapionPacketHandler) {
        if (handler == Handler.USER) {
            throw new SecurityException();
        }
        handlerMap.put(handler.identifier, yapionPacketHandler);
        return this;
    }

    public YAPIONPacketReceiver addPacketFilter(@NonNull Predicate<YAPIONPacket> yapionPacketFilter) {
        packetFilters.add(yapionPacketFilter);
        return this;
    }

    public YAPIONPacketReceiver addPacketFilter(@NonNull Class<? extends YAPIONPacket> packetType, @NonNull Predicate<YAPIONPacket> yapionPacketFilter) {
        if (!filterMap.containsKey(packetType.getTypeName())) {
            throw new SecurityException("Packet Type needs to have a YAPIONPacketHandler before declaring filters");
        }
        filterMap.get(packetType.getTypeName()).add(yapionPacketFilter);
        return this;
    }
    public YAPIONPacketReceiver addPacketFilter(@NonNull Predicate<YAPIONPacket> yapionPacketFilter, @NonNull Class<? extends YAPIONPacket> packetType) {
        return addPacketFilter(packetType, yapionPacketFilter);
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
    public void handle(@NonNull YAPIONPacket yapionPacket) {
        String type = yapionPacket.getType();
        if (!handlerMap.containsKey(type)) {
            handle(yapionPacket, Handler.UNKNOWN_PACKET);
            return;
        }
        if (packetFilters.stream().anyMatch(yapionPacketFilter -> yapionPacketFilter.test(yapionPacket))) {
            handle(yapionPacket, Handler.FILTERED);
            return;
        }
        if (filterMap.get(type).stream().anyMatch(yapionPacketFilter -> yapionPacketFilter.test(yapionPacket))) {
            handle(yapionPacket, Handler.FILTERED);
            return;
        }
        handlePacket(yapionPacket, handlerMap.get(type), Handler.USER);
    }

    private void handlePacket(YAPIONPacket yapionPacket, YAPIONPacketHandler handler, Handler currentHandler) {
        Runnable runnable = () -> {
            try {
                handler.handlePacket(yapionPacket);
            } catch (Exception e) {
                log.warn(String.format("Current packet handler executing type '%s' threw an exception. Current handler is %s.", yapionPacket.getType(), currentHandler.name()), e.getCause());
                yapionPacket.setException(e);
                if (handler.closeOnException() && yapionPacket.getYAPIONPacketStream() != null) {
                    try {
                        yapionPacket.getYAPIONPacketStream().close();
                    } catch (IOException ex) {
                        // Ignored
                    }
                    return;
                }
                if (handler.ignoreException()) {
                    return;
                }
                if (currentHandler.onException == null) {
                    throw new SecurityException(yapionPacket.getException().getMessage(), yapionPacket.getException());
                }
                handlePacket(yapionPacket, handlerMap.get(currentHandler.onException.identifier), currentHandler.onException);
            }
        };
        if (handler.runThread()) {
            Thread thread = new Thread(runnable);
            thread.setName("Packet handler Thread (" + yapionPacket.getType() + ")");
            thread.setDaemon(handler.daemonThread());
            thread.start();
        } else {
            runnable.run();
        }
    }

    void handle(YAPIONPacket yapionPacket, Handler handler) {
        handlePacket(yapionPacket, handlerMap.get(handler.identifier), handler);
    }

}
