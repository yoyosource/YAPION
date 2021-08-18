# Blank
```I
{   a()\   b()()}
```

```O
{a()\   b()()}
```

# Characters
```I
{\ !"#$%&'\(\)*+,-./0123456789:;\<=\>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ\[\\\]^_`abcdefghijklmnopqrstuvwxyz\{|\}~( !"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~)}
```

```O
{\ !"#$%&'\(\)*+,-./0123456789:;\<=\>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ\[\\\]^_`abcdefghijklmnopqrstuvwxyz\{|\}~( !"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~)}
```

# Array with Pointer
```I
{[a->b,b->c]}
```

```O
{[a->b,b->c]}
```

# Array with any Type
```I
{[{},[],(),->0000000000000000,<>]}
```

```O
{[{},[],(),->0000000000000000,<>]}
```

# Start-End Object
```I
hello()
```

```O
{hello()}
```

# Start-End Object 2
```I
hello{}
```

```O
{hello{}}
```

# Start-End Object Error
```I
hello{}}
```

```E
yapion.exceptions.parser.YAPIONParserException
```

# Array in Value Separator
```I
{[Hello\, World]}
```

```O
{[Hello\, World]}
```

# Array removed Separator
```I
{[{}{}]}
```

```O
{[{},{}]}
```

# Array removed Separator 2
```I
{[{}{}HelloWorld,->0000000000000000]}
```

```O
{[{},{},HelloWorld,->0000000000000000]}
```

# Array Value Pointer start char
```I
{[\->0000000000000000]}
```

```O
{[\->0000000000000000]}
```

# Special Strings in Array
```I
{[({Hello World})]}
```

```O
{[({Hello World})]}
```

# Array Value whitespace start
```I
{[ Hugo Hello World]}
```

```O
{[Hugo Hello World]}
```

# Map Pointer
```I
{<():->0000000000000000>}
```

```O
{<():->0000000000000000>}
```

# Comma in Object key
```I
{\,hello()}
```

```O
{\,hello()}
```

# Comma and Whitespace in Object key
```I
{\, hello()}
```

```O
{\, hello()}
```

# Comma separator in Object key
```I
{,hello()}
```

```O
{hello()}
```

# Comma and Whitespace separator in Object key
```I
{, hello()}
```

```O
{hello()}
```

# Array Value with Array inside and Value
```I
{[(@method(yapion.serializing.annotationproccessing.SerializingProcessor#test[String]))]}
```

```O
{[@method(yapion.serializing.annotationproccessing.SerializingProcessor#test\[String\])]}
```

# Array Value with Array inside and Value 2
```I
{[@method(yapion.serializing.annotationproccessing.SerializingProcessor#test\[String\])]}
```

```O
{[@method(yapion.serializing.annotationproccessing.SerializingProcessor#test\[String\])]}
```