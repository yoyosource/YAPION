package yapion.serializing;

import yapion.annotations.YAPIONLoadExclude;
import yapion.annotations.YAPIONSaveExclude;
import yapion.hierarchy.YAPIONAny;
import yapion.hierarchy.types.YAPIONObject;
import yapion.hierarchy.types.YAPIONPointer;
import yapion.serializing.serializer.number.*;
import yapion.serializing.serializer.object.*;
import yapion.serializing.serializer.other.*;

import java.util.HashMap;
import java.util.Map;

@YAPIONSaveExclude(context = "*")
@YAPIONLoadExclude(context = "*")
public class YAPIONDeserializer {

    private static final Map<String, Serializer<?>> serializerMap = new HashMap<>();
    static {
        // Other
        add(new StringSerializer());
        add(new StringBuilderSerializer());
        add(new StringBufferSerializer());
        add(new CharacterSerializer());
        add(new FileSerializer());

        // Non Floating Point Numbers
        add(new ByteSerializer());
        add(new ShortSerializer());
        add(new IntegerSerializer());
        add(new LongSerializer());
        add(new BigIntegerSerializer());

        // Floating Point Numbers
        add(new FloatSerializer());
        add(new DoubleSerializer());
        add(new BigDecimalSerializer());

        // Objects
        add(new ListSerializer());
        add(new ListSerializerArray());
        add(new ListSerializerLinked());

        add(new MapSerializer());
        add(new MapSerializerHash());
        add(new MapSerializerLinkedHash());
        add(new MapSerializerTree());

        add(new SetSerializer());
        add(new SetSerializerHash());
        add(new SetSerializerLinkedHash());
        add(new SetSerializerSorted());
    }

    private static void add(Serializer<?> serializer) {
        serializerMap.put(serializer.type(), serializer);
        if (serializer.primitiveType() != null && !serializer.primitiveType().isEmpty()) {
            serializerMap.put(serializer.primitiveType(), serializer);
        }
        if (serializer.otherTypes() != null && serializer.otherTypes().length != 0) {
            for (String s : serializer.otherTypes()) {
                if (s == null) continue;
                if (s.isEmpty()) continue;
                if (s.trim().isEmpty()) continue;
                serializerMap.put(s, serializer);
            }
        }
    }

    private final Object object;
    private YAPIONObject yapionObject;
    private final StateManager stateManager;

    private Map<Object, YAPIONPointer> pointerMap = new HashMap<>();

    public static void main(String[] args) {

    }

    public static Object deserialize(YAPIONAny yapionAny) {
        return deserialize(yapionAny, "");
    }

    public static Object deserialize(YAPIONAny yapionAny, String state) {
        return new YAPIONSerializer(yapionAny, state).parse().getYAPIONObject();
    }

    public YAPIONDeserializer(YAPIONAny yapionAny, String state) {
        stateManager = new StateManager(state);
        this.object = yapionAny;
    }

    private YAPIONDeserializer(YAPIONAny yapionAny, YAPIONDeserializer yapionDeserializer) {
        this.object = yapionAny;
        this.stateManager = yapionDeserializer.stateManager;
        this.pointerMap = yapionDeserializer.pointerMap;
    }

}
