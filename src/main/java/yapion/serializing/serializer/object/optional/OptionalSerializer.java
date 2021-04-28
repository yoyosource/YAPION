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

package yapion.serializing.serializer.object.optional;

import yapion.hierarchy.api.groups.YAPIONAnyType;
import yapion.hierarchy.types.YAPIONObject;
import yapion.serializing.InternalSerializer;
import yapion.serializing.data.DeserializeData;
import yapion.serializing.data.SerializeData;
import yapion.annotations.api.SerializerImplementation;

import java.math.BigDecimal;
import java.util.Optional;

import static yapion.utils.IdentifierUtils.TYPE_IDENTIFIER;

@SerializerImplementation(since = "0.7.0")
public class OptionalSerializer implements InternalSerializer<Optional<?>> {

    @Override
    public Class<?> type() {
        return Optional.class;
    }

    @Override
    public YAPIONAnyType serialize(SerializeData<Optional<?>> serializeData) {
        YAPIONObject yapionObject = new YAPIONObject();
        yapionObject.add(TYPE_IDENTIFIER, type());
        yapionObject.add("present", serializeData.object.isPresent());
        serializeData.object.ifPresent(o -> yapionObject.add("value", serializeData.serialize(o)));
        return yapionObject;
    }

    @Override
    @SuppressWarnings({"java:S5411"})
    public Optional<?> deserialize(DeserializeData<? extends YAPIONAnyType> deserializeData) {
        YAPIONObject yapionObject = (YAPIONObject) deserializeData.object;
        if (yapionObject.getValue("present", true).get()) {
            return Optional.ofNullable(deserializeData.deserialize(yapionObject.getYAPIONAnyType("value")));
        }
        return Optional.empty();
    }

}
