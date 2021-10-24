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

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;

public class ZarInputElement {

    private String name;

    private long lengthLeft;
    private MappedByteBuffer mappedByteBuffer;

    public ZarInputElement(MappedByteBuffer mappedByteBuffer) {
        this.mappedByteBuffer = mappedByteBuffer;
    }

    private ByteBuffer get() throws IOException {
        if (lengthLeft != 0) {
            throw new IOException("Last File was not finished completely");
        }

        long length = readLength();
        StringBuilder st = new StringBuilder();
        for (int i = 0; i < length; i++) {
            st.append((char) mappedByteBuffer.get());
        }
        name = st.toString();
        mappedByteBuffer.position(mappedByteBuffer.position() + (int) readLength());

        lengthLeft = readLength();
        return mappedByteBuffer.get(new byte[(int) lengthLeft]);
    }

    public String getFile() {
        return name;
    }

    private long readLength() throws IOException {
        int bytesToRead = mappedByteBuffer.get();
        long size = 0;
        for (int i = 0; i < bytesToRead; i++) {
            size |= mappedByteBuffer.get() * (long) Math.pow(256, i);
        }
        return size;
    }
}
