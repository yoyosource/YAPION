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

import yapion.exceptions.utils.YAPIONIOException;
import yapion.hierarchy.types.YAPIONObject;
import yapion.parser.YAPIONParser;
import yapion.serializing.TypeReMapper;
import yapion.serializing.YAPIONDeserializer;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

public class YAPIONInputStream implements AutoCloseable {

    private boolean closed = false;
    private final InputStream inputStream;

    /**
     * Creates a YAPIONInputStream from an InputStream. It is encouraged to input some
     * kind of {@link BufferedInputStream}. This will increase the {@link #read()} speed
     * tremendously.
     *
     * @param inputStream the InputStream
     */
    public YAPIONInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public int available() throws IOException {
        return inputStream.available();
    }

    public int readByte() throws IOException {
        return inputStream.read();
    }

    public long skip(long skip) throws IOException {
        return inputStream.skip(skip);
    }

    /**
     * Closes the InputStream.
     *
     * @throws IOException by {@link InputStream#close()}
     */
    public void close() throws IOException {
        if (closed) return;
        closed = true;
        inputStream.close();
    }

    /**
     * Read and parses the next YAPIONObject.
     *
     * @return the next YAPIONObject
     *
     * @throws YAPIONIOException if the inputStream was closed
     */
    public synchronized YAPIONObject read() {
        if (closed) throw new YAPIONIOException("Reading from a closed Stream");
        return YAPIONParser.parse(inputStream);
    }

    /**
     * Reads, parses and deserializes the next YAPIONObject.
     *
     * @return the next Object
     *
     * @throws YAPIONIOException if the inputStream was closed
     */
    public <T> T readObject() {
        if (closed) throw new YAPIONIOException("Reading from a closed Stream");
        return YAPIONDeserializer.deserialize(read());
    }

    /**
     * Reads, parses and deserializes the next YAPIONObject.
     *
     * @param typeReMapper the {@link TypeReMapper} to use
     *
     * @return the next Object
     *
     * @throws YAPIONIOException if the inputStream was closed
     */
    public <T> T readObject(TypeReMapper typeReMapper) {
        if (closed) throw new YAPIONIOException("Reading from a closed Stream");
        return YAPIONDeserializer.deserialize(read(), typeReMapper);
    }

}
