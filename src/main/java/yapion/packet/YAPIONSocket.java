// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020,2021 yoyosource

package yapion.packet;

import lombok.NonNull;
import yapion.annotations.deserialize.YAPIONLoadExclude;
import yapion.annotations.serialize.YAPIONSaveExclude;
import yapion.hierarchy.types.YAPIONObject;
import yapion.serializing.TypeReMapper;

import java.io.IOException;
import java.net.Socket;

@YAPIONLoadExclude(context = "*")
@YAPIONSaveExclude(context = "*")
public final class YAPIONSocket {

    private final Socket socket;
    private final YAPIONInputStream yapionInputStream;
    private final YAPIONOutputStream yapionOutputStream;

    public YAPIONSocket(Socket socket) throws IOException {
        this.socket = socket;
        yapionInputStream = new YAPIONInputStream(socket.getInputStream());
        yapionOutputStream = new YAPIONOutputStream(socket.getOutputStream());
        yapionInputStream.setRespectiveOutputStream(yapionOutputStream);
    }

    public YAPIONInputStream getYAPIONInputStream() {
        return yapionInputStream;
    }

    public YAPIONOutputStream getYAPIONOutputStream() {
        return yapionOutputStream;
    }

    public void close() throws IOException {
        socket.close();
        yapionInputStream.close();
        yapionOutputStream.close();
    }

    public void setYAPIONPacketReceiver(YAPIONPacketReceiver yapionPacketReceiver) {
        yapionInputStream.setYAPIONPacketReceiver(yapionPacketReceiver);
    }

    public void setYAPIONPacketReceiver(YAPIONPacketReceiver yapionPacketReceiver, int time) {
        yapionInputStream.setYAPIONPacketReceiver(yapionPacketReceiver, time);
    }

    public void setTypeReMapper(@NonNull TypeReMapper typeReMapper) {
        yapionInputStream.setTypeReMapper(typeReMapper);
    }

    public int available() throws IOException {
        return yapionInputStream.available();
    }

    public YAPIONObject read() throws IOException {
        return yapionInputStream.read();
    }

    public Object readObject() throws IOException {
        return yapionInputStream.readObject();
    }

    public void write(YAPIONObject yapionObject) {
        yapionOutputStream.write(yapionObject);
    }

    public void write(YAPIONPacket yapionPacket) {
        yapionOutputStream.write(yapionPacket);
    }

    public void write(Object object) {
        yapionOutputStream.write(object);
    }

    public void write(Object object, String state) {
        yapionOutputStream.write(object, state);
    }

}