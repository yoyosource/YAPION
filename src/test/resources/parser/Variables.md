# Empty Variable
```I chars
{a()}
```

```O
{a()}
```

# Quoted to unquoted String
```I
{a("Hello")}
```

```O
{a(Hello)}
```

# Quoted to unquoted String 2
```I
{a('Hello')}
```

```O
{a(Hello)}
```

# Keep Boolean
```I
{a(true)}
```

```O
{a(true)}
```

# Keep Boolean String
```I
{a("true")}
```

```O
{a("true")}
```

# Keep Number
```I
{a(0)}
```

```O
{a(0)}
```

# Keep Number String
```I
{a("0")}
```

```O
{a("0")}
```

# Number E Notation
```I
{(5E+5)}
```

```O
{(500000.0)}
```

# Special Numbers
```I
1(#A000B)2(#a000B)
```

```E
yapion.exceptions.utils.YAPIONIOException
```

# Special Numbers 2
```I
{1(#A000B)2(#a000B)}
```

```O
{1(655371)2(655371)}
```