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

import lombok.SneakyThrows;
import yapion.annotations.api.SerializerImplementation;
import yapion.exceptions.YAPIONException;
import yapion.exceptions.serializing.YAPIONSerializerException;
import yapion.hierarchy.api.groups.YAPIONAnyType;
import yapion.hierarchy.types.YAPIONObject;
import yapion.serializing.InternalSerializer;
import yapion.serializing.YAPIONFlag;
import yapion.serializing.data.DeserializeData;
import yapion.serializing.data.SerializeData;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static yapion.utils.IdentifierUtils.TYPE_IDENTIFIER;

@SerializerImplementation(since = "0.25.0")
public class ClassSerializer implements InternalSerializer<Class<?>> {

    private InternalClassLoader internalClassLoader;

    private class InternalClassLoader extends ClassLoader {
        @SneakyThrows
        protected Class<?> load(String className, byte[] bytes) {
            return defineClass(className, bytes, 0, bytes.length);
        }
    }

    @Override
    public void init() {
        YAPIONFlag.CLASS_INJECTION.setFlagDefault(false);

        internalClassLoader = new InternalClassLoader();
    }

    @Override
    public Class<?> type() {
        return Class.class;
    }

    @Override
    public YAPIONAnyType serialize(SerializeData<Class<?>> serializeData) {
        YAPIONObject yapionObject = new YAPIONObject();
        yapionObject.add(TYPE_IDENTIFIER, type());
        yapionObject.add("class", serializeData.object.getTypeName());
        serializeData.isSet(YAPIONFlag.CLASS_INJECTION, () -> {
            try {
                InputStream inputStream = ClassSerializer.class.getResourceAsStream("/" + serializeData.object.getTypeName().replace(".", "/") + ".class");
                StringBuilder st = new StringBuilder();
                List<Byte> byteList = new ArrayList<>();
                while (inputStream.available() > 0) {
                    int i = inputStream.read();
                    if (i == -1) {
                        break;
                    }
                    byteList.add((byte) i);
                    st.append(String.format("%02X", i));
                }
                yapionObject.add("byteCode", st.toString());
            } catch (IOException e) {
                throw new YAPIONSerializerException(e.getMessage(), e);
            }
        });
        return yapionObject;
    }

    @Override
    public Class<?> deserialize(DeserializeData<? extends YAPIONAnyType> deserializeData) {
        YAPIONObject yapionObject = (YAPIONObject) deserializeData.object;
        YAPIONException yapionException;
        try {
            return Class.forName(yapionObject.getPlainValue("class"));
        } catch (ClassNotFoundException e) {
            yapionException = new YAPIONException(e.getMessage(), e);
            if (!deserializeData.getYAPIONFlags().isSet(YAPIONFlag.CLASS_INJECTION)) {
                throw yapionException;
            }
        }
        try {
            if (!yapionObject.containsKey("className")) {
                throw yapionException;
            }
            if (!yapionObject.containsKey("byteCode")) {
                throw yapionException;
            }
            String className = yapionObject.getPlainValue("className");
            StringBuilder st = new StringBuilder(yapionObject.getValue("byteCode", "").get());
            byte[] bytes = new byte[st.length() / 2];
            for (int i = 0; i < bytes.length; i++) {
                bytes[i] = (byte) Integer.parseInt("" + st.charAt(i * 2) + st.charAt(i * 2 + 1), 16);
            }
            return internalClassLoader.load(className, bytes);
        } catch (Exception e) {
            throw yapionException;
        }
    }
}
