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
import yapion.serializing.data.DeserializeData;
import yapion.serializing.data.SerializeData;
import yapion.serializing.serializer.FinalInternalSerializer;

@SerializerImplementation(since = "0.20.0")
public class StackTraceElementSerializer implements FinalInternalSerializer<StackTraceElement> {

    @Override
    public Class<?> type() {
        return StackTraceElement.class;
    }

    @Override
    public YAPIONAnyType serialize(SerializeData<StackTraceElement> serializeData) {
        YAPIONObject yapionObject = new YAPIONObject(type());
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
