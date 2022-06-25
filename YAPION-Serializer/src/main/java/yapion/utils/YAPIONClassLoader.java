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

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

@InternalAPI
@Slf4j
public class YAPIONClassLoader extends ClassLoader {

    private final Map<String, Supplier<Class<?>>> classes = new HashMap<>();
    private ClassLoader parent;

    public YAPIONClassLoader(ClassLoader parent) {
        super(parent);
        this.parent = parent;
    }

    public void addData(String name, Supplier<Class<?>> supplier) {
        synchronized (classes) {
            classes.put(name, supplier);
        }
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        if (classes.containsKey(name)) {
            return classes.get(name).get();
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

    @Override
    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        synchronized (getClassLoadingLock(name)) {
            if (classes.containsKey(name)) {
                return classes.get(name).get();
            }
        }
        return super.loadClass(name, resolve);
    }

    public Class<?> forName(String name) throws ClassNotFoundException {
        return findClass(name);
    }

    public Class<?> defineClass(String name, byte[] bytes) {
        return defineClass(name, bytes, 0, bytes.length);
    }

    public Class<?> publicDefineClass(String name, byte[] bytes, int offset, int length) {
        return defineClass(name, bytes, offset, length);
    }
}
