package yapion.utils;

import yapion.hierarchy.types.*;
import yapion.parser.JSONMapper;
import yapion.serializing.YAPIONDeserializer;
import yapion.serializing.YAPIONSerializer;
import yapion.serializing.serializer.other.EnumSerializer;

public class IdentifierUtils {

    /**
     * This variable is used in the {@link YAPIONSerializer}
     * and {@link YAPIONDeserializer} for identifying an
     * {@link YAPIONObject} type by its {@link Class#getTypeName()}.
     * In the serialization this variable is used as the
     * key of a {@link YAPIONVariable} and will be called
     * type variable.
     */
    public static final String TYPE_IDENTIFIER = "@type";

    /**
     * This variable is used in the {@link EnumSerializer}
     * for identifying an {@link Enum} by its {@link Class#getTypeName()}.
     * In the serialization this variable is used as the
     * key of a {@link YAPIONVariable} and will be calles
     * enum variable.
     */
    public static final String ENUM_IDENTIFIER = "@enum";

    /**
     * This variable is used in the {@link YAPIONPointer#toJSONString()}
     * implementation and {@link YAPIONPointer#toLossyJSONString()} for
     * representation of an {@link YAPIONPointer} as a JSON object.
     * The {@link JSONMapper} uses this variable to identify a
     * {@link YAPIONPointer} in the JSON representation.
     */
    public static final String POINTER_IDENTIFIER = "@pointer";

    /**
     * This variable is used in the {@link YAPIONMap#toJSONString()}
     * implementation and {@link YAPIONMap#toLossyJSONString()} for
     * representation of an {@link YAPIONMap} as a JSON object.
     * The {@link JSONMapper} uses this variable to identify a
     * {@link YAPIONMap} in the JSON representation.
     */
    public static final String MAP_IDENTIFIER = "@mapping";

    /**
     * This variable is used in the {@link YAPIONValue#toJSONString()}
     * implementation for representation of the internal type
     * notice into the JSON structure. The {@link YAPIONValue}
     * is wrapped into an JSON object with one key value pair.
     * The key to this value is this identifier and the {@link JSONMapper}
     * will use this variable to identify a specific {@link YAPIONValue}
     * type in the JSON representation.
     */
    public static final String BYTE_IDENTIFIER = "@byte";

    /**
     * This variable is used in the {@link YAPIONValue#toJSONString()}
     * implementation for representation of the internal type
     * notice into the JSON structure. The {@link YAPIONValue}
     * is wrapped into an JSON object with one key value pair.
     * The key to this value is this identifier and the {@link JSONMapper}
     * will use this variable to identify a specific {@link YAPIONValue}
     * type in the JSON representation.
     */
    public static final String SHORT_IDENTIFIER = "@short";

    /**
     * This variable is used in the {@link YAPIONValue#toJSONString()}
     * implementation for representation of the internal type
     * notice into the JSON structure. The {@link YAPIONValue}
     * is wrapped into an JSON object with one key value pair.
     * The key to this value is this identifier and the {@link JSONMapper}
     * will use this variable to identify a specific {@link YAPIONValue}
     * type in the JSON representation.
     */
    public static final String CHAR_IDENTIFIER = "@char";

    /**
     * This variable is used in the {@link YAPIONValue#toJSONString()}
     * implementation for representation of the internal type
     * notice into the JSON structure. The {@link YAPIONValue}
     * is wrapped into an JSON object with one key value pair.
     * The key to this value is this identifier and the {@link JSONMapper}
     * will use this variable to identify a specific {@link YAPIONValue}
     * type in the JSON representation.
     */
    public static final String INT_IDENTIFIER = "@int";

    /**
     * This variable is used in the {@link YAPIONValue#toJSONString()}
     * implementation for representation of the internal type
     * notice into the JSON structure. The {@link YAPIONValue}
     * is wrapped into an JSON object with one key value pair.
     * The key to this value is this identifier and the {@link JSONMapper}
     * will use this variable to identify a specific {@link YAPIONValue}
     * type in the JSON representation.
     */
    public static final String FLOAT_IDENTIFIER = "@float";

    /**
     * This variable is used in the {@link YAPIONValue#toJSONString()}
     * implementation for representation of the internal type
     * notice into the JSON structure. The {@link YAPIONValue}
     * is wrapped into an JSON object with one key value pair.
     * The key to this value is this identifier and the {@link JSONMapper}
     * will use this variable to identify a specific {@link YAPIONValue}
     * type in the JSON representation.
     */
    public static final String LONG_IDENTIFIER = "@long";

    /**
     * This variable is used in the {@link YAPIONValue#toJSONString()}
     * implementation for representation of the internal type
     * notice into the JSON structure. The {@link YAPIONValue}
     * is wrapped into an JSON object with one key value pair.
     * The key to this value is this identifier and the {@link JSONMapper}
     * will use this variable to identify a specific {@link YAPIONValue}
     * type in the JSON representation.
     */
    public static final String DOUBLE_IDENTIFIER = "@double";

    /**
     * This variable is used in the {@link YAPIONValue#toJSONString()}
     * implementation for representation of the internal type
     * notice into the JSON structure. The {@link YAPIONValue}
     * is wrapped into an JSON object with one key value pair.
     * The key to this value is this identifier and the {@link JSONMapper}
     * will use this variable to identify a specific {@link YAPIONValue}
     * type in the JSON representation.
     */
    public static final String BIG_INTEGER_IDENTIFIER = "@bint";

    /**
     * This variable is used in the {@link YAPIONValue#toJSONString()}
     * implementation for representation of the internal type
     * notice into the JSON structure. The {@link YAPIONValue}
     * is wrapped into an JSON object with one key value pair.
     * The key to this value is this identifier and the {@link JSONMapper}
     * will use this variable to identify a specific {@link YAPIONValue}
     * type in the JSON representation.
     */
    public static final String BIG_DECIMAL_IDENTIFIER = "@bdecimal";

}
