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

import org.bukkit.Location;
import org.bukkit.World;
import yapion.hierarchy.api.groups.YAPIONAnyType;
import yapion.hierarchy.types.YAPIONObject;
import yapion.serializing.data.DeserializeData;
import yapion.serializing.data.SerializeData;
import yapion.serializing.serializer.FinalInternalSerializer;

public class LocationSerializer implements FinalInternalSerializer<Location> {

    @Override
    public Class<?> type() {
        return Location.class;
    }

    @Override
    public YAPIONAnyType serialize(SerializeData<Location> serializeData) {
        YAPIONObject yapionObject = new YAPIONObject(type());
        yapionObject.add("world", serializeData.serialize(serializeData.object.getWorld()));
        yapionObject.add("x", serializeData.object.getX());
        yapionObject.add("y", serializeData.object.getY());
        yapionObject.add("z", serializeData.object.getZ());
        yapionObject.add("pitch", serializeData.object.getPitch());
        yapionObject.add("yaw", serializeData.object.getYaw());
        return yapionObject;
    }

    @Override
    public Location deserialize(DeserializeData<? extends YAPIONAnyType> deserializeData) {
        YAPIONObject yapionObject = (YAPIONObject) deserializeData.object;
        World world = (World) deserializeData.deserialize(yapionObject.getObject("world"));
        double x = yapionObject.getPlainValue("x");
        double y = yapionObject.getPlainValue("y");
        double z = yapionObject.getPlainValue("z");
        float pitch = yapionObject.getPlainValue("pitch");
        float yaw = yapionObject.getPlainValue("yaw");
        return new Location(world, x, y, z, yaw, pitch);
    }
}
