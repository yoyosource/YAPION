// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020 yoyosource

package yapion.serializing;

import test.Test;
import yapion.annotations.deserialize.YAPIONDeserializeType;
import yapion.annotations.deserialize.YAPIONLoadExclude;
import yapion.annotations.serialize.YAPIONSaveExclude;
import yapion.exceptions.utils.YAPIONReflectionException;
import yapion.hierarchy.YAPIONAny;
import yapion.hierarchy.types.YAPIONObject;
import yapion.hierarchy.types.YAPIONPointer;
import yapion.hierarchy.types.YAPIONValue;
import yapion.serializing.serializer.number.*;
import yapion.serializing.serializer.object.*;
import yapion.serializing.serializer.other.*;
import yapion.utils.ModifierUtils;
import yapion.utils.ReflectionsUtils;
import yapion.utils.YAPIONLogger;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static yapion.serializing.YAPIONSerializer.serialize;

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
    }

    private static void add(Serializer<?> serializer) {
        serializerMap.put(serializer.type(), serializer);
        if (serializer.primitiveType() != null && !serializer.primitiveType().isEmpty()) {
            serializerMap.put(serializer.primitiveType(), serializer);
        }
    }

    private Object object;
    private final YAPIONObject yapionObject;
    private final StateManager stateManager;

    private Map<YAPIONObject, Object> pointerMap = new HashMap<>();

    public static void main(String[] args) {
        YAPIONObject yapionObject = serialize(new Test());
        Object o = deserialize(yapionObject);
        System.out.println(o);
        System.out.println(yapionObject);
        System.out.println(serialize(o));
    }

    /**
     * Serialize an YAPION Object to an Object.
     *
     * @param yapionObject to deserialize
     * @return Object from the YAPIONObject to deserialize
     */
    public static Object deserialize(YAPIONObject yapionObject) {
        return deserialize(yapionObject, "");
    }

    /**
     * Serialize an YAPION Object to an Object.
     *
     * @param yapionObject to deserialize
     * @param state the state for deserialization
     * @return Object from the YAPIONObject to deserialize
     */
    public static Object deserialize(YAPIONObject yapionObject, String state) {
        return new YAPIONDeserializer(yapionObject, state).parse().getObject();
    }

    /**
     * Creates a YAPIONDeserializer for deserializing a YAPIONObject with a specified state.
     *
     * @param yapionObject to deserialize
     * @param state the state for deserialization
     */
    public YAPIONDeserializer(YAPIONObject yapionObject, String state) {
        stateManager = new StateManager(state);
        this.yapionObject = yapionObject;
    }

    private YAPIONDeserializer(YAPIONObject yapionObject, YAPIONDeserializer yapionDeserializer) {
        this.yapionObject = yapionObject;
        this.stateManager = yapionDeserializer.stateManager;
        this.pointerMap = yapionDeserializer.pointerMap;
    }

    @SuppressWarnings({"java:S3740"})
    public Object parse(YAPIONAny yapionAny, YAPIONDeserializer yapionDeserializer, Field field) {
        if (yapionAny instanceof YAPIONPointer) {
            Optional<Object> objectOptional = ReflectionsUtils.invokeMethod("getYAPIONObject", yapionAny);
            if (!objectOptional.isPresent()) {
                return null;
            }
            System.out.println("-> " + objectOptional.get() + "   " + pointerMap.get(objectOptional.get()));
            return pointerMap.get(objectOptional.get());
        }
        if (yapionAny instanceof YAPIONValue) {
            Object o = ((YAPIONValue) yapionAny).get();
            switch (field.getType().getTypeName()) {
                case "java.lang.StringBuilder":
                    return new StringBuilder().append(o);
                case "java.lang.StringBuffer":
                    return new StringBuffer().append(o);
                default:
                    return o;
            }
        }
        if (yapionAny instanceof YAPIONObject) {
            return new YAPIONDeserializer((YAPIONObject) yapionAny, yapionDeserializer).parse().getObject();
        } else {
            String type = getSerializerType(field);
            Serializer serializer = serializerMap.get(type);
            System.out.println(type + " " + serializer + " " + yapionAny);
            return getSerializer(type, yapionAny, field);
        }
    }

    private String getSerializerType(Field field) {
        YAPIONDeserializeType yapionDeserializeType = field.getDeclaredAnnotation(YAPIONDeserializeType.class);
        String type = field.getType().getTypeName();
        if (yapionDeserializeType != null) {
            type = yapionDeserializeType.type().getTypeName();
        }
        return type;
    }

    @SuppressWarnings({"java:S3740"})
    private Object getSerializer(String type, YAPIONAny yapionAny, Field field) {
        Serializer serializer = serializerMap.get(type);
        if (serializer != null && yapionAny != null) {
            return serializer.deserialize(yapionAny, this, field);
        } else {
            return null;
        }
    }

    @SuppressWarnings({"java:S3740", "java:S3011", "java:S1117", "unchecked"})
    private YAPIONDeserializer parse(YAPIONObject yapionObject) {
        String type = ((YAPIONValue<String>)yapionObject.getVariable("@type").getValue()).get();
        try {
            if (!stateManager.is(Class.forName(type)).load) {
                return this;
            }

            object = ReflectionsUtils.constructObject(type);
            pointerMap.put(yapionObject, object);

            Field[] fields = object.getClass().getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                if (ModifierUtils.removed(field)) {
                    continue;
                }
                if (!stateManager.is(object, field).load) continue;
                if (yapionObject.getVariable(field.getName()) == null) continue;

                YAPIONAny yapionAny = yapionObject.getVariable(field.getName()).getValue();
                field.set(object, parse(yapionAny, this, field));
            }
        } catch (ClassNotFoundException e) {
            YAPIONLogger.trace(YAPIONLogger.LoggingType.DESERIALIZER, "The class '" + type + "' was not found.", e.getCause());
        } catch (IllegalAccessException e) {
            YAPIONLogger.trace(YAPIONLogger.LoggingType.DESERIALIZER, "", e.getCause());
        } catch (YAPIONReflectionException e) {
            YAPIONLogger.trace(YAPIONLogger.LoggingType.DESERIALIZER, "Exception while creating an Instance of the object '" + type + "'", e.getCause());
        }
        return this;
    }

    /**
     * Parses the YAPIONObject to the Object.
     */
    public YAPIONDeserializer parse() {
        return parse(yapionObject);
    }

    /**
     * Get the internal parsed Object.
     *
     * @return Object from the YAPIONObject to deserialize
     */
    public Object getObject() {
        return object;
    }

}