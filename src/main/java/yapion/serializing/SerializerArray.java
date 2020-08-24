package yapion.serializing;

import yapion.hierarchy.types.YAPIONArray;

public interface SerializerArray<T> {

    Class<T> type();
    YAPIONArray serialize(T object, YAPIONSerializer yapionSerializer);
    T deserialize(YAPIONArray yapionArray, YAPIONDeserializer yapionDeserializer);

}
