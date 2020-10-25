// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020 yoyosource

package yapion.serializing.serializer.overrideable;

import yapion.annotations.deserialize.YAPIONLoadExclude;
import yapion.annotations.serialize.YAPIONSaveExclude;
import yapion.hierarchy.typegroups.YAPIONAnyType;
import yapion.hierarchy.types.YAPIONObject;
import yapion.serializing.InternalOverrideableSerializer;
import yapion.serializing.YAPIONDeserializer;
import yapion.serializing.YAPIONSerializer;
import yapion.serializing.data.DeserializeData;
import yapion.serializing.data.SerializeData;
import yapion.serializing.serializer.SerializerImplementation;

@YAPIONSaveExclude(context = "*")
@YAPIONLoadExclude(context = "*")
@SerializerImplementation
public class YAPIONSerializerObject implements InternalOverrideableSerializer<YAPIONObject> {

    @Override
    public String type() {
        return "yapion.hierarchy.types.YAPIONObject";
    }

    @Override
    public YAPIONAnyType serialize(SerializeData<YAPIONObject> serializeData) {
        return serializeData.object;
    }

    @Override
    public YAPIONObject deserialize(DeserializeData<? extends YAPIONAnyType> deserializeData) {
        return (YAPIONObject) deserializeData.object;
    }

}