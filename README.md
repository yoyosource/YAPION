[![YAPION Version V0.9.0](https://img.shields.io/badge/YAPION%20Version-0.9.0-red)](https://github.com/yoyosource/YAPIOn/tree/master/)
[![Java Version V1.8.0](https://img.shields.io/badge/Java%20Version-1.8.0-blue.svg)](https://github.com/yoyosource/YAPION/tree/master/)
[![License: Apache 2.0](https://img.shields.io/badge/license-Apache%202-blue)](http://www.apache.org/licenses/LICENSE-2.0)

# YAPION
**YAPION** is a slim and fast Object Notation specifically for Java Objects.
It can handle recursive Object structures and state specific serialization and deserialization (SsS/D).
This SsS/D is achieved by a self build annotation system used by YAPION. This object notation is designed for easy usage with complex Object structures and can do even more complex stuff fairly easy.   

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
YAPION is not published on jcenter or mavenCentral. It is published directly to this GitHub repository. You can declare it as a dependency in your gradle.build with:
```groovy
repositories {
    maven {
        url = uri("https://raw.githubusercontent.com/yoyosource/YAPION/master/releases")
    }
}

dependencies {
    implementation 'yoyosource:YAPION:0.9.0'
}
```

# APIs used
- easymock/objenesis (https://github.com/easymock/objenesis)
  - [V] 3.1
  - [L] Apache-2.0
- qos-ch/slf4j (https://github.com/qos-ch/slf4j)
  - [V] 2.0.0-alpha1
  - [L] MIT

- rzwitserloot/lombok (https://github.com/rzwitserloot/lombok)
  - [V] 1.18.6
  - [L] MIT

* [V] Version
* [L] License

## Tasks
- More internal Java Types supported?
- More efficient JSON Parser (eliminate the Recursion)
- More efficient JSON Mapper (eliminate the Recursion?)

## Structure
YAPION is heavily inspired by JSON and you can see some similarities between those object notations.
Both have objects as a key, value map and arrays. An YAPION structure can only be an object. An object starts with '{' and ends with '}'. Complementary an array starts with '\[' ends with ']'.
The String representation of an YAPION Object or Array is mostly shorter than the JSON equivalent. This is achieved by eliminating most quotation marks and building the key value pairs in a enum like fashion.
YAPION as a object notation has 5 types. Those are value, array, object, map (see: Map) and pointer (see: Pointer).
Values are indicated by '(...)', arrays by '\[...]', objects by '{...}', map by '<...> and pointers by '->...', the 3 dots are representing the value, or the key value pairs.

### Benefits of YAPION
- Most of the time JSON is nearly **1.15** to **1.4** time as long as the YAPION equivalent.
- YAPION has native map support to easier serialize the Java counter part.
- YAPION has native pointer support to easier serialize Java references to other Objects in multiple places.
- YAPION's primitive types know what type they should be.

### JSON Equivalent
#### Objects, Key-Value pairs
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
#### Arrays
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
JSON:   [{"name":"yoyosource","owner":true},{"name":"chaoscaot444","owner":"false"}]
YAPION: [{name(yoyosource)owner(true)},{name(chaoscaot444)owner("false")}]

Example for embedding an YAPION array in another object.
JSON:   {"contributor":[{"name":"yoyosource","owner":true},{"name":"chaoscaot444","owner":"false"}]}
YAPION: {contributor[{name(yoyosource)owner(true)},{name(chaoscaot444)owner("false")}]}
```
### YAPION Specific
#### Maps
```
Example for an empty YAPION map
<>

Example for an YAPION map with String to String mapping
<0:1#0(hello)#1("1")>

Example for an YAPION map with Integer to String mapping
<0:1#0(1)#1("1")>

Example for an YAPION map with Object to Object mapping
<0:1#0{}#1{}>
```
### Pointer
These pointers point to another YAPION Object in the same serialization. For reconstructing the recursion this pointer points to the object it is referring to.
A pointer is not a hash and is not intended to be secure. If 2 objects in your tree have the same pointer it will point to the first by default.
All pointer shown in here are valid pointers but are mostly meaningless because a pointer gets constructed by your current object state.
The list of prefixes for pointers is as follows: '0123456789ABCDEF'
```
Example for an YAPION pointer
{hello->7FFFFFE53E6CBDFE}
```

## Code examples
### Parsing
```java
YAPIONObject yapionObject = YAPIONParser.parse("[...]");
YAPIONObject yapionObject1 = YAPIONParser.parse(new FileInputStream(new File([...])));
```

### Serializing
```java
YAPIONObject yapionObject = YAPIONSerializer.serialize([...]);
YAPIONObject yapionObject1 = YAPIONSerializer.serialize([...], "[...]");
```

### Deserializing
```java
Object object = YAPIONDeserializer.deserialize([...]);
Object object = YAPIONDeserializer.deserialize([...], "[...]");
```