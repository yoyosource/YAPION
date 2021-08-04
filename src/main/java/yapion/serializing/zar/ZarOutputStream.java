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

package yapion.serializing.zar;

import yapion.hierarchy.types.YAPIONObject;

import java.io.IOException;
import java.io.OutputStream;

public class ZarOutputStream extends OutputStream implements AutoCloseable {

    private final OutputStream outputStream;
    private long lengthLeft;

    public ZarOutputStream(OutputStream outputStream) throws IOException {
        this.outputStream = outputStream;
        outputStream.write("zar".getBytes());
    }

    public void addFile(String name, long length, YAPIONObject metaData) throws IOException {
        if (lengthLeft != 0) {
            throw new IOException("Last File was not finished completely");
        }

        writeLength(name.length());
        outputStream.write(name.getBytes());

        if (metaData == null) {
            writeLength(0);
        } else {
            writeLength(metaData.toLength());
            metaData.toStream(outputStream);
        }

        writeLength(length);
        lengthLeft = length;
    }

    public void addFile(String name, long length) throws IOException {
        addFile(name, length, null);
    }

    @Override
    public void write(int b) throws IOException {
        if (lengthLeft == 0) {
            throw new IOException("Last File is completed");
        }

        outputStream.write(b);
        lengthLeft--;
    }

    private void writeLength(long length) throws IOException {
        int size = 0;
        long tempLength = length;
        while (tempLength > 0) {
            size += 1;
            tempLength /= 256;
        }
        if (size > 7) {
            throw new IOException("Size to long");
        }
        outputStream.write((byte) size);
        while (length > 0) {
            outputStream.write((byte) (length & 0b11111111));
            length /= 256;
        }
    }

    public void close() throws IOException {
        outputStream.write((byte) 0);
        outputStream.write((byte) 0);
        outputStream.write((byte) 0);
        outputStream.close();
    }
}
