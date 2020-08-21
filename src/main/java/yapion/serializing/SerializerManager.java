package yapion.serializing;

import yapion.annotations.deserialize.YAPIONLoadExclude;
import yapion.annotations.serialize.YAPIONSaveExclude;
import yapion.hierarchy.YAPIONAny;
import yapion.hierarchy.YAPIONVariable;
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
public class SerializerManager {

    private SerializerManager() {
        throw new IllegalStateException("Utility class");
    }

    private static final Map<String, InternalSerializer<?>> serializerMap = new HashMap<>();
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
        ownSerializerMap = new HashMap<>();
    }

    private static void addPrivate(InternalSerializer<?> serializer) {
        if (ownSerializerMap != null) return;
        serializerMap.put(serializer.type(), serializer);
        if (serializer.primitiveType() != null && !serializer.primitiveType().isEmpty()) {
            serializerMap.put(serializer.primitiveType(), serializer);
        }
    }

    private static final Map<String, InternalSerializer<?>> ownSerializerMap;

    /**
     * Adds a special Serializer for a specific Object.
     *
     * @param serializer the special Serializer to add
     */
    public static <T> void add(Serializer<T> serializer) {
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
        ownSerializerMap.put(internalSerializer.type(), internalSerializer);
    }

    /**
     * Remove a special Serializer with the type name.
     *
     * @param type the typeName to remove
     */
    public static void remove(String type) {
        if (type == null) return;
        ownSerializerMap.remove(type);
    }

    /**
     * Remove a special Serializer with the Serializer.
     *
     * @param serializer the serializer to remove
     */
    public static void remove(Serializer<?> serializer) {
        if (serializer == null) return;
        if (serializer.type() == null) return;
        remove(serializer.type().getTypeName());
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
        return serializerMap.getOrDefault(type, ownSerializerMap.getOrDefault(type, null));
    }

}
