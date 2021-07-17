# Backslash
```I
{a(\\)}
```

```O
{a(\\)}
```

# Unicode Escape
```I
{a(\u0020)}
```

```O
{a( )}
```

# Unicode Escape 2
```I
{\u0020(a)}
```

```O
{\ (a)}
```

# String Escape
```I
{(""")}
```

```O
{(""")}
```

# String Escape 2
```I
{("""")}
```

```O
{("""")}
```

# String Starts With
```I
{(""Hello world")}
```

```O
{("Hello world)}
```

# String Ends With
```I
{("Hello world"")}
```

```O
{(Hello world")}
```

# \t \r \n
```I
{\r\n\t()}
```

```O
{\r\n\t()}
```

# \\r
```I
{\
   n()}
   ```

```O
{\
   n()}
```

# \\r in Array
```I
{[\
   Hello World]}
```

```O
{[\\n   Hello World]}
```

# newline
```I
{
   n()}
```

```O
{n()}
```

# newline 2
```I
{[
   Hello World]}
```

```O
{[Hello World]}
```