package yapion.serializing.serializer.objectConcurrent;

import yapion.annotations.deserialize.YAPIONLoadExclude;
import yapion.annotations.serialize.YAPIONSaveExclude;
import yapion.hierarchy.YAPIONAny;
import yapion.hierarchy.YAPIONVariable;
import yapion.hierarchy.types.YAPIONMap;
import yapion.hierarchy.types.YAPIONObject;
import yapion.hierarchy.types.YAPIONValue;
import yapion.serializing.InternalSerializer;
import yapion.serializing.SerializeManager;
import yapion.serializing.YAPIONDeserializer;
import yapion.serializing.YAPIONSerializer;
import yapion.serializing.serializer.SerializerImplementation;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;

@YAPIONSaveExclude(context = "*")
@YAPIONLoadExclude(context = "*")
@SerializerImplementation
public class MapSerializerConcurrentSkipList implements InternalSerializer<ConcurrentSkipListMap<?, ?>> {

    @Override
    public String type() {
        return "java.util.concurrent.ConcurrentSkipListMap";
    }

    @Override
    public YAPIONAny serialize(ConcurrentSkipListMap<?, ?> object, YAPIONSerializer yapionSerializer) {
        YAPIONObject yapionObject = new YAPIONObject();
        yapionObject.add(new YAPIONVariable(SerializeManager.TYPE_IDENTIFIER, new YAPIONValue<>(type())));
        YAPIONMap yapionMap = new YAPIONMap();
        yapionObject.add(new YAPIONVariable("values", yapionMap));
        for (Object obj : object.entrySet()) {
            Map.Entry entry = (Map.Entry)obj;
            yapionMap.add(yapionSerializer.parse(entry.getKey()), yapionSerializer.parse(entry.getValue()));
        }
        return yapionObject;
    }

    @Override
    public ConcurrentSkipListMap<?, ?> deserialize(YAPIONAny yapionAny, YAPIONDeserializer yapionDeserializer) {
        YAPIONMap yapionMap = ((YAPIONObject) yapionAny).getMap("values");
        ConcurrentSkipListMap<Object, Object> map = new ConcurrentSkipListMap<>();
        for (YAPIONAny key : yapionMap.getKeys()) {
            map.put(yapionDeserializer.parse(key), yapionDeserializer.parse(yapionMap.get(key)));
        }
        return map;
    }
}
