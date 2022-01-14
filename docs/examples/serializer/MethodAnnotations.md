# Hooks into Serialization/Deserialization

You can tamper with your object right before serialization or deserialization. This is useful to manipulate any data before serialization or initialising your object before deserialization or even correcting data after deserialization. You define those methods in your class and annotate them with specific annotations.

```java
import yapion.annotations.object.YAPIONPostDeserialization;
import yapion.annotations.object.YAPIONPostSerialization;
import yapion.annotations.object.YAPIONPreDeserialization;
import yapion.annotations.object.YAPIONPreSerialization;

public class ExampleMethodAnnotation {

    @YAPIONPreSerialization
    private void preSerialization() {

    }

    @YAPIONPostSerialization
    private void postSerialization() {

    }

    @YAPIONPreDeserialization
    private void preDeserialization() {

    }

    @YAPIONPostDeserialization
    private void postDeserialization() {

    }

}
```

Those methods can either be without arguments or take a `DeserializeData` or `SerializationData`, the visibility is not relevant, nor the return value. If a class extends another class for 'pre' steps the super 'pre' step will be called before yours is called. 'post' steps are called the other way around.