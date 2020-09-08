package yapion.serializing.serializer.overrideable;

import yapion.annotations.deserialize.YAPIONLoadExclude;
import yapion.annotations.serialize.YAPIONSaveExclude;
import yapion.hierarchy.YAPIONAny;
import yapion.hierarchy.types.YAPIONArray;
import yapion.serializing.InternalOverrideableSerializer;
import yapion.serializing.YAPIONDeserializer;
import yapion.serializing.YAPIONSerializer;
import yapion.serializing.serializer.SerializeLoader;

@YAPIONSaveExclude(context = "*")
@YAPIONLoadExclude(context = "*")
@SerializeLoader
public class YAPIONSerializerArray implements InternalOverrideableSerializer<YAPIONArray> {

    @Override
    public String type() {
        return "yapion.hierarchy.types.YAPIONArray";
    }

    @Override
    public YAPIONAny serialize(YAPIONArray object, YAPIONSerializer yapionSerializer) {
        return object;
    }

    @Override
    public YAPIONArray deserialize(YAPIONAny yapionAny, YAPIONDeserializer yapionDeserializer) {
        return (YAPIONArray) yapionAny;
    }

}
