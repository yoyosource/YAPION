// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020 yoyosource

package yapion.utils;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.objenesis.ObjenesisBase;
import org.objenesis.strategy.StdInstantiatorStrategy;
import yapion.annotations.deserialize.YAPIONLoadExclude;
import yapion.annotations.object.YAPIONData;
import yapion.annotations.object.YAPIONObjenesis;
import yapion.annotations.serialize.YAPIONSaveExclude;
import yapion.exceptions.YAPIONException;
import yapion.exceptions.utils.YAPIONReflectionException;

import java.lang.reflect.*;
import java.util.*;

@Slf4j
public class ReflectionsUtils {

    private ReflectionsUtils() {
        throw new IllegalStateException("Utility class");
    }

    private static final ObjenesisBase objenesisBase = new ObjenesisBase(new StdInstantiatorStrategy(), false);

    private static int cacheSize = 100;
    private static final Map<Class<?>, FieldCache> fieldCacheMap = new LinkedHashMap<Class<?>, FieldCache>() {
        @Override
        protected boolean removeEldestEntry(Map.Entry<Class<?>, FieldCache> eldest) {
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
        ReflectionsUtils.cacheSize = cacheSize;
    }

    /**
     * Discard the cache used by {@link }
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
    @SuppressWarnings({"java:S3011"})
    public static Optional<Object> invokeMethod(String name, Object object, Parameter... parameters) {
        Class<?>[] classes = new Class[parameters.length];
        Object[] objects = new Object[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            classes[i] = parameters[i].clazz;
            objects[i] = parameters[i].object;
        }

        Class<?> clazz = object.getClass();
        boolean search = true;
        while (search) {
            if (clazz.getTypeName().equals("java.lang.Object")) {
                return Optional.empty();
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
            method.setAccessible(true);
            return Optional.ofNullable(method.invoke(object, objects));
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            log.info("Exception while invoking a method '" + name + "' on the object '" + object.getClass().getTypeName() + "' with the parameters of type '" + Arrays.toString(classes) + "'", e.getCause());
            return Optional.empty();
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
    @SuppressWarnings({"java:S3011"})
    public static Optional<Object> invokeMethod(String name, Object object, Object... parameters) {
        Class<?>[] classes = new Class[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            classes[i] = parameters[i].getClass();
        }

        Class<?> clazz = object.getClass();
        boolean search = true;
        while (search) {
            if (clazz.getTypeName().equals("java.lang.Object")) {
                return Optional.empty();
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
            method.setAccessible(true);
            return Optional.ofNullable(method.invoke(object, parameters));
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | InvocationTargetException e) {
            log.info("Exception while invoking a method '" + name + "' on the object '" + object.getClass().getTypeName() + "' with the parameters of type '" + Arrays.toString(classes) + "'", e.getCause());
            return Optional.empty();
        }
    }

    /**
     * Invokes a method with the given arguments on a given object
     * and return the possible return value.
     *
     * @param method the method to be called
     * @param object the object on which the method should be called
     * @param parameters the parameters that should be used
     * @return the possible return value
     */
    public static Optional<Object> invokeMethod(Method method, Object object, Parameter... parameters) {
        Class<?>[] classes = new Class[parameters.length];
        Object[] objects = new Object[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            classes[i] = parameters[i].clazz;
            objects[i] = parameters[i].object;
        }

        try {
            method.setAccessible(true);
            return Optional.ofNullable(method.invoke(object, objects));
        } catch (SecurityException | IllegalAccessException | InvocationTargetException e) {
            log.info("Exception while invoking a method '" + method.getName() + "' on the object '" + object.getClass().getTypeName() + "' with the parameters of type '" + Arrays.toString(classes) + "'", e.getCause());
            throw new YAPIONException(e.getMessage(), e.getCause());
        }
    }

    /**
     * Invokes a method with the given arguments on a given object
     * and return the possible return value.
     *
     * @param method the method to be called
     * @param object the object on which the method should be called
     * @param parameters the parameters that should be used
     * @return the possible return value
     */
    public static Optional<Object> invokeMethod(Method method, Object object, Object... parameters) {
        try {
            method.setAccessible(true);
            return Optional.ofNullable(method.invoke(object, parameters));
        } catch (SecurityException | IllegalAccessException | InvocationTargetException e) {
            Class<?>[] classes = new Class[parameters.length];
            for (int i = 0; i < parameters.length; i++) {
                classes[i] = parameters[i].getClass();
            }

            e.printStackTrace();
            log.info("Exception while invoking a method '" + method.getName() + "' on the object '" + object.getClass().getTypeName() + "' with the parameters of type '" + Arrays.toString(classes) + "'", e.getCause());
            return Optional.empty();
        }
    }

    @YAPIONSaveExclude(context = "*")
    @YAPIONLoadExclude(context = "*")
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
            return objenesisBase.newInstance(Class.forName(className));
        } catch (ClassNotFoundException e) {
            log.info("Exception while creating an Object with Objenesis because the specified class '" + className + "' was not found", e.getCause());
            return null;
        }
    }

    /**
     * Construct an Object instance from a given className.
     * By using the {@link ObjenesisBase}, only with {@link YAPIONData}
     * or {@link YAPIONObjenesis} or a NoArgument constructor.
     *
     * @param className the class to create an instance from
     * @param data branch to {@link #constructObjectObjenesis(String)}
     * @return an instance of the specified class
     */
    public static Object constructObject(String className, boolean data) {
        if (data) {
            return constructObjectObjenesis(className);
        }
        Object o = null;
        if (className.contains("$")) {
            String cName = className.substring(0, className.lastIndexOf('$'));
            o = constructObject(cName, false);
        }
        return constructInstance(className, o);
    }

    @SuppressWarnings({"java:S3011"})
    private static Object constructInstance(String className, Object o) {
        try {
            Class<?> clazz = Class.forName(className);
            Constructor<?> constructor;
            if (o == null || Modifier.isStatic(clazz.getModifiers())) {
                constructor = clazz.getDeclaredConstructor();
            } else {
                constructor = clazz.getDeclaredConstructor(o.getClass());
            }
            constructor.setAccessible(true);
            if (o == null || Modifier.isStatic(clazz.getModifiers())) {
                return constructor.newInstance();
            } else {
                return constructor.newInstance(o);
            }
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
            log.info("Exception while creating an Object with normal Constructor", e.getCause());
            throw new YAPIONReflectionException(e.getMessage(), e.getCause());
        } catch (ClassNotFoundException e) {
            log.info("Exception while creating an Object with normal Constructor because the specified class '" + className + "' was not found", e.getCause());
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

    public static void main(String[] args) {
        System.out.println(isException(YAPIONException.class));
        System.out.println(isRuntimeException(YAPIONException.class));
        System.out.println(isThrowable(YAPIONException.class));
        System.out.println(isThrowable(YAPIONException.class));
    }

    public static boolean isException(Class<?> clazz) {
        return isClassSuperclassOf(clazz, Exception.class);
    }

    public static boolean isRuntimeException(Class<?> clazz) {
        return isClassSuperclassOf(clazz, RuntimeException.class);
    }

    public static boolean isError(Class<?> clazz) {
        return isClassSuperclassOf(clazz, Error.class);
    }

    public static boolean isThrowable(Class<?> clazz) {
        return isClassSuperclassOf(clazz, Throwable.class);
    }

    public static boolean isClassSuperclassOf(@NonNull String toCheck, @NonNull Class<?> superClass) {
        try {
            return isClassSuperclassOf(Class.forName(toCheck), superClass);
        } catch (ClassNotFoundException e) {
            return false;
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

    public static boolean implementsInterface(String type, Class<?> clazz) {
        try {
            return clazz.isAssignableFrom(Class.forName(type));
        } catch (ClassNotFoundException e) {
            return false;
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

    private static FieldCache getFieldCache(Class<?> clazz) {
        if (!fieldCacheMap.containsKey(clazz)) {
            fieldCacheMap.put(clazz, new FieldCache(clazz));
        }
        return fieldCacheMap.get(clazz);
    }

    public static List<Field> getFields(Class<?> clazz) {
        return getFieldCache(clazz).fields();
    }

    @SuppressWarnings({"java:S3011"})
    public static Object getValueOfField(Field field, Object object) {
        try {
            field.setAccessible(true);
            return field.get(object);
        } catch (IllegalAccessException e) {
            return null;
        }
    }

    @SuppressWarnings({"java:S3011"})
    public static boolean setValueOfField(Field field, Object object, Object value) {
        try {
            field.setAccessible(true);
            field.set(object, value);
            return true;
        } catch (IllegalAccessException e) {
            return false;
        }
    }

    public static Field getField(Class<?> clazz, String name) {
        return getFieldCache(clazz).getField(name);
    }

}