# Comment Ignoring
```I
{/*Test*/(Hello)}
```

```O prettified
{
  /*Test*/(Hello)
}
```

# Comment Skip
```I comments:skip
{/*Test*/(Hello)}
```

```O prettified
{
  (Hello)
}
```

# Comment Keep
```I comments:keep
{/*Test*/(Hello)}
```

```O prettified
{
  /*Test*/
  (Hello)
}
```
