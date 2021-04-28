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

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

public final class MethodManager {

    private MethodManager() {
        throw new IllegalStateException("Utility class");
    }

    private static final Map<String, ObjectCache> methodMap = new LinkedHashMap<String, ObjectCache>() {
        @Override
        protected boolean removeEldestEntry(Map.Entry<String, ObjectCache> eldest) {
            return size() > 1024;
        }
    };

    /**
     * Discard the cache used by {@link YAPIONSerializer} and {@link YAPIONDeserializer}.
     */
    public static void discardCache() {
        methodMap.clear();
    }

    static void preSerializationStep(Object object, Class<?> clazz, ContextManager contextManager) {
        step(object, clazz, contextManager, ObjectCache::preSerialization, false);
    }

    static void postSerializationStep(Object object, Class<?> clazz, ContextManager contextManager) {
        step(object, clazz, contextManager, ObjectCache::postSerialization, true);
    }

    static void preDeserializationStep(Object object, Class<?> clazz, ContextManager contextManager) {
        step(object, clazz, contextManager, ObjectCache::preDeserialization, false);
    }

    static void postDeserializationStep(Object object, Class<?> clazz, ContextManager contextManager) {
        step(object, clazz, contextManager, ObjectCache::postDeserialization, true);
    }

    private static void step(Object object, Class<?> clazz, ContextManager contextManager, TriConsumer<ObjectCache, Object, ContextManager> objectCacheConsumer, boolean order) {
        LinkedList<Class<?>> stepOrder = new LinkedList<>();
        while (clazz != null) {
            stepOrder.addLast(clazz);
            clazz = clazz.getSuperclass();
        }

        Iterator<Class<?>> iterator = order ? stepOrder.iterator() : stepOrder.descendingIterator();
        iterator.forEachRemaining(iClazz -> {
            String key = iClazz.getTypeName();
            if (!methodMap.containsKey(key)) {
                methodMap.put(key, new ObjectCache(iClazz));
            }
            objectCacheConsumer.accept(methodMap.get(key), object, contextManager);
        });
    }

    @FunctionalInterface
    private interface TriConsumer<X, Y, Z> {
        void accept(X x, Y y, Z z);
    }

}
