# Cascading [WIP]

The cascading feature is useful for integrating YAPION in an existing project. You can use this if you want to serialize a deep and complex Object tree without adding YAPION annotations to every object. You just need to add the YAPIONData annotation to the top object.

```java
import yapion.hierarchy.types.YAPIONObject;
import yapion.serializing.YAPIONSerializer;
import yapion.hierarchy.output.StringPrettifiedOutput;

import yapion.annotations.object.YAPIONData;

public class ExampleCascading {

    public static void main(String[] args){
        YAPIONObject yapionObject = YAPIONSerializer.serialize(new ExampleObjectOne());
        String stringOutput = yapionObject.toYAPION(new StringPrettifiedOutput()).getResult();
        System.out.println(stringOutput);
    }

}

@YAPIONData(cascading = true)
static class ExampleObjectOne {

    private int i = 0;
    private ExampleObjectTwo exampleObjectTwo = new ExampleObjectTwo();

}

static class ExampleObjectTwo {

    private int j = 0;

}
```

The result of the `System.out.println(stringOutput)` would be
```
{
    @type([...].ExampleObjectOne)
    i(0)
    exampleObjectTwo{
        @type([...].ExampleObjectTwo)
        j(0)
    }
}
```

As you can see this helps you if you have nested objects and want to (de)serialize this. If you want to serialize `ExampleObjectTwo` though you need to add YAPION annotations to the object. Otherwise, the YAPIONSerializer would throw an Exception.