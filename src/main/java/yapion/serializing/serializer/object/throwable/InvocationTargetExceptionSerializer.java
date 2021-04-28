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
import yapion.hierarchy.api.groups.YAPIONAnyType;
import yapion.hierarchy.types.YAPIONObject;
import yapion.serializing.InternalSerializer;
import yapion.serializing.data.DeserializeData;
import yapion.serializing.data.SerializeData;

import java.lang.reflect.InvocationTargetException;

import static yapion.utils.IdentifierUtils.TYPE_IDENTIFIER;

@SerializerImplementation(since = "0.23.1")
public class InvocationTargetExceptionSerializer implements InternalSerializer<InvocationTargetException> {

    @Override
    public Class<?> type() {
        return InvocationTargetException.class;
    }

    @Override
    public YAPIONAnyType serialize(SerializeData<InvocationTargetException> serializeData) {
        YAPIONObject yapionObject = new YAPIONObject();
        yapionObject.add(TYPE_IDENTIFIER, type());
        yapionObject.add("target", serializeData.serialize(serializeData.object.getCause()));
        yapionObject.add("message", serializeData.object.getMessage());
        yapionObject.add("cause", serializeData.serialize(serializeData.object.getCause()));
        yapionObject.add("stacktrace", serializeData.serialize(serializeData.object.getStackTrace()));
        return yapionObject;
    }

    @Override
    public InvocationTargetException deserialize(DeserializeData<? extends YAPIONAnyType> deserializeData) {
        YAPIONObject yapionObject = (YAPIONObject) deserializeData.object;
        InvocationTargetException invocationTargetException = new InvocationTargetException(null);
        deserializeData.deserialize("target", invocationTargetException, yapionObject.getObject("target"));
        deserializeData.deserialize("detailMessage", invocationTargetException, yapionObject.getValue("message"));
        deserializeData.deserialize("cause", invocationTargetException, yapionObject.getObject("cause"));
        deserializeData.deserialize("stackTrace", invocationTargetException, yapionObject.getArray("stacktrace"));
        return invocationTargetException;
    }

}
