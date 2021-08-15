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

The config can be written in YAPION or JSON, either directly as a String into the as the second field or in an external file.

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

The example config above is not complete though. When compiling the compiler will a `@name` variable is missing. This variable defines the name of the current object or generated class.  
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

This defines the same as the above, but can do one more thing, constraints:
```yapion
{
    @name(Config)
    test{
        @type(LONG)
        constraints(<SOME JAVA-LAMBDA EXPRESSION>)
    }
}
```

A constraint is one more validation step for this variable. You can use it to check for specific String lengths or number ranges or even more complex stuff with the variable given. The lambda expression needs to return a boolean where `true` means valid and `false` invalid.
The `ConstraintsUtils` class defines some utilities to validate values. You can just call them without importing them, as they are statically imported directly.
