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

package yapion.parser.charreader;

import lombok.SneakyThrows;
import yapion.exceptions.utils.YAPIONIOException;
import yapion.parser.CharReader;
import yapion.parser.YAPIONParser;

import java.io.InputStream;

public class ASCIIInputStreamCharReader implements CharReader {

    private int available = -1;
    private InputStream inputStream;
    private boolean stopOnStreamEnd;

    private CharSupplier reader;

    public ASCIIInputStreamCharReader(InputStream inputStream, boolean stopOnStreamEnd) {
        this.inputStream = inputStream;
        this.stopOnStreamEnd = stopOnStreamEnd;
        reader = () -> {
            int i = inputStream.read();
            if (i == -1 && !stopOnStreamEnd) {
                throw new YAPIONParser.ParserSkipException();
            }
            available--;
            if (i > 0x7F) {
                throw new YAPIONIOException("Unrecognized US-ASCII Sequence");
            }
            return (char) i;
        };
    }

    @Override
    @SneakyThrows
    public char next() {
        return reader.read();
    }

    @Override
    @SneakyThrows
    public boolean hasNext() {
        if (available <= 0) {
            available = inputStream.available();
        }
        return !stopOnStreamEnd || available > 0;
    }
}
