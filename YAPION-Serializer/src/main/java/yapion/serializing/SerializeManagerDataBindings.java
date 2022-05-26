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

package yapion.serializing;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import yapion.exceptions.YAPIONException;
import yapion.utils.YAPIONClassLoader;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

@UtilityClass
@Slf4j
class SerializeManagerDataBindings {

    final String[] BINDINGS = new String[]{
            "yapion-base",
            "yapion-io",
            "java-base",
            "java-other",
            "java-collections",
            "java-atomic",
            "java-function",
            "java-security",
            "java-time",

            "kotlin",

            // Third party data bindings
            "bukkit",
            "guava",
    };

    String getDataBindings() {
        return Arrays.stream(BINDINGS).collect(Collectors.joining(", "));
    }

    private Map<String, SerializerFuture> toLoadSerializerMap;
    private Map<String, SerializerFuture> toLoadInterfaceTypeSerializer;
    private Map<String, SerializerFuture> toLoadClassTypeSerializer;
    private Consumer<Class<?>> internalAddConsumer;

    private YAPIONClassLoader yapionClassLoader;

    void init(Map<String, SerializerFuture> toLoadSerializerMap, Map<String, SerializerFuture> toLoadInterfaceTypeSerializer, Map<String, SerializerFuture> toLoadClassTypeSerializer, Consumer<Class<?>> internalAddConsumer, YAPIONClassLoader yapionClassLoader, CountDownLatch initialized) {
        SerializeManagerDataBindings.toLoadSerializerMap = toLoadSerializerMap;
        SerializeManagerDataBindings.toLoadInterfaceTypeSerializer = toLoadInterfaceTypeSerializer;
        SerializeManagerDataBindings.toLoadClassTypeSerializer = toLoadClassTypeSerializer;
        SerializeManagerDataBindings.internalAddConsumer = internalAddConsumer;
        SerializeManagerDataBindings.yapionClassLoader = yapionClassLoader;

        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(4, 4, 10, TimeUnit.SECONDS, new LinkedBlockingQueue<>());
        for (String s : BINDINGS) {
            threadPoolExecutor.execute(() -> {
                initSingleBind(s);
                initialized.countDown();
            });
        }
        threadPoolExecutor.shutdown();
    }

    private void initSingleBind(String s) {
        int elements = 0;
        long time = System.currentTimeMillis();
        boolean success = false;
        try {
            InputStream packInputStream = SerializeManagerDataBindings.class.getResourceAsStream(s + ".pack");
            if (packInputStream == null) return;
            InputStream packMetaInputStream = SerializeManagerDataBindings.class.getResourceAsStream(s + ".pack.meta");
            if (packMetaInputStream == null) return;
            success = true;

            ObjectInputStream objectInputStream = new ObjectInputStream(new BufferedInputStream(packMetaInputStream));

            BufferedInputStream inputStream = new BufferedInputStream(packInputStream);
            byte[] packBytes = readNBytes(inputStream, Integer.MAX_VALUE);

            List<SerializerFuture> toDirectLoad = new ArrayList<>();

            SerializerFuture serializerFuture;
            while ((serializerFuture = readEntry(toDirectLoad, objectInputStream)) != null) {
                elements++;
                String finalName = "yapion.serializing.serializer." + serializerFuture.getName();
                serializerFuture.setClassLoader((start, length) -> yapionClassLoader.publicDefineClass(finalName, packBytes, start, length));

                yapionClassLoader.addData(finalName, serializerFuture::get);
            }

            toDirectLoad.forEach(future -> {
                internalAddConsumer.accept(future.get());
            });
        } catch (Exception e) {
            e.printStackTrace();
            log.error("No Serializer was loaded. Please inspect.");
            throw new YAPIONException("No Serializer was loaded. Please inspect.");
        } finally {
            if (success) {
                log.debug("Serializer pack {} loaded with {} serializers in {}ms", s, elements, System.currentTimeMillis() - time);
            }
        }
    }

    // Entry End    : 0x00
    // File         : 0x10 String
    // Type         : 0x11 String
    // PrimitiveType: 0x12 String
    // InterfaceType: 0x13 String
    // ClassType    : 0x14 String
    // Start        : 0x20 int
    // Length       : 0x21 int
    // DirectLoad   : 0x30

    @SneakyThrows
    private SerializerFuture readEntry(List<SerializerFuture> toDirectLoad, ObjectInputStream objectInputStream) {
        if (objectInputStream.available() == 0) {
            return null;
        }
        SerializerFuture serializerFuture = new SerializerFuture();
        do {
            int key = objectInputStream.readByte();
            if (key == 0x00) {
                return serializerFuture;
            }
            switch (key) {
                case 0x10:
                    serializerFuture.setName(objectInputStream.readUTF());
                    break;
                case 0x11, 0x12:
                    toLoadSerializerMap.put(objectInputStream.readUTF(), serializerFuture);
                    break;
                case 0x13:
                    toLoadInterfaceTypeSerializer.put(objectInputStream.readUTF(), serializerFuture);
                    break;
                case 0x14:
                    toLoadClassTypeSerializer.put(objectInputStream.readUTF(), serializerFuture);
                    break;
                case 0x20:
                    serializerFuture.setStart(objectInputStream.readInt());
                    break;
                case 0x21:
                    serializerFuture.setLength(objectInputStream.readInt());
                    break;
                case 0x30:
                    toDirectLoad.add(serializerFuture);
                    break;
                default:
                    throw new YAPIONException("Unknown key: " + key);
            }
        } while (true);
    }

    private final int DEFAULT_BUFFER_SIZE = 8192;
    private final int MAX_BUFFER_SIZE = Integer.MAX_VALUE - 8;

    private byte[] readNBytes(InputStream inputStream, int len) throws IOException {
        if (len < 0) {
            throw new IllegalArgumentException("len < 0");
        }

        List<byte[]> bufs = null;
        byte[] result = null;
        int total = 0;
        int remaining = len;
        int n;
        do {
            byte[] buf = new byte[Math.min(remaining, DEFAULT_BUFFER_SIZE)];
            int nread = 0;

            // read to EOF which may read more or less than buffer size
            while ((n = inputStream.read(buf, nread,
                    Math.min(buf.length - nread, remaining))) > 0) {
                nread += n;
                remaining -= n;
            }

            if (nread > 0) {
                if (MAX_BUFFER_SIZE - total < nread) {
                    throw new OutOfMemoryError("Required array size too large");
                }
                total += nread;
                if (result == null) {
                    result = buf;
                } else {
                    if (bufs == null) {
                        bufs = new ArrayList<>();
                        bufs.add(result);
                    }
                    bufs.add(buf);
                }
            }
            // if the last call to read returned -1 or the number of bytes
            // requested have been read then break
        } while (n >= 0 && remaining > 0);

        if (bufs == null) {
            if (result == null) {
                return new byte[0];
            }
            return result.length == total ?
                    result : Arrays.copyOf(result, total);
        }

        result = new byte[total];
        int offset = 0;
        remaining = total;
        for (byte[] b : bufs) {
            int count = Math.min(b.length, remaining);
            System.arraycopy(b, 0, result, offset, count);
            offset += count;
            remaining -= count;
        }

        return result;
    }
}
