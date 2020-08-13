package yapion.serializing.serializer.other;

import yapion.hierarchy.YAPIONAny;
import yapion.hierarchy.types.YAPIONValue;
import yapion.serializing.Serializer;
import yapion.serializing.YAPIONSerializer;

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
    public StringBuilder deserialize(YAPIONAny yapionAny) {
        return new StringBuilder().append(((YAPIONValue<String>) yapionAny).get());
    }
}
