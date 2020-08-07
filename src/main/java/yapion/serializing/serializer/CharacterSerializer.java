package yapion.serializing.serializer;

import yapion.hierarchy.YAPIONAny;
import yapion.hierarchy.types.YAPIONValue;
import yapion.serializing.Serializer;

public class CharacterSerializer implements Serializer<Character> {

    @Override
    public String type() {
        return "java.lang.Character";
    }

    @Override
    public YAPIONAny serialize(Character object) {
        return new YAPIONValue<>(object);
    }
}
