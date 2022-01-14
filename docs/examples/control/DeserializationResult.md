# Deserializer Result

Currently, you use the static serialization and deserialization calls. But if you use the instance calls you can do even more stuff. This can even be used for the parser. For now I will talk about the DeserializerResult object you can retrieve from one deserialization.

```java
import yapion.annotations.object.YAPIONData;
import yapion.hierarchy.types.YAPIONObject;
import yapion.serializing.DeserializeResult;
import yapion.serializing.YAPIONDeserializer;
import yapion.serializing.YAPIONSerializer;

@YAPIONData
public class ExampleDeserializerResult {

    private int i = 0;
    private int j = 0;

    public static void main(String[] args) {
        YAPIONObject yapionObject = YAPIONSerializer.serialize(new ExampleDeserializerResult());
        yapionObject.remove("i");

        YAPIONDeserializer yapionDeserializer = new YAPIONDeserializer(yapionObject);

        // Return the object with 'getObject()' and deserialize with 'parse()' call
        Object object = yapionDeserializer.parse().getObject();

        // Return the deserialize result
        DeserializeResult deserializeResult = yapionDeserializer.getDeserializeResult();
    }

}
```

The 'deserializeResult' will not hold any value as it will only say if the 'yapionObject' holds any value that could not be deserialized back to the object.

```java
import yapion.annotations.object.YAPIONData;
import yapion.hierarchy.types.YAPIONObject;
import yapion.serializing.DeserializeResult;
import yapion.serializing.YAPIONDeserializer;
import yapion.serializing.YAPIONSerializer;

@YAPIONData
public class ExampleDeserializerResult {

    private int i = 0;
    private int j = 0;

    public static void main(String[] args) {
        YAPIONObject yapionObject = YAPIONSerializer.serialize(new ExampleDeserializerResult());
        yapionObject.add(k, 0);

        YAPIONDeserializer yapionDeserializer = new YAPIONDeserializer(yapionObject);

        // Return the object with 'getObject()' and deserialize with 'parse()' call
        Object object = yapionDeserializer.parse().getObject();

        // Return the deserialize result
        DeserializeResult deserializeResult = yapionDeserializer.getDeserializeResult();
    }

}
```

The 'deserializeResult' will now hold the value 'k' with the Integer '0' associated to it, as this field is not in the 'ExampleDeserializerResult' class.