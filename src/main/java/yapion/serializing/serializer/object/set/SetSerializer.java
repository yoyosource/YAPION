package yapion.serializing.serializer.object.set;

import yapion.exceptions.serializing.YAPIONDeserializerException;
import yapion.hierarchy.api.groups.YAPIONAnyType;
import yapion.hierarchy.types.YAPIONArray;
import yapion.hierarchy.types.YAPIONObject;
import yapion.serializing.InternalSerializer;
import yapion.serializing.data.DeserializeData;
import yapion.serializing.data.SerializeData;
import yapion.serializing.serializer.SerializerImplementation;
import yapion.serializing.utils.DeserializeUtils;
import yapion.serializing.utils.SerializeUtils;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.CopyOnWriteArraySet;

import static yapion.utils.IdentifierUtils.TYPE_IDENTIFIER;

@SerializerImplementation(since = "0.23.0", initialSince = "0.3.0, 0.7.0, 0.12.0", standsFor = {Set.class, HashSet.class, LinkedHashSet.class, TreeSet.class, ConcurrentSkipListSet.class, CopyOnWriteArraySet.class})
public class SetSerializer implements InternalSerializer<Set<?>> {

    @Override
    public String type() {
        return "java.util.Set";
    }

    @Override
    public Class<?> interfaceType() {
        return Set.class;
    }

    @Override
    public YAPIONAnyType serialize(SerializeData<Set<?>> serializeData) {
        YAPIONObject yapionObject = new YAPIONObject();
        yapionObject.add(TYPE_IDENTIFIER, serializeData.object.getClass().getTypeName());
        return SerializeUtils.serializeSet(serializeData, yapionObject);
    }

    @SuppressWarnings({"unchecked"})
    @Override
    public Set<?> deserialize(DeserializeData<? extends YAPIONAnyType> deserializeData) {
        try {
            Object object = Class.forName(((YAPIONObject) deserializeData.object).getValue(TYPE_IDENTIFIER, String.class).get())
                    .getDeclaredConstructor().newInstance();
            YAPIONArray yapionArray = ((YAPIONObject) deserializeData.object).getArray("values");
            return DeserializeUtils.deserializeSet(deserializeData, yapionArray, (Set<Object>) object);
        } catch (Exception e) {
            throw new YAPIONDeserializerException(e.getMessage(), e);
        }
    }

}
