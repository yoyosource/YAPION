package yapion.serializing.serializer.object;

import yapion.hierarchy.YAPIONAny;
import yapion.hierarchy.types.YAPIONMap;
import yapion.serializing.Serializer;
import yapion.serializing.YAPIONSerializer;

import java.util.Map;

public class MapSerializer implements Serializer<Map> {

    @Override
    public String type() {
        return "java.util.Map";
    }

    @Override
    public String[] otherTypes() {
        return new String[]{"java.util.HashMap", "java.util.LinkedHashMap", "java.util.TreeMap"};
    }

    @Override
    public YAPIONAny serialize(Map object, YAPIONSerializer yapionSerializer) {
        YAPIONMap yapionMap = new YAPIONMap();
        for (Object obj : object.entrySet()) {
            Map.Entry entry = (Map.Entry)obj;
            yapionMap.add(yapionSerializer.parse(entry.getKey(), yapionSerializer), yapionSerializer.parse(entry.getValue(), yapionSerializer));
        }
        return yapionMap;
    }
}
