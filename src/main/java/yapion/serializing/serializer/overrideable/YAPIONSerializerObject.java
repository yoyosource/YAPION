package yapion.serializing.serializer.overrideable;

import yapion.hierarchy.YAPIONAny;
import yapion.hierarchy.types.YAPIONObject;
import yapion.serializing.InternalSerializer;
import yapion.serializing.YAPIONDeserializer;
import yapion.serializing.YAPIONSerializer;

public class YAPIONSerializerObject implements InternalSerializer<YAPIONObject> {

    @Override
    public String type() {
        return "yapion.hierarchy.types.YAPIONObject";
    }

    @Override
    public YAPIONAny serialize(YAPIONObject object, YAPIONSerializer yapionSerializer) {
        System.out.println(object);
        return object;
    }

    @Override
    public YAPIONObject deserialize(YAPIONAny yapionAny, YAPIONDeserializer yapionDeserializer) {
        return (YAPIONObject) yapionAny;
    }

}
