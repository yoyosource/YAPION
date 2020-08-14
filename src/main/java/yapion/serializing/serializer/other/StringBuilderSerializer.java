package yapion.serializing.serializer.other;

import yapion.annotations.YAPIONLoadExclude;
import yapion.annotations.YAPIONSaveExclude;
import yapion.hierarchy.YAPIONAny;
import yapion.hierarchy.types.YAPIONValue;
import yapion.serializing.Serializer;
import yapion.serializing.YAPIONDeserializer;
import yapion.serializing.YAPIONSerializer;

@YAPIONSaveExclude(context = "*")
@YAPIONLoadExclude(context = "*")
public class StringBuilderSerializer implements Serializer<StringBuilder> {

    @Override
    public String type() {
        return "java.lang.StringBuilder";
    }

    @Override
    public YAPIONAny serialize(StringBuilder object, YAPIONSerializer yapionSerializer) {
        return new YAPIONValue<>(object.toString());
    }

    @Override
    public StringBuilder deserialize(YAPIONAny yapionAny, YAPIONDeserializer yapionDeserializer) {
        return new StringBuilder().append(((YAPIONValue<String>) yapionAny).get());
    }
}
