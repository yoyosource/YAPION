package yapion.serializing;

import yapion.hierarchy.YAPIONAny;

public interface Serializer<T> {

    String type();
    default String primitiveType() {
        return "";
    }
    YAPIONAny serialize(T object);

}
