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

package yapion.serializing.serializer.object.other;

import yapion.annotations.api.SerializerImplementation;
import yapion.exceptions.YAPIONException;
import yapion.exceptions.serializing.YAPIONDeserializerException;
import yapion.exceptions.serializing.YAPIONSerializerException;
import yapion.hierarchy.api.groups.YAPIONAnyType;
import yapion.hierarchy.types.YAPIONObject;
import yapion.serializing.YAPIONFlag;
import yapion.serializing.data.DeserializeData;
import yapion.serializing.data.SerializeData;
import yapion.serializing.serializer.FinalInternalSerializer;
import yapion.utils.ReflectionsUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

@SerializerImplementation(since = "0.25.0")
public class ClassSerializer implements FinalInternalSerializer<Class<?>> {

    private static final Map<Class<?>, String> byteCodes = new IdentityHashMap<>();

    public static void reset() {
        byteCodes.clear();
    }

    @Override
    public Class<?> type() {
        return Class.class;
    }

    @Override
    public YAPIONAnyType serialize(SerializeData<Class<?>> serializeData) {
        YAPIONObject yapionObject = new YAPIONObject(type());
        yapionObject.add("class", serializeData.object);

        if (!serializeData.getYAPIONFlags().isSet(YAPIONFlag.CLASS_INJECTION)) {
            return yapionObject;
        }
        ClassLoader classLoader = serializeData.object.getClassLoader();
        if (classLoader == null) {
            return yapionObject;
        }

        if (byteCodes.containsKey(serializeData.object)) {
            yapionObject.add("byteCode", byteCodes.get(serializeData.object));
            return yapionObject;
        }
        try {
            InputStream inputStream = ClassSerializer.class.getResourceAsStream("/" + serializeData.object.getTypeName().replace(".", "/") + ".class");
            if (inputStream == null) {
                return yapionObject;
            }

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            GZIPOutputStream gzipOutputStream = new GZIPOutputStream(byteArrayOutputStream);
            while (inputStream.available() > 0) {
                gzipOutputStream.write(inputStream.read());
            }
            gzipOutputStream.close();

            String byteCode = Base64.getEncoder().encodeToString(byteArrayOutputStream.toByteArray());
            byteCodes.put(serializeData.object, byteCode);
            yapionObject.add("byteCode", byteCode);
        } catch (IOException e) {
            throw new YAPIONSerializerException(e.getMessage(), e);
        }
        return yapionObject;
    }

    @Override
    public Class<?> deserialize(DeserializeData<? extends YAPIONAnyType> deserializeData) {
        YAPIONObject yapionObject = (YAPIONObject) deserializeData.object;
        YAPIONException yapionException;
        try {
            return Class.forName(yapionObject.getPlainValue("class"));
        } catch (ClassNotFoundException e) {
            yapionException = new YAPIONDeserializerException(e.getMessage(), e);
        }

        if (!deserializeData.getYAPIONFlags().isSet(YAPIONFlag.CLASS_INJECTION)) {
            throw yapionException;
        }
        if (!yapionObject.containsKey("class")) {
            throw yapionException;
        }
        if (!yapionObject.containsKey("byteCode")) {
            throw yapionException;
        }

        String st = yapionObject.getPlainValue("byteCode");
        byte[] bytes = Base64.getDecoder().decode(st);
        try {
            GZIPInputStream gzipInputStream = new GZIPInputStream(new ByteArrayInputStream(bytes));
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

            while (gzipInputStream.available() > 0) {
                byteArrayOutputStream.write(gzipInputStream.read());
            }
            bytes = byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            throw yapionException;
        }

        try {
            String className = yapionObject.getPlainValue("class");
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            Class<?> clazz = (Class<?>) ReflectionsUtils.invokeMethod("defineClass", classLoader, className, bytes, 0, bytes.length).get();
            byteCodes.put(clazz, st);
            return clazz;
        } catch (Exception e) {
            throw yapionException;
        }
    }
}
