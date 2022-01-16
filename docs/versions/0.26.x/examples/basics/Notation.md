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
    What is the difference between YAPION and JSON?
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
    <b>Warning:</b> There is no need for any seperator between the key and the value. And the keys are not inside of quotes. The example above would something like this in JSON:
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