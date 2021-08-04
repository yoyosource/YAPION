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

package yapion.utils;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import yapion.annotations.api.InternalAPI;
import yapion.exceptions.YAPIONException;
import yapion.serializing.zar.ZarInputStream;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.zip.GZIPInputStream;

/**
 * This class should never be used outside internal usages
 */
@InternalAPI
@UtilityClass
@Slf4j
public class Unpacker {

    private final boolean SYS_LOGGER = false;

    public void unpack(InputStream inputStream, String prefix, Consumer<Class<?>> classConsumer) throws IOException {
        try (ZarInputStream zarInputStream = new ZarInputStream(new GZIPInputStream(new BufferedInputStream(inputStream)))) {
            YAPIONClassLoader classLoader = new YAPIONClassLoader(Thread.currentThread().getContextClassLoader());

            while (zarInputStream.hasFile()) {
                String name = zarInputStream.getFile();
                long size = zarInputStream.getSize();

                log.debug("Entry: {} {}", name, size);
                if (SYS_LOGGER) System.out.println("Entry: " + name + " " + size);

                List<Byte> bytes = new ArrayList<>();
                while (size > bytes.size()) {
                    bytes.add((byte) zarInputStream.read());
                }
                byte[] byteArray = new byte[bytes.size()];
                for (int i = 0; i < byteArray.length; i++) {
                    byteArray[i] = bytes.get(i);
                }

                String className = prefix + name.substring(0, name.indexOf('.')).replace("/", ".");
                log.debug("Entry Info: {} {} {}", name, size, byteArray.length);
                if (SYS_LOGGER) System.out.println("Entry Info: "  + name + " " + size + " " + byteArray.length);

                classLoader.addData(className, byteArray);
            }

            classLoader.getDataKeys().forEach(s -> {
                log.debug("Loading: {}", s);
                if (SYS_LOGGER) System.out.println("Loading: " + s);
                try {
                    classConsumer.accept(classLoader.forName(s));
                } catch (ClassNotFoundException e) {
                    log.error(e.getMessage(), e);
                }
            });
        } catch (Exception e) {
            throw new YAPIONException(e.getMessage(), e.getCause());
        }
    }
}
