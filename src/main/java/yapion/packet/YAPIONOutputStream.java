// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020 yoyosource

package yapion.packet;

import yapion.annotations.deserialize.YAPIONLoadExclude;
import yapion.annotations.serialize.YAPIONSaveExclude;
import yapion.hierarchy.types.YAPIONObject;
import yapion.serializing.YAPIONSerializer;

import java.io.IOException;
import java.io.OutputStream;

@YAPIONSaveExclude(context = "*")
@YAPIONLoadExclude(context = "*")
public final class YAPIONOutputStream {

    private final OutputStream outputStream;

    /**
     * Creates a YAPIONOutputStream from an OutputStream.
     *
     * @param outputStream the OutputStream
     */
    public YAPIONOutputStream(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    /**
     * Writing the YAPIONObject to the OutputStream.
     *
     * @param yapionObject the YAPIONObject
     * @throws IOException {@link OutputStream#flush()}
     */
    public void write(YAPIONObject yapionObject) throws IOException {
        yapionObject.toOutputStream(outputStream);
        outputStream.flush();
    }

    /**
     * Writing the YAPIONPacket to the OutputStream.
     *
     * @param yapionPacket the YAPIONPacket
     * @throws IOException {@link #write(YAPIONObject)}
     */
    public void write(YAPIONPacket yapionPacket) throws IOException {
        write(yapionPacket.getYAPION());
    }

    /**
     * Writing the Object to the OutputStream.
     *
     * @param object the Object
     * @throws IOException {@link #write(YAPIONPacket)}
     */
    public void write(Object object) throws IOException {
        write(YAPIONSerializer.serialize(object));
    }

    /**
     * Writing the Object to the OutputStream with a specific state.
     *
     * @param object the Object
     * @param state the specified state
     * @throws IOException {@link #write(Object)}
     */
    public void write(Object object, String state) throws IOException {
        write(YAPIONSerializer.serialize(object, state));
    }

    /**
     * Closes the OutputStream.
     *
     * @throws IOException {@link OutputStream#close()}
     */
    public void close() throws IOException {
        outputStream.close();
    }

}