// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020 yoyosource

package yapion.utils;

import org.objenesis.ObjenesisBase;
import org.objenesis.strategy.StdInstantiatorStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import yapion.annotations.object.YAPIONData;
import yapion.annotations.deserialize.YAPIONLoadExclude;
import yapion.annotations.object.YAPIONObjenesis;
import yapion.annotations.serialize.YAPIONSaveExclude;
import yapion.exceptions.YAPIONException;
import yapion.exceptions.utils.YAPIONReflectionException;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Optional;

public class ReflectionsUtils {

    private ReflectionsUtils() {
        throw new IllegalStateException("Utility class");
    }

    private static final Logger logger = LoggerFactory.getLogger(ReflectionsUtils.class);
    private static final ObjenesisBase objenesisBase = new ObjenesisBase(new StdInstantiatorStrategy(), false);

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
            logger.info("Exception while invoking a method '" + name + "' on the object '" + object.getClass().getTypeName() + "' with the parameters of type '" + Arrays.toString(classes) + "'", e.getCause());
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
            logger.info("Exception while invoking a method '" + name + "' on the object '" + object.getClass().getTypeName() + "' with the parameters of type '" + Arrays.toString(classes) + "'", e.getCause());
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
            logger.info("Exception while invoking a method '" + method.getName() + "' on the object '" + object.getClass().getTypeName() + "' with the parameters of type '" + Arrays.toString(classes) + "'", e.getCause());
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
            logger.info("Exception while invoking a method '" + method.getName() + "' on the object '" + object.getClass().getTypeName() + "' with the parameters of type '" + Arrays.toString(classes) + "'", e.getCause());
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
            logger.info("Exception while creating an Object with Objenesis because the specified class '" + className + "' was not found", e.getCause());
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
            logger.info("Exception while creating an Object with normal Constructor", e.getCause());
            throw new YAPIONReflectionException(e.getMessage(), e.getCause());
        } catch (ClassNotFoundException e) {
            logger.info("Exception while creating an Object with normal Constructor because the specified class '" + className + "' was not found", e.getCause());
            throw new YAPIONReflectionException(e.getMessage(), e.getCause());
        }
    }

}