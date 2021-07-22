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
import yapion.parser.YAPIONParser;

import java.io.IOException;
import java.io.InputStream;

public class ZarInputStream extends InputStream implements AutoCloseable {

    private String name;
    private YAPIONObject metaData;
    private long size;

    private final InputStream inputStream;
    private long lengthLeft;

    public ZarInputStream(InputStream inputStream) throws IOException {
        this.inputStream = inputStream;
        if (inputStream.read() != 'z' || inputStream.read() != 'a' || inputStream.read() != 'r') {
            throw new IOException("Illegal Identifier");
        }
    }

    public boolean hasFile() throws IOException {
        return lengthLeft == 0 && inputStream.available() > 0;
    }

    public void nextFile() throws IOException {
        if (lengthLeft != 0) {
            throw new IOException("Last File was not finished completely");
        }

        long length = readLength();
        StringBuilder st = new StringBuilder();
        for (int i = 0; i < length; i++) {
            st.append((char) inputStream.read());
        }
        name = st.toString();

        length = readLength();
        if (length == 0) {
            metaData = new YAPIONObject();
        } else {
            metaData = YAPIONParser.parse(inputStream);
        }

        lengthLeft = readLength();
        size = lengthLeft;
    }

    public String getFile() {
        return name;
    }

    public YAPIONObject getMetaData() {
        return metaData;
    }

    public long getSize() {
        return size;
    }

    public int available() {
        return (int) lengthLeft;
    }

    @Override
    public int read() throws IOException {
        if (lengthLeft == 0) {
            throw new IOException("Last File is completed");
        }
        lengthLeft--;
        return inputStream.read();
    }

    private long readLength() throws IOException {
        int bytesToRead = inputStream.read() + 1;
        long size = 0;
        for (int i = 0; i < bytesToRead; i++) {
            size |= inputStream.read() * (long) Math.pow(256, i);
        }
        return size;
    }

    public void close() throws IOException {
        inputStream.close();
    }
}
