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

package yapion.serializing.serializer.object.time;

import yapion.hierarchy.api.groups.YAPIONAnyType;
import yapion.hierarchy.types.YAPIONObject;
import yapion.serializing.InternalSerializer;
import yapion.serializing.data.DeserializeData;
import yapion.serializing.data.SerializeData;
import yapion.annotations.api.SerializerImplementation;

import java.time.ZoneOffset;

import static yapion.utils.IdentifierUtils.TYPE_IDENTIFIER;

@SerializerImplementation(since = "0.24.0")
public class ZoneOffsetSerializer implements InternalSerializer<ZoneOffset> {

    @Override
    public String type() {
        return "java.time.ZoneOffset";
    }

    @Override
    public YAPIONAnyType serialize(SerializeData<ZoneOffset> serializeData) {
        YAPIONObject yapionObject = new YAPIONObject();
        yapionObject.add(TYPE_IDENTIFIER, type());
        yapionObject.add("totalSeconds", serializeData.object.getTotalSeconds());
        yapionObject.add("id", serializeData.object.getId());
        return yapionObject;
    }

    @Override
    public ZoneOffset deserialize(DeserializeData<? extends YAPIONAnyType> deserializeData) {
        YAPIONObject yapionObject = (YAPIONObject) deserializeData.object;
        int totalSeconds = yapionObject.getPlainValue("totalSeconds");
        return ZoneOffset.ofTotalSeconds(totalSeconds);
    }
}
