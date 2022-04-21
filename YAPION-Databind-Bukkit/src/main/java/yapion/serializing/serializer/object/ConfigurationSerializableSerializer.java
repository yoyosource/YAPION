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

package yapion.serializing.serializer.object;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import yapion.hierarchy.api.groups.YAPIONAnyType;
import yapion.hierarchy.types.YAPIONObject;
import yapion.serializing.data.DeserializeData;
import yapion.serializing.data.SerializeData;
import yapion.serializing.serializer.FinalInternalSerializer;
import yapion.utils.IdentifierUtils;

import java.util.HashMap;
import java.util.Map;

public class ConfigurationSerializableSerializer implements FinalInternalSerializer<ConfigurationSerializable> {

    @Override
    public Class<?> type() {
        return ConfigurationSerializable.class;
    }

    @Override
    public Class<?> interfaceType() {
        return ConfigurationSerializable.class;
    }

    @Override
    public YAPIONAnyType serialize(SerializeData<ConfigurationSerializable> serializeData) {
        YAPIONObject yapionObject = new YAPIONObject(type());
        Map<String, Object> serializeDataMap = serializeData.object.serialize();
        serializeDataMap.forEach((s, o) -> {
            yapionObject.add(s, serializeData.serialize(o));
        });
        return yapionObject;
    }

    @Override
    public ConfigurationSerializable deserialize(DeserializeData<? extends YAPIONAnyType> deserializeData) {
        YAPIONObject yapionObject = (YAPIONObject) deserializeData.object;
        Map<String, Object> deserializeDataMap = new HashMap<>();
        yapionObject.forEach((s, yapionAnyType) -> {
            if (s.equals(IdentifierUtils.TYPE_IDENTIFIER)) return;
            deserializeDataMap.put(s, deserializeData.deserialize(yapionAnyType));
        });
        return ConfigurationSerialization.deserializeObject(deserializeDataMap);
    }
}
