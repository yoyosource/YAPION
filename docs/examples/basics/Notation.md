# Notation [WIP]
The YAPION notation is heavily inspired by JSON and the enums in Java.

### Types
* Object
* Array
* Map
* Pointer
* Value

## Object
An Object in YAPION starts and ends with the same character as in JSON so '{' and '}' respectively.
An Object holds Key-Value-Pairs with a String key and any type as value. Key-Value-Pairs are not seperated
by a colon. The Key and Value part is also not seperated by any special character. This results in a very
compact notation.

### Example
```
{a{}b{a{}}c{}}
```

## Array
An Array in YAPION starts and ends with the same character as in JSON so '\[' and ']' respectively.
An Array holds values with integers as keys. These values are seperated by colons and values do not need
any brackets surrounding it.

### Example
```
[{},{a{}b{}c[]}]
```

## Map
A Map in YAPION starts and ends with '<' '>' respectively.