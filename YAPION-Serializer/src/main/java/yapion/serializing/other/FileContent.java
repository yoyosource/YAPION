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

package yapion.serializing.other;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.io.*;
import java.util.HashSet;
import java.util.Set;

@RequiredArgsConstructor
@ToString
@EqualsAndHashCode
public class FileContent implements AutoCloseable {

    private Set<InputStream> inputStreamSet = new HashSet<>();
    private Set<OutputStream> outputStreamSet = new HashSet<>();

    @Getter
    private final File file;

    @Override
    public void close() throws Exception {
        for (InputStream inputStream : inputStreamSet) {
            inputStream.close();
        }
        for (OutputStream outputStream : outputStreamSet) {
            outputStream.close();
        }
    }

    public InputStream getInputStream() throws IOException {
        InputStreamCloser inputStreamCloser = new InputStreamCloser(new BufferedInputStream(new FileInputStream(file)));
        inputStreamSet.add(inputStreamCloser);
        return inputStreamCloser;
    }

    public OutputStream getOutputStream() throws IOException {
        return getOutputStream(false);
    }

    public OutputStream getOutputStream(boolean append) throws IOException {
        OutputStreamCloser outputStreamCloser = new OutputStreamCloser(new BufferedOutputStream(new FileOutputStream(file, append)));
        outputStreamSet.add(outputStreamCloser);
        return outputStreamCloser;
    }

    private class InputStreamCloser extends FilterInputStream {
        protected InputStreamCloser(InputStream in) {
            super(in);
        }

        @Override
        public int read() throws IOException {
            return in.read();
        }

        @Override
        public int read(byte[] b) throws IOException {
            return in.read(b);
        }

        @Override
        public int read(byte[] b, int off, int len) throws IOException {
            return in.read(b, off, len);
        }

        @Override
        public void close() throws IOException {
            try {
                super.close();
            } finally {
                inputStreamSet.remove(this);
            }
        }
    }

    private class OutputStreamCloser extends FilterOutputStream {
        protected OutputStreamCloser(OutputStream out) {
            super(out);
        }

        @Override
        public void write(int b) throws IOException {
            out.write(b);
        }

        @Override
        public void write(byte[] b) throws IOException {
            out.write(b);
        }

        @Override
        public void write(byte[] b, int off, int len) throws IOException {
            out.write(b, off, len);
        }

        @Override
        public void close() throws IOException {
            try {
                super.close();
            } finally {
                outputStreamSet.remove(this);
            }
        }
    }
}
