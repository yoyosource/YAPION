// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020 yoyosource

package yapion.serializing.serializer.other;

import yapion.annotations.deserialize.YAPIONLoadExclude;
import yapion.annotations.serialize.YAPIONSaveExclude;
import yapion.hierarchy.typegroups.YAPIONAnyType;
import yapion.hierarchy.types.YAPIONValue;
import yapion.serializing.InternalSerializer;
import yapion.serializing.data.DeserializeData;
import yapion.serializing.data.SerializeData;
import yapion.serializing.serializer.SerializerImplementation;

@YAPIONSaveExclude(context = "*")
@YAPIONLoadExclude(context = "*")
@SerializerImplementation
public class ProcessSerializer implements InternalSerializer<Process> {

    @Override
    public String type() {
        return "java.lang.Process";
    }

    @Override
    public YAPIONAnyType serialize(SerializeData<Process> serializeData) {
        return new YAPIONValue<>(null);
    }

    @Override
    public Process deserialize(DeserializeData<? extends YAPIONAnyType> deserializeData) {
        return null;
    }
}