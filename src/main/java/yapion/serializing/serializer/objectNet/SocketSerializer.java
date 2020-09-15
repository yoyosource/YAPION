package yapion.serializing.serializer.objectNet;

import yapion.annotations.deserialize.YAPIONLoadExclude;
import yapion.annotations.serialize.YAPIONSaveExclude;
import yapion.hierarchy.typegroups.YAPIONAnyType;
import yapion.hierarchy.types.YAPIONValue;
import yapion.serializing.InternalSerializer;
import yapion.serializing.YAPIONDeserializer;
import yapion.serializing.YAPIONSerializer;
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
    public YAPIONAnyType serialize(Socket object, YAPIONSerializer yapionSerializer) {
        return new YAPIONValue<>(null);
    }

    @Override
    public Socket deserialize(YAPIONAnyType yapionAnyType, YAPIONDeserializer yapionDeserializer) {
        return null;
    }
}
