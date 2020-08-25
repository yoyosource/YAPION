package yapion.serializing.api;

import yapion.annotations.deserialize.YAPIONLoadExclude;
import yapion.annotations.serialize.YAPIONSaveExclude;
import yapion.hierarchy.types.YAPIONMap;
import yapion.serializing.SerializeManager;
import yapion.serializing.YAPIONDeserializer;
import yapion.serializing.YAPIONSerializer;

import java.util.Map;

@YAPIONSaveExclude(context = "*")
@YAPIONLoadExclude(context = "*")
@SuppressWarnings({"java:S1610"})
public abstract class SerializerMap<T extends Map<?, ?>> {

    public abstract Class<T> type();
    public abstract YAPIONMap serialize(T object, YAPIONSerializer yapionSerializer);
    public abstract T deserialize(YAPIONMap yapionMap, YAPIONDeserializer yapionDeserializer);

    public final void add() {
        SerializeManager.add(this);
    }

}
