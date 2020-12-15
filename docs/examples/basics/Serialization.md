# Serialization

The serializer will serialize every field of the current instance as well as every superclass.
As an example class we will use this:
```java
public class Example {
    
    // This will not be saves as it is 'static'
    private static int i = 0;
    
    // This will not be saves as it is 'transient'
    private transient long l = 0;
    
    private int integer = 0;

    private String string = "Hello World";

}
```

To give the serializer hints what to serialize and what not to serialize there are some Annotations that are useful.
Here the annotated example:
```java
import yapion.annotations.serialize.YAPIONSave;

@YAPIONSave
public class Example {
    
    // This will not be saves as it is 'static'
    private static int i = 0;
    
    // This will not be saves as it is 'transient'
    private transient long l = 0;
    
    @YAPIONSave
    private int integer = 0;

    @YAPIONSave
    private String string = "Hello World";

}
```

[YAPIONSave](https://github.com/yoyosource/YAPION/blob/master/src/main/java/yapion/annotations/serialize/YAPIONSave.java) defines that this should be serialized, If you just want to save everything from this class there is a shortcut. This shortcut imposes some [limitations](https://github.com/yoyosource/YAPION/blob/master/src/main/java/yapion/annotations/object/YAPIONData.java).
```java
import yapion.annotations.object.YAPIONData;

@YAPIONData
public class Example {
    
    // This will not be saves as it is 'static'
    private static int i = 0;
    
    // This will not be saves as it is 'transient'
    private transient long l = 0;
    
    private int integer = 0;

    private String string = "Hello World";

}
```

The serialization code will look something like this:

```java
import yapion.hierarchy.types.YAPIONObject;
import yapion.serializing.YAPIONSerializer;

public class ExampleSerialization {

    public static void main(String[] args) {
        YAPIONObject yapionObject = YAPIONSerializer.serialize(new Example());
    }

}
```

The resulting hierarchy of the YAPIONObject will look something like this:

```
root YAPIONObject
`- '@type' Variable
|  `- 'Example' Value.String
`- 'integer' Variable
|  `- '0' Value.Int
`- 'string' Variable
   `- 'Hello World' Value.String
```

If you 'extend' some class the fields of the superclass will also be serialized:

```java
import yapion.annotations.object.YAPIONData;

@YAPIONData
public class ExampleTwo extends Example {

    private String text = "ExampleTwo";

}
```

The serialization code will look something like this:

```java
import yapion.hierarchy.types.YAPIONObject;
import yapion.serializing.YAPIONSerializer;

public class ExampleSerializationTwo {

    public static void main(String[] args) {
        YAPIONObject yapionObject = YAPIONSerializer.serialize(new ExampleTwo());
    }

}
```

The resulting hierarchy of the YAPIONObject will look something like this:

```
root YAPIONObject
`- '@type' Variable
|  `- 'ExampleTwo' Value.String
`- 'integer' Variable
|  `- '0' Value.Int
`- 'string' Variable
|  `- 'Hello World' Value.String
`- 'text' Variable
   `- 'ExampleTwo' Value.String
```

If we expand example two, to also print the YAPIONObject in String format you will see:

```java
import yapion.hierarchy.types.YAPIONObject;
import yapion.serializing.YAPIONSerializer;

public class ExampleSerializationTwo {

    public static void main(String[] args) {
        YAPIONObject yapionObject = YAPIONSerializer.serialize(new ExampleTwo());

        // This will result in: '{@type(ExampleTwo)text(Hello World)integer(0)string(Hello World)}'
        System.out.println(yapionObject);
    }

}
```