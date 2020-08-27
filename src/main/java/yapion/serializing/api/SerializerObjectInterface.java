package yapion.serializing.api;

import yapion.hierarchy.types.YAPIONObject;
import yapion.serializing.YAPIONDeserializer;
import yapion.serializing.YAPIONSerializer;

public interface SerializerObjectInterface<T> {

    Class<T> type();
    YAPIONObject serialize(T object, YAPIONSerializer yapionSerializer);
    T deserialize(YAPIONObject yapionObject, YAPIONDeserializer yapionDeserializer);

}
