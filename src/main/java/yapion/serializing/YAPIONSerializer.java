// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020 yoyosource

package yapion.serializing;

import yapion.annotations.deserialize.YAPIONLoadExclude;
import yapion.annotations.serialize.YAPIONSaveExclude;
import yapion.hierarchy.Type;
import yapion.hierarchy.YAPIONAny;
import yapion.hierarchy.YAPIONVariable;
import yapion.hierarchy.types.YAPIONArray;
import yapion.hierarchy.types.YAPIONObject;
import yapion.hierarchy.types.YAPIONPointer;
import yapion.hierarchy.types.YAPIONValue;
import yapion.utils.ModifierUtils;

import java.lang.reflect.Field;
import java.util.IdentityHashMap;
import java.util.Map;

@YAPIONSaveExclude(context = "*")
@YAPIONLoadExclude(context = "*")
public class YAPIONSerializer {

    private final Object object;
    private YAPIONObject yapionObject;
    private final StateManager stateManager;

    private Map<Object, YAPIONPointer> pointerMap = new IdentityHashMap<>();

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
            InternalSerializer serializer = SerializeManager.get(object.getClass().getTypeName());
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
        yapionObject.add(new YAPIONVariable(SerializeManager.typeName, new YAPIONValue<>(object.getClass().getTypeName())));
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

            InternalSerializer serializer = SerializeManager.get(fieldObject.getClass().getTypeName());
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