package yapion.serializing.serializer.other;

import yapion.annotations.deserialize.YAPIONLoadExclude;
import yapion.annotations.serialize.YAPIONSaveExclude;
import yapion.hierarchy.YAPIONAny;
import yapion.serializing.InternalSerializer;
import yapion.serializing.YAPIONDeserializer;
import yapion.serializing.YAPIONSerializer;
import yapion.serializing.serializer.SerializeLoader;

@YAPIONSaveExclude(context = "*")
@YAPIONLoadExclude(context = "*")
@SerializeLoader
public class RunnableSerializer implements InternalSerializer<Runnable> {

    @Override
    public String type() {
        return "java.lang.Runnable";
    }

    @Override
    public YAPIONAny serialize(Runnable object, YAPIONSerializer yapionSerializer) {
        return null;
    }

    @Override
    public Runnable deserialize(YAPIONAny yapionAny, YAPIONDeserializer yapionDeserializer) {
        return null;
    }

}
