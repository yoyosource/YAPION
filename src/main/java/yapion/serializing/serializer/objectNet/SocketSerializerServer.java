package yapion.serializing.serializer.objectNet;

import yapion.annotations.deserialize.YAPIONLoadExclude;
import yapion.annotations.serialize.YAPIONSaveExclude;
import yapion.hierarchy.typegroups.YAPIONAnyType;
import yapion.hierarchy.types.YAPIONValue;
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
    public YAPIONAnyType serialize(ServerSocket object, YAPIONSerializer yapionSerializer) {
        return new YAPIONValue<>(null);
    }

    @Override
    public ServerSocket deserialize(YAPIONAnyType yapionAnyType, YAPIONDeserializer yapionDeserializer) {
        return null;
    }
}
