package yapion.serializing.serializer.number;

import yapion.hierarchy.YAPIONAny;
import yapion.hierarchy.types.YAPIONValue;
import yapion.serializing.Serializer;

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
    public YAPIONAny serialize(Double object) {
        return new YAPIONValue<>(object);
    }
}
