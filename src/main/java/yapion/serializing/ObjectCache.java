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

import lombok.extern.slf4j.Slf4j;
import yapion.annotations.object.YAPIONPostDeserialization;
import yapion.annotations.object.YAPIONPostSerialization;
import yapion.annotations.object.YAPIONPreDeserialization;
import yapion.annotations.object.YAPIONPreSerialization;
import yapion.serializing.data.DeserializationContext;
import yapion.serializing.data.SerializationContext;
import yapion.utils.ReflectionsUtils;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

@Slf4j
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
            if (method.getParameterCount() > 1) continue;
            YAPIONPreSerialization[] yapionPreSerializations = method.getDeclaredAnnotationsByType(YAPIONPreSerialization.class);
            YAPIONPostSerialization[] yapionPostSerializations = method.getDeclaredAnnotationsByType(YAPIONPostSerialization.class);

            if (yapionPreSerializations.length != 0) {
                for (YAPIONPreSerialization yapionPreSerialization : yapionPreSerializations) {
                    cache(preSerializationCache, yapionPreSerialization.context(), method, this::serializationParameter);
                }
            }
            if (yapionPostSerializations.length != 0) {
                for (YAPIONPostSerialization yapionPostSerialization : yapionPostSerializations) {
                    cache(postSerializationCache, yapionPostSerialization.context(), method, this::serializationParameter);
                }
            }

            YAPIONPreDeserialization[] yapionPreDeserializations = method.getDeclaredAnnotationsByType(YAPIONPreDeserialization.class);
            YAPIONPostDeserialization[] yapionPostDeserializations = method.getDeclaredAnnotationsByType(YAPIONPostDeserialization.class);

            if (yapionPreDeserializations.length != 0) {
                for (YAPIONPreDeserialization yapionPreDeserialization : yapionPreDeserializations) {
                    cache(preDeserializationCache, yapionPreDeserialization.context(), method, this::deserializationParameter);
                }
            }
            if (yapionPostDeserializations.length != 0) {
                for (YAPIONPostDeserialization yapionPostDeserialization : yapionPostDeserializations) {
                    cache(postDeserializationCache, yapionPostDeserialization.context(), method, this::deserializationParameter);
                }
            }
        }
    }

    private void cache(Map<String, Method> cache, String[] context, Method method, Predicate<Class<?>[]> checkParameters) {
        if (method.getParameterCount() != 0 && !checkParameters.test(method.getParameterTypes())) {
            log.error("The method {} has an illegal signature", method);
            return;
        }
        for (String s : context) {
            cache.put(s, method);
        }
        if (context.length == 0) {
            cache.put("", method);
        }
    }

    private boolean serializationParameter(Class<?>[] parameters) {
        return parameters.length == 1 && parameters[0] == SerializationContext.class;
    }

    private boolean deserializationParameter(Class<?>[] parameters) {
        return parameters.length == 1 && parameters[0] == DeserializationContext.class;
    }

    void preSerialization(Object object, ContextManager contextManager, SerializationContext serializationContext) {
        String state = contextManager.get();
        if (!preSerializationCache.containsKey(state)) return;
        ReflectionsUtils.invokeMethodObjectSystem(preSerializationCache.get(state), object, serializationContext);
    }

    void postSerialization(Object object, ContextManager contextManager, SerializationContext serializationContext) {
        String state = contextManager.get();
        if (!postSerializationCache.containsKey(state)) return;
        ReflectionsUtils.invokeMethodObjectSystem(postSerializationCache.get(state), object, serializationContext);
    }

    void preDeserialization(Object object, ContextManager contextManager, DeserializationContext deserializationContext) {
        String state = contextManager.get();
        if (!preDeserializationCache.containsKey(state)) return;
        ReflectionsUtils.invokeMethodObjectSystem(preDeserializationCache.get(state), object, deserializationContext);
    }

    void postDeserialization(Object object, ContextManager contextManager, DeserializationContext deserializationContext) {
        String state = contextManager.get();
        if (!postDeserializationCache.containsKey(state)) return;
        ReflectionsUtils.invokeMethodObjectSystem(postDeserializationCache.get(state), object, deserializationContext);
    }
}
