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

import com.google.common.collect.*;
import lombok.SneakyThrows;
import yapion.hierarchy.api.groups.YAPIONAnyType;
import yapion.hierarchy.types.YAPIONMap;
import yapion.hierarchy.types.YAPIONObject;
import yapion.serializing.data.DeserializeData;
import yapion.serializing.data.SerializeData;
import yapion.serializing.serializer.FinalInternalSerializer;
import yapion.utils.IdentifierUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class MultimapSerializer implements FinalInternalSerializer<Multimap<?, ?>> {

    private Map<Class<?>, Supplier<Multimap<?, ?>>> wrapper = new HashMap<>();

    @Override
    public void init() {
        wrapper = new HashMap<>();
        wrapper.put(ArrayListMultimap.class, ArrayListMultimap::create);
        wrapper.put(HashMultimap.class, HashMultimap::create);
        wrapper.put(LinkedHashMultimap.class, LinkedHashMultimap::create);
        wrapper.put(LinkedListMultimap.class, LinkedListMultimap::create);
        wrapper.put(TreeMultimap.class, TreeMultimap::create);
    }

    @Override
    public Class<?> type() {
        return Multimap.class;
    }

    @Override
    public Class<?> interfaceType() {
        return Multimap.class;
    }

    @Override
    public YAPIONAnyType serialize(SerializeData<Multimap<?, ?>> serializeData) {
        YAPIONObject yapionObject = new YAPIONObject(serializeData.object.getClass());
        YAPIONMap yapionMap = new YAPIONMap();
        serializeData.object.asMap().forEach((o, objects) -> {
            yapionMap.put(serializeData.serialize(o), serializeData.serialize(objects));
        });
        yapionObject.put("map", yapionMap);
        return yapionObject;
    }

    @Override
    @SneakyThrows
    public Multimap<?, ?> deserialize(DeserializeData<? extends YAPIONAnyType> deserializeData) {
        YAPIONObject yapionObject = (YAPIONObject) deserializeData.object;
        YAPIONMap yapionMap = yapionObject.getMap("map");
        Multimap multimap = wrapper.get(Class.forName(yapionObject.getString(IdentifierUtils.TYPE_IDENTIFIER))).get();
        yapionMap.forEach((o, objects) -> {
            List<?> elements = deserializeData.deserialize(objects);
            for (Object element : elements) {
                multimap.put(deserializeData.deserialize(o), element);
            }
        });
        return multimap;
    }
}
