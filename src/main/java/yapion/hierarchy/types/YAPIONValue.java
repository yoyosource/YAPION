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
import yapion.hierarchy.api.ValueTypeConversion;
import yapion.hierarchy.api.groups.YAPIONValueType;
import yapion.hierarchy.output.AbstractOutput;
import yapion.hierarchy.output.StringOutput;
import yapion.hierarchy.output.flavours.Flavour;
import yapion.hierarchy.types.value.NullHandler;
import yapion.hierarchy.types.value.ValueHandler;
import yapion.hierarchy.types.value.ValueHandlerUtils;
import yapion.utils.MethodReturnValue;
import yapion.utils.ReferenceFunction;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class YAPIONValue<T> extends YAPIONValueType<YAPIONValue<T>> implements ValueTypeConversion {

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
        return (getType().getReferenceValue() ^ valueHandler.referenceValue(referenceFunction) ^ referenceFunction.stringToReferenceValue(value != null ? value.toString() : "null")) & 0x7FFFFFFFFFFFFFFFL;
    }

    @Override
    public <T extends AbstractOutput> T output(T abstractOutput, Flavour flavour) {
        flavour = convert(flavour);
        flavour.begin(Flavour.HierarchyTypes.VALUE, abstractOutput);
        flavour.elementValue(Flavour.HierarchyTypes.VALUE, abstractOutput, new Flavour.ValueData<>(this, get()));
        flavour.end(Flavour.HierarchyTypes.VALUE, abstractOutput);
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
        // System.out.println("Parsing " + s);
        for (ValueHandler<?> valueHandler : valueHandlerIterator) {
            MethodReturnValue<?> optional = valueHandler.preParse(s);
            if (optional.isPresent() && !optional.nonNullValuePresent()) {
                return new YAPIONValue(null);
            }
            if (optional.isPresent()) {
                return new YAPIONValue<>(optional.get());
            }
        }
        // System.out.println("Parsing failed");
        for (ValueHandler<?> valueHandler : valueHandlerIterator) {
            MethodReturnValue<?> optional = valueHandler.parse(s);
            if (optional.isPresent()) {
                return new YAPIONValue<>(optional.get());
            }
        }
        // System.out.println("Fallback");
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
    public Optional<YAPIONValue<?>> asValue() {
        return Optional.of(this);
    }

    @Override
    public Optional<String> asString() {
        if (isValidCastType(String.class)) {
            return Optional.of(value != null ? value.toString() : "null");
        } else {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Character> asCharacter() {
        if (isValidCastType(Character.class)) {
            return Optional.of(value != null ? (Character) value : '\0');
        } else {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Boolean> asBoolean() {
        if (isValidCastType(Boolean.class)) {
            return Optional.of(value != null ? (Boolean) value : false);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Byte> asByte() {
        if (isValidCastType(Byte.class)) {
            return Optional.of(value != null ? (Byte) value : (byte) 0);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Short> asShort() {
        if (isValidCastType(Short.class)) {
            return Optional.of(value != null ? (Short) value : (short) 0);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Integer> asInteger() {
        if (isValidCastType(Integer.class)) {
            return Optional.of(value != null ? (Integer) value : 0);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Long> asLong() {
        if (isValidCastType(Long.class)) {
            return Optional.of(value != null ? (Long) value : 0L);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public Optional<BigInteger> asBigInteger() {
        if (isValidCastType(BigInteger.class)) {
            return Optional.of(value != null ? (BigInteger) value : BigInteger.ZERO);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Float> asFloat() {
        if (isValidCastType(Float.class)) {
            return Optional.of(value != null ? (Float) value : 0f);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Double> asDouble() {
        if (isValidCastType(Double.class)) {
            return Optional.of(value != null ? (Double) value : 0d);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public Optional<BigDecimal> asBigDecimal() {
        if (isValidCastType(BigDecimal.class)) {
            return Optional.of(value != null ? (BigDecimal) value : BigDecimal.ZERO);
        } else {
            return Optional.empty();
        }
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
