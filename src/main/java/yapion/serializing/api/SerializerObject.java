package yapion.serializing.api;

import yapion.annotations.deserialize.YAPIONLoadExclude;
import yapion.annotations.serialize.YAPIONSaveExclude;
import yapion.hierarchy.types.YAPIONObject;
import yapion.serializing.YAPIONDeserializer;
import yapion.serializing.YAPIONSerializer;

@YAPIONSaveExclude(context = "*")
@YAPIONLoadExclude(context = "*")
public interface SerializerObject<T> {

    Class<T> type();
    YAPIONObject serialize(T object, YAPIONSerializer yapionSerializer);
    T deserialize(YAPIONObject yapionObject, YAPIONDeserializer yapionDeserializer);

}
