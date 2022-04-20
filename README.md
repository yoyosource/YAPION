[![YAPION Version V0.25.3](https://img.shields.io/badge/YAPION%20Version-0.25.3-red)](https://github.com/yoyosource/YAPION/tree/master/)
[![Java Version V1.8.0](https://img.shields.io/badge/Java%20Version-1.8.0-blue.svg)](https://github.com/yoyosource/YAPION/tree/master/)
[![License: Apache 2.0](https://img.shields.io/badge/license-Apache%202-blue)](http://www.apache.org/licenses/LICENSE-2.0)
[![CodeFactor](https://www.codefactor.io/repository/github/yoyosource/yapion/badge)](https://www.codefactor.io/repository/github/yoyosource/yapion)
[![DeepSource](https://deepsource.io/gh/yoyosource/YAPION.svg/?label=active+issues&token=8XHG1y8N-mhEr1VoGJVAtxv2)](https://deepsource.io/gh/yoyosource/YAPION/?ref=repository-badge)
[![Codacy Badge](https://app.codacy.com/project/badge/Grade/9ec27a77e6724060b4f7c676552955f3)](https://www.codacy.com/gh/yoyosource/YAPION/dashboard?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=yoyosource/YAPION&amp;utm_campaign=Badge_Grade)
[![Java CI with Gradle](https://github.com/yoyosource/YAPION/actions/workflows/gradle.yml/badge.svg)](https://github.com/yoyosource/YAPION/actions/workflows/gradle.yml)
[![Nightly Branch/Release](https://github.com/yoyosource/YAPION/actions/workflows/nightly.yml/badge.svg)](https://github.com/yoyosource/YAPION/actions/workflows/nightly.yml)

# YAPION
**YAPION** is a slim and fast Object Notation specifically for Java Objects.
It can handle recursive Object structures and state specific serialization and deserialization (SsS/D).
This SsS/D is achieved by a self build annotation system used by YAPION. This object notation is designed for easy usage with complex Object structures and can do even more complex stuff fairly easy.
An extensive documentation can be found [here](https://yoyosource.github.io/YAPION/).   

## YAPION's naming
```
Y   -> @yoyosource   
API -> application programming interface
ON  -> object notation
```

## License
YAPION is licensed under the terms of the Apache License 2.0. See the file LICENSE or visit http://www.apache.org/licenses/LICENSE-2.0 for details.

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.

## Using in other Projects
YAPION is not published on jcenter or mavenCentral. It is published directly to this GitHub repository. You can declare it as a dependency in your build.gradle with:
```groovy
repositories {
    maven {
        url = uri("https://raw.githubusercontent.com/yoyosource/YAPION/master/releases")
    }
}

dependencies {
    implementation 'yoyosource:YAPION:0.25.3'
}
```

### Annotation Processing
Using the YAPION annotation processor is as easy as just writing two new dependency lines:
```groovy
dependencies {
    implementation 'yoyosource:YAPION:0.25.3'
    annotationProcessor 'yoyosource:YAPION:0.25.3'
    testAnnotationProcessor 'yoyosource:YAPION:0.25.3'
}
```
This will enable both `@YAPIONAccessGenerator` and `@YAPIONSerializing` to be processed. But the later needs some more steps to create the desired output.
For that you need a post classes step to modify the class file directly and produce the desired output. For this you need to run the following command:
```
java -cp <Folder to YAPION jar>/YAPION-<VERSION>.jar yapion.serializing.annotationproccessing.SerializingApplier <output classes directory>
```

You will need to replace everything surrounded by '<>' with the values you need. One implementation in a gradle build script could look something like this:
```groovy
task applySerializingProcessing {
    description 'Finalize YAPIONAnnotationProcessing'
    group "build"

    doLast {
        shell("java -cp <Folder to YAPION jar>/YAPION-<VERSION>.jar yapion.config.annotationproccessing.SerializingApplier ${buildDir}/classes/java/main")
    }
}
classes.finalizedBy applySerializingProcessing

def shell(String command) {
    def proc
    if (!Os.isFamily(Os.FAMILY_WINDOWS)) {
        proc = ['bash', '-c', command].execute()
    } else {
        proc = ["cmd", "/c", command].execute()
    }
    def out = new StringBuilder()
    def err = new StringBuilder()
    proc.waitForProcessOutput(out, err)
    return [out.toString().trim(), err.toString().trim(), proc.exitValue()]
}
```

# APIs/Libs/Other used
- easymock/objenesis (https://github.com/easymock/objenesis)
  - [V] 3.1
  - [L] Apache-2.0
- qos-ch/slf4j (https://github.com/qos-ch/slf4j)
  - [V] 2.0.0-alpha1
  - [L] MIT
- classindex:classindex (https://github.com/atteo/classindex)
  - [V] 3.4
  - [L] Apache-2.0
- rzwitserloot/lombok (https://github.com/rzwitserloot/lombok)
  - [V] 1.18.6
  - [L] MIT
- rmuller/infomas-asl (https://github.com/rmuller/infomas-asl)
  - [V] 3.0.5
  - [L] Apache-2.0
- asm (https://gitlab.ow2.org/asm/asm)
  - [V] 9.2
  - [L] ???
- eppleton/frgaal (https://github.com/eppleton/frgaal)
  - I am using the frgaal compiler to be able to use newer java features in an older environment.
  - [V] 15.0.0
  - [L] GPLv2+CPE

* [V] Version
* [L] License

## YAPION Modules
- YAPION-Hierarchy
- YAPION-Parser
  - Dependencies:
    - YAPION-Hierarchy
- YAPION-Serializer
  - Dependencies:
    - YAPION-Hierarchy
- YAPION-IO
  - Dependencies:
    - YAPION-Hierarchy
    - YAPION-Parser
    - YAPION-Serializer
- YAPION-Other
  - Dependencies:
    - YAPION-Hierarchy
    - YAPION-Parser
    - YAPION-Serializer

## YAPION Databindings
- YAPION-Databind-JavaAtomic
- YAPION-Databind-JavaBase
- YAPION-Databind-JavaCollections
- YAPION-Databind-JavaFunction
- YAPION-Databind-JavaOther
- YAPION-Databind-JavaSecurity
- YAPION-Databind-JavaTime

All YAPION databindings need the YAPION-Serializer depdenency. When adding a new databinding, you need to add the name to the YAPION-Serializer 'yapion/serializing/SerializeManagerDataBindings.java' BINDINGS array.

# Structure
YAPION is heavily inspired by JSON and you can see some similarities between those object notations.
Both have objects as a key, value map and arrays. An YAPION structure can only be an object. An object starts with '{' and ends with '}'. Complementary an array starts with '\[' ends with ']'.
The String representation of an YAPION Object or Array is mostly shorter than the JSON equivalent. This is achieved by eliminating most quotation marks and building the key value pairs in a enum like fashion.
YAPION as a object notation has 5 types. Those are value, array, object, map (see: Map) and pointer (see: Pointer).
Values are indicated by '(...)', arrays by '\[...]', objects by '{...}', map by '<...> and pointers by '->...', the 3 dots are representing the value, or the key value pairs.

## Benefits of YAPION
- Most of the time JSON is nearly **1.15** to **1.4** time as long as the YAPION equivalent. This does not apply to the serialized stuff.
- YAPION has native map support to easier serialize the Java counter part.
- YAPION has native pointer support to easier serialize Java references to other Objects in multiple places.
- YAPION's primitive types know what Java Type they should be.

## JSON Equivalent
### Objects, Key-Value pairs
```
Example for an empty YAPION object
JSON:   {}
YAPION: {}

Example for an YAPION object with "name":"yoyosource" as value pair
JSON:   {"name":"yoyosource"}
YAPION: {name(yoyosource)}

Example for an YAPION object with "owner":true as value pair
JSON:   {"owner":true}
YAPION: {owner(true)}

Example for an YAPION object with "owner":"true" as value pair
JSON:   {"owner":"true"}
YAPION: {owner("true")}

Example for an YAPION object with "price":10 as value pair
JSON:   {"price":10}
YAPION: {price(10)}

Example for an YAPION object with "   price":10 as value pair
JSON:   {"   price":10}
YAPION: {\   price(10)}

Example for an YAPION object with "name":"yoyosource" and "owner":true as value pairs
JSON:   {"name":"yoyosource","owner":true}
YAPION: {name(yoyosource)owner(true)}
```
### Arrays
```
Example for an empty YAPION array
JSON:   []
YAPION: []

Example for an YAPION array with numbers
JSON:   [0,1,2,3,4,5,6,7,8,9,10]
YAPION: [0,1,2,3,4,5,6,7,8,9,10]

Example for an YAPION array with values
JSON:   [true,false,null,"Hello"]
YAPION: [true,false,null,Hello]

Example for an YAPION array with objects
JSON:   [{"name":"yoyosource","owner":true},{"name":"chaoscaot","owner":"false"}]
YAPION: [{name(yoyosource)owner(true)},{name(chaoscaot)owner("false")}]

Example for embedding an YAPION array in another object.
JSON:   {"contributor":[{"name":"yoyosource","owner":true},{"name":"chaoscaot","owner":"false"}]}
YAPION: {contributor[{name(yoyosource)owner(true)},{name(chaoscaot)owner("false")}]}
```
## YAPION Specific
### Maps
```
Example for an empty YAPION map
<>

Example for an YAPION map with String to String mapping
<(hello):("1")>

Example for an YAPION map with Integer to String mapping
<(1):("1")>

Example for an YAPION map with Object to Object mapping
<{}:{}>
```

### Pointer
These pointers point to another YAPION Object in the same serialization. For reconstructing the recursion this pointer points to the object it is referring to.
A pointer is not a hash and is not intended to be secure. If 2 objects in your tree have the same pointer it will point to the first by default.
All pointers shown in here are valid pointers but are mostly meaningless because a pointer gets constructed by your current object state.
The list of chars for pointers is as follows: '0123456789ABCDEF'
```
Example for an YAPION pointer
{hello->7FFFFFE53E6CBDFE}
```

### Comments
Comments are only supported by enabling them on the YAPIONParser.
```
{
    /* This is a Comment */
    name(yoyosource)
}
```

# Code examples
## [Parsing](https://github.com/yoyosource/YAPION/tree/master/src/main/java/yapion/hierarchy)
The Parser itself is located [here](https://github.com/yoyosource/YAPION/tree/master/src/main/java/yapion/parser).
The specifications are [here](https://github.com/yoyosource/YAPION/tree/master/src/main/java/yapion/parser/README.md).
```java
YAPIONObject yapionObject = YAPIONParser.parse("[...]");
YAPIONObject yapionObject = YAPIONParser.parse(new FileInputStream(new File([...])));
```

## [Serializing](https://github.com/yoyosource/YAPION/tree/master/src/main/java/yapion/serializing)
```java
YAPIONObject yapionObject = YAPIONSerializer.serialize([...]);
YAPIONObject yapionObject = YAPIONSerializer.serialize([...], "[...]");
```

## [Deserializing](https://github.com/yoyosource/YAPION/tree/master/src/main/java/yapion/serializing)
```java
Object object = YAPIONDeserializer.deserialize([...]);
Object object = YAPIONDeserializer.deserialize([...], "[...]");
```
