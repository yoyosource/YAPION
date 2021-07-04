# Output

To output an YAPIONObject you can do much more that just printing it into the console with 'System.out.println()'. You can also use the Output API found [here](https://github.com/yoyosource/YAPION/tree/master/src/main/java/yapion/hierarchy/output).
The example uses the same stuff used in [Serialization](Serialization.md), especially the 'Example' class and the 'ExampleSerializationTwo' main method.

```java
import yapion.hierarchy.types.YAPIONObject;
import yapion.serializing.YAPIONSerializer;

import yapion.hierarchy.output.StringOutput;
import yapion.hierarchy.output.FileOutput;
import yapion.hierarchy.output.FileGZIPOutput;
import yapion.hierarchy.output.LengthOutput;
import yapion.hierarchy.output.StreamOutput;
import yapion.hierarchy.output.StringBuilderOutput;

public class ExampleSerializationTwo {

    public static void main(String[] args) {
        YAPIONObject yapionObject = YAPIONSerializer.serialize(new ExampleTwo());

        // This will result in the same string as '{@type(ExampleTwo)text(Hello World)integer(0)string(Hello World)}'
        String stringOutput = yapionObject.toYAPION(new StringOutput()).getResult();

        // This will result in a prettified string:
        String stringPrettifiedOutput = yapionObject.toYAPION(new StringOutput(true)).getResult();

        try {
            // '<FILE>' should be replaced by your file. The result will be written into the file directly
            yapionObject.toYAPION(new FileOutput(new File("<FILE>"))).close();

            // '<FILE>' should be replaced by your file. The result will be written into the file directly and will be prettified
            yapionObject.toYAPION(new FileOutput(new File("<FILE>"), true)).close();

            // '<FILE>' should be replaced by your file. The result will be written into the file directly and will be prettified
            yapionObject.toYAPION(new FileGZIPOutput(new File("<FILE>"))).close();
        } catch (IOException e) {
            // Ignored
        }

        // '/*<STREAM>*/' should be replaces by any OutputStream. The result will be written into the stream directly
        yapionObject.toYAPION(new StreamOutput(/*<STREAM>*/)).close();

        // '/*<STREAM>*/' should be replaces by any OutputStream. The result will be written into the stream directly and will be prettified
        yapionObject.toYAPION(new StreamOutput(/*<STREAM>*/, true)).close();

        // '/*<STRING_BUILDER>*/' should be replaces by either a new StringBuilder or a StringBuilder to reuse
        yapionObject.toYAPION(new StringBuilderOutput(/*<STRING_BUILDER>*/)).close();

        // '/*<STRING_BUILDER>*/' should be replaces by either a new StringBuilder or a StringBuilder to reuse. The output will be prettified.
        yapionObject.toYAPION(new StringBuilderOutput(/*<STRING_BUILDER>*/, true)).close();

        // Get the length of the underlying yapionObject
        yapionObject.toYAPION(new LengthOutput()).getLength();
    }
}
```

There are some utility methods directly defined on every YAPIONAnyType. For example:
```java
public class ExampleSerializationTwo {

    public static void main(String[] args) {
        YAPIONObject yapionObject = YAPIONSerializer.serialize(new ExampleTwo());
        
        // This directly turns an YAPIONAnyType to a String
        yapionObject.toYAPION();
        
        // To output the YAPIONAnyType to a file
        yapionObject.toFile(new File("<FILE>"));

        // To output the YAPIONAnyType to a file and prettify it
        yapionObject.toFile(new File("<FILE>", true));

        // Output the YAPIONAnyType to a file and compress it
        yapionObject.toGZIPFile(new File("<FILE>"));
    }
}
```