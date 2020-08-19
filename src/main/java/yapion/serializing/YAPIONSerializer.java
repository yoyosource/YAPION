// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020 yoyosource

package yapion.serializing;

import test.Test;
import yapion.annotations.deserialize.YAPIONLoadExclude;
import yapion.annotations.serialize.YAPIONSaveExclude;
import yapion.hierarchy.Type;
import yapion.hierarchy.YAPIONAny;
import yapion.hierarchy.YAPIONVariable;
import yapion.hierarchy.types.YAPIONArray;
import yapion.hierarchy.types.YAPIONObject;
import yapion.hierarchy.types.YAPIONPointer;
import yapion.hierarchy.types.YAPIONValue;
import yapion.serializing.serializer.number.*;
import yapion.serializing.serializer.object.*;
import yapion.serializing.serializer.other.*;
import yapion.utils.ModifierUtils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

@YAPIONSaveExclude(context = "*")
@YAPIONLoadExclude(context = "*")
public class YAPIONSerializer {

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

    private final Object object;
    private YAPIONObject yapionObject;
    private final StateManager stateManager;

    private Map<Object, YAPIONPointer> pointerMap = new HashMap<>();

    public static void main(String[] args) {
        YAPIONObject yapionObject = serialize(new Test());
        System.out.println(yapionObject);
        YAPIONObject yapionObject1 = serialize(yapionObject);
        System.out.println(yapionObject1);
        YAPIONObject yapionObject2 = serialize(yapionObject1);
        System.out.println(yapionObject2);
        YAPIONObject yapionObject3 = serialize(yapionObject2);
        System.out.println(yapionObject3);
    }

    private static void multiTest() {
        Test test = new Test();
        YAPIONObject yapionObject = null;
        long globalTime = System.currentTimeMillis();
        for (int i = 0; i < 100; i++) {
            long time = System.currentTimeMillis();
            yapionObject = serialize(test);
            time = System.currentTimeMillis() - time;
            System.out.println(time + "ms");
        }
        System.out.println(System.currentTimeMillis() - globalTime);
        System.out.println(yapionObject);
    }

    /**
     * Serialize an Object to an YAPION Object.
     *
     * @param object to serialize
     * @return YAPIONObject from the object to serialize
     */
    public static YAPIONObject serialize(Object object) {
        return serialize(object, "");
    }

    /**
     * Serialize an Object to an YAPION Object.
     *
     * @param object to serialize
     * @param state the state for serialization
     * @return YAPIONObject from the object to serialize
     */
    public static YAPIONObject serialize(Object object, String state) {
        return new YAPIONSerializer(object, state).parse().getYAPIONObject();
    }

    /**
     * Creates a YAPIONSerializer for serializing an Object with a specified state.
     *
     * @param object to serialize
     * @param state the state for serialization
     */
    public YAPIONSerializer(Object object, String state) {
        stateManager = new StateManager(state);
        this.object = object;
    }

    private YAPIONSerializer(Object object, YAPIONSerializer yapionSerializer) {
        this.object = object;
        this.stateManager = yapionSerializer.stateManager;
        this.pointerMap = yapionSerializer.pointerMap;
    }

    @SuppressWarnings({"java:S3740"})
    public YAPIONAny parse(Object object, YAPIONSerializer yapionSerializer) {
        if (pointerMap.containsKey(object)) {
            return pointerMap.get(object);
        }
        if (object == null) {
            return new YAPIONValue<>(null);
        }
        if (object.getClass().getTypeName().endsWith("[]")) {
            YAPIONArray yapionArray = new YAPIONArray();
            Object[] objects = (Object[])object;
            for (Object o : objects) {
                yapionArray.add(parse(o, this));
            }
            return yapionArray;
        } else {
            Serializer serializer = serializerMap.get(object.getClass().getTypeName());
            if (serializer != null) {
                return serializer.serialize(object, this);
            } else {
                return new YAPIONSerializer(object, yapionSerializer).parse().getYAPIONObject();
            }
        }
    }

    @SuppressWarnings({"java:S3740", "java:S3011", "java:S1117", "unchecked"})
    private YAPIONSerializer parse(Object object) {
        if (!stateManager.is(object).save) {
            return this;
        }
        if (object.getClass().getSimpleName().contains("$")) {
            return this;
        }
        YAPIONObject yapionObject = new YAPIONObject();
        if (!pointerMap.containsKey(object)) {
            pointerMap.put(object, new YAPIONPointer(yapionObject));
        }
        yapionObject.add(new YAPIONVariable("@type", new YAPIONValue<>(object.getClass().getTypeName())));
        this.yapionObject = yapionObject;

        Field[] fields = object.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            if (ModifierUtils.removed(field)) {
                continue;
            }
            StateManager.YAPIONInfo yapionInfo = stateManager.is(object, field);
            if (!yapionInfo.save) continue;

            String name = field.getName();
            Object fieldObject = null;
            try {
                fieldObject = field.get(object);
            } catch (Exception e) {

            }
            if (fieldObject == null) {
                if (!yapionInfo.optimize) {
                    yapionObject.add(new YAPIONVariable(name, new YAPIONValue<>(null)));
                }
                continue;
            }

            if (pointerMap.containsKey(fieldObject)) {
                yapionObject.add(new YAPIONVariable(name, pointerMap.get(fieldObject)));
                continue;
            }

            Serializer serializer = serializerMap.get(fieldObject.getClass().getTypeName());
            if (serializer != null) {
                yapionObject.add(new YAPIONVariable(name, serializer.serialize(fieldObject, this)));
                continue;
            }
            YAPIONAny yapionAny = parse(fieldObject, this);
            if (yapionAny == null) {
                continue;
            }
            if (yapionAny.getType() == Type.OBJECT) {
                pointerMap.put(fieldObject, new YAPIONPointer((YAPIONObject) yapionAny));
            }
            yapionObject.add(new YAPIONVariable(name, yapionAny));
        }
        return this;
    }

    /**
     * Parses the Object to the YAPIONObject.
     */
    public YAPIONSerializer parse() {
        return parse(object);
    }

    /**
     * Get the internal parsed YAPIONObject.
     *
     * @return YAPIONObject from the object to serialize
     */
    public YAPIONObject getYAPIONObject() {
        return yapionObject;
    }

}