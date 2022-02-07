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

import java.io.IOException;
import java.io.InputStream;

public class UTF8InputStreamCharReader implements CharReader {

    private int available = -1;
    private InputStream inputStream;
    private boolean stopOnStreamEnd;

    private CharSupplier reader;

    public UTF8InputStreamCharReader(InputStream inputStream, boolean stopOnStreamEnd) {
        this.inputStream = inputStream;
        this.stopOnStreamEnd = stopOnStreamEnd;
        reader = () -> {
            int i = inputStream.read();
            if (i == -1 && !stopOnStreamEnd) {
                throw new YAPIONParser.ParserSkipException();
            }
            if (i >>> 7 == 0) {
                available--;
                if (i >= 0x0000 && i <= 0x007F) return (char) i;
            } else if (i >>> 5 == 0b110) {
                int j = readNext();
                i &= 0b00011111;
                available -= 2;
                int result = i << 6 | j;
                if (result >= 0x0080 && result <= 0x07FF) return (char) result;
            } else if (i >>> 4 == 0b1110) {
                int j = readNext();
                int k = readNext();
                i &= 0b00001111;
                available -= 3;
                int result = i << 12 | j << 6 | k;
                if (result >= 0x0800 && result <= 0x0FFF) return (char) result;
                if (result >= 0x1000 && result <= 0xFFFF) return (char) result;
            } else if (i >>> 3 == 0b11110) {
                int j = readNext();
                int k = readNext();
                int l = readNext();
                i &= 0b00000111;
                available -= 4;
                int result = i << 18 | j << 12 | k << 6 | l;
                if (result >= 0x10000 && result <= 0x3FFFF) return (char) result;
                if (result >= 0x40000 && result <= 0xFFFFF) return (char) result;
                if (result >= 0x100000 && result <= 0x10FFFF) return (char) result;
            }
            throw new YAPIONIOException("Unrecognized UTF-8 Sequence");
        };
    }

    private int readNext() throws IOException {
        int current = inputStream.read();
        if (current == -1 && !stopOnStreamEnd) {
            throw new YAPIONParser.ParserSkipException();
        }
        if (current >>> 6 == 0b10) {
            return current & 0b00111111;
        }
        throw new YAPIONIOException("Unrecognized UTF-8 Sequence");
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
