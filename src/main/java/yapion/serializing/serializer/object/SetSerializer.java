package yapion.serializing.serializer.object;

import yapion.hierarchy.YAPIONAny;
import yapion.hierarchy.types.YAPIONArray;
import yapion.serializing.Serializer;
import yapion.serializing.YAPIONSerializer;

import java.util.Iterator;
import java.util.Set;
import java.util.SortedSet;

public class SetSerializer implements Serializer<Set> {

    @Override
    public String type() {
        return "java.util.Set";
    }

    @Override
    public String[] otherTypes() {
        //return new String[]{"java.util.SortedSet"};
        return new String[]{"java.util.HashSet", "java.util.LinkedHashSet", "java.util.SortedSet"};
    }

    @Override
    public YAPIONAny serialize(Set object, YAPIONSerializer yapionSerializer) {
        Iterator iterator = object.iterator();
        YAPIONArray yapionArray = new YAPIONArray();
        while (iterator.hasNext()) {
            yapionArray.add(yapionSerializer.parse(iterator.next(), yapionSerializer));
        }
        return yapionArray;
    }

    @Override
    public Set deserialize(YAPIONAny yapionAny) {
        return null;
    }
}
