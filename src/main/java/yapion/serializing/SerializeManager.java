// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020 yoyosource

package yapion.serializing;

import org.atteo.classindex.ClassIndex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import yapion.annotations.deserialize.YAPIONLoadExclude;
import yapion.annotations.serialize.YAPIONSaveExclude;
import yapion.hierarchy.YAPIONAny;
import yapion.hierarchy.YAPIONVariable;
import yapion.hierarchy.types.YAPIONArray;
import yapion.hierarchy.types.YAPIONMap;
import yapion.hierarchy.types.YAPIONObject;
import yapion.hierarchy.types.YAPIONValue;
import yapion.serializing.api.*;
import yapion.serializing.serializer.SerializerImplementation;
import yapion.utils.ReflectionsUtils;

import java.util.*;

@YAPIONSaveExclude(context = "*")
@YAPIONLoadExclude(context = "*")
public class SerializeManager {

    private SerializeManager() {
        throw new IllegalStateException("Utility class");
    }

    public static final String TYPE_IDENTIFIER = "@type";
    public static final String ENUM_IDENTIFIER = "@enum";
    private static final Logger logger = LoggerFactory.getLogger(SerializeManager.class);

    @YAPIONSaveExclude(context = "*")
    @YAPIONLoadExclude(context = "*")
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

    private static final Map<String, Serializer> serializerMap = new HashMap<>();
    static {
        Iterable<Class<?>> clazzes = ClassIndex.getAnnotated(SerializerImplementation.class);
        clazzes.forEach(clazz -> {
            Object o = ReflectionsUtils.constructObjectObjenesis(clazz.getTypeName());
            if (o == null) return;
            if (!o.getClass().getTypeName().startsWith("yapion.serializing.serializer")) return;
            if (o.getClass().getInterfaces().length != 1) return;
            switch (o.getClass().getInterfaces()[0].getTypeName()) {
                case "yapion.serializing.InternalOverrideableSerializer":
                    add((InternalOverrideableSerializer<?>)o);
                    break;
                case "yapion.serializing.InternalSerializer":
                    add((InternalSerializer<?>)o);
                    break;
                default:
                    break;
            }
        });
        overrideable = true;
    }

    private static void add(InternalOverrideableSerializer<?> serializer) {
        if (serializerMap.containsKey(serializer.type()) && !serializerMap.get(serializer.type()).overrideable) return;
        serializerMap.put(serializer.type(), new Serializer(serializer, true));
        if (serializer.primitiveType() != null && !serializer.primitiveType().isEmpty()) {
            serializerMap.put(serializer.primitiveType(), new Serializer(serializer, true));
        }
    }

    private static void add(InternalSerializer<?> serializer) {
        if (serializerMap.containsKey(serializer.type()) && !serializerMap.get(serializer.type()).overrideable) return;
        serializerMap.put(serializer.type(), new Serializer(serializer, overrideable));
        if (serializer.primitiveType() != null && !serializer.primitiveType().isEmpty()) {
            serializerMap.put(serializer.primitiveType(), new Serializer(serializer, overrideable));
        }
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
            public YAPIONAny serialize(T object, YAPIONSerializer yapionSerializer) {
                try {
                    YAPIONObject yapionObject = serializer.serialize(object, yapionSerializer);
                    yapionObject.add(new YAPIONVariable(TYPE_IDENTIFIER, new YAPIONValue<>(type())));
                    return yapionObject;
                } catch (Exception e) {
                    logger.error("An unexpected error occurred", e.getCause());
                }
                return null;
            }

            @Override
            @SuppressWarnings({"java:S1905"})
            public T deserialize(YAPIONAny yapionAny, YAPIONDeserializer yapionDeserializer) {
                if (yapionAny instanceof YAPIONObject) {
                    try {
                        return serializer.deserialize((YAPIONObject) yapionAny, yapionDeserializer);
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
            public YAPIONAny serialize(T object, YAPIONSerializer yapionSerializer) {
                try {
                    return new YAPIONObject().add(TYPE_IDENTIFIER, new YAPIONValue<>(type())).add("map", serializer.serialize(object, yapionSerializer));
                } catch (Exception e) {
                    logger.error("An unexpected error occurred", e.getCause());
                }
                return null;
            }

            @Override
            @SuppressWarnings({"java:S1905"})
            public T deserialize(YAPIONAny yapionAny, YAPIONDeserializer yapionDeserializer) {
                try {
                    return serializer.deserialize(((YAPIONObject) yapionAny).getMap("map"), yapionDeserializer);
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
            public YAPIONAny serialize(T object, YAPIONSerializer yapionSerializer) {
                try {
                    return new YAPIONObject().add(TYPE_IDENTIFIER, new YAPIONValue<>(type())).add("list", serializer.serialize(object, yapionSerializer));
                } catch (Exception e) {
                    logger.error("An unexpected error occurred", e.getCause());
                }
                return null;
            }

            @Override
            @SuppressWarnings({"java:S1905"})
            public T deserialize(YAPIONAny yapionAny, YAPIONDeserializer yapionDeserializer) {
                try {
                    return serializer.deserialize(((YAPIONObject) yapionAny).getArray("list"), yapionDeserializer);
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
            public YAPIONAny serialize(T object, YAPIONSerializer yapionSerializer) {
                try {
                    return new YAPIONObject().add(TYPE_IDENTIFIER, new YAPIONValue<>(type())).add("queue", serializer.serialize(object, yapionSerializer));
                } catch (Exception e) {
                    logger.error("An unexpected error occurred", e.getCause());
                }
                return null;
            }

            @Override
            @SuppressWarnings({"java:S1905"})
            public T deserialize(YAPIONAny yapionAny, YAPIONDeserializer yapionDeserializer) {
                try {
                    return serializer.deserialize(((YAPIONObject) yapionAny).getArray("queue"), yapionDeserializer);
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
            public YAPIONAny serialize(T object, YAPIONSerializer yapionSerializer) {
                try {
                    return new YAPIONObject().add(TYPE_IDENTIFIER, new YAPIONValue<>(type())).add("set", serializer.serialize(object, yapionSerializer));
                } catch (Exception e) {
                    logger.error("An unexpected error occurred", e.getCause());
                }
                return null;
            }

            @Override
            @SuppressWarnings({"java:S1905"})
            public T deserialize(YAPIONAny yapionAny, YAPIONDeserializer yapionDeserializer) {
                try {
                    return serializer.deserialize(((YAPIONObject) yapionAny).getArray("set"), yapionDeserializer);
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
    static InternalSerializer<?> get(String type) {
        return serializerMap.getOrDefault(type, defaultSerializer).internalSerializer;
    }

    /**
     * Create a Object Serializer from three interfaces:
     * TypeGetter, SerializationGetter and
     * DeserializationGetter. Those are all
     * FunctionalInterfaces with just one method to override.
     * This makes the use off those interfaces really easy.
     * If any of the three arguments is {@code null} an
     * YAPIONException is thrown.
     *
     * @param typeGetter the TypeGetter
     * @param serializationGetter the SerializationGetter
     * @param deserializationGetter the DeserializationGetter
     * @param <T> the Type of this Serializer
     * @return the Serializer that wraps the Interfaces
     */
    @SuppressWarnings({"java:S100"})
    public static <T> SerializerObject<T> SerializerObject(TypeGetter<T> typeGetter, SerializationGetter<T, YAPIONObject> serializationGetter, DeserializationGetter<T, YAPIONObject> deserializationGetter) {
        return new SerializerObject<T>() {
            @Override
            public Class<T> type() {
                return typeGetter.type();
            }

            @Override
            public YAPIONObject serialize(T object, YAPIONSerializer yapionSerializer) {
                return serializationGetter.serialize(object, yapionSerializer);
            }

            @Override
            public T deserialize(YAPIONObject yapionObject, YAPIONDeserializer yapionDeserializer) {
                return deserializationGetter.deserialize(yapionObject, yapionDeserializer);
            }
        };
    }

    @SuppressWarnings({"java:S100"})
    public static <T> SerializerObject<T> SerializerObject(Class<T> clazz, SerializationGetter<T, YAPIONObject> serializationGetter, DeserializationGetter<T, YAPIONObject> deserializationGetter) {
        return SerializerObject(() -> clazz, serializationGetter, deserializationGetter);
    }

    /**
     * Create a Map Serializer from three interfaces:
     * TypeGetter, SerializationGetter and
     * DeserializationGetter. Those are all
     * FunctionalInterfaces with just one method to override.
     * This makes the use off those interfaces really easy.
     * If any of the three arguments is {@code null} an
     * YAPIONException is thrown.
     *
     * @param typeGetter the TypeGetter
     * @param serializationGetter the SerializationGetter
     * @param deserializationGetter the DeserializationGetter
     * @param <T> the Type of this Serializer
     * @return the Serializer that wraps the Interfaces
     */
    @SuppressWarnings({"java:S100"})
    public static <T extends Map<?, ?>> SerializerMap<T> SerializerMap(TypeGetter<T> typeGetter, SerializationGetter<T, YAPIONMap> serializationGetter, DeserializationGetter<T, YAPIONMap> deserializationGetter) {
        return new SerializerMap<T>() {
            @Override
            public Class<T> type() {
                return typeGetter.type();
            }

            @Override
            public YAPIONMap serialize(T object, YAPIONSerializer yapionSerializer) {
                return serializationGetter.serialize(object, yapionSerializer);
            }

            @Override
            public T deserialize(YAPIONMap yapionMap, YAPIONDeserializer yapionDeserializer) {
                return deserializationGetter.deserialize(yapionMap, yapionDeserializer);
            }
        };
    }

    @SuppressWarnings({"java:S100"})
    public static <T extends Map<?, ?>> SerializerMap<T> SerializerMap(Class<T> clazz, SerializationGetter<T, YAPIONMap> serializationGetter, DeserializationGetter<T, YAPIONMap> deserializationGetter) {
        return SerializerMap(() -> clazz, serializationGetter, deserializationGetter);
    }

    /**
     * Create a List Serializer from three interfaces:
     * TypeGetter, SerializationGetter and
     * DeserializationGetter. Those are all
     * FunctionalInterfaces with just one method to override.
     * This makes the use off those interfaces really easy.
     * If any of the three arguments is {@code null} an
     * YAPIONException is thrown.
     *
     * @param typeGetter the TypeGetter
     * @param serializationGetter the SerializationGetter
     * @param deserializationGetter the DeserializationGetter
     * @param <T> the Type of this Serializer
     * @return the Serializer that wraps the Interfaces
     */
    @SuppressWarnings({"java:S100"})
    public static <T extends List<?>> SerializerList<T> SerializerList(TypeGetter<T> typeGetter, SerializationGetter<T, YAPIONArray> serializationGetter, DeserializationGetter<T, YAPIONArray> deserializationGetter) {
        return new SerializerList<T>() {
            @Override
            public Class<T> type() {
                return typeGetter.type();
            }

            @Override
            public YAPIONArray serialize(T object, YAPIONSerializer yapionSerializer) {
                return serializationGetter.serialize(object, yapionSerializer);
            }

            @Override
            public T deserialize(YAPIONArray yapionArray, YAPIONDeserializer yapionDeserializer) {
                return deserializationGetter.deserialize(yapionArray, yapionDeserializer);
            }
        };
    }

    @SuppressWarnings({"java:S100"})
    public static <T extends List<?>> SerializerList<T> SerializerList(Class<T> clazz, SerializationGetter<T, YAPIONArray> serializationGetter, DeserializationGetter<T, YAPIONArray> deserializationGetter) {
        return SerializerList(() -> clazz, serializationGetter, deserializationGetter);
    }

    /**
     * Create a Queue Serializer from three interfaces:
     * TypeGetter, SerializationGetter and
     * DeserializationGetter. Those are all
     * FunctionalInterfaces with just one method to override.
     * This makes the use off those interfaces really easy.
     * If any of the three arguments is {@code null} an
     * YAPIONException is thrown.
     *
     * @param typeGetter the TypeGetter
     * @param serializationGetter the SerializationGetter
     * @param deserializationGetter the DeserializationGetter
     * @param <T> the Type of this Serializer
     * @return the Serializer that wraps the Interfaces
     */
    @SuppressWarnings({"java:S100"})
    public static <T extends Queue<?>> SerializerQueue<T> SerializerQueue(TypeGetter<T> typeGetter, SerializationGetter<T, YAPIONArray> serializationGetter, DeserializationGetter<T, YAPIONArray> deserializationGetter) {
        return new SerializerQueue<T>() {
            @Override
            public Class<T> type() {
                return typeGetter.type();
            }

            @Override
            public YAPIONArray serialize(T object, YAPIONSerializer yapionSerializer) {
                return serializationGetter.serialize(object, yapionSerializer);
            }

            @Override
            public T deserialize(YAPIONArray yapionArray, YAPIONDeserializer yapionDeserializer) {
                return deserializationGetter.deserialize(yapionArray, yapionDeserializer);
            }
        };
    }

    @SuppressWarnings({"java:S100"})
    public static <T extends Queue<?>> SerializerQueue<T> SerializerQueue(Class<T> clazz, SerializationGetter<T, YAPIONArray> serializationGetter, DeserializationGetter<T, YAPIONArray> deserializationGetter) {
        return SerializerQueue(() -> clazz, serializationGetter, deserializationGetter);
    }

    /**
     * Create a Set Serializer from three interfaces:
     * TypeGetter, SerializationGetter and
     * DeserializationGetter. Those are all
     * FunctionalInterfaces with just one method to override.
     * This makes the use off those interfaces really easy.
     * If any of the three arguments is {@code null} an
     * YAPIONException is thrown.
     *
     * @param typeGetter the TypeGetter
     * @param serializationGetter the SerializationGetter
     * @param deserializationGetter the DeserializationGetter
     * @param <T> the Type of this Serializer
     * @return the Serializer that wraps the Interfaces
     */
    @SuppressWarnings({"java:S100"})
    public static <T extends Set<?>> SerializerSet<T> SerializerSet(TypeGetter<T> typeGetter, SerializationGetter<T, YAPIONArray> serializationGetter, DeserializationGetter<T, YAPIONArray> deserializationGetter) {
        return new SerializerSet<T>() {
            @Override
            public Class<T> type() {
                return typeGetter.type();
            }

            @Override
            public YAPIONArray serialize(T object, YAPIONSerializer yapionSerializer) {
                return serializationGetter.serialize(object, yapionSerializer);
            }

            @Override
            public T deserialize(YAPIONArray yapionArray, YAPIONDeserializer yapionDeserializer) {
                return deserializationGetter.deserialize(yapionArray, yapionDeserializer);
            }
        };
    }

    @SuppressWarnings({"java:S100"})
    public static <T extends Set<?>> SerializerSet<T> SerializerSet(Class<T> clazz, SerializationGetter<T, YAPIONArray> serializationGetter, DeserializationGetter<T, YAPIONArray> deserializationGetter) {
        return SerializerSet(() -> clazz, serializationGetter, deserializationGetter);
    }

    @FunctionalInterface
    @YAPIONSaveExclude(context = "*")
    @YAPIONLoadExclude(context = "*")
    public interface TypeGetter<T> {
        Class<T> type();
    }

    @FunctionalInterface
    @YAPIONSaveExclude(context = "*")
    @YAPIONLoadExclude(context = "*")
    public interface SerializationGetter<T, R> {
        R serialize(T object, YAPIONSerializer yapionSerializer);
    }

    @FunctionalInterface
    @YAPIONSaveExclude(context = "*")
    @YAPIONLoadExclude(context = "*")
    public interface DeserializationGetter<T, R> {
        T deserialize(R object, YAPIONDeserializer yapionDeserializer);
    }

}