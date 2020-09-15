package yapion.serializing.serializer.overrideable;

import yapion.annotations.deserialize.YAPIONLoadExclude;
import yapion.annotations.serialize.YAPIONSaveExclude;
import yapion.hierarchy.typegroups.YAPIONAnyType;
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
    public YAPIONAnyType serialize(YAPIONValue<?> object, YAPIONSerializer yapionSerializer) {
        return object;
    }

    @Override
    public YAPIONValue<?> deserialize(YAPIONAnyType yapionAnyType, YAPIONDeserializer yapionDeserializer) {
        return (YAPIONValue<?>) yapionAnyType;
    }

}
