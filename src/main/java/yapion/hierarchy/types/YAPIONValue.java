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

import lombok.Getter;
import yapion.exceptions.YAPIONException;
import yapion.hierarchy.api.groups.YAPIONValueType;
import yapion.hierarchy.output.AbstractOutput;
import yapion.hierarchy.output.StringOutput;
import yapion.hierarchy.output.flavours.Flavour;
import yapion.hierarchy.types.value.NullHandler;
import yapion.hierarchy.types.value.ValueHandler;
import yapion.hierarchy.types.value.ValueHandlerUtils;
import yapion.hierarchy.types.value.ValueUtils;
import yapion.utils.MethodReturnValue;
import yapion.utils.ReferenceFunction;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Objects;

public class YAPIONValue<T> extends YAPIONValueType<YAPIONValue<T>> {

    public static List<ValueHandler<?>> allValueHandlers() {
        return ValueHandlerUtils.allValueHandlers();
    }

    private T value;

    @Getter
    private final ValueHandler<T> valueHandler;
    private final String type;

    public YAPIONValue(T value) {
        if (!validType(value)) {
            throw new YAPIONException("Invalid YAPIONValue type " + value.getClass().getTypeName() + " only " + String.join(", ", ValueHandlerUtils.allowedTypesArray()) + " allowed");
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
        String string = valueHandler.output(value, YAPIONType.ARRAY);
        if (string.startsWith(" ") || string.startsWith("-")) {
            string = "\\" + string;
        }
        if (ValueUtils.startsWith(string, ValueUtils.EscapeCharacters.KEY) || string.isEmpty()) {
            abstractOutput.consume("(").consume(string).consume(")");
        } else {
            abstractOutput.consume(string);
        }
    }

    @Override
    public <T extends AbstractOutput> T output(T abstractOutput, Flavour flavour) {
        abstractOutput.consume(flavour.beginValue());
        abstractOutput.consume(flavour.value(this));
        abstractOutput.consume(flavour.endValue());
        return abstractOutput;
    }

    @Override
    public <T extends AbstractOutput> T toJSON(T abstractOutput) {
        if (value == null) {
            abstractOutput.consume("null");
            return abstractOutput;
        }
        if (value instanceof String s) {
            abstractOutput.consume("\"")
                    .consume(s.replace("\"", "\\\"").replace("\n", "\\n").replace("\r", "\\r").replace("\t", "\\t"))
                    .consume("\"");
            return abstractOutput;
        }
        if (ValueHandlerUtils.containsTypeIdentifier(type) || value instanceof Character) {
            abstractOutput.consume("{\"").consume(ValueHandlerUtils.getTypeIdentifier(type)).consume("\":");
            toJSONLossy(abstractOutput, true);
            abstractOutput.consume("}");
            return abstractOutput;
        }
        abstractOutput.consume(value.toString());
        return abstractOutput;
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

    @Override
    public T unwrap() {
        return value;
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
        return t == null || ValueHandlerUtils.allowedType(t.getTypeName());
    }

    public T get() {
        return value;
    }

    public YAPIONValue<T> set(T value) {
        if (!validType(value)) {
            throw new IllegalArgumentException("Invalid type: " + value.getClass().getTypeName());
        }

        if (value == null) {
            if (valueHandler.getClass() == NullHandler.class) {
                this.value = null;
            } else {
                throw new IllegalArgumentException("Invalid type: null");
            }
        } else {
            if (!value.getClass().getTypeName().equals(type)) {
                throw new IllegalArgumentException("Invalid type: " + value.getClass().getTypeName());
            }
            this.value = value;
        }
        return this;
    }

    public String getValueType() {
        return type;
    }

    public boolean isValidCastType(Class<?> type) {
        if (type == null) return true;
        return isValidCastType(type.getTypeName());
    }

    public boolean isValidCastType(String type) {
        return this.type.equalsIgnoreCase(type) || this.type.equalsIgnoreCase("null");
    }

    @Override
    public YAPIONValue<T> itself() {
        return this;
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
