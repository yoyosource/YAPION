package yapion.serializing.serializer.object.list;

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

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static yapion.utils.IdentifierUtils.TYPE_IDENTIFIER;

@SerializerImplementation(since = "0.23.0", initialSince = "0.3.0, 0.12.0", standsFor = {List.class, ArrayList.class, LinkedList.class, CopyOnWriteArrayList.class})
public class ListSerializer implements InternalSerializer<List<?>> {

    @Override
    public String type() {
        return "java.util.List";
    }

    @Override
    public Class<?> interfaceType() {
        return List.class;
    }

    @Override
    public YAPIONAnyType serialize(SerializeData<List<?>> serializeData) {
        YAPIONObject yapionObject = new YAPIONObject();
        yapionObject.add(TYPE_IDENTIFIER, serializeData.object.getClass().getTypeName());
        return SerializeUtils.serializeList(serializeData, yapionObject);
    }

    @SuppressWarnings({"unchecked"})
    @Override
    public List<?> deserialize(DeserializeData<? extends YAPIONAnyType> deserializeData) {
        try {
            Object object = Class.forName(((YAPIONObject) deserializeData.object).getValue(TYPE_IDENTIFIER, String.class).get())
                    .getDeclaredConstructor().newInstance();
            YAPIONArray yapionArray = ((YAPIONObject) deserializeData.object).getArray("values");
            return DeserializeUtils.deserializeList(deserializeData, yapionArray, (List<Object>) object);
        } catch (Exception e) {
            throw new YAPIONDeserializerException();
        }
    }
}
