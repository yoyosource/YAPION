package yapion.serializing.serializer.number;

import yapion.annotations.YAPIONLoadExclude;
import yapion.annotations.YAPIONSaveExclude;
import yapion.hierarchy.YAPIONAny;
import yapion.hierarchy.types.YAPIONValue;
import yapion.serializing.Serializer;
import yapion.serializing.YAPIONDeserializer;
import yapion.serializing.YAPIONSerializer;

@YAPIONSaveExclude(context = "*")
@YAPIONLoadExclude(context = "*")
public class IntegerSerializer implements Serializer<Integer> {

    @Override
    public String type() {
        return "java.lang.Integer";
    }

    @Override
    public String primitiveType() {
        return "int";
    }

    @Override
    public YAPIONAny serialize(Integer object, YAPIONSerializer yapionSerializer) {
        return new YAPIONValue<>(object);
    }

    @Override
    public Integer deserialize(YAPIONAny yapionAny, YAPIONDeserializer yapionDeserializer) {
        return ((YAPIONValue<Integer>) yapionAny).get();
    }
}
