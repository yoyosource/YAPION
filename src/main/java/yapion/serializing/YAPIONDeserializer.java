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

import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import yapion.annotations.api.DeprecationInfo;
import yapion.annotations.deserialize.YAPIONDeserializeType;
import yapion.exceptions.serializing.YAPIONDeserializerException;
import yapion.exceptions.utils.YAPIONReflectionException;
import yapion.hierarchy.api.groups.SerializingType;
import yapion.hierarchy.api.groups.YAPIONAnyType;
import yapion.hierarchy.api.groups.YAPIONDataType;
import yapion.hierarchy.types.YAPIONArray;
import yapion.hierarchy.types.YAPIONObject;
import yapion.hierarchy.types.YAPIONPointer;
import yapion.hierarchy.types.YAPIONValue;
import yapion.serializing.data.DeserializationContext;
import yapion.serializing.data.DeserializationMutationContext;
import yapion.serializing.data.DeserializeData;
import yapion.serializing.views.Mutator;
import yapion.serializing.views.View;
import yapion.utils.ClassUtils;
import yapion.utils.MethodReturnValue;
import yapion.utils.ReflectionsUtils;

import java.lang.reflect.Field;
import java.util.IdentityHashMap;
import java.util.Map;

import static yapion.utils.IdentifierUtils.TYPE_IDENTIFIER;

@Slf4j
public final class YAPIONDeserializer {

    private Object object;
    private final SerializingType serializingType;
    private final ContextManager contextManager;
    private TypeReMapper typeReMapper = new TypeReMapper.FinalTypeReMapper(new TypeReMapper());
    private DeserializeResult deserializeResult = new DeserializeResult();
    private YAPIONFlags yapionFlags = new YAPIONFlags();

    private Map<YAPIONDataType<?, ?>, Object> pointerMap = new IdentityHashMap<>();

    @Getter
    private String arrayType = "";

    /**
     * Serialize an YAPION Object to an Object.
     *
     * @param serializingType to deserialize
     * @return Object from the YAPIONObject to deserialize
     */
    public static <T, K extends YAPIONDataType<?, ?> & SerializingType> T deserialize(@NonNull K serializingType) {
        return deserialize(serializingType, (Class<? extends View>) null);
    }

    /**
     * Serialize an YAPION Object to an Object.
     *
     * @param serializingType to deserialize
     * @return Object from the YAPIONObject to deserialize
     */
    public static <T, K extends YAPIONDataType<?, ?> & SerializingType> T deserialize(@NonNull K serializingType, @NonNull YAPIONFlags yapionFlags) {
        return deserialize(serializingType, null, yapionFlags);
    }

    /**
     * Serialize an YAPION Object to an Object.
     *
     * @param serializingType to deserialize
     * @param typeReMapper the mapper to remap any '@type' variable to a new class
     * @return Object from the YAPIONObject to deserialize
     */
    public static <T, K extends YAPIONDataType<?, ?> & SerializingType> T deserialize(@NonNull K serializingType, @NonNull TypeReMapper typeReMapper) {
        return deserialize(serializingType, null, typeReMapper);
    }

    /**
     * Serialize an YAPION Object to an Object.
     *
     * @param serializingType to deserialize
     * @param context the context for deserialization
     * @return Object from the YAPIONObject to deserialize
     */
    public static <T, K extends YAPIONDataType<?, ?> & SerializingType> T deserialize(@NonNull K serializingType, Class<? extends View> context) {
        return (T) new YAPIONDeserializer(serializingType, context).parse().getObject();
    }

    /**
     * Serialize an YAPION Object to an Object.
     *
     * @param serializingType to deserialize
     * @param context the context for deserialization
     * @return Object from the YAPIONObject to deserialize
     */
    public static <T, K extends YAPIONDataType<?, ?> & SerializingType> T deserialize(@NonNull K serializingType, Class<? extends View> context, @NonNull YAPIONFlags yapionFlags) {
        return (T) new YAPIONDeserializer(serializingType, context, new TypeReMapper(), yapionFlags).parse().getObject();
    }

    /**
     * Serialize an YAPION Object to an Object.
     *
     * @param serializingType to deserialize
     * @param context the context for deserialization
     * @param typeReMapper the mapper to remap any '@type' variable to a new class
     * @return Object from the YAPIONObject to deserialize
     */
    public static <T, K extends YAPIONDataType<?, ?> & SerializingType> T deserialize(@NonNull K serializingType, Class<? extends View> context, @NonNull TypeReMapper typeReMapper) {
        return (T) new YAPIONDeserializer(serializingType, context, typeReMapper).parse().getObject();
    }

    /**
     * Serialize an YAPION Object to an Object.
     *
     * @param serializingType to deserialize
     * @param context the context for deserialization
     * @param typeReMapper the mapper to remap any '@type' variable to a new class
     * @return Object from the YAPIONObject to deserialize
     */
    public static <T, K extends YAPIONDataType<?, ?> & SerializingType> T deserialize(@NonNull K serializingType, Class<? extends View> context, @NonNull TypeReMapper typeReMapper, @NonNull YAPIONFlags yapionFlags) {
        return (T) new YAPIONDeserializer(serializingType, context, typeReMapper, yapionFlags).parse().getObject();
    }

    /**
     * Creates a YAPIONDeserializer for deserializing a YAPIONObject.
     *
     * @param serializingType to deserialize
     */
    public <K extends YAPIONDataType<?, ?> & SerializingType> YAPIONDeserializer(@NonNull K serializingType) {
        contextManager = new ContextManager(null);
        this.serializingType = serializingType;
    }

    /**
     * Creates a YAPIONDeserializer for deserializing a YAPIONObject with a specified context.
     *
     * @param serializingType to deserialize
     * @param context the context for deserialization
     */
    public <K extends YAPIONDataType<?, ?> & SerializingType> YAPIONDeserializer(@NonNull K serializingType, Class<? extends View> context) {
        contextManager = new ContextManager(context);
        this.serializingType = serializingType;
    }

    /**
     * Creates a YAPIONDeserializer for deserializing a YAPIONObject with a specified context.
     *
     * @param serializingType to deserialize
     * @param context the context for deserialization
     * @param typeReMapper the mapper to remap any '@type' variable to a new class
     */
    public <K extends YAPIONDataType<?, ?> & SerializingType> YAPIONDeserializer(@NonNull K serializingType, Class<? extends View> context, @NonNull TypeReMapper typeReMapper) {
        contextManager = new ContextManager(context);
        this.serializingType = (K) serializingType.internalCopy();
        this.typeReMapper = new TypeReMapper.FinalTypeReMapper(typeReMapper);
    }

    /**
     * Creates a YAPIONDeserializer for deserializing a YAPIONObject with a specified context.
     *
     * @param serializingType to deserialize
     * @param context the context for deserialization
     * @param typeReMapper the mapper to remap any '@type' variable to a new class
     */
    public <K extends YAPIONDataType<?, ?> & SerializingType> YAPIONDeserializer(@NonNull K serializingType, Class<? extends View> context, @NonNull TypeReMapper typeReMapper, @NonNull YAPIONFlags yapionFlags) {
        contextManager = new ContextManager(context);
        this.serializingType = (K) serializingType.internalCopy();
        this.typeReMapper = new TypeReMapper.FinalTypeReMapper(typeReMapper);
        this.yapionFlags = yapionFlags;
    }

    private YAPIONDeserializer(@NonNull YAPIONObject serializingType, YAPIONDeserializer yapionDeserializer) {
        this.serializingType = serializingType;
        if (yapionDeserializer.contextManager.willBeCascading(serializingType) && !yapionDeserializer.contextManager.isCascading()) {
            this.contextManager = new ContextManager(yapionDeserializer.contextManager.get());
        } else {
            this.contextManager = yapionDeserializer.contextManager;
        }
        this.pointerMap = yapionDeserializer.pointerMap;
        this.typeReMapper = yapionDeserializer.typeReMapper;
        this.deserializeResult = yapionDeserializer.deserializeResult;
        this.yapionFlags = yapionDeserializer.yapionFlags;
    }

    /**
     * @param yapionAnyType to parse
     * @return the Object of the {@link YAPIONAnyType} inputted
     */
    @SuppressWarnings({"java:S3740"})
    public Object parse(YAPIONAnyType yapionAnyType) {
        if (yapionAnyType instanceof YAPIONPointer yapionPointer) {
            return pointerMap.getOrDefault(yapionPointer.get(), null);
        }
        if (yapionAnyType instanceof YAPIONValue yapionValue) {
            return yapionValue.get();
        }
        if (yapionAnyType instanceof YAPIONObject yapionObject) {
            return new YAPIONDeserializer(yapionObject, this).parse().getObject();
        }
        if (yapionAnyType instanceof YAPIONArray yapionArray) {
            Object o = SerializeManager.getArraySerializer().deserialize(new DeserializeData<>(yapionArray, contextManager.get(), this, typeReMapper));
            pointerMap.put(yapionArray, o);
            return o;
        }
        return null;
    }

    @SuppressWarnings({"java:S3740", "java:S3011", "java:S1117", "unchecked"})
    private YAPIONDeserializer parseObject(YAPIONObject yapionObject) {
        String type = ((YAPIONValue<String>) yapionObject.getAnyType(TYPE_IDENTIFIER)).get();
        type = typeReMapper.remap(type);
        Class<?> clazz = null;
        try {
            clazz = Class.forName(type);
        } catch (ClassNotFoundException e) {
            log.warn("The class '" + type + "' was not found.", e.getException());
            return this;
        }

        InternalSerializer<?> serializer = SerializeManager.getInternalSerializer(clazz);
        if (serializer != null && !serializer.empty()) {
            object = serializer.deserialize(new DeserializeData<>(yapionObject, contextManager.get(), this, typeReMapper));
            pointerMap.put(yapionObject, object);
            if (serializer.finished()) {
                return this;
            }
            clazz = object.getClass();
        }

        if (serializer == null && GeneratedSerializerLoader.loadSerializerIfNeeded(clazz)) {
            return parseObject(yapionObject);
        }

        boolean loadWithoutAnnotation = serializer != null && serializer.loadWithoutAnnotation();
        boolean createWithObjenesis = serializer != null && serializer.createWithObjenesis();

        if (!contextManager.is(clazz).load && !loadWithoutAnnotation) {
            if (yapionFlags.isSet(YAPIONFlag.CLASSES_WITHOUT_ANNOTATION_EXCEPTION)) {
                throw new YAPIONDeserializerException("No suitable deserializer found, maybe class (" + type + ") is missing YAPION annotations");
            }
            if (yapionFlags.isSet(YAPIONFlag.CLASSES_WITHOUT_ANNOTATION_AS_NULL)) {
                object = null;
                return this;
            }
            if (!yapionFlags.isSet(YAPIONFlag.CLASSES_LOAD_WITHOUT_ANNOTATION)) {
                throw new YAPIONDeserializerException("No suitable deserializer found, maybe class (" + type + ") is missing YAPION annotations");
            }
            loadWithoutAnnotation = true;
            createWithObjenesis = true;
        }
        if (serializer == null || serializer.finished() || serializer.empty()) {
            try {
                object = SerializeManager.getObjectInstance(clazz, contextManager.is(clazz).data || createWithObjenesis);
            } catch (YAPIONReflectionException e) {
                log.warn("Exception while creating an Instance of the object '" + type + "'", e.getCause());
            }
        }
        Class<? extends Mutator> mutatorClass = contextManager.getMutator(clazz);
        MutationManager mutationManager = new MutationManager(mutatorClass, DeserializationMutationContext.class);

        DeserializationContext deserializationContext = new DeserializationContext(this, yapionObject);
        MethodManager.preDeserializationStep(object, object.getClass(), contextManager, deserializationContext);
        pointerMap.put(yapionObject, object);

        for (String fieldName : yapionObject.getKeys()) {
            if (fieldName.equals(TYPE_IDENTIFIER)) continue;

            YAPIONAnyType yapionAnyType = yapionObject.getAnyType(fieldName);
            if (mutationManager.hasMutation(fieldName)) {
                DeserializationMutationContext deserializationMutationContext = new DeserializationMutationContext(fieldName, yapionAnyType.internalCopy());
                MethodReturnValue<Object> methodReturnValue = mutationManager.mutate(fieldName, deserializationMutationContext);
                if (methodReturnValue.isPresent()) {
                    deserializationMutationContext = (DeserializationMutationContext) methodReturnValue.get();
                }
                if (deserializationMutationContext.fieldName == null) {
                    continue;
                }
                fieldName = deserializationMutationContext.fieldName;
                yapionAnyType = deserializationMutationContext.value;
            }

            Field field = ReflectionsUtils.getField(clazz, fieldName);
            if (field == null) {
                deserializeResult.add(object, fieldName, yapionObject.getAnyType(fieldName));
                continue;
            }

            if (!contextManager.is(object, field).load && !loadWithoutAnnotation) continue;
            if (ClassUtils.removed(field)) continue;

            Class<?> fieldType = field.getType();
            arrayType = fieldType.getTypeName();

            if (!YAPIONAnyType.class.isAssignableFrom(fieldType) && yapionAnyType instanceof YAPIONObject currentObject && !((YAPIONObject) yapionAnyType).containsKey(TYPE_IDENTIFIER, String.class)) {
                currentObject.add(TYPE_IDENTIFIER, arrayType);
            }
            YAPIONDeserializeType yapionDeserializeType = field.getDeclaredAnnotation(YAPIONDeserializeType.class);
            if (specialSet(field, yapionAnyType)) {
                SerializeManager.getReflectionStrategy().set(field, object, yapionAnyType);
            } else if (isValid(field, yapionDeserializeType)) {
                Object o = null;
                if (serializer != null && !serializer.empty()) {
                    o = SerializeManager.getInternalSerializer(yapionDeserializeType.type()).deserialize(new DeserializeData<>(yapionAnyType, contextManager.get(), this, typeReMapper));
                }
                SerializeManager.getReflectionStrategy().set(field, object, o);
            } else {
                SerializeManager.getReflectionStrategy().set(field, object, parse(yapionAnyType));
            }

            arrayType = "";
        }
        MethodManager.postDeserializationStep(object, object.getClass(), contextManager, deserializationContext);
        return this;
    }

    private boolean specialSet(Field field, YAPIONAnyType yapionAnyType) {
        Class<?> fieldType = field.getType();
        return fieldType.isAssignableFrom(yapionAnyType.getClass()) && YAPIONAnyType.class.isAssignableFrom(fieldType);
    }

    private boolean isValid(Field field, YAPIONDeserializeType yapionDeserializeType) {
        if (!field.getType().isInterface()) return false;
        if (yapionDeserializeType == null) return false;
        String type = field.getType().getTypeName();
        Class<?> clazz = yapionDeserializeType.type();
        while (!clazz.getTypeName().equals("java.lang.Object")) {
            for (Class<?> ifc : clazz.getInterfaces()) {
                if (ifc.getTypeName().equals(type) && SerializeManager.getInternalSerializer(yapionDeserializeType.type()) != null) {
                    return true;
                }
            }
            clazz = clazz.getSuperclass();
        }
        return false;
    }

    /**
     * Set the reducedMode for this deserialization.
     */
    @Deprecated
    @DeprecationInfo(since = "0.26.0")
    public YAPIONDeserializer reducedMode(boolean reducedMode) {
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
     * Parses the YAPIONObject to the Object.
     */
    public YAPIONDeserializer parse() {
        if (serializingType instanceof YAPIONObject yapionObject) {
            return parseObject(yapionObject);
        } else {
            object = parse((YAPIONArray) serializingType);
            return this;
        }
    }

    /**
     * Get the deserialized Object.
     *
     * @return Object from the YAPIONObject to deserialize
     */
    public Object getObject() {
        return object;
    }

    /**
     * Get the deserialized Object.
     *
     * @return Object from the YAPIONObject to deserialize
     */
    public Object getObjectOrException() {
        if (object == null) throw new YAPIONDeserializerException("Deserialization yielded a null object");
        return object;
    }

    /**
     * Get the {@link DeserializeResult}.
     *
     * @return {@link DeserializeResult} used by this instance
     */
    public DeserializeResult getDeserializeResult() {
        return deserializeResult;
    }
}
