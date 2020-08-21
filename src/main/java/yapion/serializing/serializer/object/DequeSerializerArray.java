package yapion.serializing.serializer.object;

import yapion.hierarchy.YAPIONAny;
import yapion.hierarchy.YAPIONVariable;
import yapion.hierarchy.types.YAPIONArray;
import yapion.hierarchy.types.YAPIONObject;
import yapion.hierarchy.types.YAPIONValue;
import yapion.serializing.InternalSerializer;
import yapion.serializing.YAPIONDeserializer;
import yapion.serializing.YAPIONSerializer;

import java.util.ArrayDeque;
import java.util.Iterator;

public class DequeSerializerArray implements InternalSerializer<ArrayDeque> {

    @Override
    public String type() {
        return "java.util.ArrayDeque";
    }

    @Override
    public YAPIONAny serialize(ArrayDeque object, YAPIONSerializer yapionSerializer) {
        YAPIONObject yapionObject = new YAPIONObject();
        yapionObject.add(new YAPIONVariable("@type", new YAPIONValue<>(type())));
        YAPIONArray yapionArray = new YAPIONArray();
        yapionObject.add(new YAPIONVariable("values", yapionArray));
        Iterator<Object> iterator = object.iterator();
        while (iterator.hasNext()) {
            yapionArray.add(yapionSerializer.parse(iterator.next(), yapionSerializer));
        }
        return yapionObject;
    }

    @Override
    public ArrayDeque deserialize(YAPIONAny yapionAny, YAPIONDeserializer yapionDeserializer) {
        YAPIONObject yapionObject = (YAPIONObject) yapionAny;
        YAPIONArray yapionArray = yapionObject.getArray("values");
        ArrayDeque<Object> deque = new ArrayDeque<>();
        for (int i = 0; i < yapionArray.length(); i++) {
            deque.add(yapionDeserializer.parse(yapionArray.get(i), yapionDeserializer));
        }
        return deque;
    }
}
