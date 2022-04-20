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

package yapion.serializing.serializer.object.io;

import yapion.annotations.api.SerializerImplementation;
import yapion.hierarchy.api.groups.YAPIONAnyType;
import yapion.hierarchy.types.YAPIONObject;
import yapion.serializing.data.DeserializeData;
import yapion.serializing.data.SerializeData;
import yapion.serializing.serializer.FinalInternalSerializer;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

@SerializerImplementation(since = "0.26.0")
public class ByteArrayInputStreamSerializer implements FinalInternalSerializer<ByteArrayInputStream> {

    @Override
    public Class<?> type() {
        return ByteArrayInputStream.class;
    }

    @Override
    public YAPIONAnyType serialize(SerializeData<ByteArrayInputStream> serializeData) {
        YAPIONObject yapionObject = new YAPIONObject(type());
        List<Integer> byteList = new ArrayList<>();
        while (serializeData.object.available() > 0) {
            byteList.add(serializeData.object.read());
        }
        yapionObject.add("bytes", serializeData.serialize(byteList));
        return yapionObject;
    }

    @Override
    public ByteArrayInputStream deserialize(DeserializeData<? extends YAPIONAnyType> deserializeData) {
        YAPIONObject yapionObject = (YAPIONObject) deserializeData.object;
        List<Integer> byteList = deserializeData.deserialize(yapionObject.getObject("bytes"));
        byte[] bytes = new byte[byteList.size()];
        for (int i = 0; i < byteList.size(); i++) {
            bytes[i] = (byte) (int) byteList.get(i);
        }
        return new ByteArrayInputStream(bytes);
    }
}
