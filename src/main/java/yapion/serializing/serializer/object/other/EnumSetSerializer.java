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
import yapion.hierarchy.types.YAPIONArray;
import yapion.hierarchy.types.YAPIONObject;
import yapion.serializing.InternalSerializer;
import yapion.serializing.data.DeserializeData;
import yapion.serializing.data.SerializeData;
import yapion.annotations.api.SerializerImplementation;

import java.util.EnumSet;

import static yapion.utils.IdentifierUtils.TYPE_IDENTIFIER;

@SerializerImplementation(since = "0.24.0")
public class EnumSetSerializer implements InternalSerializer<EnumSet<?>> {

    @Override
    public String type() {
        return "java.util.EnumSet";
    }

    @Override
    public YAPIONAnyType serialize(SerializeData<EnumSet<?>> serializeData) {
        YAPIONObject yapionObject = new YAPIONObject();
        yapionObject.add(TYPE_IDENTIFIER, type());
        yapionObject.add("elementType", ((Class<?>) serializeData.getField("elementType")).getTypeName());
        YAPIONArray yapionArray = new YAPIONArray();
        yapionObject.add("values", yapionArray);
        serializeData.object.iterator().forEachRemaining(anEnum -> {
            yapionArray.add(serializeData.serialize(anEnum));
        });
        return yapionObject;
    }

    @Override
    @SuppressWarnings({"unchecked", "java:S3740"})
    public EnumSet<?> deserialize(DeserializeData<? extends YAPIONAnyType> deserializeData) {
        YAPIONObject yapionObject = (YAPIONObject) deserializeData.object;
        String elementType = yapionObject.getPlainValue("elementType");
        YAPIONArray values = yapionObject.getArray("values");
        if (values.length() == 0) {
            try {
                return EnumSet.noneOf((Class<Enum>)Class.forName(elementType));
            } catch (ClassNotFoundException e) {
                throw new YAPIONDeserializerException(e.getMessage(), e);
            }
        }
        return deserialize(((YAPIONObject) deserializeData.object).getArray("values"), deserializeData);
    }

    @SuppressWarnings("unchecked")
    private <E extends Enum<E>> EnumSet<?> deserialize(YAPIONArray yapionArray, DeserializeData<? extends YAPIONAnyType> deserializeData) {
        E initial = null;
        E[] enums = (E[]) new Enum[yapionArray.length() - 1];
        int index = 0;
        for (YAPIONAnyType yapionAnyType : yapionArray) {
            if (index++ == 0) {
                initial = (E) deserializeData.deserialize(yapionAnyType);
                continue;
            }
            enums[index++] = (E) deserializeData.deserialize(yapionAnyType);
        }
        return EnumSet.of(initial, enums);
    }
}
