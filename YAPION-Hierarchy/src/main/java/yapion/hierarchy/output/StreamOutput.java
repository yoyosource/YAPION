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

package yapion.hierarchy.output;

import yapion.exceptions.utils.YAPIONIOException;

import java.io.IOException;
import java.io.OutputStream;

public class StreamOutput extends AbstractOutput implements AutoCloseable {

    private final OutputStream outputStream;
    private final boolean prettified;

    public StreamOutput(OutputStream outputStream) {
        this(outputStream, false);
    }

    public StreamOutput(OutputStream outputStream, boolean prettified) {
        this.outputStream = outputStream;
        this.prettified = prettified;
    }

    @Override
    protected void internalConsume(String s) {
        try {
            outputStream.write(bytes(s));
        } catch (IOException e) {
            throw new YAPIONIOException(e.getMessage(), e);
        }
    }

    @Override
    public boolean prettified() {
        return prettified;
    }

    public void flush() {
        try {
            outputStream.flush();
        } catch (IOException e) {
            throw new YAPIONIOException(e.getMessage(), e);
        }
    }

    public void close() {
        try {
            outputStream.close();
        } catch (IOException e) {
            throw new YAPIONIOException(e.getMessage(), e);
        }
    }
}
