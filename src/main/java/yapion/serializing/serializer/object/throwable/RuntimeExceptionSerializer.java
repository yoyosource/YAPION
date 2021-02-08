// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020,2021 yoyosource

package yapion.serializing.serializer.object.throwable;

import yapion.hierarchy.api.groups.YAPIONAnyType;
import yapion.hierarchy.types.YAPIONArray;
import yapion.hierarchy.types.YAPIONObject;
import yapion.serializing.InternalSerializer;
import yapion.serializing.data.DeserializeData;
import yapion.serializing.data.SerializeData;
import yapion.serializing.serializer.SerializerImplementation;
import yapion.utils.ReflectionsUtils;

import static yapion.utils.IdentifierUtils.EXCEPTION_IDENTIFIER;
import static yapion.utils.IdentifierUtils.TYPE_IDENTIFIER;

@SerializerImplementation(since = "0.20.0")
public class RuntimeExceptionSerializer implements InternalSerializer<RuntimeException> {

    @Override
    public String type() {
        return "java.lang.RuntimeException";
    }

    @Override
    public YAPIONAnyType serialize(SerializeData<RuntimeException> serializeData) {
        YAPIONObject yapionObject = new YAPIONObject();
        yapionObject.add(TYPE_IDENTIFIER, type());
        yapionObject.add(EXCEPTION_IDENTIFIER, serializeData.object.getClass().getTypeName());
        yapionObject.add("message", serializeData.object.getMessage());
        yapionObject.add("cause", serializeData.serialize(serializeData.object.getCause()));
        yapionObject.add("stacktrace", serializeData.serialize(serializeData.object.getStackTrace()));
        return yapionObject;
    }

    @Override
    public RuntimeException deserialize(DeserializeData<? extends YAPIONAnyType> deserializeData) {
        YAPIONObject yapionObject = (YAPIONObject) deserializeData.object;
        RuntimeException runtimeException = (RuntimeException) ReflectionsUtils.constructObject(yapionObject.getValue(EXCEPTION_IDENTIFIER, "").get(), false);
        deserializeData.deserialize("detailMessage", runtimeException, yapionObject.getValue("message"));
        deserializeData.deserialize("cause", runtimeException, yapionObject.getObject("cause"));

        YAPIONArray yapionArray = yapionObject.getArray("stacktrace");
        StackTraceElement[] stackTraceElements = new StackTraceElement[yapionArray.length()];
        for (int i = 0; i < yapionArray.length(); i++) {
            stackTraceElements[i] = (StackTraceElement) deserializeData.deserialize(yapionArray.getYAPIONAnyType(i));
        }
        deserializeData.setField("stackTrace", runtimeException, stackTraceElements);
        return runtimeException;
    }

}