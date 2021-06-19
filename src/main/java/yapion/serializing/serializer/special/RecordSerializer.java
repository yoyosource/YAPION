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

package yapion.serializing.serializer.special;

import yapion.exceptions.YAPIONException;
import yapion.hierarchy.api.groups.YAPIONAnyType;
import yapion.hierarchy.types.YAPIONObject;
import yapion.serializing.InternalSerializer;
import yapion.serializing.SerializeManager;
import yapion.serializing.data.DeserializeData;
import yapion.serializing.data.SerializeData;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;

import static yapion.utils.IdentifierUtils.TYPE_IDENTIFIER;

/**
 * This Serializer is inspired by 'https://github.com/EsotericSoftware/kryo/blob/master/src/com/esotericsoftware/kryo/serializers/RecordSerializer.java'
 */
public class RecordSerializer implements InternalSerializer<Object> {

    private static Method isRecord;
    private static Method getRecordComponents;
    private static Method getName;
    private static Method getType;

    @Override
    public void init() {
        try {
            isRecord = Class.class.getDeclaredMethod("isRecord");
            Class<?> c = Class.forName("java.lang.reflect.RecordComponent");
            getRecordComponents = Class.class.getMethod("getRecordComponents");
            getName = c.getMethod("getName");
            getType = c.getMethod("getType");
        } catch (ClassNotFoundException | NoSuchMethodException e) {
            isRecord = null;
            getRecordComponents = null;
            getName = null;
            getType = null;
        }

        SerializeManager.isRecord = this::isRecord;
    }

    @Override
    public Class<?> type() {
        return null;
    }

    @Override
    public YAPIONAnyType serialize(SerializeData<Object> serializeData) {
        YAPIONObject yapionObject = new YAPIONObject(serializeData.object.getClass());
        yapionObject.add("recordValues", serializeData.serialize(recordComponents(serializeData.object.getClass(), serializeData.object)));
        return yapionObject;
    }

    @Override
    public Object deserialize(DeserializeData<? extends YAPIONAnyType> deserializeData) {
        YAPIONObject yapionObject = (YAPIONObject) deserializeData.object;
        String type = deserializeData.typeReMapper.remap(yapionObject.getPlainValue(TYPE_IDENTIFIER));
        RecordValue[] recordValues = deserializeData.deserialize(yapionObject.getArray("recordValues"));
        try {
            return invokeCanonicalConstructor(Class.forName(type), recordValues);
        } catch (ClassNotFoundException e) {
            throw new YAPIONException(e.getMessage(), e);
        }
    }

    private boolean isRecord(Class<?> type) {
        if (isRecord == null) {
            return false;
        }
        try {
            return (boolean) isRecord.invoke(type);
        } catch (Throwable t) {
            throw new YAPIONException("Could not determine type (" + type + ")");
        }
    }

    private static RecordValue[] recordComponents(Class<?> type, Object recordObject) {
        try {
            Object[] rawComponents = (Object[]) getRecordComponents.invoke(type);
            RecordValue[] recordValues = new RecordValue[rawComponents.length];
            for (int i = 0; i < rawComponents.length; i++) {
                final Object comp = rawComponents[i];
                String name = (String) getName.invoke(comp);
                recordValues[i] = new RecordValue(name, i, (Class<?>) getType.invoke(comp), componentValue(recordObject, name));
            }
            return recordValues;
        } catch (Throwable t) {
            throw new YAPIONException("Could not retrieve record components (" + type.getName() + ")", t);
        }
    }

    private static Object componentValue(Object recordObject, String name) {
        try {
            return recordObject.getClass().getDeclaredMethod(name).invoke(recordObject);
        } catch (Throwable t) {
            throw new YAPIONException("Could not retrieve record components (" + recordObject.getClass().getName() + ")", t);
        }
    }

    private static <T> T invokeCanonicalConstructor(Class<T> recordType, RecordValue[] recordValues) {
        try {
            Class<?>[] paramTypes = Arrays.stream(recordValues)
                    .map(RecordValue::getType)
                    .toArray(Class<?>[]::new);
            Constructor<T> canonicalConstructor = recordType.getConstructor(paramTypes);
            Object[] args = Arrays.stream(recordValues)
                    .map(RecordValue::getValue)
                    .toArray(Object[]::new);
            return canonicalConstructor.newInstance(args);
        } catch (Throwable t) {
            throw new YAPIONException("Could not construct type (" + recordType.getName() + ")", t);
        }
    }
}
