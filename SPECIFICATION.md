# YAPION Specifications

Every YAPION document can only have YAPIONObjects on the root level and each YAPIONObject starts and ends with curly brackets. This structure holds Key-Value-Pairs in an enum like fashion also found in Java. The Key-Value-Pairs must be directly after each other without a separator and any leading space, newLine, and tab of a key ignored if no escape character precedes it. The value to a key can either be another YAPIONObject, or YAPIONMap, YAPIONArray, YAPIONValue, or YAPIONPointer.