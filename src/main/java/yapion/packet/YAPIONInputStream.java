// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020,2021 yoyosource

package yapion.packet;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import yapion.annotations.deserialize.YAPIONLoadExclude;
import yapion.annotations.serialize.YAPIONSaveExclude;
import yapion.hierarchy.api.groups.YAPIONAnyType;
import yapion.hierarchy.types.YAPIONObject;
import yapion.hierarchy.types.YAPIONValue;
import yapion.parser.YAPIONParser;
import yapion.serializing.TypeReMapper;
import yapion.serializing.YAPIONDeserializer;
import yapion.utils.ReflectionsUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@YAPIONSaveExclude(context = "*")
@YAPIONLoadExclude(context = "*")
@Slf4j
public final class YAPIONInputStream {

    public static final int DEFAULT_WAIT = 10;
    public static final int LOW_WAIT = 1;
    public static final int HIGH_WAIT = 1000;

    private final InputStream inputStream;
    private YAPIONPacketReceiver yapionPacketReceiver = null;
    private YAPIONOutputStream respectiveOutputStream = null;
    private TypeReMapper typeReMapper = new TypeReMapper();

    private Thread yapionInputStreamHandler = null;
    private boolean running = true;

    /**
     * Creates a YAPIONInputStream from an InputStream.
     *
     * @param inputStream the InputStream
     */
    public YAPIONInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
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
                    log.warn("Something went wrong while handling the read object.", e.getCause());
                    drop();
                }
            }
        });
        yapionInputStreamHandler.setDaemon(true);
        yapionInputStreamHandler.start();
    }

    private void drop() {
        try {
            List<Byte> byteList = new ArrayList<>();
            while (inputStream.available() > 0) {
                byteList.add((byte) inputStream.read());
            }
            byte[] bytes = new byte[byteList.size()];
            for (int i = 0; i < byteList.size(); i++) {
                bytes[i] = byteList.get(i);
            }
            DropPacket dropPacket = new DropPacket(bytes);
            if (respectiveOutputStream != null) dropPacket.setYAPIONOutputStream(respectiveOutputStream);
            yapionPacketReceiver.handleDrop(dropPacket);
        } catch (IOException e) {

        }
    }

    /**
     * Returns an estimate of bytes to be able to read.
     *
     * @return the estimated byte count
     * @throws IOException {@link YAPIONPacketReceiver} not null or {@link InputStream#available()}
     */
    public synchronized int available() throws IOException {
        if (yapionPacketReceiver != null) throw new IOException();
        return inputStream.available();
    }

    /**
     * Read and parses the next YAPIONObject.
     *
     * @return the next YAPIONObject
     * @throws IOException {@link YAPIONPacketReceiver} not null
     */
    public synchronized YAPIONObject read() throws IOException {
        if (yapionPacketReceiver != null) throw new IOException();
        return YAPIONParser.parse(inputStream);
    }

    /**
     * Read, parses and deserialized the next YAPIONObject.
     *
     * @return the next Object
     * @throws IOException {@link YAPIONPacketReceiver} not null
     */
    public synchronized Object readObject() throws IOException {
        if (yapionPacketReceiver != null) throw new IOException();
        return YAPIONDeserializer.deserialize(read(), typeReMapper);
    }

    /**
     * Closes this InputStream and tries to close the handler Thread
     *
     * @throws IOException {@link InputStream#close()}
     */
    public synchronized void close() throws IOException {
        running = false;
        inputStream.close();
    }

    void setRespectiveOutputStream(YAPIONOutputStream respectiveOutputStream) {
        this.respectiveOutputStream = respectiveOutputStream;
    }

    private synchronized int handleAvailable() {
        if (yapionPacketReceiver == null) return 0;
        try {
            return inputStream.available();
        } catch (IOException e) {
            return 0;
        }
    }

    private synchronized HandleFailedPacket handle() {
        if (yapionPacketReceiver == null) return null;
        YAPIONObject yapionObject = YAPIONParser.parse(inputStream);
        YAPIONAnyType yapionAnyType = yapionObject.getYAPIONAnyType("@type");
        if (yapionAnyType == null) return new HandleFailedPacket(yapionObject);
        if (!(yapionAnyType instanceof YAPIONValue)) return new HandleFailedPacket(yapionObject);
        Object object = ((YAPIONValue) yapionAnyType).get();
        if (!(object instanceof String)) return new HandleFailedPacket(yapionObject);
        try {
            object = YAPIONDeserializer.deserialize(yapionObject);
        } catch (Exception e) {
            DeserializationExceptionPacket deserializationExceptionPacket = new DeserializationExceptionPacket(yapionObject);
            deserializationExceptionPacket.setException(e);
            yapionPacketReceiver.handleDeserializationException(deserializationExceptionPacket);
            return null;
        }
        if (!ReflectionsUtils.isClassSuperclassOf(object.getClass(), YAPIONPacket.class)) return new HandleFailedPacket(yapionObject);
        YAPIONPacket yapionPacket = (YAPIONPacket) object;
        if (respectiveOutputStream != null) yapionPacket.setYAPIONOutputStream(respectiveOutputStream);
        yapionPacketReceiver.handle(yapionPacket);
        return null;
    }

}