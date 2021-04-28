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

package yapion.serializing.serializer.object.atomic;

import yapion.annotations.api.SerializerImplementation;
import yapion.hierarchy.api.groups.YAPIONAnyType;
import yapion.hierarchy.types.YAPIONObject;
import yapion.serializing.InternalSerializer;
import yapion.serializing.data.DeserializeData;
import yapion.serializing.data.SerializeData;

import java.util.concurrent.atomic.LongAdder;

import static yapion.utils.IdentifierUtils.TYPE_IDENTIFIER;

@SerializerImplementation(since = "0.24.0")
public class LongAdderSerializer implements InternalSerializer<LongAdder> {

    @Override
    public Class<?> type() {
        return LongAdder.class;
    }

    @Override
    public YAPIONAnyType serialize(SerializeData<LongAdder> serializeData) {
        YAPIONObject yapionObject = new YAPIONObject();
        yapionObject.add(TYPE_IDENTIFIER, type());
        yapionObject.add("value", serializeData.object.longValue());
        return yapionObject;
    }

    @Override
    public LongAdder deserialize(DeserializeData<? extends YAPIONAnyType> deserializeData) {
        long l = ((YAPIONObject) deserializeData.object).getPlainValue("value");
        LongAdder longAdder = new LongAdder();
        longAdder.add(l);
        return longAdder;
    }
}
