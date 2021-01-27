// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020 yoyosource

package yapion.serializing;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ClassInfoList;
import io.github.classgraph.ScanResult;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import system.YAPIONMain;
import yapion.annotations.registration.YAPIONRegistratorProvider;
import yapion.serializing.api.*;
import yapion.utils.MethodReturnValue;
import yapion.utils.ReflectionsUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;

@Slf4j
public final class YAPIONRegistrator {

    public static void main(String[] args) {
        registration(YAPIONMain.class);
    }

    private YAPIONRegistrator() {
        throw new IllegalStateException("Utility class");
    }

    private static Set<Predicate<Object>> registerPredicates = new HashSet<>();
    private static Set<Function<Class<?>, MethodReturnValue<Object>>> constructFunctions = new HashSet<>();

    static {
        registerPredicates.add(o -> {
            if (o instanceof YAPIONSerializerRegistrator) {
                ((YAPIONSerializerRegistrator) o).register();
                return true;
            }
            return false;
        });

        registerPredicates.add(o -> {
            if (o instanceof InstanceFactoryInterface) {
                SerializeManager.add((InstanceFactoryInterface<?>) o);
                return true;
            }
            return false;
        });
        registerPredicates.add(o -> {
            if (o instanceof SerializerListInterface) {
                SerializeManager.add((SerializerListInterface<?>) o);
                return true;
            }
            return false;
        });
        registerPredicates.add(o -> {
            if (o instanceof SerializerMapInterface) {
                SerializeManager.add((SerializerMapInterface<?>) o);
                return true;
            }
            return false;
        });
        registerPredicates.add(o -> {
            if (o instanceof SerializerObjectInterface) {
                SerializeManager.add((SerializerObjectInterface<?>) o);
                return true;
            }
            return false;
        });
        registerPredicates.add(o -> {
            if (o instanceof SerializerQueueInterface) {
                SerializeManager.add((SerializerQueueInterface<?>) o);
                return true;
            }
            return false;
        });
        registerPredicates.add(o -> {
            if (o instanceof SerializerSetInterface) {
                SerializeManager.add((SerializerSetInterface<?>) o);
                return true;
            }
            return false;
        });

        registerPredicates.add(o -> {
            if (o instanceof Class<?>) {
                MethodReturnValue<Object> methodReturnValue = constructObject((Class<?>) o);
                if (methodReturnValue.isEmpty()) return false;
                return loadRegistrator(methodReturnValue.get());
            }
            return false;
        });

        constructFunctions.add(o -> {
            if (YAPIONSerializerRegistrator.class.isAssignableFrom(o)) {
                return MethodReturnValue.of(ReflectionsUtils.constructObjectObjenesis(o));
            }
            return MethodReturnValue.empty();
        });
        constructFunctions.add(o -> {
            if (InstanceFactoryInterface.class.isAssignableFrom(o)) {
                return MethodReturnValue.of(ReflectionsUtils.constructObjectObjenesis(o));
            }
            return MethodReturnValue.empty();
        });
        constructFunctions.add(o -> {
            if (SerializerListInterface.class.isAssignableFrom(o)) {
                return MethodReturnValue.of(ReflectionsUtils.constructObjectObjenesis(o));
            }
            return MethodReturnValue.empty();
        });
        constructFunctions.add(o -> {
            if (SerializerMapInterface.class.isAssignableFrom(o)) {
                return MethodReturnValue.of(ReflectionsUtils.constructObjectObjenesis(o));
            }
            return MethodReturnValue.empty();
        });
        constructFunctions.add(o -> {
            if (SerializerObjectInterface.class.isAssignableFrom(o)) {
                return MethodReturnValue.of(ReflectionsUtils.constructObjectObjenesis(o));
            }
            return MethodReturnValue.empty();
        });
        constructFunctions.add(o -> {
            if (SerializerQueueInterface.class.isAssignableFrom(o)) {
                return MethodReturnValue.of(ReflectionsUtils.constructObjectObjenesis(o));
            }
            return MethodReturnValue.empty();
        });
        constructFunctions.add(o -> {
            if (SerializerSetInterface.class.isAssignableFrom(o)) {
                return MethodReturnValue.of(ReflectionsUtils.constructObjectObjenesis(o));
            }
            return MethodReturnValue.empty();
        });
    }

    /**
     * This method will scan the packages of the given classes and any sub packages for any class which contain methods
     * annotated with {@link YAPIONRegistratorProvider}.
     *
     * <br><br>Constraints for the methods:
     * <br>- The method needs to be non static
     * <br>- The method needs to return something non {@link Void}
     * <br>- The method should need no arguments
     *
     * <br><br>After obtaining every method of a given class with this annotation, it will create an instance of given
     * class by calling {@link ReflectionsUtils#constructObjectObjenesis(Class)}. This object will be used to retrieve
     * the values of given methods. This result will be processed and loaded if possible.
     *
     * <br><br>Constraints for the method return value:
     * <br>- Instance of type {@link YAPIONSerializerRegistrator}
     * <br>- Instance of type {@link InstanceFactoryInterface}
     * <br>- Instance of type {@link SerializerListInterface}
     * <br>- Instance of type {@link SerializerMapInterface}
     * <br>- Instance of type {@link SerializerObjectInterface}
     * <br>- Instance of type {@link SerializerQueueInterface}
     * <br>- Instance of type {@link SerializerSetInterface}
     * <br>- Class of {@link YAPIONSerializerRegistrator}
     * <br>- Class of {@link InstanceFactoryInterface}
     * <br>- Class of {@link SerializerListInterface}
     * <br>- Class of {@link SerializerMapInterface}
     * <br>- Class of {@link SerializerObjectInterface}
     * <br>- Class of {@link SerializerQueueInterface}
     * <br>- Class of {@link SerializerSetInterface}
     *
     * @param classes to scan the packages and any sub packages of
     */
    public static void registration(@NonNull Class<?>... classes) {
        if (classes.length == 0) return;
        List<String> packages = new ArrayList<>();
        for (Class<?> clazz : classes) {
            if (clazz == null) return;
            packages.add(clazz.getTypeName().substring(0, clazz.getTypeName().length() - clazz.getSimpleName().length() - 1));
        }

        ScanResult scanResult = new ClassGraph()
                .whitelistPackages(packages.toArray(new String[0]))
                .ignoreMethodVisibility()
                .enableAnnotationInfo()
                .scan();
        ClassInfoList classInfoList = scanResult.getAllClasses();

        for (ClassInfo classInfo : classInfoList) {
            List<Method> methods = new ArrayList<>();
            Class<?> clazz = classInfo.loadClass();
            for (Method method : clazz.getDeclaredMethods()) {
                YAPIONRegistratorProvider yapionRegistratorProvider = method.getAnnotation(YAPIONRegistratorProvider.class);
                if (yapionRegistratorProvider == null) continue;
                if (Modifier.isStatic(method.getModifiers())) continue;
                if (method.getReturnType() == Void.TYPE) continue;
                if (method.getParameterCount() != 0) continue;
                methods.add(method);
            }
            if (methods.isEmpty()) continue;
            Object objectInstance = ReflectionsUtils.constructObjectObjenesis(classInfo.getName());

            for (Method method : methods) {
                MethodReturnValue<Object> objectMethodReturnValue = ReflectionsUtils.invokeMethodObjectSystem(method, objectInstance);
                if (objectMethodReturnValue.isEmpty() || objectMethodReturnValue.get() == null) continue;
                loadRegistrator(objectMethodReturnValue.get());
            }
        }
    }
    
    private static boolean loadRegistrator(Object object) {
        for (Predicate<Object> predicate : registerPredicates) {
            if (predicate.test(object)) {
                return true;
            }
        }
        log.warn("Registration failed as " + object.getClass().getTypeName() + " is not of any supported type");
        return false;
    }

    private static MethodReturnValue<Object> constructObject(Class<?> clazz) {
        for (Function<Class<?>, MethodReturnValue<Object>> function : constructFunctions) {
            MethodReturnValue<Object> objectMethodReturnValue = function.apply(clazz);
            if (objectMethodReturnValue.isPresent()) return objectMethodReturnValue;
        }
        log.warn("Specified class (" + clazz.getTypeName() + ") is not any supported type");
        return MethodReturnValue.empty();
    }

}