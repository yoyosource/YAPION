package yapion.serializing.serializer.number;

import yapion.hierarchy.YAPIONAny;
import yapion.hierarchy.types.YAPIONValue;
import yapion.serializing.Serializer;

public class ByteSerializer implements Serializer<Byte> {

    @Override
    public String type() {
        return "java.lang.Byte";
    }

    @Override
    public String primitiveType() {
        return "byte";
    }

    @Override
    public YAPIONAny serialize(Byte object) {
        return new YAPIONValue<>(object);
    }
}
