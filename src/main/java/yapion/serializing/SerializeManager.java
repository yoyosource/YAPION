package yapion.serializing;

import yapion.annotations.deserialize.YAPIONLoadExclude;
import yapion.annotations.serialize.YAPIONSaveExclude;
import yapion.hierarchy.YAPIONAny;
import yapion.hierarchy.YAPIONVariable;
import yapion.hierarchy.types.YAPIONArray;
import yapion.hierarchy.types.YAPIONMap;
import yapion.hierarchy.types.YAPIONObject;
import yapion.hierarchy.types.YAPIONValue;
import yapion.serializing.serializer.number.*;
import yapion.serializing.serializer.object.*;
import yapion.serializing.serializer.other.*;
import yapion.utils.YAPIONLogger;

import java.util.HashMap;
import java.util.Map;

@YAPIONSaveExclude(context = "*")
@YAPIONLoadExclude(context = "*")
public class SerializeManager {

    private SerializeManager() {
        throw new IllegalStateException("Utility class");
    }

    @YAPIONSaveExclude(context = "*")
    @YAPIONLoadExclude(context = "*")
    private static class Serializer {
        private final InternalSerializer<?> internalSerializer;
        private final boolean overrideable;

        public Serializer(InternalSerializer<?> internalSerializer, boolean overrideable) {
            this.internalSerializer = internalSerializer;
            this.overrideable = overrideable;
        }
    }

    private static boolean overrideable = false;
    private static final Serializer defaultSerializer = new Serializer(null, false);

    private static final Map<String, Serializer> serializerMap = new HashMap<>();
    static {
        // Other
        addPrivate(new StringSerializer());
        addPrivate(new StringBuilderSerializer());
        addPrivate(new StringBufferSerializer());
        addPrivate(new CharacterSerializer());
        addPrivate(new FileSerializer());

        // Non Floating Point Numbers
        addPrivate(new ByteSerializer());
        addPrivate(new ShortSerializer());
        addPrivate(new IntegerSerializer());
        addPrivate(new LongSerializer());
        addPrivate(new BigIntegerSerializer());

        // Floating Point Numbers
        addPrivate(new FloatSerializer());
        addPrivate(new DoubleSerializer());
        addPrivate(new BigDecimalSerializer());

        // Objects
        addPrivate(new DequeSerializer());
        addPrivate(new DequeSerializerArray());

        addPrivate(new ListSerializer());
        addPrivate(new ListSerializerArray());
        addPrivate(new ListSerializerLinked());

        addPrivate(new MapSerializer());
        addPrivate(new MapSerializerIdentityHash());
        addPrivate(new MapSerializerHash());
        addPrivate(new MapSerializerLinkedHash());
        addPrivate(new MapSerializerTree());
        addPrivate(new MapSerializerWeakHash());

        addPrivate(new QueueSerializer());
        addPrivate(new QueueSerializerPriority());

        addPrivate(new SetSerializer());
        addPrivate(new SetSerializerHash());
        addPrivate(new SetSerializerLinkedHash());
        addPrivate(new SetSerializerTree());

        // Other Objects
        addPrivate(new StackSerializer());
        addPrivate(new TableSerializerHash());
        addPrivate(new VectorSerializer());

        // Own Serializers Init
        overrideable = true;
    }

    private static void addPrivate(InternalSerializer<?> serializer) {
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
    public static <T> void add(SerializerObject<T> serializer) {
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
                    yapionObject.add(new YAPIONVariable("@type", new YAPIONValue<>(type())));
                    return yapionObject;
                } catch (Exception e) {
                    YAPIONLogger.error(YAPIONLogger.LoggingType.SERIALIZER, "An unexpected error occurred", e.getCause());
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
                        YAPIONLogger.error(YAPIONLogger.LoggingType.DESERIALIZER, "An unexpected error occurred", e.getCause());
                    }
                }
                return null;
            }
        };
        addPrivate(internalSerializer);
    }

    /**
     * Adds a special Serializer for a specific Map.
     *
     * @param serializer the special Serializer to add
     */
    public static <T> void add(SerializerMap<T> serializer) {
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
                    YAPIONObject yapionObject = new YAPIONObject();
                    YAPIONMap yapionMap = serializer.serialize(object, yapionSerializer);
                    yapionObject.add(new YAPIONVariable("@type", new YAPIONValue<>(type())));
                    yapionObject.add(new YAPIONVariable("map", yapionMap));
                    return yapionObject;
                } catch (Exception e) {
                    YAPIONLogger.error(YAPIONLogger.LoggingType.SERIALIZER, "An unexpected error occurred", e.getCause());
                }
                return null;
            }

            @Override
            @SuppressWarnings({"java:S1905"})
            public T deserialize(YAPIONAny yapionAny, YAPIONDeserializer yapionDeserializer) {
                try {
                    return serializer.deserialize(((YAPIONObject) yapionAny).getMap("map"), yapionDeserializer);
                } catch (Exception e) {
                    YAPIONLogger.error(YAPIONLogger.LoggingType.DESERIALIZER, "An unexpected error occurred", e.getCause());
                }
                return null;
            }
        };
        addPrivate(internalSerializer);
    }

    /**
     * Adds a special Serializer for a specific Array.
     *
     * @param serializer the special Serializer to add
     */
    public static <T> void add(SerializerArray<T> serializer) {
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
                    YAPIONObject yapionObject = new YAPIONObject();
                    YAPIONArray yapionArray = serializer.serialize(object, yapionSerializer);
                    yapionObject.add(new YAPIONVariable("@type", new YAPIONValue<>(type())));
                    yapionObject.add(new YAPIONVariable("array", yapionArray));
                    return yapionObject;
                } catch (Exception e) {
                    YAPIONLogger.error(YAPIONLogger.LoggingType.SERIALIZER, "An unexpected error occurred", e.getCause());
                }
                return null;
            }

            @Override
            @SuppressWarnings({"java:S1905"})
            public T deserialize(YAPIONAny yapionAny, YAPIONDeserializer yapionDeserializer) {
                try {
                    return serializer.deserialize(((YAPIONObject) yapionAny).getArray("array"), yapionDeserializer);
                } catch (Exception e) {
                    YAPIONLogger.error(YAPIONLogger.LoggingType.DESERIALIZER, "An unexpected error occurred", e.getCause());
                }
                return null;
            }
        };
        addPrivate(internalSerializer);
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

    public static <T> SerializerObject<T> SerializerObject(TypeGetter<T> typeGetter, SerializationGetter<T, YAPIONObject> serializationGetter, DeserializationGetter<T, YAPIONObject> deserializationGetter) {
        return new SerializerObject<T>() {
            @Override
            public Class<T> type() {
                return typeGetter.type();
            }

            @Override
            public YAPIONObject serialize(T object, YAPIONSerializer yapionSerializer) {
                YAPIONObject yapionObject = serializationGetter.serialize(object, yapionSerializer);
                yapionObject.add(new YAPIONVariable("@type", new YAPIONValue<>(type().getTypeName())));
                return yapionObject;
            }

            @Override
            public T deserialize(YAPIONObject yapionObject, YAPIONDeserializer yapionDeserializer) {
                return deserializationGetter.deserialize(yapionObject, yapionDeserializer);
            }
        };
    }

    public static <T> SerializerMap<T> SerializerMap(TypeGetter<T> typeGetter, SerializationGetter<T, YAPIONMap> serializationGetter, DeserializationGetter<T, YAPIONMap> deserializationGetter) {
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

    public static <T> SerializerArray<T> SerializerArray(TypeGetter<T> typeGetter, SerializationGetter<T, YAPIONArray> serializationGetter, DeserializationGetter<T, YAPIONArray> deserializationGetter) {
        return new SerializerArray<T>() {
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

    @FunctionalInterface
    public interface TypeGetter<T> {
        Class<T> type();
    }

    @FunctionalInterface
    public interface SerializationGetter<T, R> {
        R serialize(T object, YAPIONSerializer yapionSerializer);
    }

    @FunctionalInterface
    public interface DeserializationGetter<T, R> {
        T deserialize(R object, YAPIONDeserializer yapionDeserializer);
    }

}
