// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020,2021 yoyosource

package yapion.serializing.serializer.overrideable;

import yapion.annotations.deserialize.YAPIONLoadExclude;
import yapion.annotations.serialize.YAPIONSaveExclude;
import yapion.hierarchy.typegroups.YAPIONAnyType;
import yapion.hierarchy.types.YAPIONArray;
import yapion.serializing.InternalOverrideableSerializer;
import yapion.serializing.YAPIONDeserializer;
import yapion.serializing.YAPIONSerializer;
import yapion.serializing.data.DeserializeData;
import yapion.serializing.data.SerializeData;
import yapion.serializing.serializer.SerializerImplementation;

@YAPIONSaveExclude(context = "*")
@YAPIONLoadExclude(context = "*")
@SerializerImplementation(since = "0.10.0")
public class YAPIONArraySerializer implements InternalOverrideableSerializer<YAPIONArray> {

    @Override
    public String type() {
        return "yapion.hierarchy.types.YAPIONArray";
    }

    @Override
    public YAPIONAnyType serialize(SerializeData<YAPIONArray> serializeData) {
        return serializeData.object;
    }

    @Override
    public YAPIONArray deserialize(DeserializeData<? extends YAPIONAnyType> deserializeData) {
        return (YAPIONArray) deserializeData.object;
    }

}