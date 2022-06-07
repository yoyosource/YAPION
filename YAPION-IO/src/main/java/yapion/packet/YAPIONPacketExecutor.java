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
import yapion.hierarchy.api.groups.YAPIONAnyType;
import yapion.hierarchy.types.YAPIONObject;
import yapion.hierarchy.types.YAPIONValue;
import yapion.serializing.YAPIONDeserializer;
import yapion.utils.IdentifierUtils;
import yapion.utils.ReflectionsUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class YAPIONPacketExecutor {

    private static final AtomicInteger ID_COUNTER = new AtomicInteger(0);

    private final Set<YAPIONPacketStream> streams = new HashSet<>();
    private final Set<YAPIONPacketStream> awaitHandle = new HashSet<>();
    private boolean running = true;
    private final Thread thread;
    final ExecutorService executorService;

    private long timeout = -1;
    private Map<YAPIONPacketStream, Long> lastPacketTime = new HashMap<>();

    public YAPIONPacketExecutor() {
        this(4);
    }

    public YAPIONPacketExecutor(int threads) {
        thread = new Thread(this::run);
        thread.setDaemon(true);
        thread.setName("YAPIONPacketExecutor-" + ID_COUNTER.getAndIncrement());
        thread.start();
        executorService = Executors.newFixedThreadPool(threads, r -> {
            Thread t = new Thread(r);
            t.setDaemon(true);
            return t;
        });
    }

    public void stop() {
        running = false;
    }

    public void register(YAPIONPacketStream stream) {
        if (stream.executor != null && stream.executor != this) {
            throw new IllegalArgumentException("Stream is already registered!");
        }
        synchronized (streams) {
            streams.add(stream);
            lastPacketTime.put(stream, System.currentTimeMillis());
        }
        stream.executor = this;
    }

    public void unregister(YAPIONPacketStream stream) {
        synchronized (streams) {
            streams.remove(stream);
            lastPacketTime.remove(stream);
        }
        if (stream.executor == this) {
            stream.executor = null;
        }
    }

    public void timeout(long millis) {
        if (millis < 0) {
            timeout = -1;
        } else {
            timeout = millis;
        }
    }

    private void run() {
        Set<YAPIONPacketStream> toHandle = new HashSet<>();
        Set<YAPIONPacketStream> timeouted = new HashSet<>();
        while (running) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            toHandle.clear();
            synchronized (streams) {
                streams.stream()
                        .filter(yapionPacketStream -> !awaitHandle.contains(yapionPacketStream))
                        .filter(this::hasAvailable)
                        .forEach(toHandle::add);
            }

            timeouted.clear();
            lastPacketTime.forEach((stream, time) -> {
                if (toHandle.contains(stream)) {
                    return;
                }
                long staleTime = System.currentTimeMillis() - time;
                if ((timeout != -1 && staleTime > timeout) || (stream.getTimeout() != -1 && staleTime > stream.getTimeout())) {
                    timeouted.add(stream);
                }
            });
            timeouted.forEach(stream -> {
                log.warn("Stream {} timed out!", stream);
                TimeoutPacket timeoutPacket = new TimeoutPacket();
                timeoutPacket.setYAPIONPacketStream(stream);
                stream.getYapionPacketReceiver().handle(timeoutPacket, YAPIONPacketReceiver.Handler.TIMEOUT);
            });

            if (toHandle.isEmpty()) continue;
            toHandle.forEach(stream -> {
                lastPacketTime.put(stream, System.currentTimeMillis());
                synchronized (awaitHandle) {
                    awaitHandle.add(stream);
                }
                executorService.submit(() -> {
                    try {
                        HandleFailedPacket handleFailedPacket = handlePacket(stream);
                        if (handleFailedPacket != null) {
                            stream.getYapionPacketReceiver().handle(handleFailedPacket, YAPIONPacketReceiver.Handler.HANDLE_FAILED);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        log.warn("Something went wrong while handling the read object.", e.getCause());
                    }
                    synchronized (awaitHandle) {
                        awaitHandle.remove(stream);
                    }
                });
            });
        }
    }

    private boolean hasAvailable(YAPIONPacketStream yapionPacketStream) {
        try {
            return yapionPacketStream.getYapionSocket().available() > 0;
        } catch (IOException e) {
            return false;
        }
    }

    private HandleFailedPacket handlePacket(YAPIONPacketStream yapionPacketStream) {
        if (yapionPacketStream.getYapionPacketReceiver() == null) return null;
        YAPIONObject yapionObject = yapionPacketStream.getYapionInputStream().read();
        YAPIONAnyType yapionAnyType = yapionObject.getAnyType(IdentifierUtils.TYPE_IDENTIFIER);
        if (yapionAnyType == null) return new HandleFailedPacket(yapionObject);
        if (!(yapionAnyType instanceof YAPIONValue)) return new HandleFailedPacket(yapionObject);
        Object object = ((YAPIONValue) yapionAnyType).get();
        if (!(object instanceof String)) return new HandleFailedPacket(yapionObject);
        try {
            object = new YAPIONDeserializer(yapionObject, null, yapionPacketStream.getTypeReMapper()).parse().getObjectOrException();
        } catch (Exception e) {
            DeserializationExceptionPacket deserializationExceptionPacket = new DeserializationExceptionPacket(yapionObject);
            deserializationExceptionPacket.setException(e);
            yapionPacketStream.getYapionPacketReceiver().handle(deserializationExceptionPacket, YAPIONPacketReceiver.Handler.DESERIALIZE_EXCEPTION);
            return null;
        }
        if (!ReflectionsUtils.isClassSuperclassOf(object.getClass(), YAPIONPacket.class)) return new HandleFailedPacket(yapionObject);
        YAPIONPacket yapionPacket = (YAPIONPacket) object;
        if (yapionPacketStream.getYapionOutputStream() != null) yapionPacket.setYAPIONPacketStream(yapionPacketStream);
        yapionPacketStream.getYapionPacketReceiver().handle(yapionPacket);
        return null;
    }
}
