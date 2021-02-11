package yapion.serializing.serializer.object.map;

import yapion.exceptions.serializing.YAPIONDeserializerException;
import yapion.hierarchy.api.groups.YAPIONAnyType;
import yapion.hierarchy.types.YAPIONMap;
import yapion.hierarchy.types.YAPIONObject;
import yapion.serializing.InternalSerializer;
import yapion.serializing.data.DeserializeData;
import yapion.serializing.data.SerializeData;
import yapion.serializing.serializer.SerializerImplementation;
import yapion.serializing.utils.DeserializeUtils;
import yapion.serializing.utils.SerializeUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;

import static yapion.utils.IdentifierUtils.TYPE_IDENTIFIER;

@SerializerImplementation(since = "0.23.0", initialSince = "0.3.0, 0.7.0, 0.12.0", standsFor = {Map.class, HashMap.class, IdentityHashMap.class, LinkedHashMap.class, TreeMap.class, WeakHashMap.class, ConcurrentHashMap.class, ConcurrentSkipListMap.class})
public class MapSerializer implements InternalSerializer<Map<?, ?>> {

    @Override
    public String type() {
        return "java.util.Map";
    }

    @Override
    public Class<?> interfaceType() {
        return Map.class;
    }

    @Override
    public YAPIONAnyType serialize(SerializeData<Map<?, ?>> serializeData) {
        YAPIONObject yapionObject = new YAPIONObject();
        yapionObject.add(TYPE_IDENTIFIER, serializeData.object.getClass().getTypeName());
        return SerializeUtils.serializeMap(serializeData, yapionObject);
    }

    @SuppressWarnings({"unchecked"})
    @Override
    public Map<?, ?> deserialize(DeserializeData<? extends YAPIONAnyType> deserializeData) {
        try {
            Object object = Class.forName(((YAPIONObject) deserializeData.object).getValue(TYPE_IDENTIFIER, String.class).get())
                    .getDeclaredConstructor().newInstance();
            YAPIONMap yapionMap = ((YAPIONObject) deserializeData.object).getMap("values");
            return DeserializeUtils.deserializeMap(deserializeData, yapionMap, (Map<Object, Object>) object);
        } catch (Exception e) {
            throw new YAPIONDeserializerException();
        }
    }
}
