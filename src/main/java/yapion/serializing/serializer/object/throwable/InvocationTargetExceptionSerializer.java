// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020,2021 yoyosource

package yapion.serializing.serializer.object.throwable;

import yapion.hierarchy.api.groups.YAPIONAnyType;
import yapion.hierarchy.types.YAPIONObject;
import yapion.serializing.InternalSerializer;
import yapion.serializing.data.DeserializeData;
import yapion.serializing.data.SerializeData;
import yapion.serializing.serializer.SerializerImplementation;

import java.lang.reflect.InvocationTargetException;

import static yapion.utils.IdentifierUtils.TYPE_IDENTIFIER;

@SerializerImplementation(since = "0.23.1")
public class InvocationTargetExceptionSerializer implements InternalSerializer<InvocationTargetException> {

    @Override
    public String type() {
        return "java.lang.reflect.InvocationTargetException";
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