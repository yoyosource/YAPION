package yapion.serializing.serializer.object;

import yapion.annotations.YAPIONLoadExclude;
import yapion.annotations.YAPIONSaveExclude;
import yapion.hierarchy.YAPIONAny;
import yapion.hierarchy.types.YAPIONArray;
import yapion.serializing.Serializer;
import yapion.serializing.YAPIONSerializer;

import java.util.List;

@YAPIONSaveExclude(context = "*")
@YAPIONLoadExclude(context = "*")
public class ListSerializer implements Serializer<List> {

    @Override
    public String type() {
        return "java.util.List";
    }

    @Override
    public String[] otherTypes() {
        return new String[]{"java.util.LinkedList", "java.util.ArrayList"};
    }

    @Override
    public YAPIONAny serialize(List object, YAPIONSerializer yapionSerializer) {
        YAPIONArray yapionArray = new YAPIONArray();
        for (int i = 0; i < object.size(); i++) {
            yapionArray.add(yapionSerializer.parse(object.get(i), yapionSerializer));
        }
        return yapionArray;
    }

    @Override
    public List deserialize(YAPIONAny yapionAny) {
        return null;
    }
}
