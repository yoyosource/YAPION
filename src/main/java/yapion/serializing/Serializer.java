package yapion.serializing;

import yapion.annotations.deserialize.YAPIONLoadExclude;
import yapion.annotations.serialize.YAPIONSaveExclude;
import yapion.hierarchy.YAPIONAny;
import yapion.hierarchy.types.YAPIONObject;

@YAPIONSaveExclude(context = "*")
@YAPIONLoadExclude(context = "*")
public interface Serializer<T> {

    Class<T> type();
    YAPIONObject serialize(T object, YAPIONSerializer yapionSerializer);
    T deserialize(YAPIONObject yapionObject, YAPIONDeserializer yapionDeserializer);

}
