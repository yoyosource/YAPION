package yapion.serializing;

import yapion.hierarchy.YAPIONAny;

public interface Serializer<T> {

    String type();
    YAPIONAny serialize(T object);

}
