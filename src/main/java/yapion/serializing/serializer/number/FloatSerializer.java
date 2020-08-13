package yapion.serializing.serializer.number;

import yapion.hierarchy.YAPIONAny;
import yapion.hierarchy.types.YAPIONValue;
import yapion.serializing.Serializer;
import yapion.serializing.YAPIONSerializer;

public class FloatSerializer implements Serializer<Float> {

    @Override
    public String type() {
        return "java.lang.Float";
    }

    @Override
    public String primitiveType() {
        return "float";
    }

    @Override
    public YAPIONAny serialize(Float object, YAPIONSerializer yapionSerializer) {
        return new YAPIONValue<>(object);
    }

    @Override
    public Float deserialize(YAPIONAny yapionAny) {
        return ((YAPIONValue<Float>) yapionAny).get();
    }
}
