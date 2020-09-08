package yapion.serializing.serializer.overrideable;

import yapion.annotations.deserialize.YAPIONLoadExclude;
import yapion.annotations.serialize.YAPIONSaveExclude;
import yapion.hierarchy.YAPIONAny;
import yapion.hierarchy.types.YAPIONValue;
import yapion.serializing.InternalOverrideableSerializer;
import yapion.serializing.YAPIONDeserializer;
import yapion.serializing.YAPIONSerializer;
import yapion.serializing.serializer.SerializerImplementation;

@YAPIONSaveExclude(context = "*")
@YAPIONLoadExclude(context = "*")
@SerializerImplementation
public class YAPIONSerializerValue implements InternalOverrideableSerializer<YAPIONValue<?>> {

    @Override
    public String type() {
        return "yapion.hierarchy.types.YAPIONValue";
    }

    @Override
    public YAPIONAny serialize(YAPIONValue<?> object, YAPIONSerializer yapionSerializer) {
        return object;
    }

    @Override
    public YAPIONValue<?> deserialize(YAPIONAny yapionAny, YAPIONDeserializer yapionDeserializer) {
        return (YAPIONValue<?>) yapionAny;
    }

}
