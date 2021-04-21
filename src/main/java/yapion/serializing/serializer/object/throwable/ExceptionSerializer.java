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

package yapion.serializing.serializer.object.throwable;

import yapion.hierarchy.api.groups.YAPIONAnyType;
import yapion.hierarchy.types.YAPIONObject;
import yapion.serializing.InternalSerializer;
import yapion.serializing.data.DeserializeData;
import yapion.serializing.data.SerializeData;
import yapion.annotations.api.SerializerImplementation;
import yapion.utils.ReflectionsUtils;

import static yapion.utils.IdentifierUtils.EXCEPTION_IDENTIFIER;
import static yapion.utils.IdentifierUtils.TYPE_IDENTIFIER;

@SerializerImplementation(since = "0.20.0")
public class ExceptionSerializer implements InternalSerializer<Exception> {

    @Override
    public String type() {
        return "java.lang.Exception";
    }

    @Override
    public Class<?> classType() {
        return Exception.class;
    }

    @Override
    public YAPIONAnyType serialize(SerializeData<Exception> serializeData) {
        YAPIONObject yapionObject = new YAPIONObject();
        yapionObject.add(TYPE_IDENTIFIER, type());
        yapionObject.add(EXCEPTION_IDENTIFIER, serializeData.object.getClass().getTypeName());
        yapionObject.add("message", serializeData.object.getMessage());
        yapionObject.add("cause", serializeData.serialize(serializeData.object.getCause()));
        yapionObject.add("stacktrace", serializeData.serialize(serializeData.object.getStackTrace()));
        return yapionObject;
    }

    @Override
    public Exception deserialize(DeserializeData<? extends YAPIONAnyType> deserializeData) {
        YAPIONObject yapionObject = (YAPIONObject) deserializeData.object;
        Exception exception = (Exception) ReflectionsUtils.constructObject(yapionObject.getValue(EXCEPTION_IDENTIFIER, "").get(), false);
        deserializeData.deserialize("detailMessage", exception, yapionObject.getValue("message"));
        deserializeData.deserialize("cause", exception, yapionObject.getObject("cause"));
        deserializeData.deserialize("stackTrace", exception, yapionObject.getArray("stacktrace"));
        return exception;
    }

}
