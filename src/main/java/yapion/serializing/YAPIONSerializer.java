package yapion.serializing;

import test.Test;
import yapion.hierarchy.types.YAPIONObject;
import yapion.serializing.serializer.CharacterSerializer;
import yapion.serializing.serializer.StringSerializer;
import yapion.serializing.serializer.number.*;

import java.util.HashMap;
import java.util.Map;

public class YAPIONSerializer {

    private static Map<String, Serializer<?>> serializerMap = new HashMap<>();
    static {
        add(new StringSerializer());
        add(new CharacterSerializer());

        add(new ByteSerializer());
        add(new ShortSerializer());
        add(new IntegerSerializer());
        add(new LongSerializer());
        add(new BigIntegerSerializer());

        add(new FloatSerializer());
        add(new DoubleSerializer());
        add(new BigDecimalSerializer());
    }

    private static void add(Serializer<?> serializer) {
        serializerMap.put(serializer.type(), serializer);
        if (!serializer.primitiveType().isEmpty()) {
            serializerMap.put(serializer.primitiveType(), serializer);
        }
    }

    private Object object;
    private StateManager stateManager;

    public static void main(String[] args) {
        serialize(new Test());
    }

    public static YAPIONObject serialize(Object object) {
        return serialize(object, "");
    }

    public static YAPIONObject serialize(Object object, String state) {
        return new YAPIONSerializer(object, state).parse().getYAPIONObject();
    }

    public YAPIONSerializer(Object object, String state) {
        stateManager = new StateManager(state);
        this.object = object;
    }

    public YAPIONSerializer parse() {

        return this;
    }

    public YAPIONObject getYAPIONObject() {

        return null;
    }

}
