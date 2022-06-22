# YAPIONPath

### Simple

Simple single select path:

```yapionpath
element
```

Multiple selects after each other:

```yapionpath
element1.element2.element3
```

Index select:

```yapionpath
0.1
```

### Special

| Type             | Identifier              | Description                                                | Applicability                                     |
|------------------|-------------------------|------------------------------------------------------------|---------------------------------------------------|
| Any              | `*`                     | Any value                                                  | Only in non `Value`                               |
| AnyDeeper        | `**`                    | AnyDeeper value                                            | Only in non `Value`                               |
| Grouping         | `(`, `)`                | When only one Element is inside it will be a `Subselect`   | For `Value` and when multiple elements are needed |
| And              | `&(`, `)`               | Create an `and` group                                      | In `Value`                                        |
| Or               | `&vert;(`, `)`          | Create an `or` group                                       | In `Value`                                        |
| Root             | `$`                     | Root Element(s)                                            | Only in non `Value`                               |
| Current          | `@`                     | Current Element(s)                                         | Only in `When`                                    |
| Value            | `#(`                    | Value Element(s) first group is an implicit `and` group    | Only in non `Value`                               |
| When             | `?(`                    | When Element(s)                                            | Only in non `Value`                               |
| KeySelection     | `!`                     | KeySelection Element(s) with direct `and` group afterwards | Only in non `Value`                               |
| Spread           | `!*`                    | Spread Element(s)                                          | Only in non `Value`                               |
| Flatten          | `:`                     | Flatten Element(s)                                         | Only in non `Value`                               |
| StartsWith       | `\<Text>*`              | StartsWith Element(s)                                      | All                                               |
| EndsWith         | `*\<Text>`              | EndsWith Element(s)                                        | All                                               |
| Contains         | `*\<Text>*`             | Contains Element(s)                                        | All                                               |
| Regex            | `/\<Regex>/`            | Regex Element(s)                                           | All                                               |
| Set              | `{`, `}`                | Set Element(s), comma seperated                            | All                                               |
| Distinct         | `=`                     | Distinct Element(s)                                        | Only in non `Value`                               |
| IdentityDistinct | `@=`                    | IdentityDistinct Element(s)                                | Only in non `Value`                               |
| Min              | `[\<Number>-]`          | Min Value                                                  | All                                               |
| Max              | `[-\<Number>]`          | Max Value                                                  | All                                               |
| Range            | `[\<Number>-\<Number>]` | Range Value                                                | All                                               |
| AllOf            | `u&(`, `)`              | AllOf Element(s)                                           | Only in non `Value`                               |
| AllWith          | `u&vert;(`, `)`         | AllWith Element(s)                                         | Only in non `Value`                               |
| AnyOf            | `v&(`, `)`              | AnyOf Element(s)                                           | Only in non `Value`                               |
| AnyWith          | `v&vert;(`, `)`         | AnyWith Element(s)                                         | Only in non `Value`                               |
| ElementType      | ?                       | Type of Element(s)                                         | Only in non `Value`                               |
