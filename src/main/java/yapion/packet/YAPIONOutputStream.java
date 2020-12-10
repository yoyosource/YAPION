// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020 yoyosource

package yapion.packet;

import yapion.annotations.deserialize.YAPIONLoadExclude;
import yapion.annotations.serialize.YAPIONSaveExclude;
import yapion.hierarchy.output.StreamOutput;
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
     */
    public void write(YAPIONObject yapionObject) {
        yapionObject.toYAPION(new StreamOutput(outputStream)).flush();
    }

    /**
     * Writing the YAPIONPacket to the OutputStream.
     *
     * @param yapionPacket the YAPIONPacket
     */
    public void write(YAPIONPacket yapionPacket) {
        write(yapionPacket.toYAPION());
    }

    /**
     * Writing the Object to the OutputStream.
     *
     * @param object the Object
     */
    public void write(Object object) {
        write(YAPIONSerializer.serialize(object));
    }

    /**
     * Writing the Object to the OutputStream with a specific state.
     *
     * @param object the Object
     * @param state the specified state
     */
    public void write(Object object, String state) {
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