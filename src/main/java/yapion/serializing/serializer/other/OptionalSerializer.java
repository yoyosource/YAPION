package yapion.serializing.serializer.other;

import yapion.hierarchy.YAPIONAny;
import yapion.hierarchy.YAPIONVariable;
import yapion.hierarchy.types.YAPIONObject;
import yapion.hierarchy.types.YAPIONValue;
import yapion.serializing.InternalSerializer;
import yapion.serializing.SerializeManager;
import yapion.serializing.YAPIONDeserializer;
import yapion.serializing.YAPIONSerializer;

import java.util.Optional;

public class OptionalSerializer implements InternalSerializer<Optional> {

    @Override
    public String type() {
        return "java.util.Optional";
    }

    @Override
    public YAPIONAny serialize(Optional object, YAPIONSerializer yapionSerializer) {
        YAPIONObject yapionObject = new YAPIONObject();
        yapionObject.add(new YAPIONVariable(SerializeManager.typeName, new YAPIONValue<>("java.util.Optional")));
        yapionObject.add(new YAPIONVariable("present", new YAPIONValue<>(object.isPresent())));
        if (object.isPresent()) {
            yapionObject.add(new YAPIONVariable("value", yapionSerializer.parse(object.get(), yapionSerializer)));
        }
        return yapionObject;
    }

    @Override
    @SuppressWarnings({"java:S5411"})
    public Optional deserialize(YAPIONAny yapionAny, YAPIONDeserializer yapionDeserializer) {
        YAPIONObject yapionObject = (YAPIONObject) yapionAny;
        if (yapionObject.getValue("present", true).get()) {
            return Optional.ofNullable(yapionDeserializer.parse(yapionObject.getVariable("value").getValue(), yapionDeserializer));
        }
        return Optional.empty();
    }
}
