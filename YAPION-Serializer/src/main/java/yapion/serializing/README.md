# Serializing

There are some specific design choices, that a special and maybe unintuitive. As this is the case I want to address all of them for the serializer and deserializer. As there are many special design choices I will group them by Type.

## Enum
Enums are quite tricky to handle with. This is why I need to handle them specially throughout serializing and deserializing.
As every `enum` extends `java.lang.Enum` I need to abstract enum serialization from a specific type. Because of this there is
a special `InternalSerializer` just handling the enum serialization and deserialization. As I cannot say if a given TypeName is an
`enum` or not, I needed a way to identify a `YAPIONObject` as one.

Because of this the typical enum structure is:
```
{@type(...)value(...)ordinal(...)}
```

The YAPIONObject variable `@†ype` is the `Class.getTypeName()` value of the `enum` to serialize. The `value` variable will be set to the `Enum.name()` and the `ordinal` variable
to `Enum.ordinal()`. For deserialization the ordinal gets checked for quick deserialization. The name of this retrieved enum will be checked against the `value` variable and if
not equal I will loop through all enum values, retrieved by `Class.getEnumConstants()`.

In the `ContextManager` the enum will be handled specially to save enums by default. Because of this you do not need to explicitly put any save annotation at this field.
To exclude an enum field in serialization you need to explicitly exclude it with the annotation.

## Public API
The Public API has some limitations that the internal API can do. The main thing is that you cannot return a YAPIONAnyType but need to return some kind of YAPIONDataType.
So valid return types are `YAPIONArray`, `YAPIONMap` and `YAPIONObject`. The Public API implementation will say what type you need to return. There are different API types
for different Java Datastructures. This is why you should use the right API implementation.

## Not Serializable Stuff
Some stuff is not serializable by YAPION. If the YAPIONSerializer should not even try to serialize this field you should mark it transient.
All these classes are not serializable because they either hold internal state or data not accessible, because the underlying VM is holding it, or some other limitation.

### These types are by default (not overrideable):
A **java.lang.Process** cannot be serialized because of the operating system. As there is no way to freeze an others programs state.
A **java.lang.ProcessBuilder** cannot be serialized because of the operating system. As there is no way to freeze an others programs state.
A **java.lang.Runnable** cannot be serialized because there is no way of retrieving the class code of the runnable to instantiate it in its previous state.
A **java.lang.Thread** cannot be serialized because there is no way of serializing the Runnable, as explained above.
A **java.lang.ThreadGroup** cannot be serialized because there is no way of serializing a Thread, as explained above.
A **java.lang.ThreadLocal** will not be serializable as it should not be needed, because Threads and ThreadGroups cannot be serialized.
A **java.net.Socket** cannot be serialized as there is no way of freezing a network connection. As both sides would need to freeze and safe the internal state.
A **java.net.SocketServer** cannot be serialized as there is no way of freezing an open Port.

### As well as every Class in the following packages (not overrideable):
The **yapion.annotations.** package cannot be serialized because there is no value in being able to serialize any annotation.
The **yapion.parser.** package cannot be serialized because there is no need to serialize the parser.
The **yapion.serializing.serializer.** package cannot be serialized because there are only InternalSerializer implementations present in that package.
The **yapion.utils.** package cannot be serialized because there are only utilities and other helpful stuff in there.

### As well as every Class in the following packages that are not specially handled by a Serializer (overrideable by Serializer):
- java.io.
- java.net.
- java.nio.
- java.security.
- java.text.
- java.time.
- java.util.

## TypeReMapper
The TypeReMapper should be used if you want to deserialize a serialized Java object in another project. This can also be used to make a previously serialized compatible with
you current structure. An example would be moving a class from one Package to another.