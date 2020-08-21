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
import yapion.hierarchy.types.*;
import yapion.serializing.serializer.number.*;
import yapion.serializing.serializer.object.*;
import yapion.serializing.serializer.other.*;
import yapion.utils.ModifierUtils;
import yapion.utils.ReflectionsUtils;
import yapion.utils.YAPIONLogger;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static yapion.serializing.YAPIONSerializer.serialize;

@YAPIONSaveExclude(context = "*")
@YAPIONLoadExclude(context = "*")
public class YAPIONDeserializer {

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
        System.out.println(yapionObject.toString().equals(serialize(o).toString()));
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
    public Object parse(YAPIONAny yapionAny, YAPIONDeserializer yapionDeserializer) {
        if (yapionAny instanceof YAPIONPointer) {
            Optional<Object> objectOptional = ReflectionsUtils.invokeMethod("getYAPIONObject", yapionAny);
            if (!objectOptional.isPresent()) {
                return null;
            }
            Object object = objectOptional.get();
            for (Map.Entry<YAPIONObject, Object> entry : pointerMap.entrySet()) {
                if (entry.getKey() == object) {
                    return entry.getValue();
                }
            }
            return null;
        }
        if (yapionAny instanceof YAPIONValue) {
            return ((YAPIONValue) yapionAny).get();
        }
        if (yapionAny instanceof YAPIONObject) {
            return new YAPIONDeserializer((YAPIONObject) yapionAny, yapionDeserializer).parse().getObject();
        }
        if (yapionAny instanceof YAPIONArray) {
            return parseArray((YAPIONArray) yapionAny);
        }
        return null;
    }

    private Object[] parseArray(YAPIONArray yapionArray) {
        int length = yapionArray.length();
        Object[] objects = new Object[length];
        for (int i = 0; i < length; i++) {
            YAPIONAny yapionAny = yapionArray.get(i);
            if (yapionAny instanceof YAPIONArray) {
                objects[i] = parseArray((YAPIONArray) yapionAny);
            } else {
                objects[i] = null;
            }
        }
        System.out.println(Arrays.toString(objects));
        return objects;
    }

    @SuppressWarnings({"java:S3740", "java:S3011", "java:S1117", "unchecked"})
    private YAPIONDeserializer parse(YAPIONObject yapionObject) {
        String type = ((YAPIONValue<String>)yapionObject.getVariable("@type").getValue()).get();
        Serializer<?> serializer = SerializerManager.get(type);
        if (serializer != null) {
            object = serializer.deserialize(yapionObject, this);
            return this;
        }
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
                // getSerializerType(field)
                field.set(object, parse(yapionAny, this));
            }
        } catch (ClassNotFoundException e) {
            YAPIONLogger.trace(YAPIONLogger.LoggingType.DESERIALIZER, "The class '" + type + "' was not found.", e.getCause());
        } catch (IllegalAccessException e) {
            YAPIONLogger.trace(YAPIONLogger.LoggingType.DESERIALIZER, "", e.getCause());
        } catch (YAPIONReflectionException e) {
            YAPIONLogger.trace(YAPIONLogger.LoggingType.DESERIALIZER, "Exception while creating an Instance of the object '" + type + "'", e.getCause());
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
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