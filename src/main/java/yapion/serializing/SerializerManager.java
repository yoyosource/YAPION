package yapion.serializing;

import yapion.annotations.deserialize.YAPIONLoadExclude;
import yapion.annotations.serialize.YAPIONSaveExclude;
import yapion.serializing.serializer.number.*;
import yapion.serializing.serializer.object.*;
import yapion.serializing.serializer.other.*;

import java.util.HashMap;
import java.util.Map;

@YAPIONSaveExclude(context = "*")
@YAPIONLoadExclude(context = "*")
public class SerializerManager {

    private SerializerManager() {
        throw new IllegalStateException("Utility class");
    }

    private static final Map<String, Serializer<?>> serializerMap = new HashMap<>();
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
        addPrivate(new ListSerializer());
        addPrivate(new ListSerializerArray());
        addPrivate(new ListSerializerLinked());

        addPrivate(new MapSerializer());
        addPrivate(new MapSerializerHash());
        addPrivate(new MapSerializerLinkedHash());
        addPrivate(new MapSerializerTree());

        addPrivate(new SetSerializer());
        addPrivate(new SetSerializerHash());
        addPrivate(new SetSerializerLinkedHash());

        // Own Serializers Init
        ownSerializerMap = new HashMap<>();
    }

    private static void addPrivate(Serializer<?> serializer) {
        if (ownSerializerMap != null) return;
        serializerMap.put(serializer.type(), serializer);
        if (serializer.primitiveType() != null && !serializer.primitiveType().isEmpty()) {
            serializerMap.put(serializer.primitiveType(), serializer);
        }
    }

    private static final Map<String, Serializer<?>> ownSerializerMap;

    /**
     * Adds a special Serializer for a specific Object.
     *
     * @param serializer the special Serializer to add
     */
    public static void add(Serializer<?> serializer) {
        if (serializer == null) return;
        if (serializer.type() == null) return;
        ownSerializerMap.put(serializer.type(), serializer);
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
        remove(serializer.type());
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
    static Serializer<?> get(String type) {
        return serializerMap.getOrDefault(type, ownSerializerMap.getOrDefault(type, null));
    }

}
