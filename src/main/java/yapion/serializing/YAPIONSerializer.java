// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020,2021 yoyosource

package yapion.serializing;

import lombok.NonNull;
import yapion.annotations.deserialize.YAPIONLoadExclude;
import yapion.annotations.serialize.YAPIONSaveExclude;
import yapion.exceptions.serializing.YAPIONSerializerException;
import yapion.hierarchy.typegroups.YAPIONAnyType;
import yapion.hierarchy.types.*;
import yapion.serializing.data.SerializeData;
import yapion.utils.ModifierUtils;
import yapion.utils.ReflectionsUtils;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.*;

import static yapion.utils.IdentifierUtils.TYPE_IDENTIFIER;

@YAPIONSaveExclude(context = "*")
@YAPIONLoadExclude(context = "*")
public final class YAPIONSerializer {

    private final Object object;
    private YAPIONObject yapionObject;
    private final ContextManager contextManager;
    private boolean strict = false;

    private Map<Object, YAPIONPointer> pointerMap = new IdentityHashMap<>();

    /**
     * Serialize an Object to an YAPION Object.
     *
     * @param object to serialize
     * @return YAPIONObject from the object to serialize
     */
    public static YAPIONObject serialize(@NonNull Object object) {
        return serialize(object, "");
    }

    /**
     * Serialize an Object to an YAPION Object.
     *
     * @param object to serialize
     * @param strict if serialization should be strict and check if any data would get lost while serialization
     * @return YAPIONObject from the object to serialize
     */
    public static YAPIONObject serialize(@NonNull Object object, boolean strict) {
        return serialize(object, "", strict);
    }

    /**
     * Serialize an Object to an YAPION Object.
     *
     * @param object to serialize
     * @param context the context for serialization
     * @return YAPIONObject from the object to serialize
     */
    public static YAPIONObject serialize(@NonNull Object object, String context) {
        return new YAPIONSerializer(object, context).parse().getYAPIONObject();
    }

    /**
     * Serialize an Object to an YAPION Object.
     *
     * @param object to serialize
     * @param context the context for serialization
     * @param strict if serialization should be strict and check if any data would get lost while serialization
     * @return YAPIONObject from the object to serialize
     */
    public static YAPIONObject serialize(@NonNull Object object, String context, boolean strict) {
        return new YAPIONSerializer(object, context, strict).parse().getYAPIONObject();
    }

    /**
     * Creates a YAPIONSerializer for serializing an Object with a specified context.
     *
     * @param object to serialize
     * @param context the context for serialization
     */
    public YAPIONSerializer(@NonNull Object object, String context) {
        contextManager = new ContextManager(context);
        this.object = object;
    }

    /**
     * Creates a YAPIONSerializer for serializing an Object with a specified context.
     *
     * @param object to serialize
     * @param context the context for serialization
     * @param strict if serialization should be strict and check if any data would get lost while serialization
     */
    public YAPIONSerializer(@NonNull Object object, String context, boolean strict) {
        contextManager = new ContextManager(context);
        this.object = object;
        this.strict = strict;
    }

    private YAPIONSerializer(@NonNull Object object, YAPIONSerializer yapionSerializer) {
        this.object = object;
        // TODO: Test this cascading feature
        if (yapionSerializer.contextManager.willBeCascading(object.getClass()) && !yapionSerializer.contextManager.isCascading()) {
            this.contextManager = new ContextManager(yapionSerializer.contextManager.get());
        } else {
            this.contextManager = yapionSerializer.contextManager;
        }
        /*if (yapionSerializer.contextManager.isCascading()) {
            this.contextManager = yapionSerializer.contextManager;
        } else {
            this.contextManager = new ContextManager(yapionSerializer.contextManager.get());
        }*/
        this.pointerMap = yapionSerializer.pointerMap;
        this.strict = yapionSerializer.strict;
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
            InternalSerializer serializer = SerializeManager.getInternalSerializer(type);
            if (serializer != null && !serializer.empty()) {
                return serializer.serialize(new SerializeData<>(object, contextManager.get(), this));
            } else {
                return new YAPIONSerializer(object, this).parse().getYAPIONObject();
            }
        }
    }

    @SuppressWarnings({"java:S3740", "java:S3011", "java:S1117", "unchecked"})
    private YAPIONSerializer parseObject(Object object) {
        if (object.getClass().getSimpleName().contains("$")) {
            throw new YAPIONSerializerException("Simple class name (" + object.getClass().getTypeName() + ") is not allowed to contain '$'");
        }

        String type = object.getClass().getTypeName();
        Class<?> clazz = object.getClass();
        if (clazz.isEnum()) {
            type = "java.lang.Enum";
        }
        if (ReflectionsUtils.isRuntimeException(clazz)) {
            type = "java.lang.RuntimeException";
        } else if (ReflectionsUtils.isException(clazz)) {
            type = "java.lang.Exception";
        } else if (ReflectionsUtils.isError(clazz)) {
            type = "java.lang.Error";
        } else if (ReflectionsUtils.isThrowable(clazz)) {
            type = "java.lang.Throwable";
        }
        InternalSerializer serializer = SerializeManager.getInternalSerializer(type);
        if (serializer != null && !serializer.empty()) {
            this.yapionObject = (YAPIONObject) serializer.serialize(new SerializeData<>(object, contextManager.get(), this));
            return this;
        }

        boolean saveWithoutAnnotation = false;
        if (serializer != null && serializer.saveWithoutAnnotation()) {
            saveWithoutAnnotation = true;
        }
        if (!contextManager.is(object).save && !saveWithoutAnnotation) {
            throw new YAPIONSerializerException("No suitable serializer found, maybe class (" + object.getClass().getTypeName() + ") is missing YAPION annotations");
        }

        YAPIONObject yapionObject = new YAPIONObject();
        if (!pointerMap.containsKey(object)) {
            pointerMap.put(object, new YAPIONPointer(yapionObject));
        }
        MethodManager.preSerializationStep(object, object.getClass(), contextManager);
        yapionObject.add(new YAPIONVariable(TYPE_IDENTIFIER, new YAPIONValue<>(object.getClass().getTypeName())));
        this.yapionObject = yapionObject;

        Class<?> objectClass = object.getClass();
        for (Field field : ReflectionsUtils.getFields(objectClass)) {
            field.setAccessible(true);
            if (ModifierUtils.removed(field)) {
                continue;
            }
            ContextManager.YAPIONInfo yapionInfo = contextManager.is(object, field);
            if (!yapionInfo.save && !saveWithoutAnnotation) continue;

            String name = field.getName();
            Object fieldObject = ReflectionsUtils.getValueOfField(field, object);
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
        MethodManager.postSerializationStep(object, object.getClass(), contextManager);
        return this;
    }

    /**
     * Returns whether the serialization should be strict and should
     * not allow any data loss while serializing.
     *
     * @return {@code true} if data loss should be prohibited, {@code false} otherwise
     */
    public boolean isStrict() {
        return strict;
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