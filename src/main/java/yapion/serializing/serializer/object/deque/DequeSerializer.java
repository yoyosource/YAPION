package yapion.serializing.serializer.object.deque;

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

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.LinkedBlockingDeque;

import static yapion.utils.IdentifierUtils.TYPE_IDENTIFIER;

@SerializerImplementation(since = "0.23.0", initialSince = "0.7.0, 0.12.0", standsFor = {Deque.class, ArrayDeque.class, BlockingDeque.class, LinkedBlockingDeque.class, ConcurrentLinkedDeque.class})
public class DequeSerializer implements InternalSerializer<Deque<?>> {

    @Override
    public String type() {
        return "java.utils.Deque";
    }

    @Override
    public Class<?> interfaceType() {
        return Deque.class;
    }

    @Override
    public YAPIONAnyType serialize(SerializeData<Deque<?>> serializeData) {
        YAPIONObject yapionObject = new YAPIONObject();
        yapionObject.add(TYPE_IDENTIFIER, serializeData.object.getClass().getTypeName());
        return SerializeUtils.serializeDeque(serializeData, yapionObject);
    }

    @SuppressWarnings({"unchecked"})
    @Override
    public Deque<?> deserialize(DeserializeData<? extends YAPIONAnyType> deserializeData) {
        try {
            Object object = Class.forName(((YAPIONObject) deserializeData.object).getValue(TYPE_IDENTIFIER, String.class).get())
                    .getDeclaredConstructor().newInstance();
            YAPIONArray yapionArray = ((YAPIONObject) deserializeData.object).getArray("values");
            return DeserializeUtils.deserializeDeque(deserializeData, yapionArray, (Deque<Object>) object);
        } catch (Exception e) {
            throw new YAPIONDeserializerException();
        }
    }
}
