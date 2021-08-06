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

import lombok.extern.slf4j.Slf4j;
import yapion.annotations.api.InternalAPI;
import yapion.serializing.zar.ZarInputStream;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

@InternalAPI
@Slf4j
public class YAPIONClassLoader extends ClassLoader {

    private Map<String, Class<?>> current = new HashMap<>();
    private ClassLoader parent;

    private Map<String, byte[]> classData = new HashMap<>();

    public YAPIONClassLoader(ClassLoader parent) {
        super(parent);
        this.parent = parent;
    }

    public YAPIONClassLoader(ClassLoader parent, String prefix, ZarInputStream zarInputStream, Consumer<Class<?>> classConsumer) throws IOException {
        this(parent);

        while (zarInputStream.hasFile()) {
            String name = zarInputStream.getFile();
            long size = zarInputStream.getSize();

            byte[] bytes = new byte[(int) size];
            for (int i = 0; i < bytes.length; i++) {
                bytes[i] = (byte) zarInputStream.read();
            }

            String className = prefix + name.substring(0, name.indexOf('.')).replace("/", ".");
            log.debug("Entry Info: {} {}bytes", name, size);

            addData(className, bytes);
        }
        getDataKeys().forEach(s -> {
            log.debug("Loading: {}", s);
            try {
                classConsumer.accept(forName(s));
            } catch (ClassNotFoundException e) {
                log.error(e.getMessage(), e);
            }
        });
    }

    public void addData(String name, byte[] byteCode) {
        classData.put(name, byteCode);
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        if (current.containsKey(name)) {
            return current.get(name);
        }
        if (classData.containsKey(name)) {
            return defineClass(name, classData.get(name));
        }
        try {
            if (parent != null) {
                return parent.loadClass(name);
            }
        } catch (ClassNotFoundException e) {
            // Ignored
        }
        return Class.forName(name);
    }

    public Set<String> getDataKeys() {
        return classData.keySet();
    }

    public Set<String> loadedClasses() {
        return current.keySet();
    }

    public Class<?> forName(String name) throws ClassNotFoundException {
        return findClass(name);
    }

    public Class<?> defineClass(String name, byte[] bytes) {
        Class<?> clazz = defineClass(name, bytes, 0, bytes.length);
        current.put(name, clazz);
        return clazz;
    }
}
