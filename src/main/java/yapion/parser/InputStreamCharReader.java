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

package yapion.parser;

import lombok.SneakyThrows;
import yapion.exceptions.YAPIONException;
import yapion.exceptions.utils.YAPIONIOException;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class InputStreamCharReader implements CharReader {

    private int available = -1;
    private InputStream inputStream;
    private boolean stopOnStreamEnd;

    @FunctionalInterface
    private interface CharSupplier {
        char read() throws IOException;
    }

    private CharSupplier reader;

    public InputStreamCharReader(InputStream inputStream, boolean stopOnStreamEnd, Charset charset) {
        this.inputStream = inputStream;
        this.stopOnStreamEnd = stopOnStreamEnd;
        if (charset == StandardCharsets.US_ASCII) {
            reader = US_ASCII();
        } else if (charset == StandardCharsets.UTF_8) {
            reader = UTF_8();
        }
        else {
            throw new YAPIONException("Unsupported charset: " + charset.displayName());
        }
    }

    private CharSupplier US_ASCII() {
        return () -> {
            int i = inputStream.read();
            if (i == -1 && !stopOnStreamEnd) {
                throw new YAPIONParser.ParserSkipException();
            }
            available--;
            return (char) i;
        };
    }

    private CharSupplier UTF_8() {
        return () -> {
            int i = inputStream.read();
            if (i == -1 && !stopOnStreamEnd) {
                throw new YAPIONParser.ParserSkipException();
            }
            if (i >>> 7 == 0) {
                available--;
                return (char) i;
            }
            if (i >>> 5 == 0b110) {
                int j = inputStream.read();
                if (j == -1 && !stopOnStreamEnd) {
                    throw new YAPIONParser.ParserSkipException();
                }
                if (j >>> 6 == 0b10) {
                    i &= 0b00011111;
                    j &= 0b00111111;
                    available -= 2;
                    return (char) (i << 6 | j);
                }
            } else if (i >>> 4 == 0b1110) {
                int j = inputStream.read();
                if (j == -1 && !stopOnStreamEnd) {
                    throw new YAPIONParser.ParserSkipException();
                }
                if (j >>> 6 == 0b10) {
                    int k = inputStream.read();
                    if (k == -1 && !stopOnStreamEnd) {
                        throw new YAPIONParser.ParserSkipException();
                    }
                    if (k >>> 6 == 0b10) {
                        i &= 0b00001111;
                        j &= 0b00111111;
                        k &= 0b00111111;
                        available -= 3;
                        return (char) (i << 12 | j << 6 | k);
                    }
                }
            } else if (i >>> 3 == 0b11110) {
                int j = inputStream.read();
                if (j == -1 && !stopOnStreamEnd) {
                    throw new YAPIONParser.ParserSkipException();
                }
                if (j >>> 6 == 0b10) {
                    int k = inputStream.read();
                    if (k == -1 && !stopOnStreamEnd) {
                        throw new YAPIONParser.ParserSkipException();
                    }
                    if (k >>> 6 == 0b10) {
                        int l = inputStream.read();
                        if (l == -1 && !stopOnStreamEnd) {
                            throw new YAPIONParser.ParserSkipException();
                        }
                        if (l >>> 6 == 0b10) {
                            i &= 0b00000111;
                            j &= 0b00111111;
                            k &= 0b00111111;
                            l &= 0b00111111;
                            available -= 4;
                            return (char) (i << 18 | j << 12 | k << 6 | l);
                        }
                    }
                }
            }
            throw new YAPIONIOException("Unrecognized UTF-8 Sequence");
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
