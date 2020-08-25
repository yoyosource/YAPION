package yapion.serializing.api;

import yapion.hierarchy.types.YAPIONArray;
import yapion.serializing.YAPIONDeserializer;
import yapion.serializing.YAPIONSerializer;

import java.util.List;

public interface SerializerList<T extends List<?>> {

    Class<T> type();
    YAPIONArray serialize(T object, YAPIONSerializer yapionSerializer);
    T deserialize(YAPIONArray yapionArray, YAPIONDeserializer yapionDeserializer);

}
