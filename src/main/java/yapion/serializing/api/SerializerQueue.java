package yapion.serializing.api;

import yapion.annotations.deserialize.YAPIONLoadExclude;
import yapion.annotations.serialize.YAPIONSaveExclude;
import yapion.hierarchy.types.YAPIONArray;
import yapion.serializing.SerializeManager;
import yapion.serializing.YAPIONDeserializer;
import yapion.serializing.YAPIONSerializer;

import java.util.Queue;

@YAPIONSaveExclude(context = "*")
@YAPIONLoadExclude(context = "*")
@SuppressWarnings({"java:S1610"})
public abstract class SerializerQueue<T extends Queue<?>> {

    public abstract Class<T> type();
    public abstract YAPIONArray serialize(T object, YAPIONSerializer yapionSerializer);
    public abstract T deserialize(YAPIONArray yapionArray, YAPIONDeserializer yapionDeserializer);

    public final void add() {
        SerializeManager.add(this);
    }

}
