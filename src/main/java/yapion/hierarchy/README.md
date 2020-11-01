# YAPION's Design
YAPION is designed to serialize arbitrary Java Objects into a readable Text. This is achieved by having a similar data structure
throughout YAPION that resembles the Java Objects. Because of this there are 3 mayor Datastructures and 2 Dataholders those are `YAPIONArray`, `YAPIONMap` and `YAPIONObject` as well as `YAPIONPointer` and `YAPIONValue`.
The `YAPIONVariable` class is tying all those classes together.

## Speciality
A serialized Java Object holds META-Data that is used to deserialize it. For example the variable names of the respective Java Field as well as the
Java Type, retrieved by `Class.getTypeName()`. All this Data will be used to deserialize the serialized object. Between the Java Object and the Serialized String there is an
intermediate structure, which I will call YAPION-Representation with the Datastructures and Dataholders described above.
This intermediate will be used to create an easy structure to write out the Java Object, as well as a Data exchange format. This format specialises on handling any Java Object
encountered and has many similarities to JSON.

In a `YAPIONObject` you can also have one `YAPIONVariable` with no name. You can use this to parse something that is not a `YAPIONObject`.
To use this you can surround, what you want to parse, with `{}` and just call `YAPIONObject.getVariable("")` on the parsed String to retrieve this variable.

## YAPION as a Data exchange format
YAPION in itself has some limitations that are set by the parser and the deserializer. One of them is that the main parent object needs to be a `YAPIONObject`, as YAPION is mainly for Java everything is an object.
