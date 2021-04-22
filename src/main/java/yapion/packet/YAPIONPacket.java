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

import yapion.hierarchy.types.YAPIONObject;
import yapion.io.YAPIONOutputStream;
import yapion.io.YAPIONSocket;
import yapion.serializing.YAPIONSerializer;

import java.io.InputStream;
import java.io.OutputStream;

public abstract class YAPIONPacket {

    private transient Exception exception;
    private transient YAPIONPacketStream yapionPacketStream;

    /**
     * Serialize this {@link YAPIONPacket} to a {@link YAPIONObject}.
     *
     * @return a {@link YAPIONObject}
     */
    public final YAPIONObject toYAPION() {
        return YAPIONSerializer.serialize(this);
    }

    /**
     * The type name returned by {@link Class#getTypeName()}.
     *
     * @return result of {@link Class#getTypeName()}
     */
    public final String getType() {
        return getClass().getTypeName();
    }

    /**
     * The exception that was thrown by the {@link YAPIONPacketHandler#handlePacket(YAPIONPacket)}
     * method. You could for example log this {@link Exception} in the {@link YAPIONPacketHandler}
     * set with {@link YAPIONPacketReceiver#setHandler(YAPIONPacketReceiver.SpecialHandler, YAPIONPacketHandler)},
     * with {@link YAPIONPacketReceiver.SpecialHandler#EXCEPTION}.
     *
     * @return the {@link Exception} that was thrown
     */
    public final Exception getException() {
        return exception;
    }

    final void setException(Exception exception) {
        this.exception = exception;
    }

    /**
     * Returns the {@link YAPIONOutputStream} set by {@link YAPIONSocket} and
     * is the {@link OutputStream} to the {@link InputStream}.
     *
     * @return the {@link YAPIONOutputStream} set by {@link YAPIONSocket}
     */
    public final YAPIONPacketStream getYAPIONPacketStream() {
        return yapionPacketStream;
    }

    /**
     * Set the {@link YAPIONOutputStream} that corresponds to the {@link InputStream}.
     * This normally gets called by {@link YAPIONPacketStream} and will be set to the
     * {@link YAPIONOutputStream} by the {@link YAPIONSocket} corresponding to this.
     *
     * @param yapionPacketStream the {@link YAPIONOutputStream} to set
     */
    public final void setYAPIONPacketStream(YAPIONPacketStream yapionPacketStream) {
        this.yapionPacketStream = yapionPacketStream;
    }

}
