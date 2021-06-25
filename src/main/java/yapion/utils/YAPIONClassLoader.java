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

import yapion.annotations.api.InternalAPI;

import java.util.HashMap;
import java.util.Map;

@InternalAPI
public class YAPIONClassLoader extends ClassLoader {

    private Map<String, Class<?>> current = new HashMap<>();

    public YAPIONClassLoader(ClassLoader parent) {
        super(parent);
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        if (current.containsKey(name)) {
            return current.get(name);
        }
        return Class.forName(name);
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
