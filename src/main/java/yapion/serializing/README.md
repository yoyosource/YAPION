# Serializing

There are some design choices, that I want to address in this Text.
As there are so many I will group them by Type.

## Enum
Enums are quite tricky to handle with. This is why I need to handle them specially throughout serializing and deserializing.
As every `enum` extends `java.lang.Enum` I need to abstract enum serialization from a specific type. Because of this there is
a special `InternalSerializer` just handling the enum serialization and deserialization. As I cannot say if a given TypeName is an
`enum` or not, I needed a way to identify a `YAPIONObject` as one.

The typical enum structure:
```
{@type(java.lang.Enum)@enum(...)value(...)ordinal(...)}
```

The YAPIONObject variable *@†ype* just defines the serializer to use. As for the actual type the enum should have there is the `@enum` variable.
This variable is the `Class.getTypeName()` value. The `value` variable will be set to the `Enum.name()` and the `ordinal` variable to `Enum.ordinal()`.
For deserialization the ordinal gets checked for quick deserialization. The name of this retrieved enum will be checked against the `value` variable and if
not equal I will loop through all enum values, retrieved by `Class.getEnumConstants()`.

In the `ContextManager` the enum will be handled specially to save enums by default. Because of this you do not need to explicitly put any save annotation at this field.
To exclude an enum field in serialization you need to explicitly exclude it with the annotation.

## ContextManager

## InternalSerializer

## Public API

## Not Serializable Stuff