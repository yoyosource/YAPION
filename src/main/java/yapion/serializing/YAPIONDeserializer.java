// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020 yoyosource

package yapion.serializing;

import lombok.NonNull;
import lombok.extern.log4j.Log4j;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import yapion.annotations.deserialize.YAPIONDeserializeType;
import yapion.annotations.deserialize.YAPIONLoadExclude;
import yapion.annotations.object.YAPIONPostDeserialization;
import yapion.annotations.object.YAPIONPreDeserialization;
import yapion.annotations.serialize.YAPIONSaveExclude;
import yapion.exceptions.serializing.YAPIONDeserializerException;
import yapion.exceptions.utils.YAPIONReflectionException;
import yapion.hierarchy.typegroups.YAPIONAnyType;
import yapion.hierarchy.types.YAPIONArray;
import yapion.hierarchy.types.YAPIONObject;
import yapion.hierarchy.types.YAPIONPointer;
import yapion.hierarchy.types.YAPIONValue;
import yapion.serializing.data.DeserializeData;
import yapion.serializing.serializer.object.other.EnumSerializer;
import yapion.utils.ModifierUtils;
import yapion.utils.ReflectionsUtils;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

import static yapion.utils.IdentifierUtils.ENUM_IDENTIFIER;
import static yapion.utils.IdentifierUtils.TYPE_IDENTIFIER;

@YAPIONSaveExclude(context = "*")
@YAPIONLoadExclude(context = "*")
@Slf4j
public final class YAPIONDeserializer {

    private static int cacheSize = 100;

    private static final Map<String, ObjectCache> methodMap = new LinkedHashMap<String, ObjectCache>() {
        @Override
        protected boolean removeEldestEntry(Map.Entry<String, ObjectCache> eldest) {
            return size() > cacheSize;
        }
    };

    /**
     * Set the cache size of the internal cache to a specific
     * number above 100. If you set a number below 100 it will
     * default to 100.
     *
     * @param cacheSize the cache Size
     */
    public static void setCacheSize(int cacheSize) {
        if (cacheSize < 100) {
            cacheSize = 100;
        }
        YAPIONDeserializer.cacheSize = cacheSize;
    }

    /**
     * Discard the cache used by {@link #preDeserializationStep()}
     * and {@link #postDeserializationStep()}.
     */
    public static void discardCache() {
        methodMap.clear();
    }

    private Object object;
    private final YAPIONObject yapionObject;
    private final ContextManager contextManager;
    private TypeReMapper typeReMapper = new TypeReMapper();
    private DeserializeResult deserializeResult = new DeserializeResult();

    private Map<YAPIONObject, Object> pointerMap = new IdentityHashMap<>();

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
        if (yapionDeserializer.contextManager.isCascading()) {
            this.contextManager = yapionDeserializer.contextManager;
        } else {
            this.contextManager = new ContextManager(yapionDeserializer.contextManager.get());
        }
        this.pointerMap = yapionDeserializer.pointerMap;
        this.typeReMapper = yapionDeserializer.typeReMapper;
        this.deserializeResult = yapionDeserializer.deserializeResult;
    }

    /**
     * @param yapionAnyType to parse
     * @param yapionDeserializer to parse with
     * @return the Object of the {@link YAPIONAnyType} inputted
     * @deprecated since 0.12.0 use {@link #parse(YAPIONAnyType)} instead
     */
    @Deprecated
    public Object parse(YAPIONAnyType yapionAnyType, YAPIONDeserializer yapionDeserializer) {
        return parse(yapionAnyType);
    }

    /**
     * @param yapionAnyType to parse
     * @return the Object of the {@link YAPIONAnyType} inputted
     */
    @SuppressWarnings({"java:S3740"})
    public Object parse(YAPIONAnyType yapionAnyType) {
        if (yapionAnyType instanceof YAPIONPointer) {
            Optional<Object> objectOptional = ReflectionsUtils.invokeMethod("getYAPIONObject", yapionAnyType);
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
            return parseArray((YAPIONArray) yapionAnyType);
        }
        return null;
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
            YAPIONAnyType yapionAnyType = current.get(0);
            current = null;
            if (yapionAnyType instanceof YAPIONArray) {
                current = (YAPIONArray) yapionAnyType;
            }
        }

        int[] ints = new int[dimensions.size()];
        for (int i = 0; i < dimensions.size(); i++) {
            ints[i] = dimensions.removeFirst();
        }

        while (arrayType.charAt(arrayType.length() - 1) == ']') {
            arrayType = arrayType.substring(0, arrayType.length() - 2);
        }
        Object array = null;
        try {
            array = Array.newInstance(getClass(arrayType), ints);
        } catch (IllegalArgumentException | NegativeArraySizeException e) {
            // Ignored
        }
        if (array == null) return null;

        for (int i = 0; i < yapionArray.length(); i++) {
            Array.set(array, i, parse(yapionArray.get(i)));
        }
        return array;
    }

    public Class<?> getClass(String className) {
        switch (className) {
            case "boolean":
                return boolean.class;
            case "byte":
                return byte.class;
            case "short":
                return short.class;
            case "int":
                return int.class;
            case "long":
                return long.class;
            case "char":
                return char.class;
            case "float":
                return float.class;
            case "double":
                return double.class;
            default:
                break;
        }
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            return Object.class;
        }
    }

    private Object serialize(YAPIONAnyType yapionAnyType, String type) {
        InternalSerializer<?> serializer = SerializeManager.getInternalSerializer(type);
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

    @SuppressWarnings({"java:S3740", "java:S3011", "java:S1117", "unchecked"})
    private YAPIONDeserializer parseObject(YAPIONObject yapionObject) {
        String type = ((YAPIONValue<String>) yapionObject.getVariable(TYPE_IDENTIFIER).getValue()).get();
        type = typeReMapper.remap(type);
        Object o = serialize(yapionObject, type);
        if (o != null) {
            object = o;
            return this;
        }

        try {
            Class<?> clazz = Class.forName(type);
            if (!contextManager.is(clazz).load) {
                throw new YAPIONDeserializerException("No suitable deserializer found, maybe class (" + type + ") is missing YAPION annotations");
            }

            object = SerializeManager.getObjectInstance(clazz, type, contextManager);
            preDeserializationStep();
            pointerMap.put(yapionObject, object);

            for (String fieldName : yapionObject.getKeys()) {
                if (fieldName.equals(TYPE_IDENTIFIER)) continue;

                Field field = null;
                try {
                    field = clazz.getDeclaredField(fieldName);
                } catch (NoSuchFieldException | SecurityException e) {
                    // Ignored
                }
                if (field == null) {
                    deserializeResult.add(object, fieldName, yapionObject.getVariable(fieldName).getValue());
                    continue;
                }
                field.setAccessible(true);
                if (ModifierUtils.removed(field)) continue;
                if (!contextManager.is(object, field).load) continue;

                arrayType = remove(field.getType().getTypeName(), '[', ']');

                YAPIONAnyType yapionAnyType = yapionObject.getVariable(field.getName()).getValue();
                YAPIONDeserializeType yapionDeserializeType = field.getDeclaredAnnotation(YAPIONDeserializeType.class);
                if (isValid(field, yapionDeserializeType)) {
                    field.set(object, serialize(yapionAnyType, yapionDeserializeType.type().getTypeName()));
                } else {
                    field.set(object, parse(yapionAnyType));
                }
            }

            /*
            Field[] fields = object.getClass().getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                if (ModifierUtils.removed(field)) {
                    continue;
                }
                if (!contextManager.is(object, field).load) continue;
                if (yapionObject.getVariable(field.getName()) == null) continue;

                arrayType = remove(field.getType().getTypeName(), '[', ']');

                YAPIONAnyType yapionAnyType = yapionObject.getVariable(field.getName()).getValue();
                YAPIONDeserializeType yapionDeserializeType = field.getDeclaredAnnotation(YAPIONDeserializeType.class);
                if (isValid(field, yapionDeserializeType)) {
                    field.set(object, serialize(yapionAnyType, yapionDeserializeType.type().getTypeName()));
                } else {
                    field.set(object, parse(yapionAnyType));
                }
            }*/
            postDeserializationStep();
        } catch (ClassNotFoundException e) {
            log.warn("The class '" + type + "' was not found.", e.getCause());
        } catch (IllegalAccessException e) {
            log.warn("", e.getCause());
        } catch (YAPIONReflectionException e) {
            log.warn("Exception while creating an Instance of the object '" + type + "'", e.getCause());
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        return this;
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

    private class ObjectCache {

        private final Map<String, Method> preCache = new HashMap<>();
        private final Map<String, Method> postCache = new HashMap<>();

        public ObjectCache(Object object) {
            Method[] methods = object.getClass().getDeclaredMethods();
            for (Method method : methods) {
                if (method.getParameterCount() != 0) continue;
                YAPIONPreDeserialization yapionPreDeserialization = method.getAnnotation(YAPIONPreDeserialization.class);
                YAPIONPostDeserialization yapionPostDeserialization = method.getAnnotation(YAPIONPostDeserialization.class);

                if (yapionPreDeserialization != null) cache(preCache, yapionPreDeserialization.context(), method);
                if (yapionPostDeserialization != null) cache(postCache, yapionPostDeserialization.context(), method);
            }
        }

        private void cache(Map<String, Method> cache, String[] context, Method method) {
            for (String s : context) {
                cache.put(s, method);
            }
            if (context.length == 0) {
                cache.put("", method);
            }
        }

        private void pre(Object object, ContextManager contextManager) {
            String state = contextManager.get();
            if (!preCache.containsKey(state)) return;
            ReflectionsUtils.invokeMethod(preCache.get(state), object);
        }

        private void post(Object object, ContextManager contextManager) {
            String state = contextManager.get();
            if (!postCache.containsKey(state)) return;
            ReflectionsUtils.invokeMethod(postCache.get(state), object);
        }
    }

    private void preDeserializationStep() {
        String key = object.getClass().getTypeName();
        if (!methodMap.containsKey(key)) {
            methodMap.put(key, new ObjectCache(object));
        }
        methodMap.get(key).pre(object, contextManager);
    }

    private void postDeserializationStep() {
        String key = object.getClass().getTypeName();
        if (!methodMap.containsKey(key)) {
            methodMap.put(key, new ObjectCache(object));
        }
        methodMap.get(key).post(object, contextManager);
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
     * Get the {@link DeserializeResult}.
     *
     * @return {@link DeserializeResult} used by this instance
     */
    public DeserializeResult getDeserializeResult() {
        return deserializeResult;
    }

}