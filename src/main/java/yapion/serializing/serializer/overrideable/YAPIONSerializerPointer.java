package yapion.serializing.serializer.overrideable;

import yapion.annotations.deserialize.YAPIONLoadExclude;
import yapion.annotations.serialize.YAPIONSaveExclude;
import yapion.hierarchy.typegroups.YAPIONAnyType;
import yapion.hierarchy.types.YAPIONPointer;
import yapion.serializing.InternalOverrideableSerializer;
import yapion.serializing.YAPIONDeserializer;
import yapion.serializing.YAPIONSerializer;
import yapion.serializing.serializer.SerializerImplementation;

@YAPIONSaveExclude(context = "*")
@YAPIONLoadExclude(context = "*")
@SerializerImplementation
public class YAPIONSerializerPointer implements InternalOverrideableSerializer<YAPIONPointer> {

    @Override
    public String type() {
        return "yapion.hierarchy.types.YAPIONPointer";
    }

    @Override
    public YAPIONAnyType serialize(YAPIONPointer object, YAPIONSerializer yapionSerializer) {
        return object;
    }

    @Override
    public YAPIONPointer deserialize(YAPIONAnyType yapionAnyType, YAPIONDeserializer yapionDeserializer) {
        return (YAPIONPointer) yapionAnyType;
    }
}
