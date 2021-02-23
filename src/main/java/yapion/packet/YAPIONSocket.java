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
