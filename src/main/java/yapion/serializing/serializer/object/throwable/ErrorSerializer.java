// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020,2021 yoyosource

package yapion.serializing.serializer.object.throwable;

import yapion.annotations.deserialize.YAPIONLoadExclude;
import yapion.annotations.serialize.YAPIONSaveExclude;
import yapion.exceptions.serializing.YAPIONDataLossException;
import yapion.hierarchy.api.groups.YAPIONAnyType;
import yapion.hierarchy.types.YAPIONArray;
import yapion.hierarchy.types.YAPIONObject;
import yapion.serializing.InternalSerializer;
import yapion.serializing.YAPIONSerializerFlagDefault;
import yapion.serializing.YAPIONSerializerFlags;
import yapion.serializing.data.DeserializeData;
import yapion.serializing.data.SerializeData;
import yapion.serializing.serializer.SerializerImplementation;
import yapion.utils.ReflectionsUtils;

import static yapion.serializing.YAPIONSerializerFlagDefault.DATA_LOSS_EXCEPTION;
import static yapion.serializing.YAPIONSerializerFlagDefault.ERROR_EXCEPTION;
import static yapion.utils.IdentifierUtils.EXCEPTION_IDENTIFIER;
import static yapion.utils.IdentifierUtils.TYPE_IDENTIFIER;

@YAPIONSaveExclude(context = "*")
@YAPIONLoadExclude(context = "*")
@SerializerImplementation(since = "0.20.0")
public class ErrorSerializer implements InternalSerializer<Error> {

    @Override
    public void init() {
        YAPIONSerializerFlags.addFlag(new YAPIONSerializerFlagDefault(DATA_LOSS_EXCEPTION, false));
        YAPIONSerializerFlags.addFlag(new YAPIONSerializerFlagDefault(ERROR_EXCEPTION, false));
    }

    @Override
    public String type() {
        return "java.lang.Error";
    }

    @Override
    public YAPIONAnyType serialize(SerializeData<Error> serializeData) {
        serializeData.signalDataLoss();
        serializeData.isSet(ERROR_EXCEPTION, () -> {
            throw new YAPIONDataLossException();
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

        YAPIONArray yapionArray = yapionObject.getArray("stacktrace");
        StackTraceElement[] stackTraceElements = new StackTraceElement[yapionArray.length()];
        for (int i = 0; i < yapionArray.length(); i++) {
            stackTraceElements[i] = (StackTraceElement) deserializeData.deserialize(yapionArray.get(i));
        }
        deserializeData.setField("stackTrace", error, stackTraceElements);
        return error;
    }

}