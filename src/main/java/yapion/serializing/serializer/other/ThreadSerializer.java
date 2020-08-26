package yapion.serializing.serializer.other;

import yapion.hierarchy.YAPIONAny;
import yapion.serializing.InternalSerializer;
import yapion.serializing.YAPIONDeserializer;
import yapion.serializing.YAPIONSerializer;

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
