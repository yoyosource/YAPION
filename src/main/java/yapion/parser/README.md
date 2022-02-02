# YAPION specifications
YAPION is split into smaller types, those are YAPIONObject, YAPIONArray, YAPIONMap, YAPIONValue and YAPIONPointer.
The root type is a YAPIONObject easy explicit or implicit. If implicit a synthetic one gets generated and returned after parsing, in which the elements are inserted.
Indentation is omitted and ignored while parsing.

## YAPIONObject
A YAPIONObject is a key-value-store that can only handle String keys and any type of value. The start and end is denoted by curly brackets.  

Empty YAPIONObject:
```
{}
```

YAPIONObject with one key value mapping:
```
{key{}}
```
This can also be written in prettified form like:
```
{
  key{}
}
```

Inside a YAPIONObject the key can also be omitted for one key-value-pair. It will then be represented as an empty string.
```
{test{}{}}
```
This can also be written in prettified form like:
```
{
  test{}
  {}
}
```


Just be aware that unbalanced brackets will result in an exception.
This will throw an exception:
```
}
```
And this:
```
{
```
And this:
```
key{}
}
```
Or this:
```
{
key{}
```

## YAPIONValue
A YAPIONValue holds a YAPION primitive type, those are String, Character, Boolean, Byte, Short, Integer, Long, BigInteger, Float, Double and BigDecimal, as well as the 'null' value.

### String
A String is denoted mainly denoted by '"' at the start and end but often those can be omitted.
You can also specify a String with "'" at the start and end but only with a length not equal to 1 in between.
The leading and trailing quotes can be omitted, if no type matches with the string other than a string.

### Character
A Character is denoted by "'" at the start and end with one character in between.

### Boolean
A Boolean is either 'true' oder 'false'.

### Byte
A Byte is a number either hex or normally suffixed by a 'B'. A hex number starts with 0x or 0X or # and can optionally be negative with a leading minus.

### Short
A Short is like a Byte just with 'S' as suffix.

### Integer
An Integer is like a Byte just with 'I' as suffix or no suffix when the number is inside the range of an Integer.

### Long
A Long is like a Byte just with 'L' as suffix or no suffix when the number is inside the range of a Long and outside the range of an Integer.

### BigInteger
A BigInteger is like a Byte just with 'BI' as suffix or no suffix when the number outside the range of an Long.

### Float
A Float is a number with optional '.' suffixed by 'F'. Leading and or trailing dots are allowed.

### Double
A Double is like a Float with optional '.' suffixed by 'D' or without a suffix when the number is inside the range of a Float.

### BigDecimal
A BigDecimal is like a Float with optional '.' suffixed by 'BD' or without a suffix when the number is outside the range of a Double.

### 'null'
The null value is denoted by 'null'.

## YAPIONArray
A YAPIONArray just holds any type in the order inputted. The start and end is denoted by square brackets.

Empty YAPIONArray:
```
[]
```
This is parsed to with implicit YAPIONObject surrounding it:
```
{[]}
```
This can also be written in prettified form like:
```
{
  []
}
```

YAPIONObject with YAPIONArray inside:
```
{array[]}
```
This can also be written in prettified form like:
```
{
  array[]
}
```

You can add elements to the array separated by a comma, if multiple are used only one gets counted.
```
[{},{}]
```
This is parsed to with implicit YAPIONObject surrounding it:
```
{[{},{}]}
```
This can also be written in prettified form like:
```
{
  [
    {},
    {}
  ]
}
```

YAPIONValues are not needed to be surrounded by '()' but ended by ','. If prettified you need a trailing ',' if the last element was a YAPIONValue, otherwise this can be omitted.
```
[Hello, "true", 0]
```
This is parsed to with implicit YAPIONObject surrounding it:
```
{[Hello, "true", 0]}
```
This can also be written in prettified form like:
```
{
  [
    Hello,
    "true",
    0,
  ]
}
```

Any separating ',' can be omitted while between YAPIONObject, YAPIONArray and YAPIONMap.
```
[{}{}{}]
```
This is parsed to with implicit YAPIONObject surrounding it:
```
{[{}{}{}]}
```
This can also be written in prettified form like:
```
{
  [
    {},
    {},
    {}
  ]
}
```

## YAPIONMap
A YAPIONMap is a key-value-store that can any type of key and any type of value. The start and end is denoted by pointy ('<>') brackets.

Empty YAPIONMap:
```
<>
```
This is parsed to with implicit YAPIONObject surrounding it:
```
{<>}
```
This can also be written in prettified form like:
```
{
  <>
}
```

Keys and values are separated by a colon.
```
<key:value>
```

A Key can be any YAPION type.
```
<(key):(value)>
```
In this example both the key and value are YAPIONValues.
This is parsed to with implicit YAPIONObject surrounding it:
```
{<(key):(value)>}
```
This can also be written in prettified form like:
```
{
  <
    (key):(value)
  >
}
```

## YAPIONPointer
A YAPIONPointer is a reference to a YAPION type and starts with '->' and is followed by a 16 digit hex number without any prefix.

Example:
```
->1234567890ABCDEF
```

## Comments
Comments are started with '/\*' and end with '\*/'. They can be on their own line or on the same line as the YAPION type. They can be in between the key from a YAPIONObject or at the start of any Object or end.

Empty Comment:
```
/**/
```
This is parsed to with implicit YAPIONObject surrounding it:
```
{/**/}
```
This can also be written in prettified form like:
```
{
  /**/
}
```

You can write anything inside a comment as long as it is not '*/'. No special unicode parsing or escaping is done.
```
/*
  Hello World
  Test
*/
```
This is parsed to with implicit YAPIONObject surrounding it:
```
{/*
  Hello World
  Test
*/}
```
This can also be written in prettified form like:
```
{
  /*
    Hello World
    Test
  */
}
```
