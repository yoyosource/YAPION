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

package yapion.serializing.serializer.special;

import yapion.annotations.api.DeprecationInfo;
import yapion.annotations.api.InternalAPI;
import yapion.exceptions.serializing.YAPIONDeserializerException;
import yapion.hierarchy.api.groups.YAPIONAnyType;
import yapion.hierarchy.types.YAPIONObject;
import yapion.serializing.InternalSerializer;
import yapion.serializing.data.DeserializeData;
import yapion.serializing.data.SerializeData;

import static yapion.utils.IdentifierUtils.ENUM_IDENTIFIER;
import static yapion.utils.IdentifierUtils.TYPE_IDENTIFIER;

@SuppressWarnings({"java:S1192"})
@InternalAPI // This Serializer exists since version 0.10.0
public class EnumSerializer implements InternalSerializer<Enum<?>> {

    @Override
    public Class<?> type() {
        return null;
    }

    @Override
    public YAPIONAnyType serialize(SerializeData<Enum<?>> serializeData) {
        YAPIONObject yapionObject = new YAPIONObject();
        yapionObject.add(TYPE_IDENTIFIER, serializeData.object.getClass().getTypeName());
        yapionObject.add("value", serializeData.object.name());
        yapionObject.add("ordinal", serializeData.object.ordinal());
        return yapionObject;
    }

    @Override
    public Enum<?> deserialize(DeserializeData<? extends YAPIONAnyType> deserializeData) {
        YAPIONObject yapionObject = (YAPIONObject) deserializeData.object;
        String type = deserializeData.typeReMapper.remap(getType(yapionObject));
        String enumType = yapionObject.getPlainValue("value");
        int ordinal = -1;
        if (yapionObject.getValue("ordinal", 0) != null) {
            ordinal = yapionObject.getPlainValue("ordinal");
        }
        try {
            Class<?> clazz = Class.forName(type);
            if (!clazz.isEnum()) {
                throw new YAPIONDeserializerException("Class " + type + " is not an enum");
            }
            Enum<?>[] enums = (Enum<?>[]) clazz.getEnumConstants();
            if (ordinal >= 0 && ordinal < enums.length && enums[ordinal].name().equals(enumType)) {
                return enums[ordinal];
            }
            for (Enum<?> e : enums) {
                if (e.name().equals(enumType)) {
                    return e;
                }
            }
            throw new YAPIONDeserializerException("Enum element not found: " + type + "[" + enumType + "]");

        } catch (ClassNotFoundException e) {
            throw new YAPIONDeserializerException("Class not found: " + type);
        }
    }

    @DeprecationInfo(since = "0.26.0")
    private String getType(YAPIONObject yapionObject) {
        String type = yapionObject.getPlainValue(TYPE_IDENTIFIER);
        if (type.equals("java.lang.Enum")) { // Backwards compatibility
            return yapionObject.getPlainValue(ENUM_IDENTIFIER);
        }
        return type;
    }

}
