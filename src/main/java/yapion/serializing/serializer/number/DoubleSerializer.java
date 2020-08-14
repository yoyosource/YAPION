package yapion.serializing.serializer.number;

import yapion.annotations.YAPIONLoadExclude;
import yapion.annotations.YAPIONSaveExclude;
import yapion.hierarchy.YAPIONAny;
import yapion.hierarchy.types.YAPIONValue;
import yapion.serializing.Serializer;
import yapion.serializing.YAPIONSerializer;

@YAPIONSaveExclude(context = "*")
@YAPIONLoadExclude(context = "*")
public class DoubleSerializer implements Serializer<Double> {

    @Override
    public String type() {
        return "java.lang.Double";
    }

    @Override
    public String primitiveType() {
        return "double";
    }

    @Override
    public YAPIONAny serialize(Double object, YAPIONSerializer yapionSerializer) {
        return new YAPIONValue<>(object);
    }

    @Override
    public Double deserialize(YAPIONAny yapionAny) {
        return ((YAPIONValue<Double>) yapionAny).get();
    }
}
