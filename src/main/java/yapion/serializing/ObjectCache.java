package yapion.serializing;

import yapion.annotations.object.YAPIONPostDeserialization;
import yapion.annotations.object.YAPIONPostSerialization;
import yapion.annotations.object.YAPIONPreDeserialization;
import yapion.annotations.object.YAPIONPreSerialization;
import yapion.utils.ReflectionsUtils;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

final class ObjectCache {

    private final Map<String, Method> preSerializationCache = new HashMap<>();
    private final Map<String, Method> postSerializationCache = new HashMap<>();

    private final Map<String, Method> preDeserializationCache = new HashMap<>();
    private final Map<String, Method> postDeserializationCache = new HashMap<>();

    ObjectCache(Object object) {
        Method[] methods = object.getClass().getDeclaredMethods();
        for (Method method : methods) {
            if (method.getParameterCount() != 0) continue;
            YAPIONPreSerialization yapionPreSerialization = method.getAnnotation(YAPIONPreSerialization.class);
            YAPIONPostSerialization yapionPostSerialization = method.getAnnotation(YAPIONPostSerialization.class);

            if (yapionPreSerialization != null) cache(preSerializationCache, yapionPreSerialization.context(), method);
            if (yapionPostSerialization != null) cache(postSerializationCache, yapionPostSerialization.context(), method);

            YAPIONPreDeserialization yapionPreDeserialization = method.getAnnotation(YAPIONPreDeserialization.class);
            YAPIONPostDeserialization yapionPostDeserialization = method.getAnnotation(YAPIONPostDeserialization.class);

            if (yapionPreDeserialization != null) cache(preDeserializationCache, yapionPreDeserialization.context(), method);
            if (yapionPostDeserialization != null) cache(postDeserializationCache, yapionPostDeserialization.context(), method);
        }
    }

    private void cache(Map<String, Method> cache, String[] context, Method method) {
        for (String s : context) {
            cache.put(s, method);
        }
        if (context.length == 0) {
            cache.put("", method);
        }
    }

    void preSerialization(Object object, ContextManager contextManager) {
        String state = contextManager.get();
        if (!preSerializationCache.containsKey(state)) return;
        ReflectionsUtils.invokeMethod(preSerializationCache.get(state), object);
    }

    void postSerialization(Object object, ContextManager contextManager) {
        String state = contextManager.get();
        if (!postSerializationCache.containsKey(state)) return;
        ReflectionsUtils.invokeMethod(postSerializationCache.get(state), object);
    }

    void preDeserialization(Object object, ContextManager contextManager) {
        String state = contextManager.get();
        if (!preDeserializationCache.containsKey(state)) return;
        ReflectionsUtils.invokeMethod(preDeserializationCache.get(state), object);
    }

    void postDeserialization(Object object, ContextManager contextManager) {
        String state = contextManager.get();
        if (!postDeserializationCache.containsKey(state)) return;
        ReflectionsUtils.invokeMethod(postDeserializationCache.get(state), object);
    }

}
