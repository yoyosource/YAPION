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

package yapion.serializing.serializer.object.zip;

import yapion.annotations.api.SerializerImplementation;
import yapion.hierarchy.api.groups.YAPIONAnyType;
import yapion.hierarchy.types.YAPIONObject;
import yapion.serializing.data.DeserializeData;
import yapion.serializing.data.SerializeData;
import yapion.serializing.serializer.FinalInternalSerializer;

import java.util.zip.CRC32;

@SerializerImplementation(since = "0.24.0")
public class CRC32Serializer implements FinalInternalSerializer<CRC32> {

    @Override
    public Class<?> type() {
        return CRC32.class;
    }

    @Override
    public YAPIONAnyType serialize(SerializeData<CRC32> serializeData) {
        YAPIONObject yapionObject = new YAPIONObject(type());
        yapionObject.add("crc", serializeData.serializeField("crc"));
        return yapionObject;
    }

    @Override
    public CRC32 deserialize(DeserializeData<? extends YAPIONAnyType> deserializeData) {
        int crc = ((YAPIONObject) deserializeData.object).getPlainValue("crc");
        CRC32 crc32 = new CRC32();
        deserializeData.setField("crc", crc32, crc);
        return crc32;
    }
}
