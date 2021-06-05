/*
 * Copyright 2019,2020,2021 yoyosource
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package yapion.serializing;

import lombok.NonNull;
import yapion.exceptions.serializing.YAPIONSerializerException;
import yapion.hierarchy.api.groups.YAPIONAnyType;
import yapion.hierarchy.types.YAPIONObject;
import yapion.hierarchy.types.YAPIONPointer;
import yapion.hierarchy.types.YAPIONType;
import yapion.hierarchy.types.YAPIONValue;
import yapion.serializing.data.SerializeData;
import yapion.utils.ClassUtils;
import yapion.utils.ReflectionsUtils;

import java.lang.reflect.Field;
import java.util.IdentityHashMap;
import java.util.Map;

import static yapion.utils.IdentifierUtils.TYPE_IDENTIFIER;

public final class YAPIONSerializer {

    private final Object object;
    private YAPIONAnyType result;
    private final ContextManager contextManager;
    private YAPIONFlags yapionFlags = new YAPIONFlags();

    private Map<Object, YAPIONPointer> pointerMap = new IdentityHashMap<>();

    /**
     * Serialize an Object to an YAPION Object.
     *
     * @param object to serialize
     * @return YAPIONObject from the object to serialize
     */
    public static <T extends YAPIONAnyType> T serialize(@NonNull Object object) {
        return (T) serialize(object, "");
    }

    /**
     * Serialize an Object to an YAPION Object.
     *
     * @param object to serialize
     * @param yapionFlags the flags used for this serialization
     * @return YAPIONObject from the object to serialize
     */
    public static <T extends YAPIONAnyType> T serialize(@NonNull Object object, YAPIONFlags yapionFlags) {
        return (T) serialize(object, "", yapionFlags);
    }

    /**
     * Serialize an Object to an YAPION Object.
     *
     * @param object to serialize
     * @param context the context for serialization
     * @return YAPIONObject from the object to serialize
     */
    public static <T extends YAPIONAnyType> T serialize(@NonNull Object object, String context) {
        return (T) new YAPIONSerializer(object, context).parse().getYAPIONObject();
    }

    /**
     * Serialize an Object to an YAPION Object.
     *
     * @param object to serialize
     * @param context the context for serialization
     * @param yapionFlags the flags used for this serialization
     * @return YAPIONObject from the object to serialize
     */
    public static <T extends YAPIONAnyType> T serialize(@NonNull Object object, String context, YAPIONFlags yapionFlags) {
        return (T) new YAPIONSerializer(object, context, yapionFlags).parse().getYAPIONObject();
    }

    /**
     * Creates a YAPIONSerializer for serializing an Object.
     *
     * @param object to serialize
     */
    public YAPIONSerializer(@NonNull Object object) {
        contextManager = new ContextManager("");
        this.object = object;
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
     * @param yapionFlags the flags used for this serialization
     */
    public YAPIONSerializer(@NonNull Object object, String context, YAPIONFlags yapionFlags) {
        contextManager = new ContextManager(context);
        this.object = object;
        this.yapionFlags = yapionFlags;
    }

    private YAPIONSerializer(@NonNull Object object, YAPIONSerializer yapionSerializer) {
        this.object = object;
        if (yapionSerializer.contextManager.willBeCascading(object.getClass()) && !yapionSerializer.contextManager.isCascading()) {
            this.contextManager = new ContextManager(yapionSerializer.contextManager.get());
        } else {
            this.contextManager = yapionSerializer.contextManager;
        }
        this.pointerMap = yapionSerializer.pointerMap;
        this.yapionFlags = yapionSerializer.yapionFlags;
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
        Class<?> type = object.getClass();
        InternalSerializer serializer = SerializeManager.getInternalSerializer(type);
        if (serializer != null && !serializer.empty()) {
            YAPIONAnyType yapionAnyType = serializer.serialize(new SerializeData<>(object, contextManager.get(), this));
            if (yapionAnyType instanceof YAPIONObject) {
                pointerMap.put(object, new YAPIONPointer((YAPIONObject) yapionAnyType));
            }
            return yapionAnyType;
        } else {
            return new YAPIONSerializer(object, this).parse().getYAPIONObject();
        }
    }

    @SuppressWarnings({"java:S3740", "java:S3011", "java:S1117", "unchecked"})
    private YAPIONSerializer parseObject(Object object) {
        if (object.getClass().getSimpleName().contains("$")) {
            throw new YAPIONSerializerException("Simple class name (" + object.getClass().getTypeName() + ") is not allowed to contain '$'");
        }

        Class<?> type = object.getClass();
        InternalSerializer serializer = SerializeManager.getInternalSerializer(type);
        if (serializer != null && !serializer.empty()) {
            this.result = serializer.serialize(new SerializeData<>(object, contextManager.get(), this));
            if (result instanceof YAPIONObject) {
                pointerMap.put(object, new YAPIONPointer((YAPIONObject) result));
            }
            return this;
        }

        boolean saveWithoutAnnotation = serializer != null && serializer.saveWithoutAnnotation();
        if (!contextManager.is(object).save && !saveWithoutAnnotation) {
            throw new YAPIONSerializerException("No suitable serializer found, maybe class (" + object.getClass().getTypeName() + ") is missing YAPION annotations");
        }

        YAPIONObject yapionObject = new YAPIONObject();
        if (!pointerMap.containsKey(object)) {
            pointerMap.put(object, new YAPIONPointer(yapionObject));
        }
        MethodManager.preSerializationStep(object, object.getClass(), contextManager);
        yapionObject.add(TYPE_IDENTIFIER, new YAPIONValue<>(object.getClass().getTypeName()));
        this.result = yapionObject;

        Class<?> objectClass = object.getClass();
        for (Field field : ReflectionsUtils.getFields(objectClass)) {
            field.setAccessible(true);
            if (ClassUtils.removed(field)) {
                continue;
            }
            ContextManager.YAPIONInfo yapionInfo = contextManager.is(object, field);
            if (!yapionInfo.save && !saveWithoutAnnotation) continue;

            String name = field.getName();
            Object fieldObject = SerializeManager.getReflectionStrategy().get(field, object);
            if (fieldObject == null) {
                if (!yapionInfo.optimize) {
                    yapionObject.add(name, new YAPIONValue<>(null));
                }
                continue;
            }

            if (pointerMap.containsKey(fieldObject)) {
                yapionObject.add(name, pointerMap.get(fieldObject));
                continue;
            }

            YAPIONAnyType yapionAnyType = parse(fieldObject);
            if (yapionAnyType == null) {
                continue;
            }
            if (yapionAnyType.getType() == YAPIONType.OBJECT) {
                pointerMap.put(fieldObject, new YAPIONPointer((YAPIONObject) yapionAnyType));
            }
            yapionObject.add(name, yapionAnyType);
        }
        MethodManager.postSerializationStep(object, object.getClass(), contextManager);
        return this;
    }

    /**
     * Returns the flags this serialization should follow.
     *
     * @return the serialization flag holder
     */
    public YAPIONFlags getYAPIONFlags() {
        return yapionFlags;
    }

    /**
     * Parses the Object to the YAPIONObject.
     */
    public YAPIONSerializer parse() {
        return parseObject(object);
    }

    /**
     * Get the internal parsed result.
     *
     * @return YAPIONObject from the object to serialize
     */
    public <T extends YAPIONAnyType> T getYAPIONObject() {
        return (T) result;
    }

    /**
     * Get the internal parsed result in reduced mode.
     *
     * @return YAPIONObject from the object to serialize
     */
    public <T extends YAPIONAnyType> T getReducedYAPIONObject() {
        if (result instanceof YAPIONObject) {
            removeTypeVariables((YAPIONObject) result);
        }
        return (T) result;
    }

    private static void removeTypeVariables(YAPIONObject yapionObject) {
        yapionObject.stream().filter(yapionAnyType -> yapionAnyType instanceof YAPIONObject).forEach(yapionAnyType -> {
            ((YAPIONObject) yapionAnyType).remove(TYPE_IDENTIFIER);
            removeTypeVariables((YAPIONObject) yapionAnyType);
        });
    }
}
