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

import org.bukkit.Bukkit;
import org.bukkit.World;
import yapion.hierarchy.api.groups.YAPIONAnyType;
import yapion.hierarchy.types.YAPIONObject;
import yapion.hierarchy.types.YAPIONValue;
import yapion.serializing.data.DeserializeData;
import yapion.serializing.data.SerializeData;
import yapion.serializing.serializer.FinalInternalSerializer;

import java.util.UUID;

public class WorldSerializer implements FinalInternalSerializer<World> {

    @Override
    public Class<?> type() {
        return World.class;
    }

    @Override
    public Class<?> interfaceType() {
        return World.class;
    }

    @Override
    public YAPIONAnyType serialize(SerializeData<World> serializeData) {
        YAPIONObject yapionObject = new YAPIONObject(type());
        yapionObject.add("name", new YAPIONValue<>(serializeData.object.getName()));
        yapionObject.add("uid", serializeData.serialize(serializeData.object.getUID()));
        return yapionObject;
    }

    @Override
    public World deserialize(DeserializeData<? extends YAPIONAnyType> deserializeData) {
        YAPIONObject yapionObject = (YAPIONObject) deserializeData.object;
        return Bukkit.getWorld((UUID) deserializeData.deserialize(yapionObject.getObject("uid")));
    }
}
