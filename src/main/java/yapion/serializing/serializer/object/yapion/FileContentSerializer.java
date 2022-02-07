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

package yapion.serializing.serializer.object.yapion;

import yapion.annotations.api.SerializerImplementation;
import yapion.exceptions.serializing.YAPIONDeserializerException;
import yapion.exceptions.serializing.YAPIONSerializerException;
import yapion.hierarchy.api.groups.YAPIONAnyType;
import yapion.hierarchy.types.YAPIONArray;
import yapion.hierarchy.types.YAPIONObject;
import yapion.hierarchy.types.YAPIONValue;
import yapion.serializing.data.DeserializeData;
import yapion.serializing.data.SerializeData;
import yapion.serializing.other.FileContent;
import yapion.serializing.serializer.FinalInternalSerializer;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.NoSuchElementException;

@SerializerImplementation(since = "0.26.0")
public class FileContentSerializer implements FinalInternalSerializer<FileContent> {

    @Override
    public Class<?> type() {
        return FileContent.class;
    }

    @Override
    public YAPIONAnyType serialize(SerializeData<FileContent> serializeData) {
        YAPIONObject yapionObject = new YAPIONObject(type());
        yapionObject.add("file", serializeData.serialize(serializeData.object.getFile()));
        try (InputStream inputStream = serializeData.object.getInputStream()) {
            YAPIONArray yapionArray = new YAPIONArray();
            while (inputStream.available() > 0) {
                yapionArray.add(inputStream.read());
            }
            yapionObject.add("content", yapionArray);
        } catch (Exception e) {
            throw new YAPIONSerializerException(e.getMessage(), e);
        }
        return yapionObject;
    }

    @Override
    public FileContent deserialize(DeserializeData<? extends YAPIONAnyType> deserializeData) {
        YAPIONObject yapionObject = (YAPIONObject) deserializeData.object;
        File file = deserializeData.deserialize(yapionObject.getObject("file"));
        FileContent fileContent = new FileContent(file);
        if (file.exists()) {
            YAPIONArray yapionArray = yapionObject.getArray("content");
            try (OutputStream outputStream = fileContent.getOutputStream()) {
                yapionArray.forEach(yapionAnyType -> {
                    int value = yapionAnyType.asValue().flatMap(YAPIONValue::asInt).orElseThrow(NoSuchElementException::new);
                    try {
                        outputStream.write(value);
                    } catch (IOException e) {
                        throw new YAPIONDeserializerException(e.getMessage(), e);
                    }
                });
            } catch (IOException e) {
                throw new YAPIONDeserializerException(e.getMessage(), e);
            }
        }
        return fileContent;
    }
}
