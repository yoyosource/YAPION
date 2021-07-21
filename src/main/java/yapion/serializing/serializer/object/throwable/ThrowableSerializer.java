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
import yapion.utils.ReflectionsUtils;

import static yapion.utils.IdentifierUtils.EXCEPTION_IDENTIFIER;
import static yapion.utils.IdentifierUtils.TYPE_IDENTIFIER;

@SerializerImplementation(since = "0.20.0")
public class ThrowableSerializer implements FinalInternalSerializer<Throwable> {

    @Override
    public Class<?> type() {
        return Throwable.class;
    }

    @Override
    public Class<?> classType() {
        return Throwable.class;
    }

    @Override
    public YAPIONAnyType serialize(SerializeData<Throwable> serializeData) {
        YAPIONObject yapionObject = new YAPIONObject(serializeData.object.getClass());
        yapionObject.add("message", serializeData.object.getMessage());
        yapionObject.add("cause", serializeData.serialize(serializeData.object.getCause()));
        yapionObject.add("stacktrace", serializeData.serialize(serializeData.object.getStackTrace()));
        return yapionObject;
    }

    @Override
    public Throwable deserialize(DeserializeData<? extends YAPIONAnyType> deserializeData) {
        YAPIONObject yapionObject = (YAPIONObject) deserializeData.object;
        Throwable throwable;
        if (yapionObject.containsKey(EXCEPTION_IDENTIFIER, String.class)) {
            throwable = (Throwable) ReflectionsUtils.constructObject(yapionObject.getValue(EXCEPTION_IDENTIFIER, String.class).get(), false);
        } else {
            throwable = (Throwable) ReflectionsUtils.constructObject(yapionObject.getValue(TYPE_IDENTIFIER, String.class).get(), false);
        }
        deserializeData.deserialize("detailMessage", throwable, yapionObject.getValue("message"));
        deserializeData.deserialize("cause", throwable, yapionObject.getObject("cause"));
        deserializeData.deserialize("stackTrace", throwable, yapionObject.getArray("stacktrace"));
        return throwable;
    }

}
