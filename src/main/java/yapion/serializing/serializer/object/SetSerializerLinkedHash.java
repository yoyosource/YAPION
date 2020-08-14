package yapion.serializing.serializer.object;

import yapion.annotations.YAPIONLoadExclude;
import yapion.annotations.YAPIONSaveExclude;
import yapion.hierarchy.YAPIONAny;
import yapion.hierarchy.types.YAPIONArray;
import yapion.serializing.Serializer;
import yapion.serializing.YAPIONDeserializer;
import yapion.serializing.YAPIONSerializer;

import java.util.Iterator;
import java.util.LinkedHashSet;

@YAPIONSaveExclude(context = "*")
@YAPIONLoadExclude(context = "*")
public class SetSerializerLinkedHash implements Serializer<LinkedHashSet> {

    @Override
    public String type() {
        return "java.util.LinkedHashSet";
    }

    @Override
    public YAPIONAny serialize(LinkedHashSet object, YAPIONSerializer yapionSerializer) {
        Iterator iterator = object.iterator();
        YAPIONArray yapionArray = new YAPIONArray();
        while (iterator.hasNext()) {
            yapionArray.add(yapionSerializer.parse(iterator.next(), yapionSerializer));
        }
        return yapionArray;
    }

    @Override
    public LinkedHashSet deserialize(YAPIONAny yapionAny, YAPIONDeserializer yapionDeserializer) {
        return null;
    }

}
