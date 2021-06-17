# Serializer

This package will be compressed into a `.tar.gz` while building. This folder will not be retained in the final jar. The `SerializeManager` will load all classes and instantiate them correctly. To add a new one just put it somewhere in this folder and rebuild the jar with `gradle build` and annotate the source class with `SerializerImplementation`.