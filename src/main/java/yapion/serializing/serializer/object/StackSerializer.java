package yapion.serializing.serializer.object;

import yapion.hierarchy.YAPIONAny;
import yapion.hierarchy.YAPIONVariable;
import yapion.hierarchy.types.YAPIONArray;
import yapion.hierarchy.types.YAPIONObject;
import yapion.hierarchy.types.YAPIONValue;
import yapion.serializing.InternalSerializer;
import yapion.serializing.YAPIONDeserializer;
import yapion.serializing.YAPIONSerializer;

import java.util.Stack;

public class StackSerializer implements InternalSerializer<Stack> {

    @Override
    public String type() {
        return "java.util.Stack";
    }

    @Override
    public YAPIONAny serialize(Stack object, YAPIONSerializer yapionSerializer) {
        YAPIONObject yapionObject = new YAPIONObject();
        yapionObject.add(new YAPIONVariable("@type", new YAPIONValue<>(type())));
        YAPIONArray yapionArray = new YAPIONArray();
        yapionObject.add(new YAPIONVariable("values", yapionArray));
        for (int i = 0; i < object.size(); i++) {
            yapionArray.add(yapionSerializer.parse(object.get(i), yapionSerializer));
        }
        return yapionObject;
    }

    @Override
    public Stack deserialize(YAPIONAny yapionAny, YAPIONDeserializer yapionDeserializer) {
        YAPIONObject yapionObject = (YAPIONObject) yapionAny;
        YAPIONArray yapionArray = yapionObject.getArray("values");
        Stack<Object> stack = new Stack<>();
        for (int i = 0; i < yapionArray.length(); i++) {
            stack.push(yapionDeserializer.parse(yapionArray.get(i), yapionDeserializer));
        }
        return stack;
    }
}