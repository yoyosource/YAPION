// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020,2021 yoyosource

package yapion.serializing.serializer.object.optional;

import yapion.hierarchy.api.groups.YAPIONAnyType;
import yapion.hierarchy.types.YAPIONObject;
import yapion.serializing.InternalSerializer;
import yapion.serializing.data.DeserializeData;
import yapion.serializing.data.SerializeData;
import yapion.serializing.serializer.SerializerImplementation;

import java.util.OptionalLong;

import static yapion.utils.IdentifierUtils.TYPE_IDENTIFIER;

@SerializerImplementation(since = "0.18.0")
public class OptionalLongSerializer implements InternalSerializer<OptionalLong> {

    @Override
    public String type() {
        return "java.util.OptionalLong";
    }

    @Override
    public YAPIONAnyType serialize(SerializeData<OptionalLong> serializeData) {
        YAPIONObject yapionObject = new YAPIONObject();
        yapionObject.add(TYPE_IDENTIFIER, type());
        yapionObject.add("present", serializeData.object.isPresent());
        serializeData.object.ifPresent(o -> yapionObject.add("value", o));
        return yapionObject;
    }

    @Override
    @SuppressWarnings({"java:S5411"})
    public OptionalLong deserialize(DeserializeData<? extends YAPIONAnyType> deserializeData) {
        YAPIONObject yapionObject = (YAPIONObject) deserializeData.object;
        if (yapionObject.getValue("present", true).get()) {
            return OptionalLong.of(yapionObject.getValue("value", 0L).get());
        }
        return OptionalLong.empty();
    }
}