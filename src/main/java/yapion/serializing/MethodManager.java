// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020 yoyosource

package yapion.serializing;

import java.util.LinkedHashMap;
import java.util.Map;

public final class MethodManager {

    private MethodManager() {
        throw new IllegalStateException("Utility class");
    }

    private static int cacheSize = 100;

    private static final Map<String, ObjectCache> methodMap = new LinkedHashMap<String, ObjectCache>() {
        @Override
        protected boolean removeEldestEntry(Map.Entry<String, ObjectCache> eldest) {
            return size() > cacheSize;
        }
    };

    /**
     * Set the cache size of the internal cache to a specific
     * number above 100. If you set a number below 100 it will
     * default to 100.
     *
     * @param cacheSize the cache Size
     */
    public static void setCacheSize(int cacheSize) {
        if (cacheSize < 100) {
            cacheSize = 100;
        }
        MethodManager.cacheSize = cacheSize;
    }

    /**
     * Discard the cache used by {@link YAPIONSerializer} and {@link YAPIONDeserializer}.
     */
    public static void discardCache() {
        methodMap.clear();
    }

    static void preSerializationStep(Object object, Class<?> clazz, ContextManager contextManager) {
        while (clazz != null) {
            String key = clazz.getTypeName();
            if (!methodMap.containsKey(key)) {
                methodMap.put(key, new ObjectCache(clazz));
            }
            methodMap.get(key).preSerialization(object, contextManager);
            clazz = methodMap.get(key).superClass;
        }
    }

    static void postSerializationStep(Object object, Class<?> clazz, ContextManager contextManager) {
        while (clazz != null) {
            String key = clazz.getTypeName();
            if (!methodMap.containsKey(key)) {
                methodMap.put(key, new ObjectCache(clazz));
            }
            methodMap.get(key).postSerialization(object, contextManager);
            clazz = methodMap.get(key).superClass;
        }
    }

    static void preDeserializationStep(Object object, Class<?> clazz, ContextManager contextManager) {
        while (clazz != null) {
            String key = clazz.getTypeName();
            if (!methodMap.containsKey(key)) {
                methodMap.put(key, new ObjectCache(clazz));
            }
            methodMap.get(key).preDeserialization(object, contextManager);
            clazz = methodMap.get(key).superClass;
        }
    }

    static void postDeserializationStep(Object object, Class<?> clazz, ContextManager contextManager) {
        while (clazz != null) {
            String key = clazz.getTypeName();
            if (!methodMap.containsKey(key)) {
                methodMap.put(key, new ObjectCache(clazz));
            }
            methodMap.get(key).postDeserialization(object, contextManager);
            clazz = methodMap.get(key).superClass;
        }
    }

}