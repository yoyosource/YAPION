package yapion.serializing.serializer.overrideable;

import yapion.annotations.deserialize.YAPIONLoadExclude;
import yapion.annotations.serialize.YAPIONSaveExclude;
import yapion.hierarchy.typegroups.YAPIONAnyType;
import yapion.hierarchy.types.YAPIONMap;
import yapion.serializing.InternalOverrideableSerializer;
import yapion.serializing.YAPIONDeserializer;
import yapion.serializing.YAPIONSerializer;
import yapion.serializing.serializer.SerializerImplementation;

@YAPIONSaveExclude(context = "*")
@YAPIONLoadExclude(context = "*")
@SerializerImplementation
public class YAPIONSerializerMap implements InternalOverrideableSerializer<YAPIONMap> {

    @Override
    public String type() {
        return "yapion.hierarchy.types.YAPIONMap";
    }

    @Override
    public YAPIONAnyType serialize(YAPIONMap object, YAPIONSerializer yapionSerializer) {
        return object;
    }

    @Override
    public YAPIONMap deserialize(YAPIONAnyType yapionAnyType, YAPIONDeserializer yapionDeserializer) {
        return (YAPIONMap) yapionAnyType;
    }

}
