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

import eu.infomas.annotation.AnnotationDetector;
import lombok.NonNull;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import yapion.annotations.registration.YAPIONRegistratorProvider;
import yapion.serializing.api.*;
import yapion.utils.MethodReturnValue;
import yapion.utils.ReflectionsUtils;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Predicate;

@Slf4j
@UtilityClass
public final class YAPIONRegistrator {

    private static final Set<Predicate<Object>> registerPredicates = new HashSet<>();
    private static final Set<Class<?>> constructClass = new HashSet<>();

    static {
        registerPredicates.add(o -> {
            if (o instanceof YAPIONSerializerRegistrator yapionSerializerRegistrator) {
                yapionSerializerRegistrator.register();
                return true;
            }
            return false;
        });

        registerPredicates.add(o -> {
            if (o instanceof InstanceFactory<?> instanceFactory) {
                SerializeManager.add(instanceFactory);
                return true;
            }
            return false;
        });
        registerPredicates.add(o -> {
            if (o instanceof SerializerList<?> serializerList) {
                SerializeManager.add(serializerList);
                return true;
            }
            return false;
        });
        registerPredicates.add(o -> {
            if (o instanceof SerializerMap<?> serializerMap) {
                SerializeManager.add(serializerMap);
                return true;
            }
            return false;
        });
        registerPredicates.add(o -> {
            if (o instanceof SerializerObject<?> serializerObject) {
                SerializeManager.add(serializerObject);
                return true;
            }
            return false;
        });
        registerPredicates.add(o -> {
            if (o instanceof SerializerQueue<?> serializerQueue) {
                SerializeManager.add(serializerQueue);
                return true;
            }
            return false;
        });
        registerPredicates.add(o -> {
            if (o instanceof SerializerSet<?> serializerSet) {
                SerializeManager.add(serializerSet);
                return true;
            }
            return false;
        });

        registerPredicates.add(o -> {
            if (o instanceof Class<?> clazz) {
                MethodReturnValue<Object> methodReturnValue = constructObject(clazz);
                if (methodReturnValue.isEmpty()) return false;
                return loadRegistrator(methodReturnValue.get());
            }
            return false;
        });

        constructClass.add(YAPIONSerializerRegistrator.class);
        constructClass.add(InstanceFactory.class);
        constructClass.add(SerializerList.class);
        constructClass.add(SerializerMap.class);
        constructClass.add(SerializerObject.class);
        constructClass.add(SerializerQueue.class);
        constructClass.add(SerializerSet.class);
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
     * <br>- Instance of type {@link InstanceFactory}
     * <br>- Instance of type {@link SerializerList}
     * <br>- Instance of type {@link SerializerMap}
     * <br>- Instance of type {@link SerializerObject}
     * <br>- Instance of type {@link SerializerQueue}
     * <br>- Instance of type {@link SerializerSet}
     * <br>- Class of {@link YAPIONSerializerRegistrator}
     * <br>- Class of {@link InstanceFactory}
     * <br>- Class of {@link SerializerList}
     * <br>- Class of {@link SerializerMap}
     * <br>- Class of {@link SerializerObject}
     * <br>- Class of {@link SerializerQueue}
     * <br>- Class of {@link SerializerSet}
     *
     * @param classes to scan the packages and any sub packages of
     */
    public static void registration(@NonNull Class<?>... classes) {
        if (classes.length == 0) return;
        String[] packages = Arrays.stream(classes).filter(Objects::nonNull).map(aClass -> aClass.getPackage().getName()).toArray(String[]::new);

        AtomicReference<Set<Method>> atomicMethods = new AtomicReference<>(new HashSet<>());
        AnnotationDetector annotationDetector = new AnnotationDetector(new AnnotationDetector.MethodReporter() {
            @Override
            public void reportMethodAnnotation(Class<? extends Annotation> annotation, String className, String methodName) {
                try {
                    Method method = Class.forName(className).getDeclaredMethod(methodName);
                    if (Modifier.isStatic(method.getModifiers())) return;
                    if (method.getReturnType() == Void.TYPE) return;
                    if (method.getParameterCount() != 0) return;
                    atomicMethods.get().add(method);
                } catch (Exception e) {
                    log.warn(e.getMessage(), e);
                }
            }

            @Override
            public Class<? extends Annotation>[] annotations() {
                return new Class[]{YAPIONRegistratorProvider.class};
            }
        });

        try {
            annotationDetector.detect(packages);
        } catch (IOException e) {
            log.warn(e.getMessage(), e);
        }

        Map<Class<?>, Set<Method>> optimizedMethods = new IdentityHashMap<>();
        atomicMethods.get().forEach(method -> optimizedMethods.computeIfAbsent(method.getDeclaringClass(), aClass -> new HashSet<>()).add(method));

        optimizedMethods.forEach((clazz, methodSet) -> {
            Object objectInstance = ReflectionsUtils.constructObjectObjenesis(clazz);
            for (Method method : methodSet) {
                MethodReturnValue<Object> objectMethodReturnValue = ReflectionsUtils.invokeMethodObjectSystem(method, objectInstance);
                if (objectMethodReturnValue.isEmpty() || objectMethodReturnValue.get() == null) continue;
                loadRegistrator(objectMethodReturnValue.get());
            }
        });
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
        for (Class<?> cClazz : constructClass) {
            if (cClazz.isAssignableFrom(clazz)) {
                return MethodReturnValue.of(ReflectionsUtils.constructObjectObjenesis(clazz));
            }
        }
        log.warn("Specified class (" + clazz.getTypeName() + ") is not any supported type");
        return MethodReturnValue.empty();
    }

}
