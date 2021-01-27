// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020,2021 yoyosource

package yapion.serializing.serializer.overrideable;

import yapion.annotations.deserialize.YAPIONLoadExclude;
import yapion.annotations.serialize.YAPIONSaveExclude;
import yapion.hierarchy.typegroups.YAPIONAnyType;
import yapion.hierarchy.types.YAPIONValue;
import yapion.serializing.InternalOverrideableSerializer;
import yapion.serializing.data.DeserializeData;
import yapion.serializing.data.SerializeData;
import yapion.serializing.serializer.SerializerImplementation;

@YAPIONSaveExclude(context = "*")
@YAPIONLoadExclude(context = "*")
@SerializerImplementation(since = "0.10.0")
public class YAPIONValueSerializer implements InternalOverrideableSerializer<YAPIONValue<?>> {

    @Override
    public String type() {
        return "yapion.hierarchy.types.YAPIONValue";
    }

    @Override
    public YAPIONAnyType serialize(SerializeData<YAPIONValue<?>> serializeData) {
        return serializeData.object;
    }

    @Override
    public YAPIONValue<?> deserialize(DeserializeData<? extends YAPIONAnyType> deserializeData) {
        return (YAPIONValue<?>) deserializeData.object;
    }

}