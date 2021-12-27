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
import yapion.utils.ReflectionsUtils;

import java.io.IOException;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.function.Predicate;

@Slf4j
public class YAPIONPacketReceiver {

    private final Map<String, YAPIONPacketHandler<?>> handlerMap = new HashMap<>();
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
        registerMethodStuff();
    }

    private void registerMethodStuff() {
        Class<?> current = this.getClass();
        while (current != YAPIONPacketReceiver.class) {
            Method[] methods = current.getDeclaredMethods();
            for (Method method : methods) {
                Filter filter = method.getAnnotation(Filter.class);
                if (filter != null) {
                    if (method.getReturnType() != boolean.class) {
                        continue;
                    }
                    Parameter[] parameters = method.getParameters();
                    if (parameters.length != 1) {
                        continue;
                    }
                    Parameter parameter = parameters[0];
                    if (!YAPIONPacket.class.isAssignableFrom(parameter.getType())) {
                        continue;
                    }
                    if (parameter.getType() == YAPIONPacket.class) {
                        log.debug("Registering method '{}' as global filter", method);
                        addPacketFilter(yapionPacket -> {
                            return (boolean) ReflectionsUtils.invokeMethodObjectSystem(method, this, yapionPacket).get();
                        });
                    } else {
                        log.debug("Registering method '{}' as filter for '{}'", method, parameter.getType());
                        addPacketFilter((Class<? extends YAPIONPacket>) parameter.getType(), yapionPacket -> {
                            return (boolean) ReflectionsUtils.invokeMethodObjectSystem(method, this, yapionPacket).get();
                        });
                    }
                }
                PacketHandler packetHandler = method.getAnnotation(PacketHandler.class);
                if (packetHandler != null) {
                    if (method.getReturnType() != void.class) {
                        continue;
                    }
                    Parameter[] parameters = method.getParameters();
                    if (parameters.length != 1) {
                        continue;
                    }
                    Parameter parameter = parameters[0];
                    if (!YAPIONPacket.class.isAssignableFrom(parameter.getType())) {
                        continue;
                    }
                    log.debug("Registering method '{}' as handler for '{}'", method, parameter.getType());
                    add((Class<? extends YAPIONPacket>) parameter.getType(), yapionPacket -> {
                        ReflectionsUtils.invokeMethodObjectSystem(method, this, yapionPacket);
                    });
                }
                OtherHandler otherHandler = method.getAnnotation(OtherHandler.class);
                if (otherHandler != null) {
                    if (method.getReturnType() != void.class) {
                        continue;
                    }
                    Parameter[] parameters = method.getParameters();
                    if (parameters.length != 1) {
                        continue;
                    }
                    Parameter parameter = parameters[0];
                    if (parameter.getType() != YAPIONPacket.class) {
                        continue;
                    }
                    log.debug("Registering method '{}' as otherHandler for '{}'", method, Arrays.toString(otherHandler.value()));
                    for (Handler handler : otherHandler.value()) {
                        setHandler(handler, yapionPacket -> {
                            ReflectionsUtils.invokeMethodObjectSystem(method, this, yapionPacket);
                        });
                    }
                }
            }
            current = current.getSuperclass();
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
    public <T extends YAPIONPacket> YAPIONPacketReceiver add(@NonNull Class<T> packetType, @NonNull YAPIONPacketHandler<T> yapionPacketHandler) {
        handlerMap.put(packetType.getTypeName(), yapionPacketHandler);
        filterMap.put(packetType.getTypeName(), new HashSet<>());
        return this;
    }

    public <T extends YAPIONPacket> YAPIONPacketReceiver remove(@NonNull Class<T> packetType) {
        handlerMap.remove(packetType.getTypeName());
        filterMap.remove(packetType.getTypeName());
        return this;
    }

    /**
     * Set the SpecialHandler {@link YAPIONPacketHandler} to do something.
     *
     * @param yapionPacketHandler the {@link YAPIONPacketHandler} to set
     */
    public YAPIONPacketReceiver setHandler(@NonNull Handler handler, @NonNull YAPIONPacketHandler<YAPIONPacket> yapionPacketHandler) {
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

    public <T extends YAPIONPacket> YAPIONPacketReceiver addPacketFilter(@NonNull Class<T> packetType, @NonNull Predicate<T> yapionPacketFilter) {
        if (!filterMap.containsKey(packetType.getTypeName())) {
            throw new SecurityException("Packet Type needs to have a YAPIONPacketHandler before declaring filters");
        }
        filterMap.get(packetType.getTypeName()).add((Predicate<YAPIONPacket>) yapionPacketFilter);
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

    void handle(YAPIONPacket yapionPacket, Handler handler) {
        handlePacket(yapionPacket, handlerMap.get(handler.identifier), handler);
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

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    protected @interface Filter {
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    protected @interface PacketHandler {
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    protected @interface OtherHandler {
        Handler[] value();
    }
}
