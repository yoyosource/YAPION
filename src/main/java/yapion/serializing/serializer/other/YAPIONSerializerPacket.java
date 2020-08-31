package yapion.serializing.serializer.other;

import yapion.hierarchy.YAPIONAny;
import yapion.packet.YAPIONPacket;
import yapion.serializing.InternalSerializer;
import yapion.serializing.YAPIONDeserializer;
import yapion.serializing.YAPIONSerializer;

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
    public YAPIONAny serialize(YAPIONPacket object, YAPIONSerializer yapionSerializer) {
        return null;
    }

    @Override
    public YAPIONPacket deserialize(YAPIONAny yapionAny, YAPIONDeserializer yapionDeserializer) {
        return null;
    }
}
