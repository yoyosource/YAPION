package yapion.serializing.serializer.other;

import yapion.hierarchy.YAPIONAny;
import yapion.hierarchy.types.YAPIONValue;
import yapion.serializing.InternalSerializer;
import yapion.serializing.YAPIONDeserializer;
import yapion.serializing.YAPIONSerializer;

public class BooleanSerializer implements InternalSerializer<Boolean> {

    @Override
    public String type() {
        return "java.lang.Boolean";
    }

    @Override
    public String primitiveType() {
        return "boolean";
    }

    @Override
    public YAPIONAny serialize(Boolean object, YAPIONSerializer yapionSerializer) {
        return new YAPIONValue<>(object);
    }

    @Override
    public Boolean deserialize(YAPIONAny yapionAny, YAPIONDeserializer yapionDeserializer) {
        return ((YAPIONValue<Boolean>) yapionAny).get();
    }
}
