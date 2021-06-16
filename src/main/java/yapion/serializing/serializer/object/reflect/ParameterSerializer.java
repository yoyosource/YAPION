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
import yapion.exceptions.serializing.YAPIONDataLossException;
import yapion.exceptions.serializing.YAPIONDeserializerException;
import yapion.hierarchy.api.groups.YAPIONAnyType;
import yapion.hierarchy.types.YAPIONObject;
import yapion.hierarchy.types.YAPIONValue;
import yapion.serializing.InternalSerializer;
import yapion.serializing.YAPIONDeserializer;
import yapion.serializing.data.DeserializeData;
import yapion.serializing.data.SerializeData;

import java.lang.reflect.Executable;
import java.lang.reflect.Parameter;

import static yapion.serializing.YAPIONFlag.REFLECTION_AS_NULL;
import static yapion.serializing.YAPIONFlag.REFLECTION_EXCEPTION;

@SerializerImplementation(since = "0.26.0")
public class ParameterSerializer implements InternalSerializer<Parameter> {

    @Override
    public Class<?> type() {
        return Parameter.class;
    }

    @Override
    public YAPIONAnyType serialize(SerializeData<Parameter> serializeData) {
        serializeData.isSet(REFLECTION_EXCEPTION, () -> {
            throw new YAPIONDataLossException("The REFLECTION_EXCEPTION flag is set to not serialize reflection stuff");
        });
        if (serializeData.getYAPIONFlags().isSet(REFLECTION_AS_NULL)) {
            return new YAPIONValue<>(null);
        }

        YAPIONObject yapionObject = new YAPIONObject(type());
        yapionObject.add("executable", serializeData.serialize(serializeData.object.getDeclaringExecutable()));
        yapionObject.add("name", serializeData.object.getName());
        yapionObject.add("type", serializeData.object.getType());
        yapionObject.add("index", serializeData.serializeField("index"));
        return yapionObject;
    }

    @Override
    public Parameter deserialize(DeserializeData<? extends YAPIONAnyType> deserializeData) {
        YAPIONObject yapionObject = (YAPIONObject) deserializeData.object;
        Executable executable = deserializeData.deserialize(yapionObject.getObject("executable"));
        int index = yapionObject.getPlainValue("index");
        if (index < 0 || index >= executable.getParameterCount()) {
            throw new YAPIONDeserializerException("Parameter could not be restored as index is invalid");
        }
        Parameter parameter = executable.getParameters()[index];
        if (!parameter.getType().getTypeName().equals(yapionObject.getPlainValue("type"))) {
            throw new YAPIONDeserializerException("Parameter could not be restored as type did not match to desired type from serialization");
        }
        return parameter;
    }
}
