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
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import yapion.annotations.api.DeprecationInfo;
import yapion.annotations.api.InternalAPI;
import yapion.annotations.api.SerializerImplementation;
import yapion.annotations.object.YAPIONObjenesis;
import yapion.hierarchy.api.groups.YAPIONAnyType;
import yapion.hierarchy.types.YAPIONArray;
import yapion.hierarchy.types.YAPIONMap;
import yapion.hierarchy.types.YAPIONObject;
import yapion.hierarchy.types.YAPIONValue;
import yapion.serializing.api.*;
import yapion.serializing.data.DeserializeData;
import yapion.serializing.data.SerializeData;
import yapion.serializing.reflection.PureStrategy;
import yapion.serializing.serializer.special.ArraySerializer;
import yapion.serializing.serializer.special.EnumSerializer;
import yapion.serializing.utils.SerializeManagerUtils;
import yapion.utils.ReflectionsUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static yapion.utils.ReflectionsUtils.implementsInterface;
import static yapion.utils.ReflectionsUtils.isClassSuperclassOf;

@Slf4j
public class SerializeManager {

    private SerializeManager() {
        throw new IllegalStateException("Utility class");
    }

    static void init() {
        // Init from YAPIONSerializerFlagDefault
    }

    @ToString
    private static final class Serializer {
        private final InternalSerializer<?> internalSerializer;
        private final boolean overrideable;

        public Serializer(InternalSerializer<?> internalSerializer, boolean overrideable) {
            this.internalSerializer = internalSerializer;
            this.overrideable = overrideable;
        }
    }

    private static final boolean OVERRIDEABLE;
    private static final Serializer defaultSerializer = new Serializer(null, false);
    private static final Serializer defaultNullSerializer = new Serializer(new InternalSerializer<Object>() {
        @Override
        public Class<?> type() {
            return Void.class;
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

    private static final ArraySerializer ARRAY_SERIALIZER = new ArraySerializer();
    private static final EnumSerializer ENUM_SERIALIZER = new EnumSerializer();

    private static final String INTERNAL_SERIALIZER = InternalSerializer.class.getTypeName();

    private static final Map<Class<?>, Serializer> serializerMap = new IdentityHashMap<>();
    private static final List<InternalSerializer<?>> interfaceTypeSerializer = new ArrayList<>();
    private static final List<InternalSerializer<?>> classTypeSerializer = new ArrayList<>();

    private static final Set<String> nSerializerGroups = new HashSet<>();
    private static final Set<String> oSerializerGroups = new HashSet<>();

    private static final Map<Class<?>, InstanceFactory<?>> instanceFactoryMap = new HashMap<>();

    @Getter
    @Setter
    private static ReflectionStrategy reflectionStrategy = new PureStrategy();

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

        oSerializerGroups.add("yapion.annotations.");
        oSerializerGroups.add("yapion.hierarchy.output.     ");
        oSerializerGroups.add("yapion.hierarchy.types.utils.");
        oSerializerGroups.add("yapion.hierarchy.types.value.");
        oSerializerGroups.add("yapion.hierarchy.validators.");
        oSerializerGroups.add("yapion.parser.");
        oSerializerGroups.add("yapion.serializing.api.");
        oSerializerGroups.add("yapion.serializing.data.");
        oSerializerGroups.add("yapion.serializing.serializer.");
        oSerializerGroups.add("yapion.utils.");

        nSerializerGroups.add("java.io.");
        nSerializerGroups.add("java.net.");
        nSerializerGroups.add("java.nio.");
        nSerializerGroups.add("java.security.");
        nSerializerGroups.add("java.text.");
        nSerializerGroups.add("java.time.");
        nSerializerGroups.add("java.util.");
        OVERRIDEABLE = true;
    }

    @InternalAPI
    static void add(Class<?> clazz) {
        if (OVERRIDEABLE) return;
        String className = clazz.getTypeName();
        if (clazz.getInterfaces().length != 1) return;
        String typeName = clazz.getInterfaces()[0].getTypeName();
        Object o = ReflectionsUtils.constructObjectObjenesis(className);
        if (o == null) return;
        if (typeName.equals(INTERNAL_SERIALIZER)) {
            add((InternalSerializer<?>) o);
        }
    }

    private static void add(InternalSerializer<?> serializer) {
        if (!checkOverrideable(serializer)) return;
        serializer.init();
        Serializer serializerWrapper = new Serializer(serializer, OVERRIDEABLE);
        serializerMap.put(serializer.type(), serializerWrapper);

        if (serializer.primitiveType() != null && serializer.type() != serializer.primitiveType()) {
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
     * Remove a special Serializer with the type name.
     *
     * @param type the typeName to remove
     */
    public static void remove(Class<?> type) {
        if (type == null) return;
        if (serializerMap.containsKey(type) && !serializerMap.get(type).overrideable) return;
        serializerMap.remove(type);
    }

    @SuppressWarnings({"java:S1452"})
    static InternalSerializer<?> getInternalSerializer(Class<?> type) {
        if (type == null) return null;
        if (type.isArray()) {
            return ARRAY_SERIALIZER;
        }
        if (type.isEnum() || type == Enum.class) {
            return ENUM_SERIALIZER;
        }

        InternalSerializer<?> initialSerializer = getInternalSerializerInternal(type);
        if (initialSerializer != null) return initialSerializer;

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

        InternalSerializer<?> internalSerializer = getInternalSerializerInternal(type);
        if (internalSerializer != null) return internalSerializer;
        if (contains(type.getTypeName(), nSerializerGroups)) return defaultNullSerializer.internalSerializer;
        return null;
    }

    static InternalSerializer<?> getArraySerializer() {
        return ARRAY_SERIALIZER;
    }

    static InternalSerializer<?> getEnumSerializer() {
        return ENUM_SERIALIZER;
    }

    private static InternalSerializer<?> getInternalSerializerInternal(Class<?> type) {
        if (contains(type.getTypeName(), oSerializerGroups)) return defaultNullSerializer.internalSerializer;
        return serializerMap.getOrDefault(type, defaultSerializer).internalSerializer;
    }

    private static boolean contains(String s, Set<String> strings) {
        return strings.stream().anyMatch(s::startsWith);
    }

    /**
     * Add an {@link InstanceFactory} or {@link InstanceFactory}
     * to the SerializerManager which will be used to create instances
     * of a given {@link Class}. This can speed up the deserialization
     * process because there are fewer reflection accesses.
     *
     * @param instanceFactory the factory
     */
    public static void add(InstanceFactory<?> instanceFactory) {
        if (!instanceFactoryMap.containsKey(instanceFactory.type())) {
            instanceFactoryMap.put(instanceFactory.type(), instanceFactory);
        }
    }

    public static boolean hasFactory(Class<?> clazz) {
        return instanceFactoryMap.containsKey(clazz);
    }

    public static Object getGenericObjectInstance(Class<?> clazz) throws ClassNotFoundException {
        if (!hasFactory(clazz)) {
            throw new ClassNotFoundException("Factory for " + clazz.getTypeName() + " not found or defined.");
        }
        return instanceFactoryMap.get(clazz).instance();
    }

    @SuppressWarnings("unchecked")
    public static <T> T getObjectInstance(Class<T> clazz) throws ClassNotFoundException {
        if (!hasFactory(clazz)) {
            throw new ClassNotFoundException("Factory for " + clazz.getTypeName() + " not found or defined.");
        }
        return (T) instanceFactoryMap.get(clazz).instance();
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

    public static Set<Class<?>> listRegisteredSerializer() {
        return new HashSet<>(serializerMap.keySet());
    }

    public static Set<Class<?>> listRegisteredInterfaceSerializer() {
        return interfaceTypeSerializer.stream().map(InternalSerializer::interfaceType).collect(Collectors.toSet());
    }

    public static Set<Class<?>> listRegisteredClassSerializer() {
        return interfaceTypeSerializer.stream().map(InternalSerializer::classType).collect(Collectors.toSet());
    }

    /**
     * @deprecated since 0.24.0, {@link SerializeManagerUtils#SerializerObject(Class, SerializeManagerUtils.SerializationGetter, SerializeManagerUtils.DeserializationGetter)}
     */
    @SuppressWarnings({"java:S100"})
    @Deprecated
    @DeprecationInfo(since = "0.24.0", alternative = "SerializeManagerUtils#SerializerObject")
    public static <T> SerializerObject<T> SerializerObject(Class<T> clazz, SerializeManagerUtils.SerializationGetter<T, YAPIONObject> serializationGetter, SerializeManagerUtils.DeserializationGetter<T, YAPIONObject> deserializationGetter) {
        return SerializeManagerUtils.SerializerObject(clazz, serializationGetter, deserializationGetter);
    }

    /**
     * @deprecated since 0.24.0, {@link SerializeManagerUtils#SerializerMap(Class, SerializeManagerUtils.SerializationGetter, SerializeManagerUtils.DeserializationGetter)}
     */
    @SuppressWarnings({"java:S100"})
    @Deprecated
    @DeprecationInfo(since = "0.24.0", alternative = "SerializeManagerUtils#SerializerMap")
    public static <T extends Map<?, ?>> SerializerMap<T> SerializerMap(Class<T> clazz, SerializeManagerUtils.SerializationGetter<T, YAPIONMap> serializationGetter, SerializeManagerUtils.DeserializationGetter<T, YAPIONMap> deserializationGetter) {
        return SerializeManagerUtils.SerializerMap(clazz, serializationGetter, deserializationGetter);
    }

    /**
     * @deprecated since 0.24.0, {@link SerializeManagerUtils#SerializerList(Class, SerializeManagerUtils.SerializationGetter, SerializeManagerUtils.DeserializationGetter)}
     */
    @SuppressWarnings({"java:S100"})
    @Deprecated
    @DeprecationInfo(since = "0.24.0", alternative = "SerializeManagerUtils#SerializerList")
    public static <T extends List<?>> SerializerList<T> SerializerList(Class<T> clazz, SerializeManagerUtils.SerializationGetter<T, YAPIONArray> serializationGetter, SerializeManagerUtils.DeserializationGetter<T, YAPIONArray> deserializationGetter) {
        return SerializeManagerUtils.SerializerList(clazz, serializationGetter, deserializationGetter);
    }

    /**
     * @deprecated since 0.24.0, {@link SerializeManagerUtils#SerializerQueue(Class, SerializeManagerUtils.SerializationGetter, SerializeManagerUtils.DeserializationGetter)}
     */
    @SuppressWarnings({"java:S100"})
    @Deprecated
    @DeprecationInfo(since = "0.24.0", alternative = "SerializeManagerUtils#SerializerQueue")
    public static <T extends Queue<?>> SerializerQueue<T> SerializerQueue(Class<T> clazz, SerializeManagerUtils.SerializationGetter<T, YAPIONArray> serializationGetter, SerializeManagerUtils.DeserializationGetter<T, YAPIONArray> deserializationGetter) {
        return SerializeManagerUtils.SerializerQueue(clazz, serializationGetter, deserializationGetter);
    }

    /**
     * @deprecated since 0.24.0, {@link SerializeManagerUtils#SerializerSet(Class, SerializeManagerUtils.SerializationGetter, SerializeManagerUtils.DeserializationGetter)}
     */
    @SuppressWarnings({"java:S100"})
    @Deprecated
    @DeprecationInfo(since = "0.24.0", alternative = "SerializeManagerUtils#SerializerSet")
    public static <T extends Set<?>> SerializerSet<T> SerializerSet(Class<T> clazz, SerializeManagerUtils.SerializationGetter<T, YAPIONArray> serializationGetter, SerializeManagerUtils.DeserializationGetter<T, YAPIONArray> deserializationGetter) {
        return SerializeManagerUtils.SerializerSet(clazz, serializationGetter, deserializationGetter);
    }

    /**
     * @deprecated since 0.24.0, {@link SerializeManagerUtils#InstanceFactory(Class, SerializeManagerUtils.InstanceGetter)}
     */
    @SuppressWarnings({"java:S100"})
    @Deprecated
    @DeprecationInfo(since = "0.24.0", alternative = "SerializeManagerUtils#InstanceFactory")
    public static <T> InstanceFactory<T> InstanceFactory(Class<T> clazz, SerializeManagerUtils.InstanceGetter<T> instanceGetter) {
        return SerializeManagerUtils.InstanceFactory(clazz, instanceGetter);
    }

}
