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

package yapion.serializing.serializer.object.other;

import yapion.exceptions.serializing.YAPIONDeserializerException;
import yapion.hierarchy.api.groups.YAPIONAnyType;
import yapion.hierarchy.types.YAPIONMap;
import yapion.hierarchy.types.YAPIONObject;
import yapion.serializing.InternalSerializer;
import yapion.serializing.data.DeserializeData;
import yapion.serializing.data.SerializeData;
import yapion.annotations.api.SerializerImplementation;

import java.util.EnumMap;

import static yapion.utils.IdentifierUtils.TYPE_IDENTIFIER;

@SerializerImplementation(since = "0.24.0")
public class EnumMapSerializer implements InternalSerializer<EnumMap<?, ?>> {

    @Override
    public String type() {
        return "java.util.EnumMap";
    }

    @Override
    public YAPIONAnyType serialize(SerializeData<EnumMap<?, ?>> serializeData) {
        YAPIONObject yapionObject = new YAPIONObject();
        yapionObject.add(TYPE_IDENTIFIER, type());
        yapionObject.add("keyType", ((Class<?>) serializeData.getField("keyType")).getTypeName());
        YAPIONMap yapionMap = new YAPIONMap();
        yapionObject.add("values", yapionMap);
        serializeData.object.forEach((anEnum, o) -> {
            yapionMap.add(serializeData.serialize(anEnum), serializeData.serialize(o));
        });
        return yapionObject;
    }

    @Override
    @SuppressWarnings({"unchecked", "java:S3740"})
    public EnumMap<?, ?> deserialize(DeserializeData<? extends YAPIONAnyType> deserializeData) {
        YAPIONObject yapionObject = (YAPIONObject) deserializeData.object;
        String keyType = yapionObject.getPlainValue("keyType");
        try {
            Class<Enum> clazz = (Class<Enum>) Class.forName(keyType);
            EnumMap enumMap = new EnumMap(clazz);
            YAPIONMap yapionMap = yapionObject.getMap("values");
            yapionMap.forEach((yapionAnyType, yapionAnyType2) -> {
                enumMap.put((Enum) deserializeData.deserialize(yapionAnyType), deserializeData.deserialize(yapionAnyType2));
            });
            return enumMap;
        } catch (ClassNotFoundException e) {
            throw new YAPIONDeserializerException(e.getMessage(), e);
        }
    }
}
