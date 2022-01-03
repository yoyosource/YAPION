# Serializer

This package will be compressed into a `.pack` while building. This folder will not be retained in the final jar. The `SerializeManager` will load all classes and instantiate them correctly. To add a new one just put it somewhere in this folder and rebuild the jar with `gradle build` and annotate the source class with `SerializerImplementation`.

## Performance

For the full performance there should be a `libs` folder in the project sources with a `YAPION-SNAPSHOT.jar` in it. This will have the effect that the class `RecordValue` will have a custom generated Serializer associated with it.