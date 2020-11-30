// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020 yoyosource

package yapion.serializing.serializer.object.throwable;

import yapion.annotations.deserialize.YAPIONLoadExclude;
import yapion.annotations.serialize.YAPIONSaveExclude;
import yapion.hierarchy.typegroups.YAPIONAnyType;
import yapion.hierarchy.types.YAPIONObject;
import yapion.serializing.InternalSerializer;
import yapion.serializing.data.DeserializeData;
import yapion.serializing.data.SerializeData;
import yapion.serializing.serializer.SerializerImplementation;

import static yapion.utils.IdentifierUtils.TYPE_IDENTIFIER;

@YAPIONSaveExclude(context = "*")
@YAPIONLoadExclude(context = "*")
@SerializerImplementation
public class StackTraceElementSerializer implements InternalSerializer<StackTraceElement> {

    @Override
    public String type() {
        return "java.lang.StackTraceElement";
    }

    @Override
    public YAPIONAnyType serialize(SerializeData<StackTraceElement> serializeData) {
        YAPIONObject yapionObject = new YAPIONObject();
        yapionObject.add(TYPE_IDENTIFIER, type());
        yapionObject.add("methodName", serializeData.object.getMethodName());
        yapionObject.add("className", serializeData.object.getClassName());
        yapionObject.add("fileName", serializeData.object.getFileName());
        yapionObject.add("line", serializeData.object.getLineNumber());
        yapionObject.add("native", serializeData.object.isNativeMethod());
        return yapionObject;
    }

    @Override
    public StackTraceElement deserialize(DeserializeData<? extends YAPIONAnyType> deserializeData) {
        YAPIONObject yapionObject = (YAPIONObject) deserializeData.object;
        String methodName = yapionObject.getValue("methodName", "").get();
        String className = yapionObject.getValue("className", "").get();
        String fileName = yapionObject.getValue("fileName", "").get();
        int line = yapionObject.getValue("line", 0).get();
        return new StackTraceElement(className, methodName, fileName, line);
    }

}