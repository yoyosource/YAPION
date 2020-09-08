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
public class ProcessSerializerBuilder implements InternalSerializer<ProcessBuilder> {

    @Override
    public String type() {
        return "java.lang.ProcessBuilder";
    }

    @Override
    public YAPIONAny serialize(ProcessBuilder object, YAPIONSerializer yapionSerializer) {
        return null;
    }

    @Override
    public ProcessBuilder deserialize(YAPIONAny yapionAny, YAPIONDeserializer yapionDeserializer) {
        return null;
    }
}
