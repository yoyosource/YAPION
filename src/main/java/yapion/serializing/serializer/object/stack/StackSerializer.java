// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020,2021 yoyosource

package yapion.serializing.serializer.object.stack;

import yapion.hierarchy.api.groups.YAPIONAnyType;
import yapion.hierarchy.types.YAPIONArray;
import yapion.hierarchy.types.YAPIONObject;
import yapion.serializing.InternalSerializer;
import yapion.serializing.data.DeserializeData;
import yapion.serializing.data.SerializeData;
import yapion.serializing.serializer.SerializerImplementation;

import java.util.Stack;

import static yapion.utils.IdentifierUtils.TYPE_IDENTIFIER;

@SerializerImplementation(since = "0.17.0")
public class StackSerializer implements InternalSerializer<Stack<?>> {

    @Override
    public String type() {
        return "java.util.Stack";
    }

    @Override
    public YAPIONAnyType serialize(SerializeData<Stack<?>> serializeData) {
        YAPIONObject yapionObject = new YAPIONObject();
        yapionObject.add(TYPE_IDENTIFIER, type());
        YAPIONArray yapionArray = new YAPIONArray();
        yapionObject.add("values", yapionArray);
        for (int i = 0; i < serializeData.object.size(); i++) {
            yapionArray.add(serializeData.serialize(serializeData.object.get(i)));
        }
        return yapionObject;
    }

    @Override
    @SuppressWarnings({"java:S1149"})
    public Stack<?> deserialize(DeserializeData<? extends YAPIONAnyType> deserializeData) {
        YAPIONObject yapionObject = (YAPIONObject) deserializeData.object;
        YAPIONArray yapionArray = yapionObject.getArray("values");
        Stack<Object> stack = new Stack<>();
        for (int i = 0; i < yapionArray.length(); i++) {
            stack.push(deserializeData.deserialize(yapionArray.getYAPIONAnyType(i)));
        }
        return stack;
    }
}