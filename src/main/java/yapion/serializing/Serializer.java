package yapion.serializing;

import yapion.hierarchy.YAPIONAny;

public interface Serializer<T> {

    String type();
    default String primitiveType() {
        return "";
    }
    default String[] otherTypes() {
        return new String[0];
    }
    YAPIONAny serialize(T object, YAPIONSerializer yapionSerializer);

}
