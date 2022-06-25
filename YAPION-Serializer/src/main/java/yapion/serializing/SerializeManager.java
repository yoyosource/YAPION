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

import lombok.Getter;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import yapion.annotations.api.InternalAPI;
import yapion.annotations.object.YAPIONObjenesis;
import yapion.exceptions.YAPIONException;
import yapion.hierarchy.api.groups.YAPIONAnyType;
import yapion.serializing.api.InstanceFactory;
import yapion.serializing.api.SerializerBase;
import yapion.serializing.api.YAPIONSerializerRegistrator;
import yapion.serializing.reflection.PureStrategy;
import yapion.utils.ReflectionsUtils;
import yapion.utils.YAPIONClassLoader;

import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static yapion.utils.ReflectionsUtils.*;

@Slf4j
@UtilityClass
public class SerializeManager {

    private final InternalSerializer<Object> defaultSerializer = null;
    private InternalSerializer<Object> ARRAY_SERIALIZER = null;
    private InternalSerializer<Enum<?>> ENUM_SERIALIZER = null;
    private InternalSerializer<Object> RECORD_SERIALIZER = null;

    @InternalAPI
    public Predicate<Class<?>> isRecord = c -> false;

    private final Map<Class<?>, InternalSerializer<?>> serializerMap = new IdentityHashMap<>();
    private final Map<String, SerializerFuture> toLoadSerializerMap = new HashMap<>();
    private final List<InternalSerializer<?>> interfaceTypeSerializer = new ArrayList<>();
    private final Map<String, SerializerFuture> toLoadInterfaceTypeSerializer = new HashMap<>();
    private final List<InternalSerializer<?>> classTypeSerializer = new ArrayList<>();
    private final Map<String, SerializerFuture> toLoadClassTypeSerializer = new HashMap<>();

    private final Map<Class<?>, InstanceFactory<?>> instanceFactoryMap = new HashMap<>();

    @Getter
    private ReflectionStrategy reflectionStrategy = new PureStrategy();

    private Class<?> FinalInternalSerializerClass = null;
    private final CountDownLatch initialized = new CountDownLatch(SerializeManagerDataBindings.BINDINGS.length);

    private long time;

    void init() {
        // Init from YAPIONSerializerFlagDefault
    }

    static {
        YAPIONClassLoader yapionClassLoader = new YAPIONClassLoader(Thread.currentThread().getContextClassLoader());
        time = System.currentTimeMillis();
        SerializeManagerDataBindings.init(SerializeManager::internalAdd, yapionClassLoader, initialized);
    }

    private static synchronized void internalAdd(Class<?> clazz) {
        if (clazz.getTypeName().equals("yapion.serializing.serializer.FinalInternalSerializer") && FinalInternalSerializerClass == null) {
            FinalInternalSerializerClass = clazz;
        }
        Object o = internalAdd(clazz, SerializeManager::internalAdd);
        if (o == null) return;

        if (clazz.getTypeName().equals("yapion.serializing.serializer.special.ArraySerializer") && ARRAY_SERIALIZER == null) {
            ARRAY_SERIALIZER = (InternalSerializer<Object>) o;
        }
        if (clazz.getTypeName().equals("yapion.serializing.serializer.special.EnumSerializer") && ENUM_SERIALIZER == null) {
            ENUM_SERIALIZER = (InternalSerializer<Enum<?>>) o;
        }
        if (clazz.getTypeName().equals("yapion.serializing.serializer.special.RecordSerializer") && RECORD_SERIALIZER == null) {
            RECORD_SERIALIZER = (InternalSerializer<Object>) o;
        }
    }

    @InternalAPI
    static void add(Class<?> clazz) {
        internalAdd(clazz, SerializeManager::add);
    }

    private static Object internalAdd(Class<?> clazz, Consumer<InternalSerializer<?>> internalSerializerConsumer) {
        if (clazz.isInterface()) return null;
        Object o = ReflectionsUtils.constructObjectObjenesis(clazz);
        if (o instanceof SerializerBase<?, ?> serializerBase) {
            internalSerializerConsumer.accept(serializerBase.convert());
            return o;
        }
        if (clazz.getInterfaces().length != 1) return null;
        if (o == null) return null;
        if (o instanceof YAPIONSerializerRegistrator yapionSerializerRegistrator) {
            yapionSerializerRegistrator.register();
        } else if (o instanceof InternalSerializer internalSerializer) {
            internalSerializerConsumer.accept(internalSerializer);
        }
        return o;
    }

    @InternalAPI
    public static void add(InternalSerializer<?> serializer) {
        if (!checkOverrideable(serializer)) return;
        internalAdd(serializer);
    }

    private static void internalAdd(InternalSerializer<?> serializer) {
        serializer.init();
        if (serializer.type() != null) {
            serializerMap.put(serializer.type(), serializer);
        }
        if (serializer.primitiveType() != null && serializer.type() != serializer.primitiveType()) {
            serializerMap.put(serializer.primitiveType(), serializer);
        }
        if (serializer.interfaceType() != null) {
            interfaceTypeSerializer.add(serializer);
        }
        if (serializer.classType() != null) {
            classTypeSerializer.add(serializer);
        }
    }

    private static boolean checkOverrideable(InternalSerializer<?> serializer) {
        if (initialized.getCount() > 0) {
            return true;
        }
        if (!serializerMap.containsKey(serializer.type()) && !toLoadSerializerMap.containsKey(serializer.type().getTypeName())) {
            return true;
        }
        if (FinalInternalSerializerClass == null) return true;
        return !FinalInternalSerializerClass.isInstance(serializerMap.get(serializer.type()));
    }

    public static <T, K extends YAPIONAnyType> void add(SerializerBase<T, K> serializer) {
        if (serializer == null) return;
        if (serializer.type() == null) {
            throw new YAPIONException("Serializer needs to have a defined type. '" + serializer + "' does not have one.");
        }
        add(serializer.convert());
    }

    /**
     * Remove a special Serializer with the type name.
     *
     * @param type the typeName to remove
     */
    public static void remove(Class<?> type) {
        if (type == null) return;
        if (serializerMap.containsKey(type) && toLoadSerializerMap.containsKey(type.getTypeName()) && FinalInternalSerializerClass.isInstance(serializerMap.get(type))) return;
        serializerMap.remove(type);
    }

    @SuppressWarnings({"java:S1452"})
    static InternalSerializer<?> getInternalSerializer(Class<?> type) {
        synchronized (initialized) {
            while (initialized.getCount() != 0) {
                try {
                    initialized.await();
                    SerializeManagerDataBindings.finish(toLoadSerializerMap, toLoadInterfaceTypeSerializer, toLoadClassTypeSerializer);
                    time = System.currentTimeMillis() - time;
                    log.debug("SerializerManager initialized in {}ms", time);
                    System.out.println("Serializer init took: " + time + "ms");
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
        if (type == null) return null;
        if (type.isArray()) {
            return ARRAY_SERIALIZER;
        }
        if (type.isEnum() || type == Enum.class) {
            return ENUM_SERIALIZER;
        }
        if (isRecord.test(type)) {
            return RECORD_SERIALIZER;
        }
        if (toLoadSerializerMap.containsKey(type.getTypeName())) { // Lazy loads if needed
            log.debug("Loading serializer for '{}'", type.getTypeName());
            internalAdd(toLoadSerializerMap.get(type.getTypeName()).get());
            toLoadSerializerMap.remove(type.getTypeName());
            toLoadInterfaceTypeSerializer.remove(type.getTypeName());
            toLoadClassTypeSerializer.remove(type.getTypeName());
        }

        InternalSerializer<?> initialSerializer = serializerMap.getOrDefault(type, defaultSerializer);
        if (initialSerializer != null) return initialSerializer;

        if (!toLoadClassTypeSerializer.isEmpty()) {
            traverseSuperClasses(type, aClass -> {
                if (toLoadClassTypeSerializer.containsKey(aClass.getTypeName())) {
                    log.debug("Loading serializer for '{}'", aClass.getTypeName());
                    internalAdd(toLoadClassTypeSerializer.get(aClass.getTypeName()).get());
                    toLoadClassTypeSerializer.remove(aClass.getTypeName());
                }
            });
        }
        if (!toLoadInterfaceTypeSerializer.isEmpty()) {
            traverseInterfaceClasses(type, aClass -> {
                if (toLoadInterfaceTypeSerializer.containsKey(aClass.getTypeName())) {
                    log.debug("Loading serializer for '{}'", aClass.getTypeName());
                    internalAdd(toLoadInterfaceTypeSerializer.get(aClass.getTypeName()).get());
                    toLoadInterfaceTypeSerializer.remove(aClass.getTypeName());
                }
            });
        }

        AtomicReference<Class<?>> currentType = new AtomicReference<>(type);
        interfaceTypeSerializer.stream()
                .filter(internalSerializer -> implementsInterface(currentType.get(), internalSerializer.interfaceType()) >= 0)
                .min(Comparator.comparingInt(o -> implementsInterface(currentType.get(), o.interfaceType())))
                .ifPresent(internalSerializer -> currentType.set(internalSerializer.type()));
        classTypeSerializer.stream()
                .filter(internalSerializer -> isClassSuperclassOf(currentType.get(), internalSerializer.classType()))
                .findFirst()
                .ifPresent(serializer -> currentType.set(serializer.type()));
        type = currentType.get();

        return serializerMap.getOrDefault(type, defaultSerializer);
    }

    static InternalSerializer<?> getArraySerializer() {
        return ARRAY_SERIALIZER;
    }

    static InternalSerializer<?> getEnumSerializer() {
        return ENUM_SERIALIZER;
    }

    static InternalSerializer<?> getRecordSerializer() {
        return RECORD_SERIALIZER;
    }

    /**
     * Add an {@link InstanceFactory} to the SerializerManager
     * which will be used to create instances of a given {@link Class}.
     * This can speed up the deserialization process because there are
     * fewer reflection accesses.
     *
     * @param instanceFactory the factory
     */
    public static void add(InstanceFactory<?> instanceFactory) {
        if (!instanceFactoryMap.containsKey(instanceFactory.type())) {
            instanceFactoryMap.put(instanceFactory.type(), instanceFactory);
        }
    }

    /**
     * Check if an {@link InstanceFactory} is present for a given
     * {@link Class}.
     *
     * @param clazz the {@link Class} to check for
     * @return {@code true} if an {@link InstanceFactory} for the given type is present, {@code false} otherwise
     */
    public static boolean hasFactory(Class<?> clazz) {
        return instanceFactoryMap.containsKey(clazz);
    }

    @SuppressWarnings("unchecked")
    public static <T> T getObjectInstance(Class<T> clazz) throws ClassNotFoundException {
        if (!hasFactory(clazz)) {
            throw new ClassNotFoundException("Factory for " + clazz.getTypeName() + " not found or defined.");
        }
        return (T) instanceFactoryMap.get(clazz).instance();
    }

    static <T> T getObjectInstance(Class<T> clazz, boolean objenesis) {
        if (instanceFactoryMap.containsKey(clazz)) {
            return (T) instanceFactoryMap.get(clazz).instance();
        }
        if (clazz.getDeclaredAnnotation(YAPIONObjenesis.class) != null) {
            return ReflectionsUtils.constructObjectObjenesis(clazz);
        } else {
            return ReflectionsUtils.constructObject(clazz, objenesis);
        }
    }

    public static Set<Class<?>> listRegisteredSerializer() {
        return new HashSet<>(serializerMap.keySet());
    }

    public static Set<String> listToLoadRegisteredSerializer() {
        return new HashSet<>(toLoadSerializerMap.keySet());
    }

    public static Set<Class<?>> listRegisteredInterfaceSerializer() {
        return interfaceTypeSerializer.stream().map(InternalSerializer::interfaceType).collect(Collectors.toSet());
    }

    public static Set<String> listToLoadRegisteredInterfaceSerializer() {
        return new HashSet<>(toLoadInterfaceTypeSerializer.keySet());
    }

    public static Set<Class<?>> listRegisteredClassSerializer() {
        return classTypeSerializer.stream().map(InternalSerializer::classType).collect(Collectors.toSet());
    }

    public static Set<String> listToLoadClassSerializer() {
        return new HashSet<>(toLoadClassTypeSerializer.keySet());
    }

    public static void setReflectionStrategy(ReflectionStrategy reflectionStrategy) {
        if (reflectionStrategy == null) throw new IllegalArgumentException("reflectionStrategy should never be null");
        SerializeManager.reflectionStrategy = reflectionStrategy;
    }
}
