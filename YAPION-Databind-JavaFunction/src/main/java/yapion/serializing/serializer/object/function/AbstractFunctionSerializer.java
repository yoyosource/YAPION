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

package yapion.serializing.serializer.object.function;

import yapion.annotations.api.SerializerImplementation;
import yapion.exceptions.serializing.YAPIONDeserializerException;
import yapion.exceptions.serializing.YAPIONSerializerException;
import yapion.hierarchy.api.groups.YAPIONAnyType;
import yapion.hierarchy.types.YAPIONObject;
import yapion.hierarchy.types.YAPIONValue;
import yapion.serializing.YAPIONFlag;
import yapion.serializing.data.DeserializeData;
import yapion.serializing.data.SerializeData;

import static yapion.utils.IdentifierUtils.CLASS_IDENTIFIER;
import static yapion.utils.IdentifierUtils.TYPE_IDENTIFIER;

@SerializerImplementation(since = "0.26.0")
public class AbstractFunctionSerializer<T> {

    protected Class<T> clazz;

    public Class<?> type() {
        return clazz;
    }

    public Class<?> interfaceType() {
        return clazz;
    }

    public boolean finished() {
        return false;
    }

    public boolean saveWithoutAnnotation() {
        return true;
    }

    public boolean loadWithoutAnnotation() {
        return true;
    }

    public YAPIONAnyType serialize(SerializeData<T> serializeData) {
        if (!serializeData.getYAPIONFlags().isSet(YAPIONFlag.CLASS_INJECTION)) {
            serializeData.signalDataLoss();
            return new YAPIONValue<>(null);
        }

        YAPIONObject yapionObject = (YAPIONObject) serializeData.serialize(serializeData.object.getClass());
        yapionObject.add(CLASS_IDENTIFIER, serializeData.object.getClass());
        yapionObject.add(TYPE_IDENTIFIER, type());
        yapionObject.remap("byteCode", "@byteCode");
        yapionObject.remove("class");
        return yapionObject;
    }

    public T deserialize(DeserializeData<? extends YAPIONAnyType> deserializeData) {
        if (!deserializeData.getYAPIONFlags().isSet(YAPIONFlag.CLASS_INJECTION)) {
            deserializeData.signalDataLoss();
            throw new YAPIONSerializerException("ClassInjection is not turned on");
        }
        YAPIONObject yapionObject = (YAPIONObject) deserializeData.object;

        YAPIONObject classObject = new YAPIONObject(Class.class);
        classObject.add("class", yapionObject.getValue(CLASS_IDENTIFIER));
        classObject.add("byteCode", yapionObject.getValue("@byteCode"));
        Class<?> clazz = deserializeData.deserialize(classObject);

        try {
            return (T) clazz.cast(deserializeData.getInstanceByFactoryOrObjenesis(clazz));
        } catch (Exception e) {
            throw new YAPIONDeserializerException(e.getMessage(), e);
        }
    }
}
