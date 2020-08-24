package yapion.serializing;

import yapion.hierarchy.types.YAPIONMap;

public interface SerializerMap<T> {

    Class<T> type();
    YAPIONMap serialize(T object, YAPIONSerializer yapionSerializer);
    T deserialize(YAPIONMap yapionMap, YAPIONDeserializer yapionDeserializer);

}
