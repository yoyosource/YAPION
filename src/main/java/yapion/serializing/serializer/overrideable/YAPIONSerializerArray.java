package yapion.serializing.serializer.overrideable;

import yapion.annotations.deserialize.YAPIONLoadExclude;
import yapion.annotations.serialize.YAPIONSaveExclude;
import yapion.hierarchy.typegroups.YAPIONAnyType;
import yapion.hierarchy.types.YAPIONArray;
import yapion.serializing.InternalOverrideableSerializer;
import yapion.serializing.YAPIONDeserializer;
import yapion.serializing.YAPIONSerializer;
import yapion.serializing.serializer.SerializerImplementation;

@YAPIONSaveExclude(context = "*")
@YAPIONLoadExclude(context = "*")
@SerializerImplementation
public class YAPIONSerializerArray implements InternalOverrideableSerializer<YAPIONArray> {

    @Override
    public String type() {
        return "yapion.hierarchy.types.YAPIONArray";
    }

    @Override
    public YAPIONAnyType serialize(YAPIONArray object, YAPIONSerializer yapionSerializer) {
        return object;
    }

    @Override
    public YAPIONArray deserialize(YAPIONAnyType yapionAnyType, YAPIONDeserializer yapionDeserializer) {
        return (YAPIONArray) yapionAnyType;
    }

}
