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

Because of the implicit YAPIONObject surrounding everything you can also just start writing your key-value-pairs.
```
test{}
hello{}
```
This is parsed to with implicit YAPIONObject surrounding it:
```
{
  test{}
  hello{}
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
    {}
    {}
    {}
  ]
}
```
And with commas:
```
{
  [
    {},
    {},
    {}
  ]
}
```