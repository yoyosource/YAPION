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

import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import yapion.hierarchy.api.groups.YAPIONAnyType;
import yapion.hierarchy.types.YAPIONObject;
import yapion.hierarchy.types.YAPIONValue;
import yapion.io.YAPIONInputStream;
import yapion.io.YAPIONOutputStream;
import yapion.io.YAPIONSocket;
import yapion.serializing.TypeReMapper;
import yapion.serializing.YAPIONDeserializer;
import yapion.utils.IdentifierUtils;
import yapion.utils.ReflectionsUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public final class YAPIONPacketStream {

    public static final int DEFAULT_WAIT = 10;
    public static final int LOW_WAIT = 1;
    public static final int HIGH_WAIT = 1000;

    private YAPIONSocket yapionSocket;

    private final YAPIONInputStream yapionInputStream;
    private YAPIONPacketReceiver yapionPacketReceiver = null;

    @Getter
    private final YAPIONOutputStream yapionOutputStream;
    private TypeReMapper typeReMapper = new TypeReMapper();

    private boolean running = true;
    private Thread yapionInputStreamHandler = null;

    private Thread yapionHeartBeatHandler = null;
    private HeartBeatType heartBeatMode = null;
    private long lastHeartbeat = 0;
    private long heartBeatTimeOut = 10000;

    public YAPIONPacketStream(YAPIONSocket yapionSocket) {
        this(yapionSocket.getYAPIONInputStream(), yapionSocket.getYAPIONOutputStream());
        this.yapionSocket = yapionSocket;
    }

    public YAPIONPacketStream(YAPIONInputStream yapionInputStream, YAPIONOutputStream yapionOutputStream) {
        this.yapionInputStream = yapionInputStream;
        this.yapionOutputStream = yapionOutputStream;
        this.yapionSocket = null;
    }

    /**
     * Set a direct receiver for data from this InputStream.
     * If an exception was thrown while reading, parsing, or
     * handling the received Packet an Exception packet will
     * be raised and handled by the same YAPIONPacketReceiver.
     *
     * @param yapionPacketReceiver the receiver
     */
    public void setYAPIONPacketReceiver(YAPIONPacketReceiver yapionPacketReceiver) {
        this.yapionPacketReceiver = yapionPacketReceiver;
        thread(DEFAULT_WAIT);
    }

    /**
     * Set a direct receiver for data from this InputStream.
     * If an exception was thrown while reading, parsing, or
     * handling the received Packet an Exception packet will
     * be raised and handled by the same YAPIONPacketReceiver.
     *
     * @param yapionPacketReceiver the receiver
     * @param time the wait time of the internal receiver loop
     * this time should be between 1 and 1000 ms
     * and will be bounded to a value between it
     * when outside values are given if -1 is
     * given the default will be selected
     */
    public void setYAPIONPacketReceiver(YAPIONPacketReceiver yapionPacketReceiver, int time) {
        if (time == -1) time = DEFAULT_WAIT;
        if (time < LOW_WAIT) time = LOW_WAIT;
        if (time > HIGH_WAIT) time = HIGH_WAIT;
        this.yapionPacketReceiver = yapionPacketReceiver;
        lastHeartbeat = System.currentTimeMillis();
        thread(time);
    }

    /**
     * Set a {@link TypeReMapper} to use for the deserialize call.
     * This is useful to change {@link Package#toString()} or
     * {@link Class#getTypeName()} to another value.
     *
     * @param typeReMapper the {@link TypeReMapper} to use
     */
    public void setTypeReMapper(@NonNull TypeReMapper typeReMapper) {
        this.typeReMapper = typeReMapper;
    }

    private void thread(int time) {
        if (yapionInputStreamHandler != null) return;
        yapionInputStreamHandler = new Thread(() -> {
            while (running) {
                try {
                    Thread.sleep(time);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                if (handleAvailable() == 0) continue;
                try {
                    HandleFailedPacket handleFailedPacket = handle();
                    if (handleFailedPacket != null) {
                        yapionPacketReceiver.handleHandleFailed(handleFailedPacket);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    log.warn("Something went wrong while handling the read object.", e.getCause());
                    drop();
                }
            }
        });
        yapionInputStreamHandler.setDaemon(true);
        yapionInputStreamHandler.start();

        yapionHeartBeatHandler = new Thread(() -> {
            while (running) {
                try {
                    long sleepTime = (heartBeatTimeOut - time) / 5;
                    if (sleepTime < 50) sleepTime = 50;
                    Thread.sleep(sleepTime);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                if (heartBeatMode == null) continue;
                if (heartBeatMode == HeartBeatType.SEND) {
                    yapionOutputStream.write(new HeartBeatPacket());
                }
                if (heartBeatMode == HeartBeatType.SEND_AND_RECEIVE) {
                    yapionOutputStream.write(new HeartBeatPacket());
                }
                if (System.currentTimeMillis() - lastHeartbeat > heartBeatTimeOut) {
                    LostHeartBeatPacket lostHeartBeatPacket = new LostHeartBeatPacket(lastHeartbeat, System.currentTimeMillis() - lastHeartbeat, heartBeatTimeOut);
                    if (yapionOutputStream != null) lostHeartBeatPacket.setYAPIONPacketStream(this);
                    yapionPacketReceiver.handleLostHeartBeat(lostHeartBeatPacket);
                }
            }
        });
        yapionHeartBeatHandler.setDaemon(true);
        yapionHeartBeatHandler.start();
    }

    private void drop() {
        try {
            List<Byte> byteList = new ArrayList<>();
            while (yapionInputStream.available() > 0) {
                byteList.add((byte) yapionInputStream.readByte());
            }
            byte[] bytes = new byte[byteList.size()];
            for (int i = 0; i < byteList.size(); i++) {
                bytes[i] = byteList.get(i);
            }
            DropPacket dropPacket = new DropPacket(bytes);
            if (yapionOutputStream != null) dropPacket.setYAPIONPacketStream(this);
            yapionPacketReceiver.handleDrop(dropPacket);
        } catch (IOException e) {
            // Ignored
        }
    }

    /**
     * Set the heartBeat mode desired for this connection. This takes effect when you call {@link #setYAPIONPacketReceiver(YAPIONPacketReceiver)} or {@link #setYAPIONPacketReceiver(YAPIONPacketReceiver, int)}.
     *
     * @param heartBeatMode the specific mode to use
     * @param heartBeatTimeOut if {@param heartBeatMode} is either {@link HeartBeatType#RECEIVE} or {@link HeartBeatType#SEND_AND_RECEIVE} and no heartbeat packet was received for this amount of milliseconds the LostHeartBeatHandler gets called
     */
    public void setHeartBeatMode(HeartBeatType heartBeatMode, long heartBeatTimeOut) {
        lastHeartbeat = System.currentTimeMillis();
        this.heartBeatMode = heartBeatMode;
        this.heartBeatTimeOut = heartBeatTimeOut;
    }

    private synchronized int handleAvailable() {
        if (yapionPacketReceiver == null) return 0;
        try {
            return yapionInputStream.available();
        } catch (IOException e) {
            return 0;
        }
    }

    private synchronized HandleFailedPacket handle() {
        if (yapionPacketReceiver == null) return null;
        YAPIONObject yapionObject = yapionInputStream.read();
        YAPIONAnyType yapionAnyType = yapionObject.getYAPIONAnyType(IdentifierUtils.TYPE_IDENTIFIER);
        if (yapionAnyType == null) return new HandleFailedPacket(yapionObject);
        if (!(yapionAnyType instanceof YAPIONValue)) return new HandleFailedPacket(yapionObject);
        Object object = ((YAPIONValue) yapionAnyType).get();
        if (!(object instanceof String)) return new HandleFailedPacket(yapionObject);
        try {
            object = new YAPIONDeserializer(yapionObject, "", typeReMapper).parse().getObjectOrException();
        } catch (Exception e) {
            DeserializationExceptionPacket deserializationExceptionPacket = new DeserializationExceptionPacket(yapionObject);
            deserializationExceptionPacket.setException(e);
            yapionPacketReceiver.handleDeserializationException(deserializationExceptionPacket);
            return null;
        }
        if (!ReflectionsUtils.isClassSuperclassOf(object.getClass(), YAPIONPacket.class)) return new HandleFailedPacket(yapionObject);
        YAPIONPacket yapionPacket = (YAPIONPacket) object;
        if (yapionOutputStream != null) yapionPacket.setYAPIONPacketStream(this);
        if (yapionPacket instanceof HeartBeatPacket && heartBeatMode != null && (heartBeatMode == HeartBeatType.RECEIVE || heartBeatMode == HeartBeatType.SEND_AND_RECEIVE)) {
            lastHeartbeat = System.currentTimeMillis();
            yapionPacketReceiver.handleHeartBeat(yapionPacket);
        }
        yapionPacketReceiver.handle(yapionPacket);
        return null;
    }

    public void close() throws IOException {
        running = false;
        if (yapionSocket != null) yapionSocket.close();
        yapionInputStream.close();
        yapionOutputStream.close();
    }

    public void write(YAPIONPacket yapionPacket) {
        yapionOutputStream.write(yapionPacket);
    }

}
