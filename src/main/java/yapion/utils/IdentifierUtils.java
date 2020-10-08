package yapion.utils;

import yapion.hierarchy.typeinterfaces.ObjectOutput;
import yapion.hierarchy.types.YAPIONMap;
import yapion.hierarchy.types.YAPIONObject;
import yapion.hierarchy.types.YAPIONPointer;
import yapion.hierarchy.types.YAPIONVariable;
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
     *
     */
    public static final String BYTE_IDENTIFIER = "@byte";

    /**
     *
     */
    public static final String SHORT_IDENTIFIER = "@short";

    /**
     *
     */
    public static final String CHAR_IDENTIFIER = "@char";

    /**
     *
     */
    public static final String INT_IDENTIFIER = "@int";

    /**
     *
     */
    public static final String FLOAT_IDENTIFIER = "@float";

    /**
     *
     */
    public static final String LONG_IDENTIFIER = "@long";

    /**
     *
     */
    public static final String DOUBLE_IDENTIFIER = "@double";

    /**
     *
     */
    public static final String BIG_INTEGER_IDENTIFIER = "@bint";

    /**
     *
     */
    public static final String BIG_DECIMAL_IDENTIFIER = "@bdecimal";

}
