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

package yapion.io;

import yapion.hierarchy.types.YAPIONObject;
import yapion.packet.YAPIONPacket;
import yapion.serializing.TypeReMapper;
import yapion.serializing.views.View;

import java.io.*;
import java.net.Socket;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public final class YAPIONSocket {

    private final Socket socket;
    private final YAPIONInputStream yapionInputStream;
    private final YAPIONOutputStream yapionOutputStream;

    public YAPIONSocket(Socket socket) throws IOException {
        this(socket, false);
    }

    public YAPIONSocket(Socket socket, boolean gzipped) throws IOException {
        this.socket = socket;
        InputStream inputStream = new BufferedInputStream(socket.getInputStream());
        OutputStream outputStream = new BufferedOutputStream(socket.getOutputStream());
        if (gzipped) {
            inputStream = new GZIPInputStream(inputStream);
            outputStream = new GZIPOutputStream(outputStream);
        }
        yapionInputStream = new YAPIONInputStream(inputStream);
        yapionOutputStream = new YAPIONOutputStream(outputStream);
    }

    public Socket getSocket() {
        return socket;
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
        socket.close();
    }

    public int available() throws IOException {
        return yapionInputStream.available();
    }

    public YAPIONObject read() {
        return yapionInputStream.read();
    }

    public <T> T readObject() {
        return yapionInputStream.readObject();
    }

    public <T> T readObject(TypeReMapper typeReMapper) {
        return yapionInputStream.readObject(typeReMapper);
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

    public void write(Object object, Class<? extends View> view) {
        yapionOutputStream.write(object, view);
    }

}
