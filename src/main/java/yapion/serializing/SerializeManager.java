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

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.kamranzafar.jtar.TarEntry;
import org.kamranzafar.jtar.TarInputStream;
import yapion.annotations.api.InternalAPI;
import yapion.annotations.object.YAPIONObjenesis;
import yapion.exceptions.YAPIONException;
import yapion.hierarchy.api.groups.YAPIONAnyType;
import yapion.hierarchy.types.YAPIONValue;
import yapion.serializing.api.InstanceFactory;
import yapion.serializing.api.SerializerBase;
import yapion.serializing.data.DeserializeData;
import yapion.serializing.data.SerializeData;
import yapion.serializing.reflection.PureStrategy;
import yapion.utils.ReflectionsUtils;
import yapion.utils.YAPIONClassLoader;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.zip.GZIPInputStream;

import static yapion.utils.ReflectionsUtils.implementsInterface;
import static yapion.utils.ReflectionsUtils.isClassSuperclassOf;

@Slf4j
@UtilityClass
public class SerializeManager {

    void init() {
        // Init from YAPIONSerializerFlagDefault
    }

    private final InternalSerializer<Object> defaultSerializer = null;
    private final InternalSerializer<Void> defaultNullSerializer = new InternalSerializer<Void>() {
        @Override
        public Class<?> type() {
            return Void.class;
        }

        @Override
        public YAPIONAnyType serialize(SerializeData<Void> serializeData) {
            serializeData.signalDataLoss();
            return new YAPIONValue<>(null);
        }

        @Override
        public Void deserialize(DeserializeData<? extends YAPIONAnyType> deserializeData) {
            return null;
        }
    };

    private InternalSerializer<Object> ARRAY_SERIALIZER = null;
    private InternalSerializer<Enum<?>> ENUM_SERIALIZER = null;
    private InternalSerializer<Object> RECORD_SERIALIZER = null;

    @InternalAPI
    public Predicate<Class<?>> isRecord = c -> false;

    private final Map<Class<?>, InternalSerializer<?>> serializerMap = new IdentityHashMap<>();
    private final List<InternalSerializer<?>> interfaceTypeSerializer = new ArrayList<>();
    private final List<InternalSerializer<?>> classTypeSerializer = new ArrayList<>();

    private final Set<String> serializerGroups = new HashSet<>();

    private final Map<Class<?>, InstanceFactory<?>> instanceFactoryMap = new HashMap<>();

    @Getter
    @Setter
    private ReflectionStrategy reflectionStrategy = new PureStrategy();

    private Class<?> FinalInternalSerializerClass = null;

    @AllArgsConstructor
    private static class WrappedClass {
        private final String name;
        private final byte[] bytes;

        @Override
        public String toString() {
            return "SerializeManager.WrappedClass(" + name + ")";
        }
    }

    static {
        InputStream inputStream = SerializeManager.class.getResourceAsStream("serializer.tar.gz");
        if (inputStream == null) {
            log.error("No Serializer was loaded. Please inspect.");
            throw new YAPIONException("No Serializer was loaded. Please inspect.");
        }
        try (TarInputStream tarInputStream = new TarInputStream(new GZIPInputStream(new BufferedInputStream(inputStream), 8192))) {
            Map<Integer, List<WrappedClass>> depthMap = new TreeMap<>(Comparator.comparingInt(value -> value));
            int deepest = 0;

            TarEntry entry;
            while ((entry = tarInputStream.getNextEntry()) != null) {
                log.debug("Entry: {} {} {}", entry.getName(), entry.getSize(), entry.isDirectory());
                if (entry.isDirectory() || !entry.getName().endsWith(".class")) continue;

                List<Byte> bytes = new ArrayList<>();
                while (tarInputStream.available() > 0 && bytes.size() < entry.getSize()) {
                    bytes.add((byte) tarInputStream.read());
                }
                byte[] byteArray = new byte[bytes.size()];
                for (int i = 0; i < byteArray.length; i++) {
                    byteArray[i] = bytes.get(i);
                }

                String className = "yapion.serializing.serializer." + entry.getName().substring(0, entry.getName().indexOf('.')).replace("/", ".");
                int depth = className.length() - className.replace(".", "").length();
                if (className.endsWith(".KeySpecSerializer")) depth -= 1;
                if (!className.endsWith("Serializer")) depth -= 1;
                depth -= className.length() - className.replace("$", "").length();
                log.debug("Entry Info: {} {} {} {} {}", entry.getName(), entry.getSize(), byteArray.length, depth, deepest);

                deepest = Math.max(deepest, depth);
                depthMap.computeIfAbsent(depth, d -> new ArrayList<>()).add(new WrappedClass(className, byteArray));
            }
            log.debug("ToLoad: {}", depthMap);

            YAPIONClassLoader classLoader = new YAPIONClassLoader(Thread.currentThread().getContextClassLoader());
            depthMap.forEach((i, wrappedClasses) -> {
                log.debug("Depth: {}", i);
                wrappedClasses.forEach(wrappedClass -> {
                    log.debug("Loading: {}", wrappedClass.name);
                    add(classLoader.defineClass(wrappedClass.name, wrappedClass.bytes));
                });
            });
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage(), e);
        }
        if (serializerMap.isEmpty()) {
            log.error("No Serializer was loaded. Please inspect.");
        }

        serializerGroups.add("java.");
    }

    @InternalAPI
    static void add(Class<?> clazz) {
        if (clazz.getTypeName().equals("yapion.serializing.serializer.FinalInternalSerializer") && FinalInternalSerializerClass == null) {
            FinalInternalSerializerClass = clazz;
        }
        if (clazz.isInterface()) return;
        if (clazz.getInterfaces().length != 1) return;
        Object o = ReflectionsUtils.constructObjectObjenesis(clazz);
        if (o == null) return;
        if (!(o instanceof InternalSerializer)) return;
        add((InternalSerializer<?>) o);

        if (o.getClass().getTypeName().equals("yapion.serializing.serializer.special.ArraySerializer") && ARRAY_SERIALIZER == null) {
            ARRAY_SERIALIZER = (InternalSerializer<Object>) o;
        }
        if (o.getClass().getTypeName().equals("yapion.serializing.serializer.special.EnumSerializer") && ENUM_SERIALIZER == null) {
            ENUM_SERIALIZER = (InternalSerializer<Enum<?>>) o;
        }
        if (o.getClass().getTypeName().equals("yapion.serializing.serializer.special.RecordSerializer") && RECORD_SERIALIZER == null) {
            RECORD_SERIALIZER = (InternalSerializer<Object>) o;
        }
    }

    @InternalAPI
    public static void add(InternalSerializer<?> serializer) {
        if (!checkOverrideable(serializer)) return;
        serializer.init();
        serializerMap.put(serializer.type(), serializer);

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
        if (!serializerMap.containsKey(serializer.type())) {
            return true;
        }
        if (FinalInternalSerializerClass == null) return true;
        return !FinalInternalSerializerClass.isInstance(serializerMap.get(serializer.type()));
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
        if (serializerMap.containsKey(type) && FinalInternalSerializerClass.isInstance(serializerMap.get(type))) return;
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
        if (isRecord.test(type)) {
            return RECORD_SERIALIZER;
        }

        InternalSerializer<?> initialSerializer = serializerMap.getOrDefault(type, defaultSerializer);
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

        InternalSerializer<?> internalSerializer = serializerMap.getOrDefault(type, defaultSerializer);
        if (internalSerializer != null) return internalSerializer;
        String typeName = type.getTypeName();
        if (serializerGroups.stream().anyMatch(typeName::startsWith)) return defaultNullSerializer;
        return null;
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

    static <T> T getObjectInstance(Class<T> clazz, String type, boolean objenesis) {
        if (instanceFactoryMap.containsKey(clazz)) {
            return (T) instanceFactoryMap.get(clazz).instance();
        }
        if (clazz.getDeclaredAnnotation(YAPIONObjenesis.class) != null) {
            return (T) ReflectionsUtils.constructObjectObjenesis(type);
        } else {
            return (T) ReflectionsUtils.constructObject(type, objenesis);
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
}
