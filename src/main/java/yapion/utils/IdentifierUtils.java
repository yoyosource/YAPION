/*
 * Copyright 2019,2020,2021 yoyosource
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package yapion.utils;

import lombok.experimental.UtilityClass;
import yapion.hierarchy.output.AbstractOutput;
import yapion.hierarchy.types.YAPIONMap;
import yapion.hierarchy.types.YAPIONObject;
import yapion.hierarchy.types.YAPIONPointer;
import yapion.hierarchy.types.YAPIONValue;
import yapion.parser.JSONMapper;
import yapion.serializing.YAPIONDeserializer;
import yapion.serializing.YAPIONSerializer;
import yapion.serializing.serializer.object.collection.SetSerializer;
import yapion.serializing.serializer.object.map.MapSerializer;
import yapion.serializing.serializer.object.security.PrivateKeySerializer;
import yapion.serializing.serializer.object.security.PublicKeySerializer;
import yapion.serializing.serializer.object.throwable.ErrorSerializer;
import yapion.serializing.serializer.object.throwable.ThrowableSerializer;
import yapion.serializing.serializer.special.EnumSerializer;

import java.security.PrivateKey;
import java.security.PublicKey;

@UtilityClass
public class IdentifierUtils {

    /**
     * This variable is used in the {@link YAPIONSerializer}
     * and {@link YAPIONDeserializer} for identifying an
     * {@link YAPIONObject} type by its {@link Class#getTypeName()}.
     * In the serialization this variable is used as the
     * key of a the so called type variable.
     */
    public static final String TYPE_IDENTIFIER = "@type";

    /**
     * This variable is used in the {@link YAPIONSerializer}
     * and {@link YAPIONDeserializer} for identifying an
     * {@link YAPIONObject} type by its {@link Class#getTypeName()}.
     * In the serialization this variable is used as the
     * key of a the so called type variable.
     */
    public static final String CLASS_IDENTIFIER = "@class";

    /**
     * This variable is used in the {@link EnumSerializer}
     * for identifying an {@link Enum} by its {@link Class#getTypeName()}.
     * In the serialization this variable is used as the
     * key of a so called enum variable.
     */
    public static final String ENUM_IDENTIFIER = "@enum";

    /**
     * This variable is used anywhere you need an {@link Enum} as type.
     * Currently this is only used by {@link SetSerializer} and
     * {@link MapSerializer}. The value of this is the
     * {@link Class#getTypeName()} of the {@link Enum} in question.
     */
    public static final String ENUM_TYPE_IDENTIFIER = "@enum-type";

    /**
     * This variable is used in the {@link ThrowableSerializer} and
     * {@link ErrorSerializer} for identifying an {@link Throwable},
     * {@link Exception}, {@link RuntimeException} and {@link Error}
     * respectively by its {@link Class#getTypeName()}.
     * In the serialization this variable is used as the
     * key of a so called exception variable.
     */
    public static final String EXCEPTION_IDENTIFIER = "@exception";

    /**
     * This variable is used in the {@link PrivateKeySerializer} and
     * {@link PublicKeySerializer} for identifying an {@link PublicKey}
     * and {@link PrivateKey} respectively by its {@link Class#getTypeName()}.
     * In the serialization this variable is used as the key of a so called
     * key variable.
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
