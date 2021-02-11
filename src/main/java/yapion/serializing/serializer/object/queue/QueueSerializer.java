package yapion.serializing.serializer.object.queue;

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

import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.*;

import static yapion.utils.IdentifierUtils.TYPE_IDENTIFIER;

@SerializerImplementation(since = "0.23.0", initialSince = "0.7.0, 0.12.0", standsFor = {Queue.class, PriorityQueue.class, ConcurrentLinkedQueue.class, LinkedBlockingQueue.class, LinkedTransferQueue.class, SynchronousQueue.class})
public class QueueSerializer implements InternalSerializer<Queue<?>> {

    @Override
    public String type() {
        return "java.util.Queue";
    }

    @Override
    public Class<?> interfaceType() {
        return Queue.class;
    }

    @Override
    public YAPIONAnyType serialize(SerializeData<Queue<?>> serializeData) {
        YAPIONObject yapionObject = new YAPIONObject();
        yapionObject.add(TYPE_IDENTIFIER, serializeData.object.getClass().getTypeName());
        return SerializeUtils.serializeQueue(serializeData, yapionObject);
    }

    @SuppressWarnings({"unchecked"})
    @Override
    public Queue<?> deserialize(DeserializeData<? extends YAPIONAnyType> deserializeData) {
        try {
            Object object = Class.forName(((YAPIONObject) deserializeData.object).getValue(TYPE_IDENTIFIER, String.class).get())
                    .getDeclaredConstructor().newInstance();
            YAPIONArray yapionArray = ((YAPIONObject) deserializeData.object).getArray("values");
            return DeserializeUtils.deserializeQueue(deserializeData, yapionArray, (Queue<Object>) object);
        } catch (Exception e) {
            throw new YAPIONDeserializerException();
        }
    }
}
