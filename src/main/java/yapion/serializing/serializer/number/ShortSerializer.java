package yapion.serializing.serializer.number;

import yapion.annotations.YAPIONLoadExclude;
import yapion.annotations.YAPIONSaveExclude;
import yapion.hierarchy.YAPIONAny;
import yapion.hierarchy.types.YAPIONValue;
import yapion.serializing.Serializer;
import yapion.serializing.YAPIONSerializer;

@YAPIONSaveExclude(context = "*")
@YAPIONLoadExclude(context = "*")
public class ShortSerializer implements Serializer<Short> {

    @Override
    public String type() {
        return "java.lang.Short";
    }

    @Override
    public String primitiveType() {
        return "short";
    }

    @Override
    public YAPIONAny serialize(Short object, YAPIONSerializer yapionSerializer) {
        return new YAPIONValue<>(object);
    }

    @Override
    public Short deserialize(YAPIONAny yapionAny) {
        return ((YAPIONValue<Short>) yapionAny).get();
    }
}
