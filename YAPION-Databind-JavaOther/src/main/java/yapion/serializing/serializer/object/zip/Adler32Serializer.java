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

import java.util.zip.Adler32;

@SerializerImplementation(since = "0.24.0")
public class Adler32Serializer implements FinalInternalSerializer<Adler32> {

    @Override
    public Class<?> type() {
        return Adler32.class;
    }

    @Override
    public YAPIONAnyType serialize(SerializeData<Adler32> serializeData) {
        YAPIONObject yapionObject = new YAPIONObject(type());
        yapionObject.add("adler", serializeData.serializeField("adler"));
        return yapionObject;
    }

    @Override
    public Adler32 deserialize(DeserializeData<? extends YAPIONAnyType> deserializeData) {
        int adler = ((YAPIONObject) deserializeData.object).getPlainValue("adler");
        Adler32 adler32 = new Adler32();
        deserializeData.setField("adler", adler32, adler);
        return adler32;
    }
}
