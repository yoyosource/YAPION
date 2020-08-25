package yapion.serializing.serializer.object;

import yapion.hierarchy.YAPIONAny;
import yapion.hierarchy.YAPIONVariable;
import yapion.hierarchy.types.YAPIONArray;
import yapion.hierarchy.types.YAPIONObject;
import yapion.hierarchy.types.YAPIONValue;
import yapion.serializing.InternalSerializer;
import yapion.serializing.SerializeManager;
import yapion.serializing.YAPIONDeserializer;
import yapion.serializing.YAPIONSerializer;

import java.util.Vector;

public class VectorSerializer implements InternalSerializer<Vector> {

    @Override
    public String type() {
        return "java.util.Vector";
    }

    @Override
    public YAPIONAny serialize(Vector object, YAPIONSerializer yapionSerializer) {
        YAPIONObject yapionObject = new YAPIONObject();
        yapionObject.add(new YAPIONVariable(SerializeManager.typeName, new YAPIONValue<>(type())));
        YAPIONArray yapionArray = new YAPIONArray();
        yapionObject.add(new YAPIONVariable("values", yapionArray));
        for (int i = 0; i < object.size(); i++) {
            yapionArray.add(yapionSerializer.parse(object.get(i), yapionSerializer));
        }
        return yapionObject;
    }

    @Override
    public Vector deserialize(YAPIONAny yapionAny, YAPIONDeserializer yapionDeserializer) {
        YAPIONObject yapionObject = (YAPIONObject) yapionAny;
        YAPIONArray yapionArray = yapionObject.getArray("values");
        Vector<Object> vector = new Vector<>();
        for (int i = 0; i < yapionArray.length(); i++) {
            vector.add(yapionDeserializer.parse(yapionArray.get(i), yapionDeserializer));
        }
        return vector;
    }
}
