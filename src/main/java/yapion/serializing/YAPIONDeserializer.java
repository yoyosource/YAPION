// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020 yoyosource

package yapion.serializing;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import yapion.annotations.deserialize.YAPIONLoadExclude;
import yapion.annotations.object.YAPIONObjenesis;
import yapion.annotations.object.YAPIONPostDeserialization;
import yapion.annotations.object.YAPIONPreDeserialization;
import yapion.annotations.serialize.YAPIONSaveExclude;
import yapion.exceptions.utils.YAPIONReflectionException;
import yapion.hierarchy.YAPIONAny;
import yapion.hierarchy.types.YAPIONArray;
import yapion.hierarchy.types.YAPIONObject;
import yapion.hierarchy.types.YAPIONPointer;
import yapion.hierarchy.types.YAPIONValue;
import yapion.utils.ModifierUtils;
import yapion.utils.ReflectionsUtils;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.IdentityHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Optional;

@YAPIONSaveExclude(context = "*")
@YAPIONLoadExclude(context = "*")
public class YAPIONDeserializer {

    private Object object;
    private final YAPIONObject yapionObject;
    private final StateManager stateManager;

    private Map<YAPIONObject, Object> pointerMap = new IdentityHashMap<>();

    private String arrayType = "";

    private static final Logger logger = LoggerFactory.getLogger(YAPIONDeserializer.class);

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

    private Object parseArray(YAPIONArray yapionArray) {
        LinkedList<Integer> dimensions = new LinkedList<>();
        YAPIONArray current = yapionArray;
        while (current != null) {
            dimensions.add(current.length());
            if (dimensions.size() > 0) {
                YAPIONAny yapionAny = current.get(0);
                if (yapionAny instanceof YAPIONArray) {
                    current = (YAPIONArray) yapionAny;
                } else {
                    current = null;
                }
            }
        }

        int[] ints = new int[dimensions.size()];
        for (int i = 0; i < dimensions.size(); i++) {
            ints[i] = dimensions.removeLast();
        }

        while (arrayType.endsWith("[]")) {
            arrayType = arrayType.substring(0, arrayType.length() - 2);
        }
        Object array = null;
        try {
            array = Array.newInstance(Class.forName(arrayType), ints);
        } catch (ClassNotFoundException e) {
            // Ignored
        } catch (IllegalArgumentException e) {
            // Ignored
        } catch (NegativeArraySizeException e) {
            // Ignored
        }
        if (array == null) return array;
        // if (yapionArray == null) return array;

        for (int i = 0; i < yapionArray.length(); i++) {
            Array.set(array, i, parse(yapionArray.get(i), this));
        }
        return array;
    }

    @SuppressWarnings({"java:S3740", "java:S3011", "java:S1117", "unchecked"})
    private YAPIONDeserializer parse(YAPIONObject yapionObject) {
        String type = ((YAPIONValue<String>)yapionObject.getVariable(SerializeManager.TYPE_IDENTIFIER).getValue()).get();
        InternalSerializer<?> serializer = SerializeManager.get(type);
        if (serializer != null) {
            object = serializer.deserialize(yapionObject, this);
            return this;
        }

        try {
            Class<?> clazz = Class.forName(type);
            if (!stateManager.is(clazz).load) {
                return this;
            }

            if (clazz.getDeclaredAnnotation(YAPIONObjenesis.class) != null) {
                object = ReflectionsUtils.constructObjectObjenesis(type);
            } else {
                object = ReflectionsUtils.constructObject(type);
            }
            preDeserializationStep(object);
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
                arrayType = remove(field.getType().getTypeName(), '[', ']');
                field.set(object, parse(yapionAny, this));
            }
            postDeserializationStep(object);
        } catch (ClassNotFoundException e) {
            logger.trace("The class '" + type + "' was not found.", e.getCause());
        } catch (IllegalAccessException e) {
            logger.trace("", e.getCause());
        } catch (YAPIONReflectionException e) {
            logger.trace("Exception while creating an Instance of the object '" + type + "'", e.getCause());
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        return this;
    }

    private String remove(String s, char... chars) {
        StringBuilder st = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            for (char d : chars) {
                if (d == c) c = '\u0000';
            }
            if (c != '\u0000') st.append(c);
        }
        return st.toString();
    }

    private void preDeserializationStep(Object object) {
        Method[] methods = object.getClass().getDeclaredMethods();
        for (Method method : methods) {
            if (stateManager.is(method.getAnnotation(YAPIONPreDeserialization.class))) {
                ReflectionsUtils.invokeMethod(method.getName(), object);
                break;
            }
        }
    }

    private void postDeserializationStep(Object object) {
        Method[] methods = object.getClass().getDeclaredMethods();
        for (Method method : methods) {
            if (stateManager.is(method.getAnnotation(YAPIONPostDeserialization.class))) {
                ReflectionsUtils.invokeMethod(method.getName(), object);
                break;
            }
        }
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