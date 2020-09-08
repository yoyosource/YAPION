package yapion.serializing.serializer.objectNet;

import yapion.annotations.deserialize.YAPIONLoadExclude;
import yapion.annotations.serialize.YAPIONSaveExclude;
import yapion.hierarchy.YAPIONAny;
import yapion.serializing.InternalSerializer;
import yapion.serializing.YAPIONDeserializer;
import yapion.serializing.YAPIONSerializer;
import yapion.serializing.serializer.SerializerImplementation;

import java.net.ServerSocket;

@YAPIONSaveExclude(context = "*")
@YAPIONLoadExclude(context = "*")
@SerializerImplementation
public class SocketSerializerServer implements InternalSerializer<ServerSocket> {

    @Override
    public String type() {
        return "java.net.ServerSocket";
    }

    @Override
    public YAPIONAny serialize(ServerSocket object, YAPIONSerializer yapionSerializer) {
        return null;
    }

    @Override
    public ServerSocket deserialize(YAPIONAny yapionAny, YAPIONDeserializer yapionDeserializer) {
        return null;
    }
}
