# Parser [WIP]

The parser is used to parse a String to a YAPIONObject. You can either use static calls or use an instance.
As an example parsing this String would be:
```
{hello(World)}
```

Parsing code:
```java
import yapion.hierarchy.types.YAPIONObject;
import yapion.parser.YAPIONParser;

public class Example {

    public static void main(String[] args) {
        // Static call
        YAPIONObject yapionObject = YAPIONParser.parse("{hello(World)}");

        // Dynamic instance
        YAPIONParser yapionParser = new YAPIONParser("{hello(World)}");
        yapionParser.parse();
        yapionObject = yapionParser.result();
    }

}
```

The internal hierarchy of the resulting YAPIONObject will look as follows:
```
root YAPIONObject
`- 'hello' Variable
   `- 'World' Value.String
```

If you try to parse an YAPIONArray directly, the parser will throw you an exception. But you can still parse it if you just surround the input with Object start and end characters.

Array:
```
[0,1,"Hello World"]
```

Array wrapped in Object:
```
{[0,1,"Hello World"]}
```

Parsing code:
```java
import yapion.hierarchy.types.YAPIONObject;
import yapion.parser.YAPIONParser;

public class Example {

    public static void main(String[] args) {
        try {
            // This will throw you an exception
            YAPIONObject yapionObject = YAPIONParser.parse("[0,1,\"Hello World\"]");
        } catch (YAPIONException e) {
            e.pringStackTrace();
        }

        // This works because there is an object as root object
        YAPIONObject yapionObject = YAPIONParser.parse("{[0,1,\"Hello World\"]}");
    }

}
```

The internal hierarchy of the resulting YAPIONObject will look as follows:
```
root YAPIONObject
`- '' Variable
   `- array YAPIONArray
      `- 0 Value.Int
      `- 1 Value.Int
      `- 'Hello World' Value.String
```

To find out more about the [Parser](https://github.com/yoyosource/YAPION/tree/master/src/main/java/yapion/hierarchy) look here.