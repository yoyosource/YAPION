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
import lombok.extern.slf4j.Slf4j;
import yapion.annotations.deserialize.YAPIONDeserializeType;
import yapion.annotations.deserialize.YAPIONLoadExclude;
import yapion.annotations.serialize.YAPIONSaveExclude;
import yapion.exceptions.serializing.YAPIONDeserializerException;
import yapion.exceptions.serializing.YAPIONSerializerException;
import yapion.exceptions.utils.YAPIONReflectionException;
import yapion.hierarchy.api.groups.YAPIONAnyType;
import yapion.hierarchy.types.YAPIONArray;
import yapion.hierarchy.types.YAPIONObject;
import yapion.hierarchy.types.YAPIONPointer;
import yapion.hierarchy.types.YAPIONValue;
import yapion.serializing.data.DeserializeData;
import yapion.serializing.serializer.object.other.EnumSerializer;
import yapion.utils.ClassUtils;
import yapion.utils.MethodReturnValue;
import yapion.utils.ModifierUtils;
import yapion.utils.ReflectionsUtils;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.IdentityHashMap;
import java.util.LinkedList;
import java.util.Map;

import static yapion.utils.IdentifierUtils.ENUM_IDENTIFIER;
import static yapion.utils.IdentifierUtils.TYPE_IDENTIFIER;

@YAPIONSaveExclude(context = "*")
@YAPIONLoadExclude(context = "*")
@Slf4j
public final class YAPIONDeserializer {

    private Object object;
    private final YAPIONObject yapionObject;
    private final ContextManager contextManager;
    private TypeReMapper typeReMapper = new TypeReMapper();
    private DeserializeResult deserializeResult = new DeserializeResult();

    private Map<YAPIONObject, Object> pointerMap = new IdentityHashMap<>();

    private boolean reducedMode = false;
    private String arrayType = "";

    /**
     * Serialize an YAPION Object to an Object.
     *
     * @param yapionObject to deserialize
     * @return Object from the YAPIONObject to deserialize
     */
    public static Object deserialize(@NonNull YAPIONObject yapionObject) {
        return deserialize(yapionObject, "");
    }

    /**
     * Serialize an YAPION Object to an Object.
     *
     * @param yapionObject to deserialize
     * @param typeReMapper the mapper to remap any '@type' variable to a new class
     * @return Object from the YAPIONObject to deserialize
     */
    public static Object deserialize(@NonNull YAPIONObject yapionObject, @NonNull TypeReMapper typeReMapper) {
        return deserialize(yapionObject, "", typeReMapper);
    }

    /**
     * Serialize an YAPION Object to an Object.
     *
     * @param yapionObject to deserialize
     * @param context the context for deserialization
     * @return Object from the YAPIONObject to deserialize
     */
    public static Object deserialize(@NonNull YAPIONObject yapionObject, String context) {
        return new YAPIONDeserializer(yapionObject, context).parse().getObject();
    }

    /**
     * Serialize an YAPION Object to an Object.
     *
     * @param yapionObject to deserialize
     * @param context the context for deserialization
     * @param typeReMapper the mapper to remap any '@type' variable to a new class
     * @return Object from the YAPIONObject to deserialize
     */
    public static Object deserialize(@NonNull YAPIONObject yapionObject, String context, @NonNull TypeReMapper typeReMapper) {
        return new YAPIONDeserializer(yapionObject, context, typeReMapper).parse().getObject();
    }

    /**
     * Creates a YAPIONDeserializer for deserializing a YAPIONObject.
     *
     * @param yapionObject to deserialize
     */
    public YAPIONDeserializer(@NonNull YAPIONObject yapionObject) {
        contextManager = new ContextManager("");
        this.yapionObject = yapionObject;
    }

    /**
     * Creates a YAPIONDeserializer for deserializing a YAPIONObject with a specified context.
     *
     * @param yapionObject to deserialize
     * @param context the context for deserialization
     */
    public YAPIONDeserializer(@NonNull YAPIONObject yapionObject, String context) {
        contextManager = new ContextManager(context);
        this.yapionObject = yapionObject;
    }

    /**
     * Creates a YAPIONDeserializer for deserializing a YAPIONObject with a specified context.
     *
     * @param yapionObject to deserialize
     * @param context the context for deserialization
     * @param typeReMapper the mapper to remap any '@type' variable to a new class
     */
    public YAPIONDeserializer(@NonNull YAPIONObject yapionObject, String context, @NonNull TypeReMapper typeReMapper) {
        contextManager = new ContextManager(context);
        this.yapionObject = yapionObject;
        this.typeReMapper = typeReMapper;
    }

    private YAPIONDeserializer(@NonNull YAPIONObject yapionObject, YAPIONDeserializer yapionDeserializer) {
        this.yapionObject = yapionObject;
        if (yapionDeserializer.contextManager.willBeCascading(yapionObject) && !yapionDeserializer.contextManager.isCascading()) {
            this.contextManager = new ContextManager(yapionDeserializer.contextManager.get());
        } else {
            this.contextManager = yapionDeserializer.contextManager;
        }
        this.pointerMap = yapionDeserializer.pointerMap;
        this.typeReMapper = yapionDeserializer.typeReMapper;
        this.deserializeResult = yapionDeserializer.deserializeResult;
        this.reducedMode = yapionDeserializer.reducedMode;
    }

    /**
     * @param yapionAnyType to parse
     * @return the Object of the {@link YAPIONAnyType} inputted
     */
    @SuppressWarnings({"java:S3740"})
    public Object parse(YAPIONAnyType yapionAnyType) {
        if (yapionAnyType instanceof YAPIONPointer) {
            MethodReturnValue<Object> objectOptional = ReflectionsUtils.invokeMethod("getYAPIONObject", yapionAnyType);
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
        if (yapionAnyType instanceof YAPIONValue) {
            return ((YAPIONValue) yapionAnyType).get();
        }
        if (yapionAnyType instanceof YAPIONObject) {
            return new YAPIONDeserializer((YAPIONObject) yapionAnyType, this).parse().getObject();
        }
        if (yapionAnyType instanceof YAPIONArray) {
            arrayType = checkType((YAPIONArray) yapionAnyType);
            return parseArray((YAPIONArray) yapionAnyType);
        }
        return null;
    }

    @SuppressWarnings({"java:S128", "java:S1117", "unchecked"})
    private String checkType(YAPIONArray yapionArray) {
        if (!arrayType.isEmpty()) return arrayType;
        String type = null;
        boolean primitive = true;
        for (int i = 0; i < yapionArray.length(); i++) {
            YAPIONAnyType yapionAnyType = yapionArray.getYAPIONAnyType(i);
            switch (yapionAnyType.getType()) {
                case OBJECT:
                    primitive = false;
                    YAPIONObject yapionObject = (YAPIONObject) yapionAnyType;
                    if (yapionObject.getYAPIONAnyType(TYPE_IDENTIFIER) != null) {
                        type = ((YAPIONValue<String>) yapionObject.getYAPIONAnyType(TYPE_IDENTIFIER)).get();
                        break;
                    }
                case MAP:
                    primitive = false;
                    type = "java.lang.Object";
                    break;
                case VALUE:
                    YAPIONValue<?> yapionValue = (YAPIONValue<?>) yapionAnyType;
                    if (yapionValue.getValueType().equals("null")) {
                        primitive = false;
                        break;
                    }
                    if (type != null) {
                        if (!yapionValue.getValueType().equals(type)) {
                            type = "java.lang.Object";
                        }
                        break;
                    }
                    type = yapionValue.getValueType();
                    break;
                case ARRAY:
                    String s = checkType((YAPIONArray) yapionAnyType);
                    if (type != null) {
                        if (ClassUtils.getBoxed(s).equals(type)) {
                            primitive = false;
                            break;
                        }
                        if (!s.equals(type)) {
                            type = "java.lang.Object";
                        }
                        break;
                    }
                    type = s;
                default:
                    break;
            }
        }
        if (type == null) type = "java.lang.Object";
        return primitive ? ClassUtils.getPrimitive(type) : type;
    }

    private Object parseArray(YAPIONArray yapionArray) {
        if (yapionArray == null) return null;
        LinkedList<Integer> dimensions = new LinkedList<>();
        YAPIONArray current = yapionArray;
        while (current != null) {
            dimensions.add(current.length());
            if (current.length() <= 0) {
                break;
            }
            YAPIONAnyType yapionAnyType = current.getYAPIONAnyType(0);
            current = null;
            if (yapionAnyType instanceof YAPIONArray) {
                current = (YAPIONArray) yapionAnyType;
            }
        }

        int[] ints = new int[dimensions.size()];
        for (int i = 0; i < dimensions.size(); i++) {
            ints[i] = dimensions.removeFirst();
        }

        arrayType = arrayType.replace("[", "").replace("]", "");
        Object array = null;
        try {
            array = Array.newInstance(ClassUtils.getClass(arrayType), ints);
        } catch (IllegalArgumentException | NegativeArraySizeException e) {
            // Ignored
        }
        if (array == null) return null;

        for (int i = 0; i < yapionArray.length(); i++) {
            Array.set(array, i, parse(yapionArray.getYAPIONAnyType(i)));
        }
        return array;
    }

    @SuppressWarnings({"java:S3740", "java:S3011", "java:S1117", "unchecked"})
    private YAPIONDeserializer parseObject(YAPIONObject yapionObject) {
        String type = ((YAPIONValue<String>) yapionObject.getYAPIONAnyType(TYPE_IDENTIFIER)).get();
        type = typeReMapper.remap(type);
        InternalSerializer<?> serializer = SerializeManager.getInternalSerializer(type);
        Object o = deserialize(serializer, yapionObject);
        if (o != null) {
            pointerMap.put(yapionObject, o);
            object = o;
            return this;
        }

        boolean loadWithoutAnnotation = serializer != null && serializer.loadWithoutAnnotation();
        boolean createWithObjenesis = serializer != null && serializer.createWithObjenesis();

        try {
            Class<?> clazz = Class.forName(type);
            if (!contextManager.is(clazz).load && !loadWithoutAnnotation) {
                throw new YAPIONDeserializerException("No suitable deserializer found, maybe class (" + type + ") is missing YAPION annotations");
            }

            object = SerializeManager.getObjectInstance(clazz, type, contextManager.is(clazz).data || createWithObjenesis);
            MethodManager.preDeserializationStep(object, object.getClass(), contextManager);
            pointerMap.put(yapionObject, object);

            for (String fieldName : yapionObject.getKeys()) {
                if (fieldName.equals(TYPE_IDENTIFIER)) continue;

                Field field = ReflectionsUtils.getField(clazz, fieldName);
                if (field == null) {
                    deserializeResult.add(object, fieldName, yapionObject.getYAPIONAnyType(fieldName));
                    continue;
                }
                if (ModifierUtils.removed(field)) continue;
                if (!contextManager.is(object, field).load && !loadWithoutAnnotation) continue;

                Class<?> fieldType = field.getType();
                arrayType = fieldType.getTypeName();

                YAPIONAnyType yapionAnyType = yapionObject.getYAPIONAnyType(field.getName());
                if (reducedMode && yapionAnyType instanceof YAPIONObject && !((YAPIONObject) yapionAnyType).hasValue(TYPE_IDENTIFIER, String.class)) {
                    if (fieldType.isEnum()) {
                        ((YAPIONObject) yapionAnyType).add(TYPE_IDENTIFIER, "java.lang.Enum");
                    } else {
                        ((YAPIONObject) yapionAnyType).add(TYPE_IDENTIFIER, arrayType);
                    }
                }
                YAPIONDeserializeType yapionDeserializeType = field.getDeclaredAnnotation(YAPIONDeserializeType.class);
                if (specialSet(field, yapionAnyType)) {
                    ReflectionsUtils.setValueOfField(field, object, yapionAnyType);
                } else if (isValid(field, yapionDeserializeType)) {
                    ReflectionsUtils.setValueOfField(field, object, deserialize(SerializeManager.getInternalSerializer(yapionDeserializeType.type().getTypeName()), yapionAnyType));
                } else {
                    ReflectionsUtils.setValueOfField(field, object, parse(yapionAnyType));
                }

                arrayType = "";
            }
            MethodManager.postDeserializationStep(object, object.getClass(), contextManager);
        } catch (ClassNotFoundException e) {
            log.warn("The class '" + type + "' was not found.", e.getCause());
        } catch (YAPIONReflectionException e) {
            log.warn("Exception while creating an Instance of the object '" + type + "'", e.getCause());
        }
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
                if (ifc.getTypeName().equals(type) && SerializeManager.getInternalSerializer(yapionDeserializeType.type().getTypeName()) != null) {
                    return true;
                }
            }
            clazz = clazz.getSuperclass();
        }
        return false;
    }

    private Object deserialize(InternalSerializer<?> serializer, YAPIONAnyType yapionAnyType) {
        if (serializer != null && !serializer.empty()) {
            if (serializer instanceof EnumSerializer) {
                YAPIONObject enumObject = (YAPIONObject) yapionAnyType;
                String enumType = enumObject.getValue(ENUM_IDENTIFIER, "").get();
                enumType = typeReMapper.remap(enumType);

                YAPIONObject enumCopyObject = new YAPIONObject();
                enumCopyObject.add(TYPE_IDENTIFIER, Enum.class.getTypeName());
                enumCopyObject.add(ENUM_IDENTIFIER, enumType);
                enumCopyObject.add("value", enumObject.getValue("value", "").get());
                enumCopyObject.add("ordinal", enumObject.getValue("ordinal", 0).get());

                return serializer.deserialize(new DeserializeData<>(enumCopyObject, contextManager.get(), this));
            }
            return serializer.deserialize(new DeserializeData<>(yapionAnyType, contextManager.get(), this));
        }
        return null;
    }

    /**
     * Set the reducedMode for this deserialization.
     */
    public YAPIONDeserializer reducedMode(boolean reducedMode) {
        this.reducedMode = reducedMode;
        return this;
    }

    /**
     * Parses the YAPIONObject to the Object.
     */
    public YAPIONDeserializer parse() {
        return parseObject(yapionObject);
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
        if (object == null) throw new YAPIONSerializerException();
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
