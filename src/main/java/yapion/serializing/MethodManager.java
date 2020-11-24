package yapion.serializing;

import java.util.LinkedHashMap;
import java.util.Map;

public final class MethodManager {

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

    static void preSerializationStep(Object object, ContextManager contextManager) {
        String key = object.getClass().getTypeName();
        if (!methodMap.containsKey(key)) {
            methodMap.put(key, new ObjectCache(object));
        }
        methodMap.get(key).preSerialization(object, contextManager);
    }

    static void postSerializationStep(Object object, ContextManager contextManager) {
        String key = object.getClass().getTypeName();
        if (!methodMap.containsKey(key)) {
            methodMap.put(key, new ObjectCache(object));
        }
        methodMap.get(key).postSerialization(object, contextManager);
    }

    static void preDeserializationStep(Object object, ContextManager contextManager) {
        String key = object.getClass().getTypeName();
        if (!methodMap.containsKey(key)) {
            methodMap.put(key, new ObjectCache(object));
        }
        methodMap.get(key).preDeserialization(object, contextManager);
    }

    static void postDeserializationStep(Object object, ContextManager contextManager) {
        String key = object.getClass().getTypeName();
        if (!methodMap.containsKey(key)) {
            methodMap.put(key, new ObjectCache(object));
        }
        methodMap.get(key).postDeserialization(object, contextManager);
    }

}
