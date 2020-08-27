package yapion.serializing.api;

import yapion.hierarchy.types.YAPIONMap;
import yapion.serializing.YAPIONDeserializer;
import yapion.serializing.YAPIONSerializer;

import java.util.Map;

public interface SerializerMapInterface<T extends Map<?, ?>> {

    Class<T> type();
    YAPIONMap serialize(T object, YAPIONSerializer yapionSerializer);
    T deserialize(YAPIONMap yapionMap, YAPIONDeserializer yapionDeserializer);

}
