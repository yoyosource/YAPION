// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020 yoyosource

package yapion.serializing.serializer.object.other;

import yapion.annotations.deserialize.YAPIONLoadExclude;
import yapion.annotations.serialize.YAPIONSaveExclude;
import yapion.hierarchy.typegroups.YAPIONAnyType;
import yapion.packet.YAPIONPacket;
import yapion.serializing.InternalSerializer;
import yapion.serializing.data.DeserializeData;
import yapion.serializing.data.SerializeData;
import yapion.serializing.serializer.SerializerImplementation;

@YAPIONSaveExclude(context = "*")
@YAPIONLoadExclude(context = "*")
@SerializerImplementation(since = "0.10.0")
public class YAPIONPacketSerializer implements InternalSerializer<YAPIONPacket> {

    @Override
    public String type() {
        return "yapion.packet.YAPIONPacket";
    }

    @Override
    public boolean empty() {
        return true;
    }

    @Override
    public Class<?> classType() {
        return YAPIONPacket.class;
    }

    @Override
    public boolean saveWithoutAnnotation() {
        return true;
    }

    @Override
    public boolean loadWithoutAnnotation() {
        return true;
    }

    @Override
    public boolean createWithObjenesis() {
        return true;
    }

    @Override
    public YAPIONAnyType serialize(SerializeData<YAPIONPacket> serializeData) {
        return null;
    }

    @Override
    public YAPIONPacket deserialize(DeserializeData<? extends YAPIONAnyType> deserializeData) {
        return null;
    }

}