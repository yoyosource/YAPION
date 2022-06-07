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
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import yapion.io.YAPIONInputStream;
import yapion.io.YAPIONOutputStream;
import yapion.io.YAPIONSocket;
import yapion.serializing.TypeReMapper;

import java.io.IOException;
import java.net.Socket;

@Slf4j
public final class YAPIONPacketStream {

    @Getter
    private YAPIONSocket yapionSocket = null;

    @Getter
    private final YAPIONInputStream yapionInputStream;

    @Getter
    private YAPIONPacketReceiver yapionPacketReceiver = null;

    @Getter
    private final YAPIONOutputStream yapionOutputStream;

    @Getter
    private TypeReMapper typeReMapper = new TypeReMapper();

    YAPIONPacketExecutor executor = null;

    @Getter
    @Setter
    private long timeout = -1;

    public YAPIONPacketStream(Socket socket) throws IOException {
        this(new YAPIONSocket(socket));
    }

    public YAPIONPacketStream(YAPIONSocket yapionSocket) {
        this(yapionSocket.getYAPIONInputStream(), yapionSocket.getYAPIONOutputStream());
        this.yapionSocket = yapionSocket;
    }

    public YAPIONPacketStream(YAPIONInputStream yapionInputStream, YAPIONOutputStream yapionOutputStream) {
        this.yapionInputStream = yapionInputStream;
        this.yapionOutputStream = yapionOutputStream;
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

    public void close() throws IOException {
        if (executor != null) executor.unregister(this);
        if (yapionSocket != null) yapionSocket.close();
        yapionInputStream.close();
        yapionOutputStream.close();
    }

    public void write(YAPIONPacket yapionPacket) {
        yapionOutputStream.write(yapionPacket);
    }
}
