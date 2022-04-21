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

import org.bukkit.Material;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import yapion.hierarchy.api.groups.YAPIONAnyType;
import yapion.hierarchy.types.YAPIONObject;
import yapion.hierarchy.types.YAPIONType;
import yapion.serializing.data.DeserializeData;
import yapion.serializing.data.SerializeData;
import yapion.serializing.serializer.FinalInternalSerializer;

import java.util.Map;

public class ItemStackSerializer implements FinalInternalSerializer<ItemStack> {

    @Override
    public Class<?> type() {
        return ItemStack.class;
    }

    @Override
    public YAPIONAnyType serialize(SerializeData<ItemStack> serializeData) {
        YAPIONObject yapionObject = new YAPIONObject(type());
        yapionObject.add("material", serializeData.serialize(serializeData.object.getType()));
        yapionObject.add("amount", serializeData.serialize(serializeData.object.getAmount()));
        if (serializeData.object.getItemMeta() != null) {
            Map<String, Object> metaMap = serializeData.object.getItemMeta().serialize();
            yapionObject.add("meta", serializeData.serialize(metaMap));
        }
        return yapionObject;
    }

    @Override
    public ItemStack deserialize(DeserializeData<? extends YAPIONAnyType> deserializeData) {
        YAPIONObject yapionObject = (YAPIONObject) deserializeData.object;
        Material material = (Material) deserializeData.deserialize(yapionObject.getObject("material"));
        ItemStack itemStack = new ItemStack(material);
        itemStack.setAmount(yapionObject.getInt("amount"));
        if (yapionObject.containsKey("meta", YAPIONType.OBJECT)) {
            Map<String, Object> metaMap = deserializeData.deserialize(yapionObject.getObject("meta"));
            itemStack.setItemMeta((ItemMeta) ConfigurationSerialization.deserializeObject(metaMap));
        }
        return itemStack;
    }
}
