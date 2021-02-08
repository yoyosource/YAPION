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
by a comma. The Key and Value part is also not seperated by any special character. This results in a very
compact notation.

### Example
```
{a{}b{a{}}c{}}
```

## Array
An Array in YAPION starts and ends with the same character as in JSON so '\[' and ']' respectively.
An Array holds values with integers as keys. These values are seperated by commas and values do not need
any brackets surrounding it.

### Example
```
[{},{a{}b{}c[]}]
```

## Map
A Map in YAPION starts and ends with '<' and '>' respectively. This type can hold Key-Value-Pairs with any key to any value relationships.
The values are stored like in an Object, but the mapping structure needs to be stored as well. This is done by the colon separator.

### Example
```
<0:1#0{}#1{}>
```

## Pointer
A Pointer points to an Object as a reference. This is used to deserialize an Object with all its referenced to other Objects in the hierarchy.

### Example
```
a->0000000000000000
```

## Value
A Value is a "primitive" Object just storing one type of data. This can be:
- boolean
- byte
- short
- char
- int
- float
- long
- double
- BigInteger
- BigDecimal
- String
- 'null'

The value is surrounded by '(' and ')' in any Key-Value-Pair, except in Arrays. If a String is not ambigious you can remove the single or double quotes. If you want to have a String of length 1 that is ambigious you need to use double quotes. 

### Example
```
(0)
```