package yapion.serializing.serializer.other;

import yapion.annotations.deserialize.YAPIONLoadExclude;
import yapion.annotations.serialize.YAPIONSaveExclude;
import yapion.hierarchy.YAPIONAny;
import yapion.serializing.InternalSerializer;
import yapion.serializing.YAPIONDeserializer;
import yapion.serializing.YAPIONSerializer;
import yapion.serializing.serializer.SerializerImplementation;

@YAPIONSaveExclude(context = "*")
@YAPIONLoadExclude(context = "*")
@SerializerImplementation
public class ThreadSerializerLocal implements InternalSerializer<ThreadLocal<?>> {

    @Override
    public String type() {
        return "java.lang.ThreadLocal";
    }

    @Override
    public YAPIONAny serialize(ThreadLocal<?> object, YAPIONSerializer yapionSerializer) {
        return null;
    }

    @Override
    public ThreadLocal<?> deserialize(YAPIONAny yapionAny, YAPIONDeserializer yapionDeserializer) {
        return null;
    }
}
