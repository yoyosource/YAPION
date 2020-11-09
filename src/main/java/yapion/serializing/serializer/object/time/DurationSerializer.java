package yapion.serializing.serializer.object.time;

import yapion.annotations.deserialize.YAPIONLoadExclude;
import yapion.annotations.serialize.YAPIONSaveExclude;
import yapion.hierarchy.typegroups.YAPIONAnyType;
import yapion.hierarchy.types.YAPIONObject;
import yapion.serializing.InternalSerializer;
import yapion.serializing.data.DeserializeData;
import yapion.serializing.data.SerializeData;
import yapion.serializing.serializer.SerializerImplementation;

import java.time.Duration;

import static yapion.utils.IdentifierUtils.TYPE_IDENTIFIER;

@YAPIONSaveExclude(context = "*")
@YAPIONLoadExclude(context = "*")
@SerializerImplementation
public class DurationSerializer implements InternalSerializer<Duration> {

    @Override
    public String type() {
        return "java.time.Duration";
    }

    @Override
    public YAPIONAnyType serialize(SerializeData<Duration> serializeData) {
        YAPIONObject yapionObject = new YAPIONObject();
        yapionObject.add(TYPE_IDENTIFIER, type());
        yapionObject.add("seconds", serializeData.object.getSeconds());
        yapionObject.add("nano", serializeData.object.getNano());
        return yapionObject;
    }

    @Override
    public Duration deserialize(DeserializeData<? extends YAPIONAnyType> deserializeData) {
        YAPIONObject yapionObject = (YAPIONObject) deserializeData.object;
        long seconds = yapionObject.getValue("seconds", 0L).get();
        long nano = yapionObject.getValue("nano", 0).get();
        return Duration.ofSeconds(seconds, nano);
    }
}
