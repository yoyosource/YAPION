// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020 yoyosource

package yapion.packet;

import yapion.annotations.deserialize.YAPIONLoadExclude;
import yapion.annotations.serialize.YAPIONSaveExclude;
import yapion.hierarchy.YAPIONAny;
import yapion.hierarchy.types.YAPIONObject;
import yapion.hierarchy.types.YAPIONValue;
import yapion.parser.YAPIONParser;
import yapion.serializing.YAPIONDeserializer;

import java.io.IOException;
import java.io.InputStream;

@YAPIONSaveExclude(context = "*")
@YAPIONLoadExclude(context = "*")
public class YAPIONInputStream {

    private final InputStream inputStream;
    private YAPIONPacketReceiver yapionPacketReceiver = null;

    private Thread yapionInputStreamHandler = null;
    private boolean running = true;

    public YAPIONInputStream(InputStream inputStream) {
        yapionInputStreamHandler = new Thread(() -> {
           while (running) {
               try {
                   Thread.sleep(100);
               } catch (InterruptedException e) {
                   Thread.currentThread().interrupt();
               }
               if (handleAvailable() == 0) continue;
               handle();
           }
        });
        yapionInputStreamHandler.setDaemon(true);
        yapionInputStreamHandler.start();
        this.inputStream = inputStream;
    }

    private void setYAPIONPacketReceiver(YAPIONPacketReceiver yapionPacketReceiver) {
        this.yapionPacketReceiver = yapionPacketReceiver;
    }

    public synchronized int available() throws IOException {
        if (yapionPacketReceiver != null) throw new IOException();
        return inputStream.available();
    }

    public synchronized YAPIONObject read() throws IOException {
        if (yapionPacketReceiver != null) throw new IOException();
        return YAPIONParser.parse(inputStream);
    }

    public synchronized Object readObject() throws IOException {
        if (yapionPacketReceiver != null) throw new IOException();
        return YAPIONDeserializer.deserialize(read());
    }

    public synchronized void close() throws IOException {
        running = false;
        inputStream.close();
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
        if (yapionObject.getVariable("@type") == null) return;
        YAPIONAny yapionAny = yapionObject.getVariable("@type").getValue();
        if (!(yapionAny instanceof YAPIONValue)) return;
        Object object = ((YAPIONValue)yapionAny).get();
        if (!(object instanceof String)) return;
        if (!object.equals(YAPIONPacket.class.getTypeName())) return;
        yapionPacketReceiver.handle((YAPIONPacket) YAPIONDeserializer.deserialize(yapionObject));
    }

}