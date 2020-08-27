package yapion.serializing.serializer.overrideable;

import yapion.hierarchy.YAPIONAny;
import yapion.hierarchy.types.YAPIONMap;
import yapion.serializing.InternalSerializer;
import yapion.serializing.YAPIONDeserializer;
import yapion.serializing.YAPIONSerializer;

public class YAPIONSerializerMap implements InternalSerializer<YAPIONMap> {

    @Override
    public String type() {
        return "yapion.hierarchy.types.YAPIONMap";
    }

    @Override
    public YAPIONAny serialize(YAPIONMap object, YAPIONSerializer yapionSerializer) {
        System.out.println(object);
        return object;
    }

    @Override
    public YAPIONMap deserialize(YAPIONAny yapionAny, YAPIONDeserializer yapionDeserializer) {
        return (YAPIONMap) yapionAny;
    }

}
