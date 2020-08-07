package yapion.serializing.serializer.number;

import yapion.hierarchy.YAPIONAny;
import yapion.hierarchy.types.YAPIONValue;
import yapion.serializing.Serializer;
import yapion.serializing.YAPIONSerializer;

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
}
