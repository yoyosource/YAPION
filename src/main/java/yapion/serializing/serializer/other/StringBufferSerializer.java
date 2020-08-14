package yapion.serializing.serializer.other;

import yapion.annotations.YAPIONLoadExclude;
import yapion.annotations.YAPIONSaveExclude;
import yapion.hierarchy.YAPIONAny;
import yapion.hierarchy.types.YAPIONValue;
import yapion.serializing.Serializer;
import yapion.serializing.YAPIONSerializer;

@YAPIONSaveExclude(context = "*")
@YAPIONLoadExclude(context = "*")
public class StringBufferSerializer implements Serializer<StringBuffer> {

    @Override
    public String type() {
        return "java.lang.StringBuffer";
    }

    @Override
    public YAPIONAny serialize(StringBuffer object, YAPIONSerializer yapionSerializer) {
        return new YAPIONValue<>(object.toString());
    }

    @Override
    public StringBuffer deserialize(YAPIONAny yapionAny) {
        return new StringBuffer().append(((YAPIONValue<String>) yapionAny).get());
    }
}
