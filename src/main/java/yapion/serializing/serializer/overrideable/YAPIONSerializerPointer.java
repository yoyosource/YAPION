package yapion.serializing.serializer.overrideable;

import yapion.hierarchy.YAPIONAny;
import yapion.hierarchy.types.YAPIONPointer;
import yapion.serializing.InternalSerializer;
import yapion.serializing.YAPIONDeserializer;
import yapion.serializing.YAPIONSerializer;

public class YAPIONSerializerPointer implements InternalSerializer<YAPIONPointer> {

    @Override
    public String type() {
        return "yapion.hierarchy.types.YAPIONPointer";
    }

    @Override
    public YAPIONAny serialize(YAPIONPointer object, YAPIONSerializer yapionSerializer) {
        System.out.println(object);
        return object;
    }

    @Override
    public YAPIONPointer deserialize(YAPIONAny yapionAny, YAPIONDeserializer yapionDeserializer) {
        return (YAPIONPointer) yapionAny;
    }
}
