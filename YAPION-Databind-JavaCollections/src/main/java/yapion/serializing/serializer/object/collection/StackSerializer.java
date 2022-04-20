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

package yapion.serializing.serializer.object.collection;

import yapion.annotations.api.SerializerImplementation;
import yapion.hierarchy.api.groups.YAPIONAnyType;
import yapion.hierarchy.types.YAPIONArray;
import yapion.hierarchy.types.YAPIONObject;
import yapion.serializing.data.DeserializeData;
import yapion.serializing.data.SerializeData;
import yapion.serializing.serializer.FinalInternalSerializer;

import java.util.Stack;

@SerializerImplementation(since = "0.17.0")
public class StackSerializer implements FinalInternalSerializer<Stack<?>> {

    @Override
    public Class<?> type() {
        return Stack.class;
    }

    @Override
    public YAPIONAnyType serialize(SerializeData<Stack<?>> serializeData) {
        YAPIONObject yapionObject = new YAPIONObject(type());
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
            stack.push(deserializeData.deserialize(yapionArray.getAnyType(i)));
        }
        return stack;
    }
}
