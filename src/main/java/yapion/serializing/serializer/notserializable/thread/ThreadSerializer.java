// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020,2021 yoyosource

package yapion.serializing.serializer.notserializable.thread;

import yapion.hierarchy.api.groups.YAPIONAnyType;
import yapion.hierarchy.types.YAPIONValue;
import yapion.serializing.InternalSerializer;
import yapion.serializing.data.DeserializeData;
import yapion.serializing.data.SerializeData;
import yapion.serializing.serializer.SerializerImplementation;

@SerializerImplementation(since = "0.10.0")
public class ThreadSerializer implements InternalSerializer<Thread> {

    @Override
    public String type() {
        return "java.lang.Thread";
    }

    @Override
    public YAPIONAnyType serialize(SerializeData<Thread> serializeData) {
        serializeData.signalDataLoss();
        return new YAPIONValue<>(null);
    }

    @Override
    public Thread deserialize(DeserializeData<? extends YAPIONAnyType> deserializeData) {
        return null;
    }
}