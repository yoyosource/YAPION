package yapion.serializing.api;

import yapion.hierarchy.types.YAPIONArray;
import yapion.serializing.YAPIONDeserializer;
import yapion.serializing.YAPIONSerializer;

import java.util.Queue;

public interface SerializerQueueInterface<T extends Queue<?>> {

    Class<T> type();
    YAPIONArray serialize(T object, YAPIONSerializer yapionSerializer);
    T deserialize(YAPIONArray yapionArray, YAPIONDeserializer yapionDeserializer);

}