package yapion.serializing.serializer.overrideable;

import yapion.hierarchy.YAPIONAny;
import yapion.hierarchy.types.YAPIONArray;
import yapion.serializing.InternalSerializer;
import yapion.serializing.YAPIONDeserializer;
import yapion.serializing.YAPIONSerializer;

public class YAPIONSerializerArray implements InternalSerializer<YAPIONArray> {

    @Override
    public String type() {
        return "yapion.hierarchy.types.YAPIONArray";
    }

    @Override
    public YAPIONAny serialize(YAPIONArray object, YAPIONSerializer yapionSerializer) {
        System.out.println(object);
        return object;
    }

    @Override
    public YAPIONArray deserialize(YAPIONAny yapionAny, YAPIONDeserializer yapionDeserializer) {
        return (YAPIONArray) yapionAny;
    }

}
