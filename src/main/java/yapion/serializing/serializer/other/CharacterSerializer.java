package yapion.serializing.serializer.other;

import yapion.hierarchy.YAPIONAny;
import yapion.hierarchy.types.YAPIONValue;
import yapion.serializing.Serializer;
import yapion.serializing.YAPIONSerializer;

public class CharacterSerializer implements Serializer<Character> {

    @Override
    public String type() {
        return "java.lang.Character";
    }

    @Override
    public String primitiveType() {
        return "char";
    }

    @Override
    public YAPIONAny serialize(Character object, YAPIONSerializer yapionSerializer) {
        return new YAPIONValue<>(object);
    }

    @Override
    public Character deserialize(YAPIONAny yapionAny) {
        return ((YAPIONValue<Character>) yapionAny).get();
    }
}
