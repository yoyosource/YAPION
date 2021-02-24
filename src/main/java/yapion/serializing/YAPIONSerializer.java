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
import yapion.annotations.deserialize.YAPIONLoadExclude;
import yapion.annotations.serialize.YAPIONSaveExclude;
import yapion.exceptions.serializing.YAPIONSerializerException;
import yapion.hierarchy.api.groups.YAPIONAnyType;
import yapion.hierarchy.types.*;
import yapion.serializing.data.SerializeData;
import yapion.utils.ModifierUtils;
import yapion.utils.ReflectionsUtils;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.IdentityHashMap;
import java.util.Map;

import static yapion.utils.IdentifierUtils.TYPE_IDENTIFIER;

@YAPIONSaveExclude(context = "*")
@YAPIONLoadExclude(context = "*")
public final class YAPIONSerializer {

    private final Object object;
    private YAPIONObject yapionObject;
    private final ContextManager contextManager;
    private YAPIONSerializerFlags yapionSerializerFlags = new YAPIONSerializerFlags();

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
     * @param yapionSerializerFlags the flags used for this serialization
     * @return YAPIONObject from the object to serialize
     */
    public static YAPIONObject serialize(@NonNull Object object, YAPIONSerializerFlags yapionSerializerFlags) {
        return serialize(object, "", yapionSerializerFlags);
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
     * @param yapionSerializerFlags the flags used for this serialization
     * @return YAPIONObject from the object to serialize
     */
    public static YAPIONObject serialize(@NonNull Object object, String context, YAPIONSerializerFlags yapionSerializerFlags) {
        return new YAPIONSerializer(object, context, yapionSerializerFlags).parse().getYAPIONObject();
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
     * @param yapionSerializerFlags the flags used for this serialization
     */
    public YAPIONSerializer(@NonNull Object object, String context, YAPIONSerializerFlags yapionSerializerFlags) {
        contextManager = new ContextManager(context);
        this.object = object;
        this.yapionSerializerFlags = yapionSerializerFlags;
    }

    private YAPIONSerializer(@NonNull Object object, YAPIONSerializer yapionSerializer) {
        this.object = object;
        if (yapionSerializer.contextManager.willBeCascading(object.getClass()) && !yapionSerializer.contextManager.isCascading()) {
            this.contextManager = new ContextManager(yapionSerializer.contextManager.get());
        } else {
            this.contextManager = yapionSerializer.contextManager;
        }
        this.pointerMap = yapionSerializer.pointerMap;
        this.yapionSerializerFlags = yapionSerializer.yapionSerializerFlags;
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
                YAPIONAnyType yapionAnyType = serializer.serialize(new SerializeData<>(object, contextManager.get(), this));
                if (yapionAnyType instanceof YAPIONObject) {
                    pointerMap.put(object, new YAPIONPointer((YAPIONObject) yapionAnyType));
                }
                return yapionAnyType;
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
        InternalSerializer serializer = SerializeManager.getInternalSerializer(type);
        if (serializer != null && !serializer.empty()) {
            this.yapionObject = (YAPIONObject) serializer.serialize(new SerializeData<>(object, contextManager.get(), this));
            pointerMap.put(object, new YAPIONPointer(yapionObject));
            return this;
        }

        boolean saveWithoutAnnotation = serializer != null && serializer.saveWithoutAnnotation();
        if (!contextManager.is(object).save && !saveWithoutAnnotation) {
            throw new YAPIONSerializerException("No suitable serializer found, maybe class (" + object.getClass().getTypeName() + ") is missing YAPION annotations");
        }

        YAPIONObject yapionObject = new YAPIONObject();
        if (!pointerMap.containsKey(object)) {
            // Todo: implement old mode maybe?
            pointerMap.put(object, new YAPIONPointer(yapionObject));
        }
        MethodManager.preSerializationStep(object, object.getClass(), contextManager);
        yapionObject.add(TYPE_IDENTIFIER, new YAPIONValue<>(object.getClass().getTypeName()));
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
    public YAPIONSerializerFlags getYAPIONSerializerFlags() {
        return yapionSerializerFlags;
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
