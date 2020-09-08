package yapion.serializing.serializer.overrideable;

import yapion.annotations.deserialize.YAPIONLoadExclude;
import yapion.annotations.serialize.YAPIONSaveExclude;
import yapion.hierarchy.YAPIONAny;
import yapion.hierarchy.types.YAPIONObject;
import yapion.serializing.InternalOverrideableSerializer;
import yapion.serializing.YAPIONDeserializer;
import yapion.serializing.YAPIONSerializer;
import yapion.serializing.serializer.SerializerImplementation;

@YAPIONSaveExclude(context = "*")
@YAPIONLoadExclude(context = "*")
@SerializerImplementation
public class YAPIONSerializerObject implements InternalOverrideableSerializer<YAPIONObject> {

    @Override
    public String type() {
        return "yapion.hierarchy.types.YAPIONObject";
    }

    @Override
    public YAPIONAny serialize(YAPIONObject object, YAPIONSerializer yapionSerializer) {
        return object;
    }

    @Override
    public YAPIONObject deserialize(YAPIONAny yapionAny, YAPIONDeserializer yapionDeserializer) {
        return (YAPIONObject) yapionAny;
    }

}
