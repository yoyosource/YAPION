// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020 yoyosource

package yapion.serializing;

import yapion.annotations.deserialize.YAPIONLoadExclude;
import yapion.annotations.serialize.YAPIONSaveExclude;
import yapion.hierarchy.types.YAPIONType;
import yapion.hierarchy.typegroups.YAPIONAnyType;
import yapion.hierarchy.types.YAPIONVariable;
import yapion.hierarchy.types.YAPIONArray;
import yapion.hierarchy.types.YAPIONObject;
import yapion.hierarchy.types.YAPIONPointer;
import yapion.hierarchy.types.YAPIONValue;
import yapion.utils.ModifierUtils;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.IdentityHashMap;
import java.util.Map;

@YAPIONSaveExclude(context = "*")
@YAPIONLoadExclude(context = "*")
public final class YAPIONSerializer {

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

    /**
     * @deprecated since 0.12.0 use {@link #parse(Object)} instead
     *
     * @param object to parse
     * @param yapionSerializer to parse with
     *
     * @return the {@link YAPIONAnyType} of the Object inputted
     */
    @Deprecated
    public YAPIONAnyType parse(Object object, YAPIONSerializer yapionSerializer) {
        return parse(object);
    }

    /**
     * @param object to parse
     * @return the {@link YAPIONAnyType} of the Object inputted
     */
    @SuppressWarnings({"java:S3740"})
    public YAPIONAnyType parse(Object object) {
        if (pointerMap.containsKey(object)) {
            return pointerMap.get(object);
        }
        if (object == null) {
            return new YAPIONValue<>(null);
        }
        if (object.getClass().isArray()) {
            YAPIONArray yapionArray = new YAPIONArray();
            for (int i = 0; i < Array.getLength(object); i++) {
                yapionArray.add(parse(Array.get(object, i)));
            }
            return yapionArray;
        } else {
            String type = object.getClass().getTypeName();
            if (object.getClass().isEnum()) {
                type = "java.lang.Enum";
            }
            InternalSerializer serializer = SerializeManager.get(type);
            if (serializer != null && !serializer.empty()) {
                return serializer.serialize(object, this);
            } else {
                return new YAPIONSerializer(object, this).parse().getYAPIONObject();
            }
        }
    }

    @SuppressWarnings({"java:S3740", "java:S3011", "java:S1117", "unchecked"})
    private YAPIONSerializer parseObject(Object object) {
        if (!stateManager.is(object).save) {
            return this;
        }
        if (object.getClass().getSimpleName().contains("$")) {
            return this;
        }

        String type = object.getClass().getTypeName();
        if (object.getClass().isEnum()) {
            type = "java.lang.Enum";
        }
        InternalSerializer serializer = SerializeManager.get(type);
        if (serializer != null && !serializer.empty()) {
            this.yapionObject = (YAPIONObject) serializer.serialize(object, this);
            return this;
        }

        YAPIONObject yapionObject = new YAPIONObject();
        if (!pointerMap.containsKey(object)) {
            pointerMap.put(object, new YAPIONPointer(yapionObject));
        }
        yapionObject.add(new YAPIONVariable(SerializeManager.TYPE_IDENTIFIER, new YAPIONValue<>(object.getClass().getTypeName())));
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
                // Ignored
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

            YAPIONAnyType yapionAnyType = parse(fieldObject);
            if (yapionAnyType == null) {
                continue;
            }
            if (yapionAnyType.getType() == YAPIONType.OBJECT) {
                pointerMap.put(fieldObject, new YAPIONPointer((YAPIONObject) yapionAnyType));
            }
            yapionObject.add(new YAPIONVariable(name, yapionAnyType));
        }
        return this;
    }

    /**
     * Parses the Object to the YAPIONObject.
     */
    public YAPIONSerializer parse() {
        return parseObject(object);
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