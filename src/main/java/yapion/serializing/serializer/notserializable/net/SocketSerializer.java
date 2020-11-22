// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020 yoyosource

package yapion.serializing.serializer.notserializable.net;

import yapion.annotations.deserialize.YAPIONLoadExclude;
import yapion.annotations.serialize.YAPIONSaveExclude;
import yapion.hierarchy.typegroups.YAPIONAnyType;
import yapion.hierarchy.types.YAPIONValue;
import yapion.serializing.InternalSerializer;
import yapion.serializing.data.DeserializeData;
import yapion.serializing.data.SerializeData;
import yapion.serializing.serializer.SerializerImplementation;

import java.net.Socket;

@YAPIONSaveExclude(context = "*")
@YAPIONLoadExclude(context = "*")
@SerializerImplementation
public class SocketSerializer implements InternalSerializer<Socket> {

    @Override
    public String type() {
        return "java.net.Socket";
    }

    @Override
    public YAPIONAnyType serialize(SerializeData<Socket> serializeData) {
        return new YAPIONValue<>(null);
    }

    @Override
    public Socket deserialize(DeserializeData<? extends YAPIONAnyType> deserializeData) {
        return null;
    }
}