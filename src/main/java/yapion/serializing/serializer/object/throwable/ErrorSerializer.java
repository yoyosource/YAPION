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

import yapion.annotations.api.SerializerImplementation;
import yapion.exceptions.serializing.YAPIONDataLossException;
import yapion.hierarchy.api.groups.YAPIONAnyType;
import yapion.hierarchy.types.YAPIONObject;
import yapion.serializing.InternalSerializer;
import yapion.serializing.data.DeserializeData;
import yapion.serializing.data.SerializeData;
import yapion.utils.ReflectionsUtils;

import static yapion.serializing.YAPIONFlag.ERROR_EXCEPTION;
import static yapion.utils.IdentifierUtils.EXCEPTION_IDENTIFIER;
import static yapion.utils.IdentifierUtils.TYPE_IDENTIFIER;

@SerializerImplementation(since = "0.20.0")
public class ErrorSerializer implements InternalSerializer<Error> {

    @Override
    public Class<?> type() {
        return Error.class;
    }

    @Override
    public Class<?> classType() {
        return Error.class;
    }

    @Override
    public YAPIONAnyType serialize(SerializeData<Error> serializeData) {
        serializeData.signalDataLoss();
        serializeData.isSet(ERROR_EXCEPTION, () -> {
            throw new YAPIONDataLossException("The ERROR_EXCEPTION flag is set to not serialize Errors");
        });

        YAPIONObject yapionObject = new YAPIONObject();
        yapionObject.add(TYPE_IDENTIFIER, type());
        yapionObject.add(EXCEPTION_IDENTIFIER, serializeData.object.getClass().getTypeName());
        yapionObject.add("message", serializeData.object.getMessage());
        yapionObject.add("cause", serializeData.serialize(serializeData.object.getCause()));
        yapionObject.add("stacktrace", serializeData.serialize(serializeData.object.getStackTrace()));
        return yapionObject;
    }

    @Override
    public Error deserialize(DeserializeData<? extends YAPIONAnyType> deserializeData) {
        YAPIONObject yapionObject = (YAPIONObject) deserializeData.object;
        Error error = (Error) ReflectionsUtils.constructObject(yapionObject.getValue(EXCEPTION_IDENTIFIER, "").get(), false);
        deserializeData.deserialize("detailMessage", error, yapionObject.getValue("message"));
        deserializeData.deserialize("cause", error, yapionObject.getObject("cause"));
        deserializeData.deserialize("stackTrace", error, yapionObject.getArray("stacktrace"));
        return error;
    }

}
