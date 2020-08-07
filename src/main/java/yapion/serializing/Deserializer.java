package yapion.serializing;

import yapion.hierarchy.YAPIONAny;

public interface Deserializer<T> {

    String type();
    T deserialize(YAPIONAny yapionAny);

}
