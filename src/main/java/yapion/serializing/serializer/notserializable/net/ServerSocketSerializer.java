// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020,2021 yoyosource

package yapion.serializing.serializer.notserializable.net;

import yapion.annotations.deserialize.YAPIONLoadExclude;
import yapion.annotations.serialize.YAPIONSaveExclude;
import yapion.hierarchy.api.groups.YAPIONAnyType;
import yapion.hierarchy.types.YAPIONValue;
import yapion.serializing.InternalSerializer;
import yapion.serializing.YAPIONSerializerFlagDefault;
import yapion.serializing.YAPIONSerializerFlags;
import yapion.serializing.data.DeserializeData;
import yapion.serializing.data.SerializeData;
import yapion.serializing.serializer.SerializerImplementation;

import java.net.ServerSocket;

import static yapion.serializing.YAPIONSerializerFlagDefault.DATA_LOSS_EXCEPTION;

@YAPIONSaveExclude(context = "*")
@YAPIONLoadExclude(context = "*")
@SerializerImplementation(since = "0.12.0")
public class ServerSocketSerializer implements InternalSerializer<ServerSocket> {

    @Override
    public void init() {
        YAPIONSerializerFlags.addFlag(new YAPIONSerializerFlagDefault(DATA_LOSS_EXCEPTION, false));
    }

    @Override
    public String type() {
        return "java.net.ServerSocket";
    }

    @Override
    public YAPIONAnyType serialize(SerializeData<ServerSocket> serializeData) {
        serializeData.signalDataLoss();
        return new YAPIONValue<>(null);
    }

    @Override
    public ServerSocket deserialize(DeserializeData<? extends YAPIONAnyType> deserializeData) {
        return null;
    }
}