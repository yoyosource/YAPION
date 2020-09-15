// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020 yoyosource

package yapion.serializing.serializer.object;

import yapion.annotations.deserialize.YAPIONLoadExclude;
import yapion.annotations.serialize.YAPIONSaveExclude;
import yapion.hierarchy.typegroups.YAPIONAnyType;
import yapion.hierarchy.YAPIONVariable;
import yapion.hierarchy.types.YAPIONArray;
import yapion.hierarchy.types.YAPIONObject;
import yapion.hierarchy.types.YAPIONValue;
import yapion.serializing.InternalSerializer;
import yapion.serializing.SerializeManager;
import yapion.serializing.YAPIONDeserializer;
import yapion.serializing.YAPIONSerializer;
import yapion.serializing.serializer.SerializerImplementation;

import java.util.Stack;

@YAPIONSaveExclude(context = "*")
@YAPIONLoadExclude(context = "*")
@SerializerImplementation
public class StackSerializer implements InternalSerializer<Stack<?>> {

    @Override
    public String type() {
        return "java.util.Stack";
    }

    @Override
    public YAPIONAnyType serialize(Stack<?> object, YAPIONSerializer yapionSerializer) {
        YAPIONObject yapionObject = new YAPIONObject();
        yapionObject.add(new YAPIONVariable(SerializeManager.TYPE_IDENTIFIER, new YAPIONValue<>(type())));
        YAPIONArray yapionArray = new YAPIONArray();
        yapionObject.add(new YAPIONVariable("values", yapionArray));
        for (int i = 0; i < object.size(); i++) {
            yapionArray.add(yapionSerializer.parse(object.get(i)));
        }
        return yapionObject;
    }

    @Override
    @SuppressWarnings({"java:S1149"})
    public Stack<?> deserialize(YAPIONAnyType yapionAnyType, YAPIONDeserializer yapionDeserializer) {
        YAPIONObject yapionObject = (YAPIONObject) yapionAnyType;
        YAPIONArray yapionArray = yapionObject.getArray("values");
        Stack<Object> stack = new Stack<>();
        for (int i = 0; i < yapionArray.length(); i++) {
            stack.push(yapionDeserializer.parse(yapionArray.get(i)));
        }
        return stack;
    }
}