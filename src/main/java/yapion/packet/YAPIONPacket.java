// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020 yoyosource

package yapion.packet;

import yapion.annotations.object.YAPIONData;
import yapion.hierarchy.types.YAPIONObject;
import yapion.serializing.YAPIONSerializer;

import java.io.InputStream;
import java.io.OutputStream;

@YAPIONData
public abstract class YAPIONPacket {

    private transient Exception exception;
    private transient YAPIONOutputStream yapionOutputStream;

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
     * set with {@link YAPIONPacketReceiver#setErrorHandler(YAPIONPacketHandler)}.
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
    public final YAPIONOutputStream getYAPIONOutputStream() {
        return yapionOutputStream;
    }

    /**
     * Set the {@link YAPIONOutputStream} that corresponds to the {@link InputStream}.
     * This normally gets called by {@link YAPIONInputStream} and will be set to the
     * {@link YAPIONInputStream} by the {@link YAPIONSocket} corresponding to this.
     *
     * @param yapionOutputStream the {@link YAPIONOutputStream} to set
     */
    public final void setYAPIONOutputStream(YAPIONOutputStream yapionOutputStream) {
        this.yapionOutputStream = yapionOutputStream;
    }
}