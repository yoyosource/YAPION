# Output

To output an YAPIONObject you can do much more that just printing it into the console with 'System.out.println()'. You can also use the Output API found [here](https://github.com/yoyosource/YAPION/tree/master/src/main/java/yapion/hierarchy/output).
The example uses the same stuff used in [Serialization](Serialization.md), especially the 'Example' class and the 'ExampleSerializationTwo' main method.

```java
import yapion.hierarchy.types.YAPIONObject;
import yapion.serializing.YAPIONSerializer;

import yapion.hierarchy.output.StringOutput;
import yapion.hierarchy.output.StringPrettifiedOutput;
import yapion.hierarchy.output.FileOutput;
import yapion.hierarchy.output.FilePrettifiedOutput;
import yapion.hierarchy.output.FileGZIPOutput;
import yapion.hierarchy.output.LengthOutput;
import yapion.hierarchy.output.StreamOutput;
import yapion.hierarchy.output.StringPrettifiedOutput;
import yapion.hierarchy.output.StringBuilderOutput;
import yapion.hierarchy.output.StringBuilderPrettifiedOutput;

public class ExampleSerializationTwo {

    public static void main(String[] args) {
        YAPIONObject yapionObject = YAPIONSerializer.serialize(new ExampleTwo());

        // This will result in the same string as '{@type(ExampleTwo)text(Hello World)integer(0)string(Hello World)}'
        String stringOutput = yapionObject.toYAPION(new StringOutput()).getResult();

        // This will result in a prettified string:
        String stringPrettifiedOutput = yapionObject.toYAPION(new StringPrettifiedOutput()).getResult();

        try {
            // '<FILE>' should be replaced by your file. The result will be written into the file directly
            yapionObject.toYAPION(new FileOutput(new File("<FILE>"))).close();

            // '<FILE>' should be replaced by your file. The result will be written into the file directly and will be prettified
            yapionObject.toYAPION(new FilePrettifiedOutput(new File("<FILE>"))).close();

            // '<FILE>' should be replaced by your file. The result will be written into the file directly and will be prettified
            yapionObject.toYAPION(new FileGZIPOutput(new File("<FILE>"))).close();
        } catch (IOException e) {
            // Ignored
        }

        // '/*<STREAM>*/' should be replaces by any OutputStream. The result will be written into the stream directly
        yapionObject.toYAPION(new StreamOutput(/*<STREAM>*/)).close();

        // '/*<STREAM>*/' should be replaces by any OutputStream. The result will be written into the stream directly and will be prettified
        yapionObject.toYAPION(new StringPrettifiedOutput(/*<STREAM>*/)).close();

        // '/*<STRING_BUILDER>*/' should be replaces by either a new StringBuilder or a StringBuilder to reuse
        yapionObject.toYAPION(new StringBuilderOutput(/*<STRING_BUILDER>*/)).close();

        // '/*<STRING_BUILDER>*/' should be replaces by either a new StringBuilder or a StringBuilder to reuse. The output will be prettified.
        yapionObject.toYAPION(new StringBuilderPrettifiedOutput(/*<STRING_BUILDER>*/)).close();

        // Get the length of the underlying yapionObject
        yapionObject.toYAPION(new LengthOutput()).getLength();
    }

}
```

## Version 0.25.0
As of Version `0.25.0` the OutputAPI was reworked and every `Prettified` implementation deprecated. You can now simply add a boolean to the constructor of the non `Prettified` implementation to prettify it.
You can also use custom indentation, while prettifying, with the `Indentator` class. Use the `setIndentator` method on an `AbstractOutput` to set a custom Indentator.