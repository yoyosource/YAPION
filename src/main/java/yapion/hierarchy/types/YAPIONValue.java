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

import yapion.annotations.deserialize.YAPIONLoad;
import yapion.annotations.serialize.YAPIONSave;
import yapion.exceptions.YAPIONException;
import yapion.hierarchy.api.groups.YAPIONAnyType;
import yapion.hierarchy.api.groups.YAPIONValueType;
import yapion.hierarchy.output.AbstractOutput;
import yapion.hierarchy.output.StringOutput;
import yapion.hierarchy.types.value.*;
import yapion.utils.MethodReturnValue;
import yapion.utils.ReferenceFunction;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

import static yapion.hierarchy.types.value.FractionNumberHandler.*;
import static yapion.hierarchy.types.value.WholeNumberHandler.*;
import static yapion.utils.IdentifierUtils.*;

@YAPIONSave(context = "*")
@YAPIONLoad(context = "*")
public class YAPIONValue<T> extends YAPIONValueType {

    private static final LinkedHashMap<String, ValueHandler<?>> valueHandlers = new LinkedHashMap<>();

    private static final String[] allowedTypes = new String[] {
            "java.lang.Boolean",
            "java.lang.Byte", "java.lang.Short", "java.lang.Integer", "java.lang.Long", "java.math.BigInteger",
            "java.lang.Float", "java.lang.Double", "java.math.BigDecimal",
            "java.lang.String", "java.lang.Character"
    };
    private static final Map<String, String> typeIdentifier = new HashMap<>();

    static {
        valueHandlers.put("java.lang.Boolean", new BooleanHandler());
        valueHandlers.put(null, new NullHandler());

        valueHandlers.put("java.lang.Byte", new ByteHandler());
        valueHandlers.put("java.lang.Short", new ShortHandler());
        valueHandlers.put("java.lang.Integer", new IntegerHandler());
        valueHandlers.put("java.lang.Long", new LongHandler());
        valueHandlers.put("java.math.BigInteger", new BigIntegerHandler());

        valueHandlers.put("java.lang.Float", new FloatHandler());
        valueHandlers.put("java.lang.Double", new DoubleHandler());
        valueHandlers.put("java.math.BigDecimal", new BigDecimalHandler());

        valueHandlers.put("java.lang.Character", new CharacterHandler());
        valueHandlers.put("java.lang.String", new StringHandler());

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
        this.valueHandler = (ValueHandler<T>) valueHandlers.get(value == null ? null : value.getClass().getTypeName());

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
        return getType().getReferenceValue() ^ valueHandler.referenceValue(referenceFunction);
    }

    void toStrippedYAPION(AbstractOutput abstractOutput) {
        abstractOutput.consume(valueHandler.output(value));
    }

    @Override
    public <T extends AbstractOutput> T toYAPION(T abstractOutput) {
        abstractOutput.consume("(" + valueHandler.output(value) + ")");
        return abstractOutput;
    }

    @Override
    public <T extends AbstractOutput> T toJSON(T abstractOutput) {
        if (value instanceof String) {
            abstractOutput.consume("\"")
                    .consume(value.toString().replace("\"", "\\\"").replace("\n", "\\n").replace("\r", "\\r").replace("\t", "\\t"))
                    .consume("\"");
            return abstractOutput;
        }
        if (value instanceof Character) {
            YAPIONObject yapionObject = new YAPIONObject();
            yapionObject.add(typeIdentifier.get(type), value.toString().replace("\"", "\\\"").replace("\n", "\\n").replace("\r", "\\r").replace("\t", "\\t"));
            yapionObject.toJSONLossy(abstractOutput);
            return abstractOutput;
        }
        if (value instanceof BigInteger || value instanceof BigDecimal) {
            YAPIONObject yapionObject = new YAPIONObject();
            yapionObject.add(typeIdentifier.get(type), value.toString());
            yapionObject.toJSONLossy(abstractOutput);
            return abstractOutput;
        }
        if (value == null) {
            abstractOutput.consume("null");
            return abstractOutput;
        }
        if (typeIdentifier.containsKey(type)) {
            YAPIONObject yapionObject = new YAPIONObject();
            for (int i = 0; i < getDepth(); i++) {
                YAPIONObject object = new YAPIONObject();
                yapionObject.add("", object);
                yapionObject = object;
            }
            yapionObject.add(typeIdentifier.get(type), this);
            yapionObject.toJSONLossy(abstractOutput);
            return abstractOutput;
        }
        abstractOutput.consume(value.toString());
        return abstractOutput;
    }

    @Override
    public <T extends AbstractOutput> T toJSONLossy(T abstractOutput) {
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
        abstractOutput.consume(value.toString());
        return abstractOutput;
    }

    @SuppressWarnings({"java:S3740", "java:S2789"})
    public static YAPIONValue parseValue(String s) {
        for (Map.Entry<String, ValueHandler<?>> valueHandlerEntry : valueHandlers.entrySet()) {
            MethodReturnValue<?> optional = valueHandlerEntry.getValue().preParse(s);
            if (optional.isPresent() && !optional.nonNullValuePresent()) {
                return new YAPIONValue(null);
            }
            if (optional.isPresent()) {
                return new YAPIONValue<>(optional.get());
            }
        }
        for (Map.Entry<String, ValueHandler<?>> valueHandlerEntry : valueHandlers.entrySet()) {
            MethodReturnValue<?> optional = valueHandlerEntry.getValue().parse(s);
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
        for (int i = 0; i < allowedTypes.length; i++) {
            if (allowedTypes[i].endsWith(typeName)) {
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

}
