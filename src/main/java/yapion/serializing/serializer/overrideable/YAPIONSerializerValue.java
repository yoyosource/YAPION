package yapion.serializing.serializer.overrideable;

import yapion.hierarchy.YAPIONAny;
import yapion.hierarchy.types.YAPIONValue;
import yapion.serializing.InternalSerializer;
import yapion.serializing.YAPIONDeserializer;
import yapion.serializing.YAPIONSerializer;

public class YAPIONSerializerValue implements InternalSerializer<YAPIONValue<?>> {

    @Override
    public String type() {
        return "yapion.hierarchy.types.YAPIONValue";
    }

    @Override
    public YAPIONAny serialize(YAPIONValue<?> object, YAPIONSerializer yapionSerializer) {
        System.out.println(object);
        return object;
    }

    @Override
    public YAPIONValue<?> deserialize(YAPIONAny yapionAny, YAPIONDeserializer yapionDeserializer) {
        return (YAPIONValue<?>) yapionAny;
    }

}
