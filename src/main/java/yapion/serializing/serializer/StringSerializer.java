package yapion.serializing.serializer;

import yapion.hierarchy.YAPIONAny;
import yapion.hierarchy.types.YAPIONValue;
import yapion.serializing.Serializer;

public class StringSerializer implements Serializer<String> {

    @Override
    public String type() {
        return "java.lang.String";
    }

    @Override
    public YAPIONAny serialize(String object) {
        return new YAPIONValue<>(object);
    }

}
