// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020 yoyosource

package yapion.serializing.serializer.other;

import yapion.annotations.deserialize.YAPIONLoadExclude;
import yapion.annotations.serialize.YAPIONSaveExclude;
import yapion.hierarchy.typegroups.YAPIONAnyType;
import yapion.packet.YAPIONPacket;
import yapion.serializing.InternalSerializer;
import yapion.serializing.YAPIONDeserializer;
import yapion.serializing.YAPIONSerializer;
import yapion.serializing.serializer.SerializerImplementation;

@YAPIONSaveExclude(context = "*")
@YAPIONLoadExclude(context = "*")
@SerializerImplementation
public class YAPIONSerializerPacket implements InternalSerializer<YAPIONPacket> {

    @Override
    public String type() {
        return "yapion.packet.YAPIONPacket";
    }

    @Override
    public boolean empty() {
        return true;
    }

    @Override
    public YAPIONAnyType serialize(YAPIONPacket object, YAPIONSerializer yapionSerializer) {
        return null;
    }

    @Override
    public YAPIONPacket deserialize(YAPIONAnyType yapionAnyType, YAPIONDeserializer yapionDeserializer) {
        return null;
    }
}