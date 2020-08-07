// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020 yoyosource

package yapion.hierarchy.types;

import yapion.exceptions.YAPIONException;
import yapion.hierarchy.Type;
import yapion.hierarchy.YAPIONAny;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

import static yapion.utils.ReferenceIDUtils.calc;

public class YAPIONValue<T> extends YAPIONAny {

    private static final String[] allowedTypes = new String[]{
            "java.lang.Boolean",
            "java.lang.Byte", "java.lang.Short", "java.lang.Integer", "java.lang.Long", "java.math.BigInteger",
            "java.lang.Float", "java.lang.Double", "java.math.BigDecimal",
            "java.lang.String", "java.lang.Character"
    };

    private T value;
    String type;

    @Override
    public Type getType() {
        return Type.VALUE;
    }

    @Override
    public long referenceValue() {
        if (value == null) {
            return getType().getReferenceValue() ^ calc(type);
        }
        return getType().getReferenceValue() ^ calc(value.getClass().getTypeName());
    }

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
    public void toOutputStream(OutputStream outputStream) throws IOException {
        outputStream.write(toString().getBytes(StandardCharsets.UTF_8));
    }

    public static YAPIONValue<?> parseValue(String s) {
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
            YAPIONValue<?> value = tryParse(s, 16, ParseType.INTEGER, ParseType.LONG, ParseType.BIGINTEGER);
            if (value != null) return value;
        }
        {
            YAPIONValue<?> value = tryParse(s, 16, NUMBER_HEX, ParseType.BIGINTEGER, ParseType.LONG, ParseType.INTEGER, ParseType.SHORT, ParseType.BYTE);
            if (value != null) return value;
        }
        if (s.matches(NUMBER_NORMAL)) {
            YAPIONValue<?> value = tryParse(s, 10, ParseType.INTEGER, ParseType.LONG, ParseType.BIGINTEGER);
            if (value != null) return value;
        }
        {
            YAPIONValue<?> value = tryParse(s, 10, NUMBER_NORMAL, ParseType.BIGINTEGER, ParseType.LONG, ParseType.INTEGER, ParseType.SHORT, ParseType.BYTE, ParseType.BIGDECIMAL, ParseType.DOUBLE, ParseType.FLOAT);
            if (value != null) return value;
        }
        if (s.matches(NUMBER_FLOAT)) {
            YAPIONValue<?> value = tryParse(s, 10, ParseType.DOUBLE, ParseType.BIGDECIMAL);
            if (value != null) return value;
        }
        {
            YAPIONValue<?> value = tryParse(s, 10, NUMBER_FLOAT, ParseType.BIGDECIMAL, ParseType.DOUBLE, ParseType.FLOAT);
            if (value != null) return value;
        }

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

    @Override
    public String toString() {
        if (value == null) return assembleOutput(type);
        String string = value.toString().replaceAll("[()]", "\\\\$0");
        if (value instanceof String) {
            return assembleString(string);
        }
        if (value instanceof Character) {
            return assembleOutput("'" + string + "'");
        }

        if (value instanceof Integer) {
            return assembleOutput(string);
        }
        if (value instanceof Long) {
            return assembleOutput(string + "L");
        }
        if (value instanceof BigInteger) {
            return assembleOutput(string + "BI");
        }

        if (value instanceof Float) {
            return assembleOutput(string + "F");
        }
        if (value instanceof Double) {
            return assembleOutput(string);
        }
        if (value instanceof BigDecimal) {
            return assembleOutput(string + "BD");
        }
        return assembleOutput(value.toString());
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

    private static YAPIONValue<?> tryParse(String s, int radix, String regex, ParseType... possibleTypes) {
        for (int i = 0; i < possibleTypes.length; i++) {
            if (!s.endsWith(possibleTypes[i].getSuffix())) continue;
            String t = s.substring(0, s.length() - possibleTypes[i].getSuffix().length());
            if (!t.matches(regex)) continue;
            YAPIONValue<?> value = tryParse(t, radix, possibleTypes[i]);
            if (value != null) return value;
        }
        return null;
    }

    private static YAPIONValue<?> tryParse(String s, int radix, ParseType... parseOrder) {
        for (int i = 0; i < parseOrder.length; i++) {
            YAPIONValue<?> value = tryParse(s, radix, parseOrder[i]);
            if (value != null) return value;
        }
        return null;
    }

    private static YAPIONValue<?> tryParse(String s, int radix, ParseType parseType) {
        try {
            switch (parseType) {
                case BYTE: return new YAPIONValue<>(Byte.parseByte(s, radix));
                case SHORT: return new YAPIONValue<>(Short.parseShort(s, radix));
                case INTEGER: return new YAPIONValue<>(Integer.parseInt(s, radix));
                case LONG: return new YAPIONValue<>(Long.parseLong(s, radix));
                case BIGINTEGER: return new YAPIONValue<>(new BigInteger(s));

                case FLOAT: return new YAPIONValue<>(Float.parseFloat(s));
                case DOUBLE: return new YAPIONValue<>(Double.parseDouble(s));
                case BIGDECIMAL: return new YAPIONValue<>(new BigDecimal(s));

                default: break;
            }
        } catch (NumberFormatException e) {}
        return null;
    }

    private static final String NUMBER_NORMAL = "-?[0-9]+";
    private static final String NUMBER_HEX = "-?(0[xX]|#)[0-9A-F]+";
    private static final String NUMBER_FLOAT = "(-?[0-9]+\\.([0-9]+)?)|(-?\\.[0-9]+)";

    private enum ParseType {
        BYTE("B"),
        SHORT("S"),
        INTEGER("I"),
        LONG( "L"),
        BIGINTEGER("BI"),

        FLOAT("F"),
        DOUBLE("D"),
        BIGDECIMAL("BD");

        private String suffix;

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
        return Objects.equals(value, that.value) &&
                Objects.equals(type, that.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, type);
    }

}