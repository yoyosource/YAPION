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

import yapion.annotations.object.YAPIONPostDeserialization;
import yapion.annotations.object.YAPIONPostSerialization;
import yapion.annotations.object.YAPIONPreDeserialization;
import yapion.annotations.object.YAPIONPreSerialization;
import yapion.utils.ReflectionsUtils;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

final class ObjectCache {

    final Class<?> superClass;

    private final Map<String, Method> preSerializationCache = new HashMap<>();
    private final Map<String, Method> postSerializationCache = new HashMap<>();

    private final Map<String, Method> preDeserializationCache = new HashMap<>();
    private final Map<String, Method> postDeserializationCache = new HashMap<>();

    ObjectCache(Class<?> clazz) {
        superClass = clazz.getSuperclass();

        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            if (method.getParameterCount() != 0) continue;
            YAPIONPreSerialization[] yapionPreSerializations = method.getDeclaredAnnotationsByType(YAPIONPreSerialization.class);
            YAPIONPostSerialization[] yapionPostSerializations = method.getDeclaredAnnotationsByType(YAPIONPostSerialization.class);

            if (yapionPreSerializations != null) {
                for (YAPIONPreSerialization yapionPreSerialization : yapionPreSerializations) {
                    cache(preSerializationCache, yapionPreSerialization.context(), method);
                }
            }
            if (yapionPostSerializations != null) {
                for (YAPIONPostSerialization yapionPostSerialization : yapionPostSerializations) {
                    cache(postSerializationCache, yapionPostSerialization.context(), method);
                }
            }

            YAPIONPreDeserialization[] yapionPreDeserializations = method.getDeclaredAnnotationsByType(YAPIONPreDeserialization.class);
            YAPIONPostDeserialization[] yapionPostDeserializations = method.getDeclaredAnnotationsByType(YAPIONPostDeserialization.class);

            if (yapionPreDeserializations != null) {
                for (YAPIONPreDeserialization yapionPreDeserialization : yapionPreDeserializations) {
                    cache(preDeserializationCache, yapionPreDeserialization.context(), method);
                }
            }
            if (yapionPostDeserializations != null) {
                for (YAPIONPostDeserialization yapionPostDeserialization : yapionPostDeserializations) {
                    cache(postDeserializationCache, yapionPostDeserialization.context(), method);
                }
            }
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
        ReflectionsUtils.invokeMethodObjectSystem(preSerializationCache.get(state), object);
    }

    void postSerialization(Object object, ContextManager contextManager) {
        String state = contextManager.get();
        if (!postSerializationCache.containsKey(state)) return;
        ReflectionsUtils.invokeMethodObjectSystem(postSerializationCache.get(state), object);
    }

    void preDeserialization(Object object, ContextManager contextManager) {
        String state = contextManager.get();
        if (!preDeserializationCache.containsKey(state)) return;
        ReflectionsUtils.invokeMethodObjectSystem(preDeserializationCache.get(state), object);
    }

    void postDeserialization(Object object, ContextManager contextManager) {
        String state = contextManager.get();
        if (!postDeserializationCache.containsKey(state)) return;
        ReflectionsUtils.invokeMethodObjectSystem(postDeserializationCache.get(state), object);
    }

}
