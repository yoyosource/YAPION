package yapion.serializing;

import yapion.annotations.YAPIONLoadExclude;
import yapion.annotations.YAPIONSaveExclude;
import yapion.hierarchy.YAPIONAny;

@YAPIONSaveExclude(context = "*")
@YAPIONLoadExclude(context = "*")
public interface Serializer<T> {

    String type();
    default String primitiveType() {
        return "";
    }
    default String[] otherTypes() {
        return new String[0];
    }
    YAPIONAny serialize(T object, YAPIONSerializer yapionSerializer);
    T deserialize(YAPIONAny yapionAny, YAPIONDeserializer yapionDeserializer);

}
