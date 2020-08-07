package yapion.serializing;

import test.Test;
import yapion.hierarchy.YAPIONAny;
import yapion.hierarchy.types.YAPIONObject;
import yapion.serializing.serializer.CharacterSerializer;
import yapion.serializing.serializer.StringSerializer;
import yapion.serializing.serializer.number.*;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
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
        if (serializer.primitiveType() == null) return;
        if (serializer.primitiveType().isEmpty()) return;
        serializerMap.put(serializer.primitiveType(), serializer);
    }

    private Test object;
    private StateManager stateManager;

    public static void main(String[] args) {
        serialize(new Test());
    }

    public static YAPIONObject serialize(Test object) {
        return serialize(object, "");
    }

    public static YAPIONObject serialize(Test object, String state) {
        return new YAPIONSerializer(object, state).parse().getYAPIONObject();
    }

    public YAPIONSerializer(Test object, String state) {
        stateManager = new StateManager(state);
        this.object = object;
    }

    public YAPIONSerializer parse() {
        Field[] fields = object.getClass().getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                if (Modifier.toString(field.getModifiers()).contains("static")) {
                    continue;
                }
                Serializer serializer = serializerMap.get(field.getType().getTypeName());
                if (serializer != null) {
                    YAPIONAny yapionAny = serializer.serialize(field.get(object));
                    System.out.println(yapionAny);
                } else {
                    System.out.println(field.getType().getTypeName() + "   " + field.get(object) + "   " + field.getName());
                }
            } catch (Exception e) {

            }
        }
        return this;
    }

    public YAPIONObject getYAPIONObject() {

        return null;
    }

}
