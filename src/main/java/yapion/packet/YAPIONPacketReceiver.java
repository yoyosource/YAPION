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

import lombok.extern.slf4j.Slf4j;
import yapion.annotations.deserialize.YAPIONLoadExclude;
import yapion.annotations.serialize.YAPIONSaveExclude;
import yapion.exceptions.YAPIONException;
import yapion.exceptions.utils.YAPIONPacketException;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

@YAPIONLoadExclude(context = "*")
@YAPIONSaveExclude(context = "*")
@Slf4j
public class YAPIONPacketReceiver {

    private final Map<String, YAPIONPacketHandler> handlerMap = new HashMap<>();

    private static final String ERROR_HANDLER = "@error";
    private static final String EXCEPTION_HANDLER = "@exception";
    private static final String UNKNOWN_PACKET_HANDLER = "@unknown";
    private static final String DROP_HANDLER = "@drop";
    private static final String DESERIALIZE_EXCEPTION_HANDLER = "@deserialize";
    private static final String HANDLE_FAILED_HANDLER = "@handle";

    /**
     * Creates an YAPIONPacketReceiver
     */
    public YAPIONPacketReceiver() {
        handlerMap.put(ERROR_HANDLER, yapionPacket -> {
        });
        handlerMap.put(EXCEPTION_HANDLER, yapionPacket -> {
        });
        handlerMap.put(UNKNOWN_PACKET_HANDLER, yapionPacket -> {
        });
        handlerMap.put(DROP_HANDLER, yapionPacket -> {
        });
        handlerMap.put(DESERIALIZE_EXCEPTION_HANDLER, yapionPacket -> {
        });
        handlerMap.put(HANDLE_FAILED_HANDLER, yapionPacket -> {
        });
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
     */
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
     */
    public YAPIONPacketReceiver add(YAPIONPacketHandler yapionPacketHandler, Class<? extends YAPIONPacket>... packetTypes) {
        return add(packetTypes, yapionPacketHandler);
    }

    /**
     * Set the Exception {@link YAPIONPacketHandler} to do something when an exception gets thrown.
     *
     * @param yapionPacketHandler the {@link YAPIONPacketHandler} to set
     */
    public YAPIONPacketReceiver setExceptionHandler(YAPIONPacketHandler yapionPacketHandler) {
        if (yapionPacketHandler == null) {
            throw new YAPIONException();
        }
        handlerMap.put(EXCEPTION_HANDLER, yapionPacketHandler);
        return this;
    }

    /**
     * Set the Unknown {@link YAPIONPacketHandler} to do something when an unknown packet was handled occurred.
     *
     * @param yapionPacketHandler
     */
    public YAPIONPacketReceiver setUnknownHandler(YAPIONPacketHandler yapionPacketHandler) {
        if (yapionPacketHandler == null) {
            throw new YAPIONException();
        }
        handlerMap.put(UNKNOWN_PACKET_HANDLER, yapionPacketHandler);
        return this;
    }

    /**
     * Set the Drop {@link YAPIONPacketHandler} to do something when a data drop occurred.
     *
     * @param yapionPacketHandler
     */
    public YAPIONPacketReceiver setDropHandler(YAPIONPacketHandler yapionPacketHandler) {
        if (yapionPacketHandler == null) {
            throw new YAPIONException();
        }
        handlerMap.put(DROP_HANDLER, yapionPacketHandler);
        return this;
    }

    /**
     * Set the Deserialization Exception {@link YAPIONPacketHandler} to do something when a deserialization exception occurred.
     *
     * @param yapionPacketHandler
     */
    public YAPIONPacketReceiver setDeserializationExceptionHandler(YAPIONPacketHandler yapionPacketHandler) {
        if (yapionPacketHandler == null) {
            throw new YAPIONException();
        }
        handlerMap.put(DESERIALIZE_EXCEPTION_HANDLER, yapionPacketHandler);
        return this;
    }

    /**
     * Set the HandleFailed {@link YAPIONPacketHandler} to do something when the internal {@link YAPIONInputStream}.handle() failed.
     *
     * @param yapionPacketHandler
     */
    public YAPIONPacketReceiver setHandleFailedHandler(YAPIONPacketHandler yapionPacketHandler) {
        if (yapionPacketHandler == null) {
            throw new YAPIONException();
        }
        handlerMap.put(HANDLE_FAILED_HANDLER, yapionPacketHandler);
        return this;
    }

    /**
     * Set the Error {@link YAPIONPacketHandler} to do something when an error occurred.
     *
     * @param yapionPacketHandler the {@link YAPIONPacketHandler} to set
     */
    public YAPIONPacketReceiver setErrorHandler(YAPIONPacketHandler yapionPacketHandler) {
        if (yapionPacketHandler == null) {
            throw new YAPIONException();
        }
        handlerMap.put(ERROR_HANDLER, yapionPacketHandler);
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

    void handleDrop(YAPIONPacket yapionPacket) {
        handlePacket(yapionPacket, DROP_HANDLER, handlerMap.get(DROP_HANDLER), this::handleError);
    }

    void handleDeserializationException(YAPIONPacket yapionPacket) {
        handlePacket(yapionPacket, DESERIALIZE_EXCEPTION_HANDLER, handlerMap.get(DESERIALIZE_EXCEPTION_HANDLER), this::handleError);
    }

    void handleHandleFailed(YAPIONPacket yapionPacket) {
        handlePacket(yapionPacket, HANDLE_FAILED_HANDLER, handlerMap.get(HANDLE_FAILED_HANDLER), this::handleError);
    }

    private void handleUnknown(YAPIONPacket yapionPacket) {
        handlePacket(yapionPacket, UNKNOWN_PACKET_HANDLER, handlerMap.get(UNKNOWN_PACKET_HANDLER), this::handleError);
    }

    private void handleException(YAPIONPacket yapionPacket) {
        handlePacket(yapionPacket, EXCEPTION_HANDLER, handlerMap.get(EXCEPTION_HANDLER), this::handleError);
    }

    private void handleError(YAPIONPacket yapionPacket) {
        handlePacket(yapionPacket, ERROR_HANDLER, handlerMap.get(ERROR_HANDLER), packet -> {
            throw new SecurityException(packet.getException().getMessage(), packet.getException());
        });
    }

}
