# Deserialization

The deserializer will deserialize every field found in the YAPIONObject and set it back into an instance of an object. This examples will use some code of the [Serializer](Serialization.md).
We will start with our class:
```java
public class Example {
    
    // This will not be saved or loaded as it is 'static'
    private static int i = 0;
    
    // This will not be saved or loaded as it is 'transient'
    private transient long l = 0;
    
    private int integer = 0;

    private String string = "Hello World";

}
```
Now we add our basic save annotations:
```java
import yapion.annotations.serialize.YAPIONSave;

@YAPIONSave
public class Example {
    
    // This will not be saved or loaded as it is 'static'
    private static int i = 0;
    
    // This will not be saved or loaded as it is 'transient'
    private transient long l = 0;
    
    @YAPIONSave
    private int integer = 0;

    @YAPIONSave
    private String string = "Hello World";

}
```
Now we need to hint the deserializer what to load:
```java
import yapion.annotations.serialize.YAPIONSave;
import yapion.annotations.deserialize.YAPIONLoad;

@YAPIONSave
@YAPIONLoad
public class Example {
    
    // This will not be saved or loaded as it is 'static'
    private static int i = 0;
    
    // This will not be saved or loaded as it is 'transient'
    private transient long l = 0;
    
    @YAPIONSave
    @YAPIONLoad
    private int integer = 0;

    @YAPIONSave
    @YAPIONLoad
    private String string = "Hello World";

}
```

The save and load annotation can be combined to the data annotation. But this comes with some [limiations](https://github.com/yoyosource/YAPION/blob/master/src/main/java/yapion/annotations/object/YAPIONData.java). If you use the load and save annotations you need a no args constructor for the object. This is because YAPION needs a way to create an instance of such objects. This has the result that every field has the default value defined in the class. When you use the YAPIONData annotation you do not need to create a no args constructor for the object but if some values are not loaded because they are declared transient or static they will have the java default value. So numbers 0, booleans false and every object null. For this example we will use the YAPIONData annotation as follows:
```java
import yapion.annotations.object.YAPIONData;

@YAPIONData
public class Example {
    
    // This will not be saved as it is 'static'
    private static int i = 0;
    
    // This will not be saved as it is 'transient'
    private transient long l = 0;
    
    private int integer = 0;

    private String string = "Hello World";

}
```

Now we will use the same Serialization code from the [Serialization](Serialization.md) example and expand it to handle deserialization as well.

```java
import yapion.hierarchy.types.YAPIONObject;
import yapion.serializing.YAPIONSerializer;
import yapion.serializing.YAPIONDeserializer;

public class ExampleDeserialization {

    public static void main(String[] args) {
        YAPIONObject yapionObject = YAPIONSerializer.serialize(new Example());

        // Now we need to deserialize back to an Example instance
        // The deserializer will an generic Object and you need to cast it back to type you expect
        Oject object = YAPIONDeserializer.deserialize(yapionObject);

        // This is to cast the object:
        Example example = (Example) object;
    }

}
```

This also works when you 'extend' one object from another.
```java
import yapion.annotations.object.YAPIONData;

@YAPIONData
public class ExampleTwo extends Example {

    private String text = "ExampleTwo";

}
```

Deserializing will look something like this:

```java
import yapion.hierarchy.types.YAPIONObject;
import yapion.serializing.YAPIONSerializer;

public class ExampleSerializationTwo {

    public static void main(String[] args) {
        YAPIONObject yapionObject = YAPIONSerializer.serialize(new ExampleTwo());
    
        // Now we need to deserialize back to an ExampleTwo instance
        // The deserializer will an generic Object and you need to cast it back to type you expect
        Oject object = YAPIONDeserializer.deserialize(yapionObject);
      
        // This is to cast the object:
        ExampleTwo exampleTwo = (ExampleTwo) object;
    }

}
```