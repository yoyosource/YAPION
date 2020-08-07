package yapion.serializing.serializer.number;

import yapion.hierarchy.YAPIONAny;
import yapion.hierarchy.types.YAPIONValue;
import yapion.serializing.Serializer;

public class IntegerSerializer implements Serializer<Integer> {

    @Override
    public String type() {
        return "java.lang.Integer";
    }

    @Override
    public String primitiveType() {
        return "int";
    }

    @Override
    public YAPIONAny serialize(Integer object) {
        return new YAPIONValue<>(object);
    }
}
