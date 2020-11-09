// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020 yoyosource

package yapion.serializing;

import lombok.ToString;
import org.atteo.classindex.ClassIndex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import yapion.annotations.deserialize.YAPIONLoadExclude;
import yapion.annotations.object.YAPIONObjenesis;
import yapion.annotations.serialize.YAPIONSaveExclude;
import yapion.exceptions.YAPIONException;
import yapion.hierarchy.typegroups.YAPIONAnyType;
import yapion.hierarchy.types.YAPIONArray;
import yapion.hierarchy.types.YAPIONMap;
import yapion.hierarchy.types.YAPIONObject;
import yapion.hierarchy.types.YAPIONValue;
import yapion.serializing.api.*;
import yapion.serializing.data.DeserializeData;
import yapion.serializing.data.SerializeData;
import yapion.serializing.serializer.SerializerImplementation;
import yapion.utils.ReflectionsUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;

import static yapion.utils.IdentifierUtils.TYPE_IDENTIFIER;

@YAPIONSaveExclude(context = "*")
@YAPIONLoadExclude(context = "*")
public class SerializeManager {

    private SerializeManager() {
        throw new IllegalStateException("Utility class");
    }

    private static final Logger logger = LoggerFactory.getLogger(SerializeManager.class);

    @YAPIONSaveExclude(context = "*")
    @YAPIONLoadExclude(context = "*")
    @ToString
    private static final class Serializer {
        private InternalSerializer<?> internalSerializer;
        private boolean overrideable;

        public Serializer(InternalSerializer<?> internalSerializer, boolean overrideable) {
            this.internalSerializer = internalSerializer;
            this.overrideable = overrideable;
        }
    }

    private static final boolean overrideable;
    private static final Serializer defaultSerializer = new Serializer(null, false);
    private static final Serializer defaultNullSerializer = new Serializer(new InternalSerializer<Object>() {
        @Override
        public String type() {
            return "";
        }

        @Override
        public YAPIONAnyType serialize(SerializeData<Object> serializeData) {
            return new YAPIONValue<>(null);
        }

        @Override
        public Object deserialize(DeserializeData<? extends YAPIONAnyType> deserializeData) {
            return null;
        }
    }, false);

    private static final String internalOverrideableSerializer = InternalOverrideableSerializer.class.getTypeName();
    private static final String internalSerializer = InternalSerializer.class.getTypeName();

    private static final Map<String, Serializer> serializerMap = new HashMap<>();
    private static final GroupList nSerializerGroups = new GroupList();
    private static final GroupList oSerializerGroups = new GroupList();

    private static final Map<Class<?>, InstanceFactoryInterface<?>> instanceFactoryMap = new HashMap<>();

    static {
        ClassIndex.getAnnotated(SerializerImplementation.class).forEach(SerializeManager::add);
        if (serializerMap.isEmpty()) {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(SerializeManager.class.getResourceAsStream("/yapion/" + SerializerImplementation.class.getSimpleName())));
            bufferedReader.lines().forEach(s -> {
                try {
                    add(Class.forName(s));
                } catch (ClassNotFoundException e) {
                    // Ignored
                }
            });
        }

        oSerializerGroups.add(() -> "yapion.annotations.");
        oSerializerGroups.add(() -> "yapion.exceptions.");
        oSerializerGroups.add(() -> "yapion.parser.");
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
        if (typeName.equals(internalOverrideableSerializer)) {
            add((InternalOverrideableSerializer<?>) o);
        } else if (typeName.equals(internalSerializer)) {
            add((InternalSerializer<?>) o);
        }
    }

    private static void add(InternalOverrideableSerializer<?> serializer) {
        if (!checkOverrideable(serializer)) return;
        Serializer serializerWrapper = new Serializer(serializer, true);
        serializerMap.put(serializer.type(), serializerWrapper);
        if (serializer.primitiveType() != null && !serializer.primitiveType().isEmpty()) {
            serializerMap.put(serializer.primitiveType(), serializerWrapper);
        }
    }

    private static void add(InternalSerializer<?> serializer) {
        if (!checkOverrideable(serializer)) return;
        Serializer serializerWrapper = new Serializer(serializer, overrideable);
        serializerMap.put(serializer.type(), serializerWrapper);
        if (serializer.primitiveType() != null && !serializer.primitiveType().isEmpty()) {
            serializerMap.put(serializer.primitiveType(), serializerWrapper);
        }
    }

    private static boolean checkOverrideable(InternalSerializer<?> serializer) {
        return !serializerMap.containsKey(serializer.type()) || serializerMap.get(serializer.type()).overrideable;
    }

    /**
     * Adds a special Serializer for a specific Object.
     *
     * @param serializer the special Serializer to add
     */
    public static <T> void add(SerializerObjectInterface<T> serializer) {
        if (serializer == null) return;
        if (serializer.type() == null) return;
        InternalSerializer<T> internalSerializer = new InternalSerializer<T>() {
            @Override
            public String type() {
                return serializer.type().getTypeName();
            }

            @Override
            public YAPIONAnyType serialize(SerializeData<T> serializeData) {
                try {
                    YAPIONObject yapionObject = serializer.serialize(serializeData);
                    yapionObject.add(TYPE_IDENTIFIER, type());
                    return yapionObject;
                } catch (Exception e) {
                    logger.error("An unexpected error occurred", e.getCause());
                }
                return null;
            }

            @Override
            @SuppressWarnings({"java:S1905"})
            public T deserialize(DeserializeData<? extends YAPIONAnyType> deserializeData) {
                if (deserializeData.object instanceof YAPIONObject) {
                    try {
                        return serializer.deserialize((DeserializeData<YAPIONObject>) deserializeData);
                    } catch (Exception e) {
                        logger.error("An unexpected error occurred", e.getCause());
                    }
                }
                return null;
            }
        };
        add(internalSerializer);
    }

    /**
     * Adds a special Serializer for a specific Map.
     *
     * @param serializer the special Serializer to add
     */
    public static <T extends Map<?, ?>> void add(SerializerMapInterface<T> serializer) {
        if (serializer == null) return;
        if (serializer.type() == null) return;
        InternalSerializer<T> internalSerializer = new InternalSerializer<T>() {
            @Override
            public String type() {
                return serializer.type().getTypeName();
            }

            @Override
            public YAPIONAnyType serialize(SerializeData<T> serializeData) {
                try {
                    YAPIONObject yapionObject = new YAPIONObject();
                    yapionObject.add(TYPE_IDENTIFIER, type());
                    yapionObject.add("map", serializer.serialize(serializeData));
                    return yapionObject;
                } catch (Exception e) {
                    logger.error("An unexpected error occurred", e.getCause());
                }
                return null;
            }

            @Override
            @SuppressWarnings({"java:S1905"})
            public T deserialize(DeserializeData<? extends YAPIONAnyType> deserializeData) {
                try {
                    return serializer.deserialize(deserializeData.clone(((YAPIONObject) deserializeData.object).getMap("map")));
                } catch (Exception e) {
                    logger.error("An unexpected error occurred", e.getCause());
                }
                return null;
            }
        };
        add(internalSerializer);
    }

    /**
     * Adds a special Serializer for a specific List.
     *
     * @param serializer the special Serializer to add
     */
    public static <T extends List<?>> void add(SerializerListInterface<T> serializer) {
        if (serializer == null) return;
        if (serializer.type() == null) return;
        InternalSerializer<T> internalSerializer = new InternalSerializer<T>() {
            @Override
            public String type() {
                return serializer.type().getTypeName();
            }

            @Override
            public YAPIONAnyType serialize(SerializeData<T> serializeData) {
                try {
                    YAPIONObject yapionObject = new YAPIONObject();
                    yapionObject.add(TYPE_IDENTIFIER, type());
                    yapionObject.add("list", serializer.serialize(serializeData));
                    return yapionObject;
                } catch (Exception e) {
                    logger.error("An unexpected error occurred", e.getCause());
                }
                return null;
            }

            @Override
            @SuppressWarnings({"java:S1905"})
            public T deserialize(DeserializeData<? extends YAPIONAnyType> deserializeData) {
                try {
                    return serializer.deserialize(deserializeData.clone(((YAPIONObject) deserializeData.object).getArray("list")));
                } catch (Exception e) {
                    logger.error("An unexpected error occurred", e.getCause());
                }
                return null;
            }
        };
        add(internalSerializer);
    }

    /**
     * Adds a special Serializer for a specific Queue.
     *
     * @param serializer the special Serializer to add
     */
    public static <T extends Queue<?>> void add(SerializerQueueInterface<T> serializer) {
        if (serializer == null) return;
        if (serializer.type() == null) return;
        InternalSerializer<T> internalSerializer = new InternalSerializer<T>() {
            @Override
            public String type() {
                return serializer.type().getTypeName();
            }

            @Override
            public YAPIONAnyType serialize(SerializeData<T> serializeData) {
                try {
                    YAPIONObject yapionObject = new YAPIONObject();
                    yapionObject.add(TYPE_IDENTIFIER, type());
                    yapionObject.add("queue", serializeData.serialize(serializeData));
                    return yapionObject;
                } catch (Exception e) {
                    logger.error("An unexpected error occurred", e.getCause());
                }
                return null;
            }

            @Override
            @SuppressWarnings({"java:S1905"})
            public T deserialize(DeserializeData<? extends YAPIONAnyType> deserializeData) {
                try {
                    return serializer.deserialize(deserializeData.clone(((YAPIONObject) deserializeData.object).getArray("queue")));
                } catch (Exception e) {
                    logger.error("An unexpected error occurred", e.getCause());
                }
                return null;
            }
        };
        add(internalSerializer);
    }

    /**
     * Adds a special Serializer for a specific Set.
     *
     * @param serializer the special Serializer to add
     */
    public static <T extends Set<?>> void add(SerializerSetInterface<T> serializer) {
        if (serializer == null) return;
        if (serializer.type() == null) return;
        InternalSerializer<T> internalSerializer = new InternalSerializer<T>() {
            @Override
            public String type() {
                return serializer.type().getTypeName();
            }

            @Override
            public YAPIONAnyType serialize(SerializeData<T> serializeData) {
                try {
                    YAPIONObject yapionObject = new YAPIONObject();
                    yapionObject.add(TYPE_IDENTIFIER, type());
                    yapionObject.add("set", serializer.serialize(serializeData));
                    return yapionObject;
                } catch (Exception e) {
                    logger.error("An unexpected error occurred", e.getCause());
                }
                return null;
            }

            @Override
            @SuppressWarnings({"java:S1905"})
            public T deserialize(DeserializeData<? extends YAPIONAnyType> deserializeData) {
                try {
                    return serializer.deserialize(deserializeData.clone(((YAPIONObject) deserializeData.object).getArray("set")));
                } catch (Exception e) {
                    logger.error("An unexpected error occurred", e.getCause());
                }
                return null;
            }
        };
        add(internalSerializer);
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

    /**
     * Remove a special Serializer with the Class type name.
     *
     * @param clazz the Class type name
     */
    public static void remove(Class<?> clazz) {
        if (clazz == null) return;
        remove(clazz.getTypeName());
    }

    @SuppressWarnings({"java:S1452"})
    static InternalSerializer<?> getInternalSerializer(String type) {
        if (oSerializerGroups.contains(type)) return defaultNullSerializer.internalSerializer;
        InternalSerializer<?> serializer = serializerMap.getOrDefault(type, defaultSerializer).internalSerializer;
        if (serializer != null) return serializer;
        if (nSerializerGroups.contains(type)) return defaultNullSerializer.internalSerializer;
        return null;
    }

    /**
     * Create a {@link SerializerQueue} from two interfaces:
     * {@link SerializationGetter} and {@link DeserializationGetter}.
     * Those are all FunctionalInterfaces with just one
     * method to override. This makes the use off those
     * interfaces really easy. If any of the three arguments
     * are {@code null} an YAPIONException is thrown.
     *
     * @param clazz the Class
     * @param serializationGetter the SerializationGetter
     * @param deserializationGetter the DeserializationGetter
     * @param <T> the Type of this Serializer
     * @return the Serializer that wraps the Interfaces
     */

    @SuppressWarnings({"java:S100"})
    public static <T> SerializerObject<T> SerializerObject(Class<T> clazz, SerializationGetter<T, YAPIONObject> serializationGetter, DeserializationGetter<T, YAPIONObject> deserializationGetter) {
        if (clazz == null) throw new YAPIONException();
        if (serializationGetter == null) throw new YAPIONException();
        if (deserializationGetter == null) throw new YAPIONException();
        return new SerializerObject<T>() {
            @Override
            public Class<T> type() {
                return clazz;
            }

            @Override
            public YAPIONObject serialize(SerializeData<T> serializeData) {
                return serializationGetter.serialize(serializeData);
            }

            @Override
            public T deserialize(DeserializeData<YAPIONObject> deserializeData) {
                return deserializationGetter.deserialize(deserializeData);
            }
        };
    }

    /**
     * Create a {@link SerializerMap} from two interfaces:
     * {@link SerializationGetter} and {@link DeserializationGetter}.
     * Those are all FunctionalInterfaces with just one
     * method to override. This makes the use off those
     * interfaces really easy. If any of the three arguments
     * are {@code null} an YAPIONException is thrown.
     *
     * @param clazz the Class
     * @param serializationGetter the SerializationGetter
     * @param deserializationGetter the DeserializationGetter
     * @param <T> the Type of this Serializer
     * @return the Serializer that wraps the Interfaces
     */

    @SuppressWarnings({"java:S100"})
    public static <T extends Map<?, ?>> SerializerMap<T> SerializerMap(Class<T> clazz, SerializationGetter<T, YAPIONMap> serializationGetter, DeserializationGetter<T, YAPIONMap> deserializationGetter) {
        if (clazz == null) throw new YAPIONException();
        if (serializationGetter == null) throw new YAPIONException();
        if (deserializationGetter == null) throw new YAPIONException();
        return new SerializerMap<T>() {
            @Override
            public Class<T> type() {
                return clazz;
            }

            @Override
            public YAPIONMap serialize(SerializeData<T> serializeData) {
                return serializationGetter.serialize(serializeData);
            }

            @Override
            public T deserialize(DeserializeData<YAPIONMap> deserializeData) {
                return deserializationGetter.deserialize(deserializeData);
            }
        };
    }

    /**
     * Create a {@link SerializerList} from two interfaces:
     * {@link SerializationGetter} and {@link DeserializationGetter}.
     * Those are all FunctionalInterfaces with just one
     * method to override. This makes the use off those
     * interfaces really easy. If any of the three arguments
     * are {@code null} an YAPIONException is thrown.
     *
     * @param clazz the Class
     * @param serializationGetter the SerializationGetter
     * @param deserializationGetter the DeserializationGetter
     * @param <T> the Type of this Serializer
     * @return the Serializer that wraps the Interfaces
     */

    @SuppressWarnings({"java:S100"})
    public static <T extends List<?>> SerializerList<T> SerializerList(Class<T> clazz, SerializationGetter<T, YAPIONArray> serializationGetter, DeserializationGetter<T, YAPIONArray> deserializationGetter) {
        if (clazz == null) throw new YAPIONException();
        if (serializationGetter == null) throw new YAPIONException();
        if (deserializationGetter == null) throw new YAPIONException();
        return new SerializerList<T>() {
            @Override
            public Class<T> type() {
                return clazz;
            }

            @Override
            public YAPIONArray serialize(SerializeData<T> serializeData) {
                return serializationGetter.serialize(serializeData);
            }

            @Override
            public T deserialize(DeserializeData<YAPIONArray> deserializeData) {
                return deserializationGetter.deserialize(deserializeData);
            }
        };
    }

    /**
     * Create a {@link SerializerQueue} from two interfaces:
     * {@link SerializationGetter} and {@link DeserializationGetter}.
     * Those are all FunctionalInterfaces with just one
     * method to override. This makes the use off those
     * interfaces really easy. If any of the three arguments
     * are {@code null} an YAPIONException is thrown.
     *
     * @param clazz the Class
     * @param serializationGetter the SerializationGetter
     * @param deserializationGetter the DeserializationGetter
     * @param <T> the Type of this Serializer
     * @return the Serializer that wraps the Interfaces
     */
    @SuppressWarnings({"java:S100"})
    public static <T extends Queue<?>> SerializerQueue<T> SerializerQueue(Class<T> clazz, SerializationGetter<T, YAPIONArray> serializationGetter, DeserializationGetter<T, YAPIONArray> deserializationGetter) {
        if (clazz == null) throw new YAPIONException();
        if (serializationGetter == null) throw new YAPIONException();
        if (deserializationGetter == null) throw new YAPIONException();
        return new SerializerQueue<T>() {
            @Override
            public Class<T> type() {
                return clazz;
            }

            @Override
            public YAPIONArray serialize(SerializeData<T> serializeData) {
                return serializationGetter.serialize(serializeData);
            }

            @Override
            public T deserialize(DeserializeData<YAPIONArray> deserializeData) {
                return deserializationGetter.deserialize(deserializeData);
            }
        };
    }

    /**
     * Create a {@link SerializerSet} from two interfaces:
     * {@link SerializationGetter} and {@link DeserializationGetter}.
     * Those are all FunctionalInterfaces with just one
     * method to override. This makes the use off those
     * interfaces really easy. If any of the three arguments
     * are {@code null} an YAPIONException is thrown.
     *
     * @param clazz the Class
     * @param serializationGetter the SerializationGetter
     * @param deserializationGetter the DeserializationGetter
     * @param <T> the Type of this Serializer
     * @return the Serializer that wraps the Interfaces
     */
    @SuppressWarnings({"java:S100"})
    public static <T extends Set<?>> SerializerSet<T> SerializerSet(Class<T> clazz, SerializationGetter<T, YAPIONArray> serializationGetter, DeserializationGetter<T, YAPIONArray> deserializationGetter) {
        if (clazz == null) throw new YAPIONException();
        if (serializationGetter == null) throw new YAPIONException();
        if (deserializationGetter == null) throw new YAPIONException();
        return new SerializerSet<T>() {
            @Override
            public Class<T> type() {
                return clazz;
            }

            @Override
            public YAPIONArray serialize(SerializeData<T> serializeData) {
                return serializationGetter.serialize(serializeData);
            }

            @Override
            public T deserialize(DeserializeData<YAPIONArray> deserializeData) {
                return deserializationGetter.deserialize(deserializeData);
            }
        };
    }

    @FunctionalInterface
    @YAPIONSaveExclude(context = "*")
    @YAPIONLoadExclude(context = "*")
    public interface SerializationGetter<T, R extends YAPIONAnyType> {
        R serialize(SerializeData<T> serializeData);
    }

    @FunctionalInterface
    @YAPIONSaveExclude(context = "*")
    @YAPIONLoadExclude(context = "*")
    public interface DeserializationGetter<T, R extends YAPIONAnyType> {
        T deserialize(DeserializeData<R> deserializeData);
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

    /**
     * Create a {@link InstanceFactory} from one interface:
     * {@link InstanceGetter}
     * Which is a FunctionalInterface with just one method
     * to override. This makes the use off this interface
     * really easy. If any of the two arguments are
     * {@code null} an YAPIONException is thrown.
     *
     * @param clazz the Class
     * @param instanceGetter the InstanceGetter
     * @param <T> the Instance type
     * @return the InstanceFactory that wraps the Interface
     */
    public static <T> InstanceFactory<T> InstanceFactory(Class<T> clazz, InstanceGetter<T> instanceGetter) {
        if (clazz == null) throw new YAPIONException();
        if (instanceGetter == null) throw new YAPIONException();
        return new InstanceFactory<T>() {
            @Override
            public Class type() {
                return clazz;
            }

            @Override
            public T instance() {
                return instanceGetter.instance();
            }
        };
    }

    @FunctionalInterface
    @YAPIONSaveExclude(context = "*")
    @YAPIONLoadExclude(context = "*")
    public interface InstanceGetter<T> {
        T instance();
    }

    static Object getObjectInstance(Class<?> clazz, String type, ContextManager contextManager) {
        if (instanceFactoryMap.containsKey(clazz)) {
            return instanceFactoryMap.get(clazz).instance();
        }
        if (clazz.getDeclaredAnnotation(YAPIONObjenesis.class) != null) {
            return ReflectionsUtils.constructObjectObjenesis(type);
        } else {
            return ReflectionsUtils.constructObject(type, contextManager.is(clazz).data);
        }
    }

}