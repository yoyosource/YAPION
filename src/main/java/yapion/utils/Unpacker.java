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

import lombok.AllArgsConstructor;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import yapion.annotations.api.InternalAPI;
import yapion.exceptions.YAPIONException;
import yapion.serializing.zar.ZarInputStream;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
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

    public void unpack(InputStream inputStream, String prefix, BiFunction<String, Integer, Integer> depthCorrector, Consumer<Class<?>> classConsumer) throws IOException {
        try (ZarInputStream zarInputStream = new ZarInputStream(new GZIPInputStream(new BufferedInputStream(inputStream)))) {
            Map<Integer, List<WrappedClass>> depthMap = new TreeMap<>(Comparator.comparingInt(value -> value));
            int deepest = 0;

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
                int depth = className.length() - className.replace(".", "").length();
                depth = depthCorrector.apply(className, depth);
                depth -= className.length() - className.replace("$", "").length();
                log.debug("Entry Info: {} {} {} {} {}", name, size, byteArray.length, depth, deepest);
                if (SYS_LOGGER) System.out.println("Entry Info: "  + name + " " + size + " " + byteArray.length + " " + depth + " " + deepest);

                deepest = Math.max(deepest, depth);
                depthMap.computeIfAbsent(depth, d -> new ArrayList<>()).add(new WrappedClass(className, byteArray));
            }

            YAPIONClassLoader classLoader = new YAPIONClassLoader(Thread.currentThread().getContextClassLoader());
            depthMap.forEach((i, wrappedClasses) -> {
                log.debug("Depth: {}", i);
                if (SYS_LOGGER) System.out.println("Depth: " + i);
                wrappedClasses.forEach(wrappedClass -> {
                    log.debug("Loading: {}", wrappedClass.name);
                    if (SYS_LOGGER) System.out.println("Loading: " + wrappedClass.name);
                    classConsumer.accept(classLoader.defineClass(wrappedClass.name, wrappedClass.bytes));
                });
            });
        } catch (Exception e) {
            throw new YAPIONException(e.getMessage(), e.getCause());
        }
    }

    @AllArgsConstructor
    private static class WrappedClass {
        private final String name;
        private final byte[] bytes;

        @Override
        public String toString() {
            return "SerializeManager.WrappedClass(" + name + ")";
        }
    }

}
