# Classes and Packages

If you want to serialize something in one package and class structure to another one, even in another VM, you need to remap the structure while deserializing. To achieve this there is the `TypeReMapper`. The TypeReMapper can either remap packages or just classes. It tries to remap classes first and then packages from the deepest upwards. 

```java
import yapion.serializing.TypeReMapper;

public class ExampleTypeReMapper {

    public static void main(String[] args) {
        TypeReMapper typeReMapper = new TypeReMapper();

        // Add a class mapping from Class to Class
        typeReMapper.addClassMapping(Class, Class);

        // Add a class mapping from Class to String
        typeReMapper.addClassMapping(Class, String);

        // Add a class mapping from String to Class
        typeReMapper.addClassMapping(String, Class);

        // Add a class mapping from String to String
        typeReMapper.addClassMapping(String, String);

        // Add a package mapping from Package to Package
        typeReMapper.addPackageMapping(Package, Package);

        // Add a package mapping from Package to String
        typeReMapper.addPackageMapping(Package, String);

        // Add a package mapping from String to Package
        typeReMapper.addPackageMapping(String, Package);

        // Add a package mapping from String to String
        typeReMapper.addPackageMapping(String, String);
    }

}
```

To use the `TypeReMapper` you need to pass an instance to your YAPIONDeserializer alongside an YAPIONObject.

```java
import yapion.serializing.TypeReMapper;

public class ExampleTypeReMapper {

    public static void main(String[] args) {
        TypeReMapper typeReMapper = new TypeReMapper();
        /* [...] */

        // To deserialize with a given TypeReMapper 
        Object object = YAPIONDeserializer.deserialize(new YAPIONObject(), typeReMapper);
    }

}
```