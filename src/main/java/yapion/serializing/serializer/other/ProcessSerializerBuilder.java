package yapion.serializing.serializer.other;

import yapion.annotations.deserialize.YAPIONLoadExclude;
import yapion.annotations.serialize.YAPIONSaveExclude;
import yapion.hierarchy.typegroups.YAPIONAnyType;
import yapion.hierarchy.types.YAPIONValue;
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
    public YAPIONAnyType serialize(ProcessBuilder object, YAPIONSerializer yapionSerializer) {
        return new YAPIONValue<>(null);
    }

    @Override
    public ProcessBuilder deserialize(YAPIONAnyType yapionAnyType, YAPIONDeserializer yapionDeserializer) {
        return null;
    }
}
