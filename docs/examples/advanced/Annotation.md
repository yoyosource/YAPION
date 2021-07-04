# Annotation

Every annotation in a table with the supported values (2-4 row) and where you can put it (6-8 row).

| Annotation                | context() | type() | cascading() | serializationStep() | deserializationStep() | Usage                       | Type | Method | Field |
|---------------------------|-----------|--------|-------------|---------------------|-----------------------|-----------------------------|------|--------|-------|
| YAPIONSave                | yes       | no     | no          | no                  | no                    | for serialization           | yes  | no     | yes   |
| YAPIONSaveExclude         | yes       | no     | no          | no                  | no                    | for serialization           | yes  | no     | yes   |
| YAPIONOptimize            | yes       | no     | no          | no                  | no                    | for serialization           | no   | no     | yes   |
| YAPIONLoad                | yes       | no     | no          | no                  | no                    | for deserialization         | yes  | no     | yes   |
| YAPIONLoadExclude         | yes       | no     | no          | no                  | no                    | for deserialization         | yes  | no     | yes   |
| YAPIONDeserializeType     | no        | yes    | no          | no                  | no                    | for deserialization         | no   | no     | yes   |
| YAPIONData                | yes       | no     | yes         | no                  | no                    | for (de)serialization       | yes  | no     | no    |
| YAPIONField               | yes       | no     | no          | no                  | no                    | for (de)serialization       | no   | no     | yes   |
| YAPIONObjenesis           | no        | no     | no          | no                  | no                    | for deserialization         | yes  | no     | no    |
| YAPIONPreSerialization    | yes       | no     | no          | no                  | no                    | before serialization        | no   | yes    | no    |
| YAPIONPostSerialization   | yes       | no     | no          | no                  | no                    | after serialization         | no   | yes    | no    |
| YAPIONPreDeserialization  | yes       | no     | no          | no                  | no                    | before deserialization      | no   | yes    | no    |
| YAPIONPostDeserialization | yes       | no     | no          | no                  | no                    | after deserialization       | no   | yes    | no    |
| YAPIONRegistratorProvider | no        | no     | no          | no                  | no                    | for serializer registration | no   | yes    | no    |
| YAPIONSerializing         | no        | no     | no          | yes                 | yes                   | generates a serializer      | yes  | no     | no    |
