// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020 yoyosource

package yapion.packet;

import lombok.extern.slf4j.Slf4j;
import yapion.annotations.deserialize.YAPIONLoadExclude;
import yapion.annotations.serialize.YAPIONSaveExclude;
import yapion.hierarchy.typegroups.YAPIONAnyType;
import yapion.hierarchy.types.YAPIONObject;
import yapion.hierarchy.types.YAPIONValue;
import yapion.hierarchy.types.YAPIONVariable;
import yapion.parser.YAPIONParser;
import yapion.serializing.YAPIONDeserializer;

import java.io.IOException;
import java.io.InputStream;

@YAPIONSaveExclude(context = "*")
@YAPIONLoadExclude(context = "*")
@Slf4j
public final class YAPIONInputStream {

    public static final int DEFAULT_WAIT = 10;
    public static final int LOW_WAIT = 1;
    public static final int HIGH_WAIT = 1000;

    private final InputStream inputStream;
    private YAPIONPacketReceiver yapionPacketReceiver = null;

    private YAPIONPacketIdentifier<?> staticIdentifier = null;
    private YAPIONPacketIdentifierCreator<?> dynamicIdentifier = null;

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
                    handle();
                } catch (Exception e) {
                    drop();
                    if (yapionPacketReceiver != null) {
                        YAPIONPacket yapionPacket = new YAPIONPacket("");
                        addIdentifier(yapionPacket);
                        yapionPacketReceiver.handleException(yapionPacket, e);
                    }
                    log.warn("Something went wrong while handling the read object.", e.getCause());
                }
            }
        });
        yapionInputStreamHandler.setDaemon(true);
        yapionInputStreamHandler.start();
    }

    private void drop() {
        try {
            while (inputStream.available() > 0) inputStream.read();
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
        return YAPIONDeserializer.deserialize(read());
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

    public synchronized void identifier(YAPIONPacketIdentifier<?> yapionPacketIdentifier) {
        this.dynamicIdentifier = null;
        this.staticIdentifier = yapionPacketIdentifier;
    }

    public synchronized void identifier(YAPIONPacketIdentifierCreator<?> yapionPacketIdentifierCreator) {
        this.staticIdentifier = null;
        this.dynamicIdentifier = yapionPacketIdentifierCreator;
    }

    private synchronized int handleAvailable() {
        if (yapionPacketReceiver == null) return 0;
        try {
            return inputStream.available();
        } catch (IOException e) {
            return 0;
        }
    }

    private synchronized void handle() {
        if (yapionPacketReceiver == null) return;
        YAPIONObject yapionObject = YAPIONParser.parse(inputStream);
        YAPIONVariable variable = yapionObject.getVariable("@type");
        if (variable == null) return;
        YAPIONAnyType yapionAnyType = variable.getValue();
        if (!(yapionAnyType instanceof YAPIONValue)) return;
        Object object = ((YAPIONValue) yapionAnyType).get();
        if (!(object instanceof String)) return;
        if (!object.equals(YAPIONPacket.class.getTypeName())) return;
        YAPIONPacket yapionPacket = (YAPIONPacket) YAPIONDeserializer.deserialize(yapionObject);
        addIdentifier(yapionPacket);
        yapionPacketReceiver.handle(yapionPacket);
    }

    private void addIdentifier(YAPIONPacket yapionPacket) {
        if (staticIdentifier != null) yapionPacket.setYapionPacketIdentifier(staticIdentifier);
        if (dynamicIdentifier != null) yapionPacket.setYapionPacketIdentifier(dynamicIdentifier.identifier());
    }

}