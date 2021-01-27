// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020,2021 yoyosource

package yapion.utils;

import yapion.hierarchy.output.AbstractOutput;
import yapion.hierarchy.types.*;
import yapion.parser.JSONMapper;
import yapion.serializing.YAPIONDeserializer;
import yapion.serializing.YAPIONSerializer;
import yapion.serializing.serializer.object.other.EnumSerializer;
import yapion.serializing.serializer.object.throwable.ErrorSerializer;
import yapion.serializing.serializer.object.throwable.ExceptionSerializer;
import yapion.serializing.serializer.object.throwable.RuntimeExceptionSerializer;
import yapion.serializing.serializer.object.throwable.ThrowableSerializer;

public class IdentifierUtils {

    private IdentifierUtils() {
        throw new IllegalStateException("Utility class");
    }

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
     * key of a {@link YAPIONVariable} and will be called
     * enum variable.
     */
    public static final String ENUM_IDENTIFIER = "@enum";

    /**
     * This variable is used in the {@link ThrowableSerializer},
     * {@link ExceptionSerializer}, {@link RuntimeExceptionSerializer} and
     * {@link ErrorSerializer} for identifying an {@link Throwable},
     * {@link Exception}, {@link RuntimeException} and {@link Error}
     * respectively by its {@link Class#getTypeName()}.
     * In the serialization this variable is used as the
     * key of a {@link YAPIONVariable} and will be called
     * exception variable.
     */
    public static final String EXCEPTION_IDENTIFIER = "@exception";

    /**
     *
     */
    public static final String KEY_IDENTIFIER = "@key";

    /**
     * This variable is used in the {@link YAPIONPointer#toJSON(AbstractOutput)}
     * implementation and {@link YAPIONPointer#toJSONLossy(AbstractOutput)} for
     * representation of an {@link YAPIONPointer} as a JSON object.
     * The {@link JSONMapper} uses this variable to identify a
     * {@link YAPIONPointer} in the JSON representation.
     */
    public static final String POINTER_IDENTIFIER = "@pointer";

    /**
     * This variable is used in the {@link YAPIONMap#toJSON(AbstractOutput)}
     * implementation and {@link YAPIONMap#toJSONLossy(AbstractOutput)} for
     * representation of an {@link YAPIONMap} as a JSON object.
     * The {@link JSONMapper} uses this variable to identify a
     * {@link YAPIONMap} in the JSON representation.
     */
    public static final String MAP_IDENTIFIER = "@mapping";

    /**
     * This variable is used in the {@link YAPIONValue#toJSON(AbstractOutput)}
     * implementation for representation of the internal type
     * notice into the JSON structure. The {@link YAPIONValue}
     * is wrapped into an JSON object with one key value pair.
     * The key to this value is this identifier and the {@link JSONMapper}
     * will use this variable to identify a specific {@link YAPIONValue}
     * type in the JSON representation.
     */
    public static final String BYTE_IDENTIFIER = "@byte";

    /**
     * This variable is used in the {@link YAPIONValue#toJSON(AbstractOutput)}
     * implementation for representation of the internal type
     * notice into the JSON structure. The {@link YAPIONValue}
     * is wrapped into an JSON object with one key value pair.
     * The key to this value is this identifier and the {@link JSONMapper}
     * will use this variable to identify a specific {@link YAPIONValue}
     * type in the JSON representation.
     */
    public static final String SHORT_IDENTIFIER = "@short";

    /**
     * This variable is used in the {@link YAPIONValue#toJSON(AbstractOutput)}
     * implementation for representation of the internal type
     * notice into the JSON structure. The {@link YAPIONValue}
     * is wrapped into an JSON object with one key value pair.
     * The key to this value is this identifier and the {@link JSONMapper}
     * will use this variable to identify a specific {@link YAPIONValue}
     * type in the JSON representation.
     */
    public static final String CHAR_IDENTIFIER = "@char";

    /**
     * This variable is used in the {@link YAPIONValue#toJSON(AbstractOutput)}
     * implementation for representation of the internal type
     * notice into the JSON structure. The {@link YAPIONValue}
     * is wrapped into an JSON object with one key value pair.
     * The key to this value is this identifier and the {@link JSONMapper}
     * will use this variable to identify a specific {@link YAPIONValue}
     * type in the JSON representation.
     */
    public static final String INT_IDENTIFIER = "@int";

    /**
     * This variable is used in the {@link YAPIONValue#toJSON(AbstractOutput)}
     * implementation for representation of the internal type
     * notice into the JSON structure. The {@link YAPIONValue}
     * is wrapped into an JSON object with one key value pair.
     * The key to this value is this identifier and the {@link JSONMapper}
     * will use this variable to identify a specific {@link YAPIONValue}
     * type in the JSON representation.
     */
    public static final String FLOAT_IDENTIFIER = "@float";

    /**
     * This variable is used in the {@link YAPIONValue#toJSON(AbstractOutput)}
     * implementation for representation of the internal type
     * notice into the JSON structure. The {@link YAPIONValue}
     * is wrapped into an JSON object with one key value pair.
     * The key to this value is this identifier and the {@link JSONMapper}
     * will use this variable to identify a specific {@link YAPIONValue}
     * type in the JSON representation.
     */
    public static final String LONG_IDENTIFIER = "@long";

    /**
     * This variable is used in the {@link YAPIONValue#toJSON(AbstractOutput)}
     * implementation for representation of the internal type
     * notice into the JSON structure. The {@link YAPIONValue}
     * is wrapped into an JSON object with one key value pair.
     * The key to this value is this identifier and the {@link JSONMapper}
     * will use this variable to identify a specific {@link YAPIONValue}
     * type in the JSON representation.
     */
    public static final String DOUBLE_IDENTIFIER = "@double";

    /**
     * This variable is used in the {@link YAPIONValue#toJSON(AbstractOutput)}
     * implementation for representation of the internal type
     * notice into the JSON structure. The {@link YAPIONValue}
     * is wrapped into an JSON object with one key value pair.
     * The key to this value is this identifier and the {@link JSONMapper}
     * will use this variable to identify a specific {@link YAPIONValue}
     * type in the JSON representation.
     */
    public static final String BIG_INTEGER_IDENTIFIER = "@bint";

    /**
     * This variable is used in the {@link YAPIONValue#toJSON(AbstractOutput)}
     * implementation for representation of the internal type
     * notice into the JSON structure. The {@link YAPIONValue}
     * is wrapped into an JSON object with one key value pair.
     * The key to this value is this identifier and the {@link JSONMapper}
     * will use this variable to identify a specific {@link YAPIONValue}
     * type in the JSON representation.
     */
    public static final String BIG_DECIMAL_IDENTIFIER = "@bdecimal";

}