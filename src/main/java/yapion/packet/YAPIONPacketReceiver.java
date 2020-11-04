// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020 yoyosource

package yapion.packet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import yapion.annotations.deserialize.YAPIONLoadExclude;
import yapion.annotations.serialize.YAPIONSaveExclude;
import yapion.exceptions.utils.YAPIONPacketException;

import java.util.HashMap;
import java.util.Map;

@YAPIONLoadExclude(context = "*")
@YAPIONSaveExclude(context = "*")
public class YAPIONPacketReceiver {

    private final Map<String, YAPIONPacketHandler> handlerMap = new HashMap<>();
    private static final Logger logger = LoggerFactory.getLogger(YAPIONPacketReceiver.class);

    public static final String ERROR_HANDLER = "@error";
    public static final String EXCEPTION_HANDLER = "@exception";

    /**
     * Creates an YAPIONPacketReceiver
     */
    public YAPIONPacketReceiver() {
        handlerMap.put(ERROR_HANDLER, yapionPacket -> {
        });
        handlerMap.put(EXCEPTION_HANDLER, yapionPacket -> {
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
    public YAPIONPacketReceiver add(String packetType, YAPIONPacketHandler yapionPacketHandler) {
        if (yapionPacketHandler == null || packetType == null) {
            throw new YAPIONPacketException();
        }
        handlerMap.put(packetType, yapionPacketHandler);
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
    public YAPIONPacketReceiver add(String[] packetTypes, YAPIONPacketHandler yapionPacketHandler) {
        if (packetTypes == null) {
            throw new YAPIONPacketException();
        }
        for (String s : packetTypes) {
            if (s == null) continue;
            add(s, yapionPacketHandler);
        }
        return this;
    }

    /**
     * A wrapper function to {@link #add(String, YAPIONPacketHandler)}
     */
    public YAPIONPacketReceiver add(YAPIONPacketHandler yapionPacketHandler, String packetType) {
        return add(packetType, yapionPacketHandler);
    }

    /**
     * A wrapper function to {@link #add(String[], YAPIONPacketHandler)}
     */
    public YAPIONPacketReceiver add(YAPIONPacketHandler yapionPacketHandler, String... packetTypes) {
        return add(packetTypes, yapionPacketHandler);
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
            handleError(yapionPacket);
            return;
        }
        handlePacket(yapionPacket, type, handlerMap.get(type));
    }

    private void handlePacket(YAPIONPacket yapionPacket, String type, YAPIONPacketHandler handler) {
        Runnable runnable = () -> {
            try {
                handler.handlePacket(yapionPacket);
            } catch (Exception e) {
                logger.warn(String.format("The packet handler with type '%s' threw an exception.", type), e.getCause());
                if (!handler.ignoreException()) handleException(yapionPacket, e);
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

    private void handleError(YAPIONPacket yapionPacket) {
        try {
            handlerMap.get(ERROR_HANDLER).handlePacket(yapionPacket);
        } catch (Exception e) {
            logger.warn("The packet handler with type '" + ERROR_HANDLER + "' threw an exception.", e.getCause());
            handleException(yapionPacket, e);
        }
    }

    void handleException(YAPIONPacket yapionPacket, Exception exception) {
        try {
            yapionPacket.add(EXCEPTION_HANDLER, exception);
            handlerMap.get(EXCEPTION_HANDLER).handlePacket(yapionPacket);
        } catch (Exception e) {
            logger.warn("The packet handler with type '" + EXCEPTION_HANDLER + "' threw an exception.", e.getCause());
        }
    }

}