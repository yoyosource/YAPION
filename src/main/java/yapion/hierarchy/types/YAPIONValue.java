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

package yapion.hierarchy.types;

import yapion.exceptions.YAPIONException;
import yapion.hierarchy.api.groups.YAPIONValueType;
import yapion.hierarchy.output.AbstractOutput;
import yapion.hierarchy.output.StringOutput;
import yapion.hierarchy.types.value.*;
import yapion.hierarchy.types.value.HexNumberHandler.ByteHexHandler;
import yapion.hierarchy.types.value.HexNumberHandler.IntegerHexHandler;
import yapion.hierarchy.types.value.HexNumberHandler.LongHexHandler;
import yapion.hierarchy.types.value.HexNumberHandler.ShortHexHandler;
import yapion.utils.MethodReturnValue;
import yapion.utils.ReferenceFunction;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

import static yapion.hierarchy.types.value.FractionNumberHandler.*;
import static yapion.hierarchy.types.value.WholeNumberHandler.*;
import static yapion.utils.IdentifierUtils.*;

public class YAPIONValue<T> extends YAPIONValueType {

    private static final String[] allowedTypes = new String[] {
            "java.lang.Boolean",
            "java.lang.Byte", "java.lang.Short", "java.lang.Integer", "java.lang.Long", "java.math.BigInteger",
            "java.lang.Float", "java.lang.Double", "java.math.BigDecimal",
            "java.lang.String", "java.lang.Character"
    };
    private static final Map<String, String> typeIdentifier = new HashMap<>();

    public static List<ValueHandler<?>> allValueHandlers() {
        return ValueHandlerUtils.allValueHandlers();
    }

    static {
        typeIdentifier.put(allowedTypes[1], BYTE_IDENTIFIER);
        typeIdentifier.put(allowedTypes[2], SHORT_IDENTIFIER);
        typeIdentifier.put(allowedTypes[3], INT_IDENTIFIER);
        typeIdentifier.put(allowedTypes[4], LONG_IDENTIFIER);
        typeIdentifier.put(allowedTypes[5], BIG_INTEGER_IDENTIFIER);
        typeIdentifier.put(allowedTypes[6], FLOAT_IDENTIFIER);
        typeIdentifier.put(allowedTypes[7], DOUBLE_IDENTIFIER);
        typeIdentifier.put(allowedTypes[8], BIG_DECIMAL_IDENTIFIER);
        typeIdentifier.put(allowedTypes[10], CHAR_IDENTIFIER);
    }

    private final T value;
    private final ValueHandler<T> valueHandler;
    private final String type;

    public YAPIONValue(T value) {
        if (!validType(value)) {
            throw new YAPIONException("Invalid YAPIONValue type " + value.getClass().getTypeName() + " only " + String.join(", ", allowedTypes) + " allowed");
        }
        this.value = value;
        this.valueHandler = (ValueHandler<T>) ValueHandlerUtils.get(value == null ? null : value.getClass().getTypeName());

        if (value == null) {
            this.type = "null";
        } else {
            this.type = value.getClass().getTypeName();
        }
    }

    @Override
    public YAPIONType getType() {
        return YAPIONType.VALUE;
    }

    @Override
    protected long referenceValueProvider(ReferenceFunction referenceFunction) {
        return getType().getReferenceValue() ^ valueHandler.referenceValue(referenceFunction) & 0x7FFFFFFFFFFFFFFFL;
    }

    void toStrippedYAPION(AbstractOutput abstractOutput) {
        String string = valueHandler.output(value).replace(",", "\\,");
        if (string.startsWith(" ") || string.startsWith("-")) {
            string = "\\" + string;
        }
        string = ValueUtils.stringToUTFEscapedString(string, ValueUtils.EscapeCharacters.VALUE);
        if (ValueUtils.startsWith(string, ValueUtils.EscapeCharacters.KEY) || string.isEmpty()) {
            abstractOutput.consume("(").consume(string).consume(")");
        } else {
            abstractOutput.consume(string);
        }
    }

    @Override
    public <T extends AbstractOutput> T toYAPION(T abstractOutput) {
        abstractOutput.consume("(" + valueHandler.output(value) + ")");
        return abstractOutput;
    }

    @Override
    public <T extends AbstractOutput> T toJSON(T abstractOutput) {
        if (value == null) {
            abstractOutput.consume("null");
            return abstractOutput;
        }
        if (value instanceof String) {
            abstractOutput.consume("\"")
                    .consume(value.toString().replace("\"", "\\\"").replace("\n", "\\n").replace("\r", "\\r").replace("\t", "\\t"))
                    .consume("\"");
            return abstractOutput;
        }
        if (typeIdentifier.containsKey(type) || value instanceof Character) {
            abstractOutput.consume("{\"").consume(typeIdentifier.get(type)).consume("\":");
            toJSONLossy(abstractOutput, true);
            abstractOutput.consume("}");
            return abstractOutput;
        }
        abstractOutput.consume(value.toString());
        return abstractOutput;
    }

    @Override
    public <T extends AbstractOutput> T toJSONLossy(T abstractOutput) {
        return toJSONLossy(abstractOutput, false);
    }

    private  <T extends AbstractOutput> T toJSONLossy(T abstractOutput, boolean bigAsStrings) {
        if (value instanceof String || value instanceof Character) {
            abstractOutput.consume("\"")
                    .consume(value.toString().replace("\"", "\\\"").replace("\n", "\\n").replace("\r", "\\r").replace("\t", "\\t"))
                    .consume("\"");
            return abstractOutput;
        }
        if (value == null) {
            abstractOutput.consume("null");
            return abstractOutput;
        }
        if (bigAsStrings && (value instanceof BigInteger || value instanceof BigDecimal)) {
            abstractOutput.consume("\"").consume(value.toString()).consume("\"");
            return abstractOutput;
        }
        abstractOutput.consume(value.toString());
        return abstractOutput;
    }

    @SuppressWarnings({"java:S3740", "java:S2789"})
    public static YAPIONValue parseValue(String s) {
        return parseValue(s, ValueHandlerUtils.allValueHandlers());
    }

    @SuppressWarnings({"java:S3740", "java:S2789"})
    public static YAPIONValue parseValue(String s, Iterable<ValueHandler<?>> valueHandlerIterator) {
        for (ValueHandler<?> valueHandler : valueHandlerIterator) {
            MethodReturnValue<?> optional = valueHandler.preParse(s);
            if (optional.isPresent() && !optional.nonNullValuePresent()) {
                return new YAPIONValue(null);
            }
            if (optional.isPresent()) {
                return new YAPIONValue<>(optional.get());
            }
        }
        for (ValueHandler<?> valueHandler : valueHandlerIterator) {
            MethodReturnValue<?> optional = valueHandler.parse(s);
            if (optional.isPresent()) {
                return new YAPIONValue<>(optional.get());
            }
        }
        return new YAPIONValue<>(s);
    }

    public static <T> boolean validType(T t) {
        if (t == null) return true;
        return validType(t.getClass());
    }

    public static boolean validType(Class<?> t) {
        if (t == null) return true;
        String typeName = t.getTypeName();
        for (String allowedType : allowedTypes) {
            if (allowedType.equals(typeName)) {
                return true;
            }
        }
        return false;
    }

    public T get() {
        return value;
    }

    public String getValueType() {
        return type;
    }

    public boolean isValidCastType(String type) {
        return this.type.equalsIgnoreCase(type) || this.type.equalsIgnoreCase("null");
    }

    @Override
    public String toString() {
        StringOutput stringOutput = new StringOutput();
        toYAPION(stringOutput);
        return stringOutput.getResult();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof YAPIONValue)) return false;
        YAPIONValue<?> that = (YAPIONValue<?>) o;
        return type.equals(that.type) && Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, type);
    }

    public YAPIONValue<T> copy() {
        return (YAPIONValue<T>) internalCopy();
    }
}
