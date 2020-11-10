package yapion.serializing.serializer.object.optional;

import yapion.annotations.deserialize.YAPIONLoadExclude;
import yapion.annotations.serialize.YAPIONSaveExclude;
import yapion.hierarchy.typegroups.YAPIONAnyType;
import yapion.hierarchy.types.YAPIONObject;
import yapion.serializing.InternalSerializer;
import yapion.serializing.data.DeserializeData;
import yapion.serializing.data.SerializeData;
import yapion.serializing.serializer.SerializerImplementation;

import java.util.OptionalDouble;

import static yapion.utils.IdentifierUtils.TYPE_IDENTIFIER;

@YAPIONSaveExclude(context = "*")
@YAPIONLoadExclude(context = "*")
@SerializerImplementation
public class OptionalDoubleSerializer implements InternalSerializer<OptionalDouble> {

    @Override
    public String type() {
        return "java.util.OptionalDouble";
    }

    @Override
    public YAPIONAnyType serialize(SerializeData<OptionalDouble> serializeData) {
        YAPIONObject yapionObject = new YAPIONObject();
        yapionObject.add(TYPE_IDENTIFIER, type());
        yapionObject.add("present", serializeData.object.isPresent());
        serializeData.object.ifPresent(o -> yapionObject.add("value", o));
        return yapionObject;
    }

    @Override
    @SuppressWarnings({"java:S5411"})
    public OptionalDouble deserialize(DeserializeData<? extends YAPIONAnyType> deserializeData) {
        YAPIONObject yapionObject = (YAPIONObject) deserializeData.object;
        if (yapionObject.getValue("present", true).get()) {
            return OptionalDouble.of(yapionObject.getValue("value", 0D).get());
        }
        return OptionalDouble.empty();
    }
}
