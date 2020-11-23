// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020 yoyosource

package yapion.hierarchy.types;

import yapion.annotations.deserialize.YAPIONLoad;
import yapion.annotations.serialize.YAPIONSave;
import yapion.exceptions.YAPIONException;
import yapion.hierarchy.output.AbstractOutput;
import yapion.hierarchy.output.StringOutput;
import yapion.hierarchy.typegroups.YAPIONAnyType;
import yapion.hierarchy.typegroups.YAPIONValueType;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static yapion.utils.IdentifierUtils.*;
import static yapion.utils.ReferenceIDUtils.calc;

@YAPIONSave(context = "*")
@YAPIONLoad(context = "*")
public class YAPIONValue<T> extends YAPIONValueType {

    private static final String[] allowedTypes = new String[]{
            "java.lang.Boolean",
            "java.lang.Byte", "java.lang.Short", "java.lang.Integer", "java.lang.Long", "java.math.BigInteger",
            "java.lang.Float", "java.lang.Double", "java.math.BigDecimal",
            "java.lang.String", "java.lang.Character"
    };
    private static final Map<String, String> typeIdentifier = new HashMap<>();

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
    private final String type;

    public YAPIONValue(T value) {
        if (!validType(value)) {
            throw new YAPIONException("Invalid YAPIONValue type " + value.getClass().getTypeName() + " only " + String.join(", ", allowedTypes) + " allowed");
        }
        this.value = value;
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
    public long referenceValue() {
        if (!hasReferenceValue()) {
            cacheReferenceValue(getType().getReferenceValue() ^ calc(type));
        }
        return getReferenceValue();
    }

    @Override
    public YAPIONAnyType copy() {
        return new YAPIONValue<>(value);
    }

    void toStrippedYAPION(AbstractOutput abstractOutput) {
        if (value == null) {
            abstractOutput.consume(type);
            return;
        }
        String string = value.toString().replaceAll("[()]", "\\\\$0");
        if (value instanceof String) {
            abstractOutput.consume(string);
            return;
        }
        if (value instanceof Character) {
            abstractOutput.consume("'" + string + "'");
            return;
        }

        if (value instanceof Byte) {
            abstractOutput.consume(string + "B");
            return;
        }
        if (value instanceof Short) {
            abstractOutput.consume(string + "S");
            return;
        }
        if (value instanceof Integer) {
            abstractOutput.consume(string);
            return;
        }
        if (value instanceof Long) {
            abstractOutput.consume(string + "L");
            return;
        }
        if (value instanceof BigInteger) {
            abstractOutput.consume(string + "BI");
            return;
        }

        if (value instanceof Float) {
            abstractOutput.consume(string + "F");
            return;
        }
        if (value instanceof Double) {
            abstractOutput.consume(string + "D");
            return;
        }
        if (value instanceof BigDecimal) {
            abstractOutput.consume(string + "BD");
            return;
        }
        abstractOutput.consume(value.toString());
    }

    @Override
    public <T extends AbstractOutput> T toYAPION(T abstractOutput) {
        if (value == null) {
            abstractOutput.consume(assembleOutput(type));
            return abstractOutput;
        }
        String string = value.toString().replaceAll("[()]", "\\\\$0");
        if (value instanceof String) {
            abstractOutput.consume(assembleString(string));
            return abstractOutput;
        }
        if (value instanceof Character) {
            abstractOutput.consume(assembleOutput("'" + string + "'"));
            return abstractOutput;
        }

        if (value instanceof Byte) {
            abstractOutput.consume(assembleOutput(string + "B"));
            return abstractOutput;
        }
        if (value instanceof Short) {
            abstractOutput.consume(assembleOutput(string + "S"));
            return abstractOutput;
        }
        if (value instanceof Integer) {
            abstractOutput.consume(assembleOutput(string));
            return abstractOutput;
        }
        if (value instanceof Long) {
            abstractOutput.consume(assembleOutput(string + "L"));
            return abstractOutput;
        }
        if (value instanceof BigInteger) {
            abstractOutput.consume(assembleOutput(string + "BI"));
            return abstractOutput;
        }

        if (value instanceof Float) {
            abstractOutput.consume(assembleOutput(string + "F"));
            return abstractOutput;
        }
        if (value instanceof Double) {
            abstractOutput.consume(assembleOutput(string));
            return abstractOutput;
        }
        if (value instanceof BigDecimal) {
            abstractOutput.consume(assembleOutput(string + "BD"));
            return abstractOutput;
        }
        abstractOutput.consume(assembleOutput(value.toString()));
        return abstractOutput;
    }

    @Override
    public <T extends AbstractOutput> T toYAPIONPrettified(T abstractOutput) {
        return toYAPION(abstractOutput);
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

    @SuppressWarnings({"java:S3740"})
    public static YAPIONValue parseValue(String s) {
        if (s.equals("true") || s.equals("false")) {
            return new YAPIONValue<>(s.equals("true"));
        }
        if (s.equals("null")) {
            return new YAPIONValue<>(null);
        }
        if (s.startsWith("\"") && s.endsWith("\"")) {
            return new YAPIONValue<>(s.substring(1, s.length() - 1));
        }
        if (s.startsWith("'") && s.endsWith("'")) {
            if (s.length() == 3) {
                return new YAPIONValue<>(s.charAt(1));
            } else {
                return new YAPIONValue<>(s.substring(1, s.length() - 1));
            }
        }

        if (s.matches(NUMBER_HEX)) {
            YAPIONValue value = tryParse(s, 16, ParseType.INTEGER, ParseType.LONG, ParseType.BIGINTEGER);
            if (value != null) return value;
        }
        YAPIONValue value1 = tryParse(s, 16, NUMBER_HEX, ParseType.BIGINTEGER, ParseType.LONG, ParseType.INTEGER, ParseType.SHORT, ParseType.BYTE);
        if (value1 != null) return value1;

        if (s.matches(NUMBER_NORMAL)) {
            YAPIONValue value = tryParse(s, 10, ParseType.INTEGER, ParseType.LONG, ParseType.BIGINTEGER);
            if (value != null) return value;
        }
        YAPIONValue value2 = tryParse(s, 10, NUMBER_NORMAL, ParseType.BIGINTEGER, ParseType.LONG, ParseType.INTEGER, ParseType.SHORT, ParseType.BYTE, ParseType.BIGDECIMAL, ParseType.DOUBLE, ParseType.FLOAT);
        if (value2 != null) return value2;

        if (s.matches(NUMBER_FLOAT)) {
            YAPIONValue value = tryParse(s, 10, ParseType.DOUBLE, ParseType.BIGDECIMAL);
            if (value != null) return value;
        }
        YAPIONValue value3 = tryParse(s, 10, NUMBER_FLOAT, ParseType.BIGDECIMAL, ParseType.DOUBLE, ParseType.FLOAT);
        if (value3 != null) return value3;

        return new YAPIONValue<>(s);
    }

    static <T> boolean validType(T t) {
        if (t == null) return true;
        String typeName = t.getClass().getTypeName();
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

    private String assembleOutput(String s) {
        return "(" + s + ")";
    }

    private String assembleString(String s) {
        if (s.equals("true") || s.equals("false")) {
            return assembleOutput('"' + s + '"');
        }
        if (s.equals("null")) {
            return assembleOutput('"' + s + '"');
        }

        if (s.matches(NUMBER_HEX)) {
            return assembleOutput('"' + s + '"');
        }
        if (s.matches(NUMBER_NORMAL)) {
            return assembleOutput('"' + s + '"');
        }
        if (s.matches(NUMBER_FLOAT)) {
            return assembleOutput('"' + s + '"');
        }

        if (tryAssemble(s, NUMBER_HEX, ParseType.BYTE, ParseType.SHORT, ParseType.INTEGER, ParseType.LONG, ParseType.BIGINTEGER)) {
            return assembleOutput('"' + s + '"');
        }
        if (tryAssemble(s, NUMBER_NORMAL, ParseType.BYTE, ParseType.SHORT, ParseType.INTEGER, ParseType.LONG, ParseType.BIGINTEGER)) {
            return assembleOutput('"' + s + '"');
        }
        if (tryAssemble(s, NUMBER_FLOAT, ParseType.FLOAT, ParseType.DOUBLE, ParseType.BIGDECIMAL)) {
            return assembleOutput('"' + s + '"');
        }

        return assembleOutput(s);
    }

    public static boolean tryAssemble(String s, String regex, ParseType... possibleTypes) {
        for (int i = 0; i < possibleTypes.length; i++) {
            if (!s.endsWith(possibleTypes[i].getSuffix())) continue;
            String t = s.substring(0, s.length() - possibleTypes[i].getSuffix().length());
            if (!t.matches(regex)) continue;
            return true;
        }
        return false;
    }

    @SuppressWarnings({"java:S3740"})
    private static YAPIONValue tryParse(String s, int radix, String regex, ParseType... possibleTypes) {
        for (int i = 0; i < possibleTypes.length; i++) {
            if (!s.endsWith(possibleTypes[i].getSuffix())) continue;
            String t = s.substring(0, s.length() - possibleTypes[i].getSuffix().length());
            if (!t.matches(regex)) continue;
            YAPIONValue value = tryParse(t, radix, possibleTypes[i]);
            if (value != null) return value;
        }
        return null;
    }

    @SuppressWarnings({"java:S3740"})
    private static YAPIONValue tryParse(String s, int radix, ParseType... parseOrder) {
        for (int i = 0; i < parseOrder.length; i++) {
            YAPIONValue value = tryParse(s, radix, parseOrder[i]);
            if (value != null) return value;
        }
        return null;
    }

    @SuppressWarnings({"java:S3740"})
    private static YAPIONValue tryParse(String s, int radix, ParseType parseType) {
        try {
            switch (parseType) {
                case BYTE:
                    return new YAPIONValue<>(Byte.parseByte(s, radix));
                case SHORT:
                    return new YAPIONValue<>(Short.parseShort(s, radix));
                case INTEGER:
                    return new YAPIONValue<>(Integer.parseInt(s, radix));
                case LONG:
                    return new YAPIONValue<>(Long.parseLong(s, radix));
                case BIGINTEGER:
                    return new YAPIONValue<>(new BigInteger(s));

                case FLOAT:
                    return new YAPIONValue<>(Float.parseFloat(s));
                case DOUBLE:
                    return new YAPIONValue<>(Double.parseDouble(s));
                case BIGDECIMAL:
                    return new YAPIONValue<>(new BigDecimal(s));

                default:
                    break;
            }
        } catch (NumberFormatException e) {
            // Ignored
        }
        return null;
    }

    private static final String NUMBER_NORMAL = "-?[0-9]+";
    private static final String NUMBER_HEX = "-?(0[xX]|#)[0-9A-F]+";
    private static final String NUMBER_FLOAT = "(-?[0-9]+\\.([0-9]+)?)|(-?\\.[0-9]+)";

    private enum ParseType {
        BYTE("B"),
        SHORT("S"),
        INTEGER("I"),
        LONG("L"),
        BIGINTEGER("BI"),

        FLOAT("F"),
        DOUBLE("D"),
        BIGDECIMAL("BD");

        private final String suffix;

        ParseType(String suffix) {
            this.suffix = suffix;
        }

        public String getSuffix() {
            return suffix;
        }
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