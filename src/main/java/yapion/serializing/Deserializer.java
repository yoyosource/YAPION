package yapion.serializing;

import yapion.hierarchy.YAPIONAny;

public interface Deserializer<T> {

    String type();
    default String primitiveType() {
        return "";
    }
    T deserialize(YAPIONAny yapionAny);

}
