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

package yapion.serializing.serializer.object.reflect;

import yapion.annotations.api.SerializerImplementation;
import yapion.exceptions.YAPIONException;
import yapion.exceptions.serializing.YAPIONDataLossException;
import yapion.hierarchy.api.groups.YAPIONAnyType;
import yapion.hierarchy.types.YAPIONObject;
import yapion.hierarchy.types.YAPIONValue;
import yapion.serializing.data.DeserializeData;
import yapion.serializing.data.SerializeData;
import yapion.serializing.serializer.FinalInternalSerializer;

import java.lang.reflect.Field;

import static yapion.serializing.YAPIONFlag.REFLECTION_AS_NULL;
import static yapion.serializing.YAPIONFlag.REFLECTION_EXCEPTION;

@SerializerImplementation(since = "0.25.0")
public class FieldSerializer implements FinalInternalSerializer<Field> {

    @Override
    public Class<?> type() {
        return Field.class;
    }

    @Override
    public YAPIONAnyType serialize(SerializeData<Field> serializeData) {
        serializeData.isSet(REFLECTION_EXCEPTION, () -> {
            throw new YAPIONDataLossException("The REFLECTION_EXCEPTION flag is set to not serialize reflection stuff");
        });
        if (serializeData.getYAPIONFlags().isSet(REFLECTION_AS_NULL)) {
            return new YAPIONValue<>(null);
        }

        YAPIONObject yapionObject = new YAPIONObject(type());
        yapionObject.add("class", serializeData.serialize(serializeData.object.getDeclaringClass()));
        yapionObject.add("fieldName", serializeData.object.getName());
        return yapionObject;
    }

    @Override
    public Field deserialize(DeserializeData<? extends YAPIONAnyType> deserializeData) {
        YAPIONObject yapionObject = (YAPIONObject) deserializeData.object;
        Class<?> clazz = deserializeData.deserialize(yapionObject.getObject("class"));
        try {
            return clazz.getDeclaredField(yapionObject.getPlainValue("fieldName"));
        } catch (NoSuchFieldException e) {
            throw new YAPIONException(e.getMessage(), e);
        }
    }

}