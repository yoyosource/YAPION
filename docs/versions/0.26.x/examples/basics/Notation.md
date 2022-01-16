# Notation

```yapion
{
  key(value)
  array_value[
    Hello,
    World!,
  ]
  inline_array[Hello, World!]
  /*
  This is a comment
  */
  map<
    (key):(value)
  >
  pointer->0000000000000000
}
```

The example above contain every thing that is supported by YAPION. You can see some similarities between the YAPION notation and the JSON notation. Since YAPION is a superset of JSON, any JSON file is a valid YAPION file.

<div style="padding: 10px; background-color: #00000010; border-radius: 5px;">
    <img src="../../../../icons/question.png" width="22" alt="" style="vertical-align: middle; margin-left: 5px; float: right">
    What is the difference between YAPION and JSON?<br>
    The main difference is that YAPION supports more types than JSON. This is needed to support serialization and deserialization of Java objects.
    Comments are syntactic sugar which are not needed fot the serialization and deserialization but are quite useful for the programmer if they want to use YAPION as a configuration format.
</div>

### The building blocks
YAPION itself is composed of the following building blocks:
- **Objects**: Identified by the `{` and `}` characters. Present in JSON
- **Arrays**: Identified by the `[` and `]` characters. Present in JSON
- **Maps**: Identified by the `<` and `>` characters.
- **Pointers**: Starting with `->`.
- **Comments**: Starting with `/*` and ending with `*/`. Present in JSON5
- **Values**: Identified by the `(` and `)` characters. Present in JSON with a different syntax.

## Objects
Objects are very similar to JSON objects. They start with `{` and end with `}`. They also contain a list of key-value pairs. The key is a string and the value is any valid building block.

```yapion
{
  key{}
}
```

<div style="padding: 10px; background-color: #00000010; border-radius: 5px;">
    <img src="../../../../icons/exclamation.png" width="22" alt="" style="vertical-align: middle; margin-left: 5px; float: right">
    <b>Warning:</b> There is no need for any seperator between the key and the value. And the keys are not inside of quotes. The example above would translate to something like this in JSON:
    <pre class="highlight"><code>{<br>    "key": {}<br>}</code></pre>
</div>

## Arrays
Arrays are very similar to JSON arrays. They start with `[` and end with `]`. They also contain a list of values. The values are any valid building block.

```yapion
[
  [],
  [],
  {}
]
```

## Maps
Maps start with `<` and end with `>`. They contain a list of key-value pairs. The key and the value are both any valid building block. Both seperated by a `:`.

```yapion
<
  {}:{}
>
```

<div style="padding: 10px; background-color: #00000010; border-radius: 5px;">
    <img src="../../../../icons/exclamation.png" width="22" alt="" style="vertical-align: middle; margin-left: 5px; float: right">
    <b>Warning:</b> This is not available in JSON syntax and is mainly used to be able to serialize Java maps.
</div>

## Pointers
Pointers start with `->`. They are used to identify a building block in the YAPION hierarchy. The pointer is followed by a 64-bit hexadecimal number, which is later known under the name `reference-id`.

```yapion
->0000000000000000
```

<div style="padding: 10px; background-color: #00000010; border-radius: 5px;">
    <img src="../../../../icons/exclamation.png" width="22" alt="" style="vertical-align: middle; margin-left: 5px; float: right">
    <b>Warning:</b> This is not available in JSON syntax and is mainly used to be able to serialize Java references to other Objects in the same hierarchy. The benefit of using pointers is that you can have multiple objects with the same value in the hierarchy and only store the object once. This building block is also used during the deserializing of an object to recreate all reference in the given hierarchy correctly.
</div>

## Comments
Comments start with `/*` and end with `*/`. They are used to add comments to the YAPION hierarchy.

```yapion
/*
  This is a comment
*/
```

<div style="padding: 10px; background-color: #00000010; border-radius: 5px;">
    <img src="../../../../icons/exclamation.png" width="22" alt="" style="vertical-align: middle; margin-left: 5px; float: right">
    <b>Warning:</b> Comments are not supported by JSON. The YAPIONParser does not parse comments by default.
</div>

## Values
