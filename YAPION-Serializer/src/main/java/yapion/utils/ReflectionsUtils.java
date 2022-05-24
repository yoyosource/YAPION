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

import lombok.NonNull;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.objenesis.ObjenesisBase;
import org.objenesis.strategy.StdInstantiatorStrategy;
import yapion.annotations.api.InternalAPI;
import yapion.annotations.object.YAPIONData;
import yapion.annotations.object.YAPIONObjenesis;
import yapion.exceptions.YAPIONException;
import yapion.exceptions.utils.YAPIONReflectionException;
import yapion.exceptions.utils.YAPIONReflectionInvocationException;
import yapion.hierarchy.types.YAPIONObject;
import yapion.serializing.InternalSerializer;
import yapion.serializing.SerializeManager;
import yapion.serializing.TypeReMapper;

import java.io.Externalizable;
import java.io.Serializable;
import java.lang.reflect.*;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

import static yapion.utils.IdentifierUtils.TYPE_IDENTIFIER;

@Slf4j
@UtilityClass
public class ReflectionsUtils {

    private static final ObjenesisBase objenesisBase = new ObjenesisBase(new StdInstantiatorStrategy(), true);

    private static final Map<Class<?>, FieldCache> fieldCacheMap = new LinkedHashMap<Class<?>, FieldCache>() {
        @Override
        protected boolean removeEldestEntry(Map.Entry<Class<?>, FieldCache> eldest) {
            return size() > 256;
        }
    };

    private static final Map<Class<?>, Function<YAPIONObject, ?>> SPECIAL_CREATOR = new HashMap<>();

    @InternalAPI
    public static <T> void addSpecialCreator(Class<T> clazz, Function<YAPIONObject, T> creator) {
        SPECIAL_CREATOR.put(clazz, creator);
    }

    /**
     * Discard the cache used by {@link #getFields(Class)}
     */
    public static void discardCache() {
        fieldCacheMap.clear();
    }

    /**
     * Invokes a method with the given arguments on a given object
     * and return the possible return value.
     *
     * @param name the name of the method to be called
     * @param object the object on which the method should be called
     * @param parameters the parameters that should be used
     * @return the possible return value
     */
    public static MethodReturnValue<Object> invokeMethod(String name, Object object, Parameter... parameters) {
        Class<?>[] classes = new Class[parameters.length];
        Object[] objects = new Object[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            classes[i] = parameters[i].clazz;
            objects[i] = parameters[i].object;
        }

        Class<?> clazz = object.getClass();
        boolean search = true;
        while (search) {
            if (clazz == null || clazz.getTypeName().equals("java.lang.Object")) {
                return MethodReturnValue.empty();
            }
            try {
                clazz.getDeclaredMethod(name, classes);
                search = false;
            } catch (NoSuchMethodException e) {
                clazz = clazz.getSuperclass();
            }
        }

        try {
            Method method = clazz.getDeclaredMethod(name, classes);
            return invokeMethodInternal(method, object, objects);
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | InvocationTargetException e) {
            log.info("Exception while invoking a method '" + name + "' on the object '" + object.getClass().getTypeName() + "' with the parameters of type '" + Arrays.toString(classes) + "'", e.getCause());
            return MethodReturnValue.empty();
        }
    }

    /**
     * Invokes a method with the given arguments on a given object
     * and return the possible return value.
     *
     * @param name the name of the method to be called
     * @param object the object on which the method should be called
     * @param parameters the parameters that should be used
     * @return the possible return value
     */
    public static MethodReturnValue<Object> invokeMethod(String name, Object object, Object... parameters) {
        Class<?>[] classes = new Class[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            classes[i] = parameters[i].getClass();
        }

        Class<?> clazz = object.getClass();
        boolean search = true;
        while (search) {
            if (clazz == null || clazz.getTypeName().equals("java.lang.Object")) {
                return MethodReturnValue.empty();
            }
            try {
                clazz.getDeclaredMethod(name, classes);
                search = false;
            } catch (NoSuchMethodException e) {
                clazz = clazz.getSuperclass();
            }
        }

        try {
            Method method = clazz.getDeclaredMethod(name, classes);
            return invokeMethodInternal(method, object, parameters);
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | InvocationTargetException e) {
            log.info("Exception while invoking a method '" + name + "' on the object '" + object.getClass().getTypeName() + "' with the parameters of type '" + Arrays.toString(classes) + "'", e.getCause());
            return MethodReturnValue.empty();
        }
    }

    /**
     * Invokes a method with the given arguments on a given object
     * and return the possible return value.
     *
     * @param method the method to be called
     * @param object the object on which the method should be called
     * @return the possible return value
     */
    public static MethodReturnValue<Object> invokeMethodObjectSystem(Method method, Object object, Object... objects) {
        try {
            if (method.getParameters().length == 0) {
                return invokeMethodInternal(method, object);
            } else {
                return invokeMethodInternal(method, object, objects);
            }
        } catch (InvocationTargetException e) {
            log.info("Exception in method call '" + method.getName() + "' declared in class '" + method.getDeclaringClass().getTypeName() + "' on the object '" + object.getClass().getTypeName() + "' with no parameters", e.getCause());
            throw new YAPIONReflectionInvocationException(e);
        } catch (SecurityException | IllegalAccessException e) {
            log.info("Exception while invoking a method '" + method.getName() + "' declared in class '" + method.getDeclaringClass().getTypeName() + "' on the object '" + object.getClass().getTypeName() + "' with no parameters", e.getCause());
            throw new YAPIONReflectionException(e.getMessage(), e);
        }
    }

    @SuppressWarnings({"java:S3011"})
    private static MethodReturnValue<Object> invokeMethodInternal(Method method, Object object, Object... objects) throws InvocationTargetException, IllegalAccessException {
        method.setAccessible(true);
        Object returnValue = method.invoke(object, objects);
        if (method.getReturnType().equals(Void.TYPE)) {
            return MethodReturnValue.empty();
        }
        return MethodReturnValue.of(returnValue);
    }

    public static class Parameter {

        private final Class<?> clazz;
        private final Object object;

        public Parameter(Class<?> clazz, Object object) {
            this.clazz = clazz;
            this.object = object;
        }
    }

    /**
     * Construct an Object instance from a given className.
     * By using the {@link ObjenesisBase}.
     *
     * @param className the class to create an instance from
     * @return an instance of the specified class
     */
    public static Object constructObjectObjenesis(String className) {
        try {
            return constructObjectObjenesis(Class.forName(className));
        } catch (ClassNotFoundException e) {
            log.info("Exception while creating an Object with Objenesis because the specified class '" + className + "' was not found", e.getException());
            return null;
        }
    }

    /**
     * Construct an Object instance from a given {@link Class}.
     * By using the {@link ObjenesisBase}.
     *
     * @param clazz the class to create an instance from
     * @return an instance of the specified class
     */
    public static <T> T constructObjectObjenesis(Class<T> clazz) {
        return objenesisBase.newInstance(clazz);
    }

    public static <T> T constructObject(String className, boolean data) {
        try {
            return (T) constructObject(Class.forName(className), data);
        } catch (ClassNotFoundException e) {
            throw new YAPIONException(e.getMessage(), e);
        }
    }

    /**
     * Construct an Object instance from a given className.
     * By using the {@link ObjenesisBase}, only with {@link YAPIONData}
     * or {@link YAPIONObjenesis} or a NoArgument constructor.
     *
     * @param clazz the class to create an instance from
     * @param data branch to {@link #constructObjectObjenesis(String)}
     * @return an instance of the specified class
     */
    public static <T> T constructObject(Class<T> clazz, boolean data) {
        if (data) {
            return constructObjectObjenesis(clazz);
        }
        Object o = null;
        if (clazz.getDeclaringClass() != null) {
            o = constructObject(clazz.getDeclaringClass(), false);
        }
        return constructInstance(clazz, o);
    }

    /**
     * Construct an Object instance from a given yapionObject.
     * By using the {@link ObjenesisBase}, only with {@link YAPIONData}
     * or {@link YAPIONObjenesis} or a NoArgument constructor.
     *
     * @param yapionObject the class to create an instance from
     * @param internalSerializer the internalSerializer using it
     * @param data branch to {@link #constructObjectObjenesis(String)}
     * @return an instance of the specified class
     */
    public static <T> T constructObject(YAPIONObject yapionObject, InternalSerializer<?> internalSerializer, boolean data) {
        return constructObject(yapionObject, internalSerializer, data, null);
    }

    /**
     * Construct an Object instance from a given yapionObject.
     * By using the {@link ObjenesisBase}, only with {@link YAPIONData}
     * or {@link YAPIONObjenesis} or a NoArgument constructor.
     *
     * @param yapionObject the class to create an instance from
     * @param internalSerializer the internalSerializer using it
     * @param data branch to {@link #constructObjectObjenesis(String)}
     * @return an instance of the specified class
     */
    public static <T> T constructObject(YAPIONObject yapionObject, InternalSerializer<?> internalSerializer, boolean data, TypeReMapper typeReMapper) {
        if (!yapionObject.containsKey(TYPE_IDENTIFIER, String.class)) {
            throw new YAPIONReflectionException("YAPIONObject does not contain value for key '" + TYPE_IDENTIFIER + "'");
        }
        String type = yapionObject.getPlainValue(TYPE_IDENTIFIER);
        if (typeReMapper != null) {
            type = typeReMapper.remap(type);
        }
        Class<?> clazz = null;
        try {
            clazz = Class.forName(type);
        } catch (ClassNotFoundException e) {
            throw new YAPIONException(e.getMessage(), e);
        }
        for (Map.Entry<Class<?>, Function<YAPIONObject, ?>> entry : SPECIAL_CREATOR.entrySet()) {
            if (entry.getKey().isAssignableFrom(clazz)) {
                return (T) entry.getValue().apply(yapionObject);
            }
        }
        if (SerializeManager.hasFactory(clazz)) {
            try {
                return (T) SerializeManager.getObjectInstance(clazz);
            } catch (ClassNotFoundException e) {
                throw new YAPIONException(e.getMessage(), e);
            }
        }
        if (internalSerializer.interfaceType() != null && internalSerializer.defaultImplementation() != null && internalSerializer.interfaceType().getTypeName().equals(type)) {
            clazz = internalSerializer.defaultImplementation();
        }
        return (T) constructObject(clazz, data);
    }

    @SuppressWarnings({"java:S3011"})
    private static <T> T constructInstance(Class<T> clazz, Object o) {
        try {
            Constructor<?> constructor;
            if (o == null || Modifier.isStatic(clazz.getModifiers())) {
                constructor = clazz.getDeclaredConstructor();
            } else {
                constructor = clazz.getDeclaredConstructor(o.getClass());
            }
            constructor.setAccessible(true);
            if (o == null || Modifier.isStatic(clazz.getModifiers())) {
                return (T) constructor.newInstance();
            } else {
                return (T) constructor.newInstance(o);
            }
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
            log.info("Exception while creating an Object with normal Constructor", e.getCause());
            throw new YAPIONReflectionException(e.getMessage(), e.getCause());
        }
    }

    public static Object accessField(String s, Object o) {
        try {
            return accessField(o.getClass().getDeclaredField(s), o);
        } catch (NoSuchFieldException e) {
            log.info("Exception while getting a Field", e.getCause());
            throw new YAPIONReflectionException(e.getMessage(), e.getCause());
        }
    }

    @SuppressWarnings({"java:S3011"})
    public static Object accessField(Field f, Object o) {
        try {
            f.setAccessible(true);
            return f.get(o);
        } catch (IllegalAccessException e) {
            log.info("Exception while accessing a Field", e.getCause());
            throw new YAPIONReflectionException(e.getMessage(), e.getCause());
        }
    }

    public static void traverseSuperClasses(@NonNull Class<?> toCheck, Consumer<Class<?>> consumer) {
        Class<?> clazz = toCheck;
        while (clazz != null) {
            consumer.accept(clazz);
            clazz = clazz.getSuperclass();
        }
    }

    public static boolean isClassSuperclassOf(@NonNull Class<?> toCheck, @NonNull Class<?> superClass) {
        if (toCheck == superClass) return true;
        Class<?> superClassToCheck = toCheck.getSuperclass();
        if (superClassToCheck == null) return false;
        if (superClassToCheck == superClass) return true;
        if (superClassToCheck == Object.class) return false;
        return isClassSuperclassOf(superClassToCheck, superClass);
    }

    public static void traverseInterfaceClasses(@NonNull Class<?> toCheck, Consumer<Class<?>> consumer) {
        Class<?>[] interfaces = toCheck.getInterfaces();
        for (Class<?> i : interfaces) {
            consumer.accept(i);
            traverseInterfaceClasses(i, consumer);
        }
        if (toCheck.getSuperclass() != null) {
            traverseInterfaceClasses(toCheck.getSuperclass(), consumer);
        }
    }

    public static int implementsInterface(@NonNull Class<?> toCheck, @NonNull Class<?> interfaceClass) {
        if (!interfaceClass.isAssignableFrom(toCheck)) return -1;
        if (interfaceClass == Serializable.class || interfaceClass == Externalizable.class) return Integer.MAX_VALUE;
        Set<Class<?>> classesToCheck = new HashSet<>();
        classesToCheck.add(toCheck);
        int depth = 0;
        while (true) {
            Set<Class<?>> classesToCheckCopy = new HashSet<>();
            for (Class<?> clazz : classesToCheck) {
                if (clazz == null) continue;
                if (clazz == interfaceClass) {
                    return depth;
                }
                classesToCheckCopy.addAll(Arrays.asList(clazz.getInterfaces()));
                classesToCheckCopy.add(clazz.getSuperclass());
            }
            depth++;
            classesToCheck = new HashSet<>(classesToCheckCopy);
            classesToCheckCopy.clear();
            if (classesToCheck.isEmpty()) return -1;
        }
    }

    private static class FieldCache {

        private final Field[] fields;
        private final Class<?> superClass;

        private FieldCache(Class<?> clazz) {
            fields = clazz.getDeclaredFields();
            superClass = clazz.getSuperclass();
        }

        private List<Field> fields() {
            List<Field> accumulatedFields = new ArrayList<>(Arrays.asList(this.fields));
            if (superClass != null) {
                accumulatedFields.addAll(ReflectionsUtils.getFields(superClass));
            }
            return accumulatedFields;
        }

        private Field getField(String name) {
            Optional<Field> optionalField = Arrays.stream(fields).filter(field -> field.getName().equals(name)).findAny();
            if (optionalField.isPresent()) {
                return optionalField.get();
            }
            if (superClass == null) return null;
            return getFieldCache(superClass).getField(name);
        }
    }

    private static synchronized FieldCache getFieldCache(Class<?> clazz) {
        return fieldCacheMap.computeIfAbsent(clazz, FieldCache::new);
    }

    public static List<Field> getFields(Class<?> clazz) {
        return getFieldCache(clazz).fields();
    }

    public static Field getField(Class<?> clazz, String name) {
        return getFieldCache(clazz).getField(name);
    }
}
