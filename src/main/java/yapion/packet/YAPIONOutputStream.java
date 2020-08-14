// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020 yoyosource

package yapion.packet;

import yapion.annotations.YAPIONLoadExclude;
import yapion.annotations.YAPIONSaveExclude;
import yapion.hierarchy.types.YAPIONObject;
import yapion.serializing.YAPIONSerializer;

import java.io.IOException;
import java.io.OutputStream;

@YAPIONSaveExclude(context = "*")
@YAPIONLoadExclude(context = "*")
public class YAPIONOutputStream {

    private final OutputStream outputStream;

    public YAPIONOutputStream(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    public void write(YAPIONObject yapionObject) throws IOException {
        yapionObject.toOutputStream(outputStream);
        outputStream.flush();
    }

    public void write(Object object) throws IOException {
        write(YAPIONSerializer.serialize(object));
    }

    public void write(Object object, String state) throws IOException {
        write(YAPIONSerializer.serialize(object, state));
    }

    public void close() throws IOException {
        outputStream.close();
    }

}