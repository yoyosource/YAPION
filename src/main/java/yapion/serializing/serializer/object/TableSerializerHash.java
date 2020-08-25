package yapion.serializing.serializer.object;

import yapion.hierarchy.YAPIONAny;
import yapion.hierarchy.YAPIONVariable;
import yapion.hierarchy.types.YAPIONMap;
import yapion.hierarchy.types.YAPIONObject;
import yapion.hierarchy.types.YAPIONValue;
import yapion.serializing.InternalSerializer;
import yapion.serializing.SerializeManager;
import yapion.serializing.YAPIONDeserializer;
import yapion.serializing.YAPIONSerializer;

import java.util.Hashtable;
import java.util.Map;

public class TableSerializerHash implements InternalSerializer<Hashtable> {

    @Override
    public String type() {
        return "java.util.Hashtable";
    }

    @Override
    public YAPIONAny serialize(Hashtable object, YAPIONSerializer yapionSerializer) {
        YAPIONObject yapionObject = new YAPIONObject();
        yapionObject.add(new YAPIONVariable(SerializeManager.typeName, new YAPIONValue<>(type())));
        YAPIONMap yapionMap = new YAPIONMap();
        yapionObject.add(new YAPIONVariable("values", yapionMap));
        for (Object obj : object.entrySet()) {
            Map.Entry entry = (Map.Entry)obj;
            yapionMap.add(yapionSerializer.parse(entry.getKey(), yapionSerializer), yapionSerializer.parse(entry.getValue(), yapionSerializer));
        }
        return yapionObject;
    }

    @Override
    public Hashtable deserialize(YAPIONAny yapionAny, YAPIONDeserializer yapionDeserializer) {
        YAPIONMap yapionMap = ((YAPIONObject) yapionAny).getMap("values");
        Hashtable<Object, Object> table = new Hashtable<>();
        for (YAPIONAny key : yapionMap.getKeys()) {
            table.put(yapionDeserializer.parse(key, yapionDeserializer), yapionDeserializer.parse(yapionMap.get(key), yapionDeserializer));
        }
        return table;
    }
}
