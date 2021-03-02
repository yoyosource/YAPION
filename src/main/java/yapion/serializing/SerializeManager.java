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

import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import yapion.annotations.deserialize.YAPIONLoadExclude;
import yapion.annotations.object.YAPIONObjenesis;
import yapion.annotations.serialize.YAPIONSaveExclude;
import yapion.hierarchy.api.groups.YAPIONAnyType;
import yapion.hierarchy.types.YAPIONValue;
import yapion.serializing.api.*;
import yapion.serializing.data.DeserializeData;
import yapion.serializing.data.SerializeData;
import yapion.serializing.serializer.SerializerImplementation;
import yapion.utils.ReflectionsUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static yapion.serializing.YAPIONSerializerFlagDefault.DATA_LOSS_EXCEPTION;
import static yapion.utils.ReflectionsUtils.implementsInterface;
import static yapion.utils.ReflectionsUtils.isClassSuperclassOf;

@YAPIONSaveExclude(context = "*")
@YAPIONLoadExclude(context = "*")
@Slf4j
public class SerializeManager {

    private SerializeManager() {
        throw new IllegalStateException("Utility class");
    }

    static void init() {
        // Init from YAPIONSerializerFlagDefault
    }

    @YAPIONSaveExclude(context = "*")
    @YAPIONLoadExclude(context = "*")
    @ToString
    private static final class Serializer {
        private final InternalSerializer<?> internalSerializer;
        private final boolean overrideable;

        public Serializer(InternalSerializer<?> internalSerializer, boolean overrideable) {
            this.internalSerializer = internalSerializer;
            this.overrideable = overrideable;
        }
    }

    private static final boolean overrideable;
    private static final Serializer defaultSerializer = new Serializer(null, false);
    private static final Serializer defaultNullSerializer = new Serializer(new InternalSerializer<Object>() {
        @Override
        public void init() {
            YAPIONSerializerFlags.addFlag(new YAPIONSerializerFlagDefault(DATA_LOSS_EXCEPTION, false));
        }

        @Override
        public String type() {
            return "";
        }

        @Override
        public YAPIONAnyType serialize(SerializeData<Object> serializeData) {
            serializeData.signalDataLoss();
            return new YAPIONValue<>(null);
        }

        @Override
        public Object deserialize(DeserializeData<? extends YAPIONAnyType> deserializeData) {
            return null;
        }
    }, false);

    private static final String internalSerializer = InternalSerializer.class.getTypeName();

    private static final Map<String, Serializer> serializerMap = new HashMap<>();
    private static final List<InternalSerializer<?>> interfaceTypeSerializer = new ArrayList<>();
    private static final List<InternalSerializer<?>> classTypeSerializer = new ArrayList<>();
    private static final GroupList nSerializerGroups = new GroupList();
    private static final GroupList oSerializerGroups = new GroupList();

    private static final Map<Class<?>, InstanceFactoryInterface<?>> instanceFactoryMap = new HashMap<>();

    static {
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(SerializeManager.class.getResourceAsStream("/yapion/" + SerializerImplementation.class.getTypeName())))) {
            bufferedReader.lines().forEach(s -> {
                try {
                    add(Class.forName(s));
                } catch (ClassNotFoundException e) {
                    log.warn(e.getMessage(), e);
                }
            });
        } catch (IOException e) {
            log.warn(e.getMessage(), e);
        }
        if (serializerMap.isEmpty()) {
            log.error("No Serializer was loaded. Please inspect.");
        }

        oSerializerGroups.add(() -> "yapion.annotations.");
        oSerializerGroups.add(() -> "yapion.exceptions.");
        oSerializerGroups.add(() -> "yapion.hierarchy.");
        oSerializerGroups.add(() -> "yapion.parser.");
        oSerializerGroups.add(() -> "yapion.serializing.api");
        oSerializerGroups.add(() -> "yapion.serializing.data");
        oSerializerGroups.add(() -> "yapion.serializing.serializer");
        oSerializerGroups.add(() -> "yapion.utils.");
        oSerializerGroups.build();

        nSerializerGroups.add(() -> "java.io.");
        nSerializerGroups.add(() -> "java.net.");
        nSerializerGroups.add(() -> "java.nio.");
        nSerializerGroups.add(() -> "java.security.");
        nSerializerGroups.add(() -> "java.text.");
        nSerializerGroups.add(() -> "java.time.");
        nSerializerGroups.add(() -> "java.util.");
        nSerializerGroups.build();
        overrideable = true;
    }

    private static void add(Class<?> clazz) {
        if (overrideable) return;
        String className = clazz.getTypeName();
        if (!className.startsWith("yapion.serializing.serializer")) return;
        if (clazz.getInterfaces().length != 1) return;
        String typeName = clazz.getInterfaces()[0].getTypeName();
        Object o = ReflectionsUtils.constructObjectObjenesis(className);
        if (o == null) return;
        if (typeName.equals(internalSerializer)) {
            add((InternalSerializer<?>) o);
        }
    }

    private static void add(InternalSerializer<?> serializer) {
        if (!checkOverrideable(serializer)) return;
        serializer.init();
        Serializer serializerWrapper = new Serializer(serializer, overrideable);
        serializerMap.put(serializer.type(), serializerWrapper);
        if (serializer.primitiveType() != null && !serializer.primitiveType().isEmpty()) {
            serializerMap.put(serializer.primitiveType(), serializerWrapper);
        }
        if (serializer.interfaceType() != null) {
            interfaceTypeSerializer.add(serializer);
        }
        if (serializer.classType() != null) {
            classTypeSerializer.add(serializer);
        }
    }

    private static boolean checkOverrideable(InternalSerializer<?> serializer) {
        return !serializerMap.containsKey(serializer.type()) || serializerMap.get(serializer.type()).overrideable;
    }

    public static <T> void add(SerializerBase<T, ?> serializer) {
        if (serializer == null) return;
        if (serializer.type() == null) return;
        add(serializer.convert());
    }

    /**
     * Remove a special Serializer with the Class type name.
     *
     * @param clazz the Class type name
     */
    public static void remove(Class<?> clazz) {
        if (clazz == null) return;
        remove(clazz.getTypeName());
    }

    /**
     * Remove a special Serializer with the type name.
     *
     * @param type the typeName to remove
     */
    public static void remove(String type) {
        if (type == null) return;
        if (serializerMap.containsKey(type) && !serializerMap.get(type).overrideable) return;
        serializerMap.remove(type);
    }

    @SuppressWarnings({"java:S1452"})
    static InternalSerializer<?> getInternalSerializer(String type) {
        InternalSerializer<?> initialSerializer = getInternalSerializerInternal(type);
        if (initialSerializer != null) return initialSerializer;

        AtomicReference<String> currentType = new AtomicReference<>(type);
        interfaceTypeSerializer.stream()
                .filter(internalSerializer -> implementsInterface(currentType.get(), internalSerializer.interfaceType()) >= 0)
                .min(Comparator.comparingInt(o -> implementsInterface(currentType.get(), o.interfaceType())))
                .ifPresent(internalSerializer -> currentType.set(internalSerializer.type()));
        classTypeSerializer.stream()
                .filter(internalSerializer -> isClassSuperclassOf(currentType.get(), internalSerializer.classType()))
                .findFirst()
                .ifPresent(serializer -> currentType.set(serializer.type()));
        type = currentType.get();

        InternalSerializer<?> internalSerializer = getInternalSerializerInternal(type);
        if (internalSerializer != null) return internalSerializer;
        if (nSerializerGroups.contains(type)) return defaultNullSerializer.internalSerializer;
        return null;
    }

    private static InternalSerializer<?> getInternalSerializerInternal(String type) {
        if (oSerializerGroups.contains(type)) return defaultNullSerializer.internalSerializer;
        return serializerMap.getOrDefault(type, defaultSerializer).internalSerializer;
    }

    /**
     * Add an {@link InstanceFactory} or {@link InstanceFactoryInterface}
     * to the SerializerManager which will be used to create instances
     * of a given {@link Class}. This can speed up the deserialization
     * process because there are fewer reflection accesses.
     *
     * @param instanceFactory the factory
     */
    public static void add(InstanceFactoryInterface<?> instanceFactory) {
        if (!instanceFactoryMap.containsKey(instanceFactory.type())) {
            instanceFactoryMap.put(instanceFactory.type(), instanceFactory);
        }
    }

    static Object getObjectInstance(Class<?> clazz, String type, boolean objenesis) {
        if (instanceFactoryMap.containsKey(clazz)) {
            return instanceFactoryMap.get(clazz).instance();
        }
        if (clazz.getDeclaredAnnotation(YAPIONObjenesis.class) != null) {
            return ReflectionsUtils.constructObjectObjenesis(type);
        } else {
            return ReflectionsUtils.constructObject(type, objenesis);
        }
    }

    public static Set<String> listRegisteredSerializer() {
        return new HashSet<>(serializerMap.keySet());
    }

    public static Set<Class<?>> listRegisteredInterfaceSerializer() {
        return interfaceTypeSerializer.stream().map(InternalSerializer::interfaceType).collect(Collectors.toSet());
    }

    public static Set<Class<?>> listRegisteredClassSerializer() {
        return interfaceTypeSerializer.stream().map(InternalSerializer::classType).collect(Collectors.toSet());
    }

}
