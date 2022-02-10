# Flavours

Flavours should make it possible to use YAPION in different environments and with different targets.

A Flavour defines the following:
- How a YAPION object is outputed
- If prettifying is possible

## Types
- YAPION -> default
- YAPIONNoComment -> default but comments are just ignored
- YAPIONExceptionOnComment -> default but if you write a comment an UnsupportedOperationException will be thrown
- JSON
- JSON5 -> JSON with comments
- ThunderFile
- HJSON
- XML
- YAML
- TOML?
- BSON?
- Hocon
- ini??
- properties
- NBT?

Everything with a Question mark might not be implemented. The more questions marks there are the more unlikely it is that it will be implemented.
