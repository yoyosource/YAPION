package yapion.serializing;

import yapion.hierarchy.YAPIONAny;

public interface Deserializer<T> {

    String type();
    default String primitiveType() {
        return "";
    }
    default String[] otherTypes() {
        return new String[0];
    }
    T deserialize(YAPIONAny yapionAny);

}
