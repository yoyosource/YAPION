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
public class ThreadSerializer implements InternalSerializer<Thread> {

    @Override
    public String type() {
        return "java.lang.Thread";
    }

    @Override
    public YAPIONAny serialize(Thread object, YAPIONSerializer yapionSerializer) {
        return null;
    }

    @Override
    public Thread deserialize(YAPIONAny yapionAny, YAPIONDeserializer yapionDeserializer) {
        return null;
    }

}
