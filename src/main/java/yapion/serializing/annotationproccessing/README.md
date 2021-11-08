# SerializingProcessor

# AccessGeneratorProcessor
The AccessGeneratorProcessor is for generating class files used to access YAPIONObjects statically and strong types. This can be used to access configs or some other kind of configuration oder data file.

This feature consist of two mayor parts, the java code to define the generation and an optional external source.

## Simple usage
For the generator to function you need a field in any class annotated by `YAPIONAccessGenerator` holding a string and having the modifiers `static` and `final`.
```java
import yapion.annotations.registration.YAPIONAccessGenerator;

public class TestAccessGenerator {

    @YAPIONAccessGenerator
    private static final String exampleField = "<RELATIVE PATH TO CONFIG FROM PROJECT ROOT>";

    @YAPIONAccessGenerator(inline = true)
    private static final String exampleFieldInline = "<INLINE CONFIG>";
}
```

The config can be written in YAPION or JSON, either directly as a String into the field or in an external file.

### A simple Config
A simple config just defines some values to hold, nothing fancy.
```yapion
{
    test(LONG)
}
```

This simple config will define that there can be a variable in the YAPIONObject named `test` with the specified value type of `LONG`.  
Allowed value types are, mixed case is allowed:
- string
- byte
- short
- int or integer
- long
- biginteger
- float
- double
- bigdecimal
- char or character
- bool or boolean
- any or anything else will be converted to Object or any value

The example config above is not complete though. When compiling the compiler will say that a `@name` variable is missing. This variable defines the name of the current object or generated class.  
The complete config would look something like this:
```yapion
{
    @name(Config)
    test(LONG)
}
```

While dealing with really long configs nesting objects and dealing with those is crucial.  
You can just use nested objects int the config like:
```yapion
{
    @name(Config)
    auth{
        token(LONG)
    }
    message{
        hello(STRING)
    }
}
```

The config would have some kind of `auth` section with a token as a long and a `messsage` with a hello string.
You can optionally assign nested objects a name with the `@name` variable.
```yapion
{
    @name(Config)
    auth{
        @name(Auth)
        token(LONG)
    }
    message{
        @name(Message)
        hello(STRING)
    }
}
```

When no name is specified a name in the format `_%i` is generated where `%i` is an incrementing integer starting at 0.

One example config for the above stated definition could look something like this:
```yapion
{
    auth{
        token(0L)
    }
    message{
        hello(Hello World)
    }
}
```

## Using the generated code
After generating such a config file or config string we can use the so called `Config` class within our code.
```java
import yapion.annotations.registration.YAPIONAccessGenerator;
import yapion.hierarchy.types.YAPIONObject;

public class TestAccessGenerator {

    @YAPIONAccessGenerator
    private static final String exampleField = "<RELATIVE PATH TO CONFIG FROM PROJECT ROOT>";

    @YAPIONAccessGenerator(inline = true)
    private static final String exampleFieldInline = "<INLINE CONFIG>";

    public static void main(String[] args) {
        // A generated access object needs an YAPIONObject as input, so we will provide one
        Config config = new Config(new YAPIONObject());
        
        // With the above config there are the following methods defined on `config`
        // - getAuth()
        // - isAuthPresent()
        // - getMessage()
        // - isMessagePresent()
        // And on the by `getAuth()` returned object `getToken()` and `isTokenPresent()`
        // On the by `getMessage()` returned object `getHello()` and `isHelloPresent()`
        
        // Since we just inputted an empty YAPIONObject everything is just empty/null.
    }
}
```

### Null magic
Sometimes null values are not desired, if so just append an exclamation point after the variable name. This does not apply to any variable prefixed with '@'.
```yapion
{
    @name(Config)
    auth!{
        @name(Auth)
        token!(LONG)
    }
    message{
        @name(Message)
        hello(STRING)
    }
}
```

This now defines to always have the `auth` section and also the `token` variable within the `auth` section. The `is???Present()` methods will not be generated as the keys cannot be null.

### This is too short
If we go back to the basic config:
```yapion
{
    @name(Config)
    test(LONG)
}
```

The variable definition for `test` can be written in another way like:
```yapion
{
    @name(Config)
    test{
        @type(LONG)
    }
}
```

This defines the same as the above, but can do more things:
```yapion
{
    @name(Config)
    test{
        @type(LONG)
        constraints(<SOME JAVA-LAMBDA EXPRESSION>)
        default(<DEFAULT VALUE>)
    }
}
```

#### Constraints
A constraint is one more validation step for this variable. You can use it to check for specific String lengths or number ranges or even more complex stuff with the variable given. The lambda expression needs to return a boolean where `true` means valid and `false` invalid.
The `ConstraintsUtils` class defines some utilities to validate values. You can just call them without importing them, as they are statically imported directly.

#### Default
The default value needs to have the same type as the `@type` variable defines and can have any value. It will not be checked against the constraints.

## What about duplications?
If you have 2 parts in one configuration that have the same structure you can use a `reference`.
```yapion
{
    @name(Config)
    database{
        @name(Auth)
        port{
            @type(INT)
            constraints(i -> i >= 0 && i < 65536)
        }
        username(STRING)
        password(String)
    }
    websiteLogin(@Auth)
    queryLogin{
        @reference(Auth)
    }
}
```

You can either use the long form or the short form, as you see above. This can be useful when many sections are the same in your config.
While using the long reference form you can add more values to the object if need be. Something like this:
```yapion
{
    @name(Config)
    database{
        @name(Auth)
        port{
            @type(INT)
            constraints(i -> i >= 0 && i < 65536)
        }
        username(STRING)
        password(String)
    }
    websiteLogin(@Auth)
    queryLogin{
        @reference(Auth)
        ip(STRING)
    }
}
```

### Hidden
You can also hide an object. This will not generate a field in the current class.
```yapion
{
    @name(Config)
    auth{
        @name(Auth)
        @hidden(true)
        port{
            @type(INT)
            constraints(i -> i >= 0 && i < 65536)
        }
        username(STRING)
        password(String)
    }
    authWithIp{
        @name(AuthWithIp)
        @reference(Auth)
        @hidden(true)
        ip(String)
    }
    database(@Auth)
    websiteLogin(@Auth)
    queryLogin(@AuthWithIp)
}
```

## Arrays
There are two types of arrays, reference arrays and value arrays. Both have a short and long form. Any array will be a `java.util.List` as type and a `java.util.ArrayList` as implementation.
```yapion
{
    @name(Config)
    longArray(LONG[])
    anotherLongArray{
        @arrayType(LONG)
        constraints(element -> element >= 0)
    }
    reference{
        @name(SSH)
        @hidden(true)
        name(STRING)
        ip!(String)
    }
    sshArray(@SSH[])
    anotherSshArray{
        @arrayReference(SSH)
        @name(SHHWithPort)
        port{
            @type(INT)
            cosntraints(port -> port >= 0 && port <= 65536)
        }
    }
}
```

## Maps
Any map will be a `java.util.Map` as type and a `java.util.HashMap` as implementation. You indicate that you want to use a map by the `@value` value inside an Object, without a `@key` value every element will be converted to the specified `@value`, if not applicable the key will be ignored. If the map is defined as a non null value and no keys match it will be considered as null.
```yapion
{
    @name(Config)
    test{
        @value(STRING)
    }
}
```

Example with key constraints:
```yapion
{
    @name(Config)
    test{
        @keys(s -> s.length() == 3)
        @value(STRING)
    }
}
```

The limitations of the map are, that you cannot have a map inside another map directly.

## Pointers
YAPIONPointer's are not supported by this AccessGeneratorProcessor **yet**.

## Defaults

### Values
See section 'Default' under 'This is too short'.

### Objects/Object References
You can define defaults for any internal objects by using the variable '@default'. The root object cannot have a default defined. The default need to define a valid object part, this will be checked at compile time.
```yapion
{
    @name(Config)
    auth{
        @name(Auth)
        @hidden(true)
        @default{
            port(4000)
            userName(Hugo)
            password(1234)
        }
        port{
            @type(INT)
            constraints(i -> i >= 0 && i < 65536)
        }
        username(STRING)
        password(String)
    }
}
```

### Arrays/Array References
Currently, unsupported.

## Imports
You can also import special Classes by using the variable '@imports'.
```yapion
{
    @name(Config)
    @imports[
        '<your import>',
        '<another import>',
    ]
}
```
