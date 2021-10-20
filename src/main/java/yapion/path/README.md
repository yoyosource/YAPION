# YAPIONPath

## Examples:
[Examples](examples/README.md)

## Syntax

### Elements
One Element:
```
<TEXT/NUMBER>
```
Example `test`

Chaining Elements:
```
<TEXT/NUMBER>.<TEXT/NUMBER>
```
Example `test.test`

### Advanced number checks
Index greater number:
```
><NUMBER>
```
Example `>0`

Index smaller number:
```
<<NUMBER>
```
Example `<10`

Number range, both ends inclusive:
```
[<NUMBER>-<NUMBER>]
```
Example `[0-10]`

Multiple Keys:
```
[<TEXT>,<TEXT>,...]
```
Example `[test,hugo,example]`

### Any Element on this level
```
*
```
Example `*`

Using the star as an element:
```
\*
```
Example `\*`

Starts or ends with:
```
<START>*<END>
```
Example `t*t`

Contains:
```
*<CONTAINS>*
```
Example `*t*`

### Any Element in this level or deeper
```
**
```
Example `**`

Using the stars as an element:
```
\*\*
```
Example `\*\*`

### Regex
```
/<REGEX>/
```
Example:
1. `/.*/`
2. `/-?[0-9]+/`
3. `/-?[0-9]+(\.[0-9]+)?/`
