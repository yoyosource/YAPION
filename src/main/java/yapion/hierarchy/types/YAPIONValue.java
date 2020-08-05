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
        return getType().getReferenceValue() ^ calc(value.getClass().getTypeName());
    }

    public YAPIONValue(T value) {
        if (!validType(value)) {
            throw new YAPIONException("Invalid YAPIONValue type " + value.getClass().getTypeName() + " only " + String.join(", ", allowedTypes) + " allowed");
        }
        this.value = value;
        this.type = value.getClass().getTypeName();
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

        if (s.matches("-?(0[xX]|#)[0-9A-F]+")) {
            YAPIONValue<?> value = tryParse(s, 16, ParseType.INTEGER, ParseType.LONG, ParseType.BYTE, ParseType.SHORT, ParseType.BIGINTEGER);;
            if (value != null) {
                return value;
            }
        }
        if (s.matches("-?(0[xX]|#)[0-9A-F]+B")) {
            try {
                return new YAPIONValue<>(Byte.parseByte(s, 16));
            } catch (NumberFormatException e) {}
        }
        if (s.matches("-?(0[xX]|#)[0-9A-F]+S")) {
            try {
                return new YAPIONValue<>(Short.parseShort(s, 16));
            } catch (NumberFormatException e) {}
        }
        if (s.matches("-?(0[xX]|#)[0-9A-F]+I")) {
            try {
                return new YAPIONValue<>(Integer.parseInt(s.substring(0, s.length() - 1), 16));
            } catch (NumberFormatException e) {}
        }
        if (s.matches("-?(0[xX]|#)[0-9A-F]+L")) {
            try {
                return new YAPIONValue<>(Long.parseLong(s.substring(0, s.length() - 1), 16));
            } catch (NumberFormatException e) {}
        }
        if (s.matches("-?(0[xX]|#)[0-9A-F]+BI")) {
            return new YAPIONValue<>(new BigInteger(s.substring(0, s.length() - 2), 16));
        }

        if (s.matches("-?[0-9]+")) {
            try {
                return new YAPIONValue<>(Byte.parseByte(s));
            } catch (NumberFormatException e) {}
            try {
                return new YAPIONValue<>(Short.parseShort(s));
            } catch (NumberFormatException e) {}
            try {
                return new YAPIONValue<>(Integer.parseInt(s));
            } catch (NumberFormatException e) {}
            try {
                return new YAPIONValue<>(Long.parseLong(s));
            } catch (NumberFormatException e) {}
            return new YAPIONValue<>(new BigInteger(s));
        }
        if (s.matches("-?[0-9]+I")) {
            try {
                return new YAPIONValue<>(Integer.parseInt(s.substring(0, s.length() - 1)));
            } catch (NumberFormatException e) {}
        }
        if (s.matches("-?[0-9]+L")) {
            try {
                return new YAPIONValue<>(Long.parseLong(s.substring(0, s.length() - 1)));
            } catch (NumberFormatException e) {}
        }
        if (s.matches("-?[0-9]+BI")) {
            return new YAPIONValue<>(new BigInteger(s.substring(0, s.length() - 2)));
        }
        if (s.matches("-?[0-9]+F")) {
            try {
                return new YAPIONValue<>(Float.parseFloat(s.substring(0, s.length() - 1)));
            } catch (NumberFormatException e) {}
        }
        if (s.matches("-?[0-9]+D")) {
            try {
                return new YAPIONValue<>(Double.parseDouble(s.substring(0, s.length() - 1)));
            } catch (NumberFormatException e) {}
        }
        if (s.matches("-?[0-9]+BD")) {
            return new YAPIONValue<>(new BigDecimal(s.substring(0, s.length() - 2)));
        }

        if (s.matches("-?[0-9]+\\.([0-9]+)?")) {
            try {
                return new YAPIONValue<>(Double.parseDouble(s));
            } catch (NumberFormatException e) {}
            return new YAPIONValue<>(new BigDecimal(s));
        }
        if (s.matches("-?[0-9]+\\.([0-9]+)?F")) {
            try {
                return new YAPIONValue<>(Float.parseFloat(s.substring(0, s.length() - 1)));
            } catch (NumberFormatException e) {}
        }
        if (s.matches("-?[0-9]+\\.([0-9]+)?D")) {
            try {
                return new YAPIONValue<>(Double.parseDouble(s.substring(0, s.length() - 1)));
            } catch (NumberFormatException e) {}
        }
        if (s.matches("-?[0-9]+\\.([0-9]+)?BD")) {
            return new YAPIONValue<>(new BigDecimal(s.substring(0, s.length() - 2)));
        }

        if (s.matches("\\.[0-9]+")) {
            try {
                return new YAPIONValue<>(Float.parseFloat(s));
            } catch (NumberFormatException e) {}
            try {
                return new YAPIONValue<>(Double.parseDouble(s));
            } catch (NumberFormatException e) {}
            return new YAPIONValue<>(new BigDecimal(s));
        }
        if (s.matches("\\.[0-9]+F")) {
            try {
                return new YAPIONValue<>(Float.parseFloat(s.substring(0, s.length() - 1)));
            } catch (NumberFormatException e) {}
        }
        if (s.matches("\\.[0-9]+D")) {
            try {
                return new YAPIONValue<>(Double.parseDouble(s.substring(0, s.length() - 1)));
            } catch (NumberFormatException e) {}
        }
        if (s.matches("\\.[0-9]+BD")) {
            return new YAPIONValue<>(new BigDecimal(s.substring(0, s.length() - 2)));
        }

        return new YAPIONValue<>(s);
    }

    static <T> boolean validType(T t) {
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

        if (s.matches("-?(0[xX]|#)[0-9A-F]+")) {
            return assembleOutput('"' + s + '"');
        }
        if (s.matches("-?(0[xX]|#)[0-9A-F]+I")) {
            return assembleOutput('"' + s + '"');
        }
        if (s.matches("-?(0[xX]|#)[0-9A-F]+L")) {
            return assembleOutput('"' + s + '"');
        }
        if (s.matches("-?(0[xX]|#)[0-9A-F]+BI")) {
            return assembleOutput('"' + s + '"');
        }

        if (s.matches("-?[0-9]+")) {
            return assembleOutput('"' + s + '"');
        }
        if (s.matches("-?[0-9]+I")) {
            return assembleOutput('"' + s + '"');
        }
        if (s.matches("-?[0-9]+L")) {
            return assembleOutput('"' + s + '"');
        }
        if (s.matches("-?[0-9]+BI")) {
            return assembleOutput('"' + s + '"');
        }
        if (s.matches("-?[0-9]+F")) {
            return assembleOutput('"' + s + '"');
        }
        if (s.matches("-?[0-9]+D")) {
            return assembleOutput('"' + s + '"');
        }
        if (s.matches("-?[0-9]+BD")) {
            return assembleOutput('"' + s + '"');
        }

        if (s.matches("-?[0-9]+\\.([0-9]+)?")) {
            return assembleOutput('"' + s + '"');
        }
        if (s.matches("-?[0-9]+\\.([0-9]+)?F")) {
            return assembleOutput('"' + s + '"');
        }
        if (s.matches("-?[0-9]+\\.([0-9]+)?D")) {
            return assembleOutput('"' + s + '"');
        }
        if (s.matches("-?[0-9]+\\.([0-9]+)?BD")) {
            return assembleOutput('"' + s + '"');
        }

        if (s.matches("\\.[0-9]+")) {
            return assembleOutput('"' + s + '"');
        }
        if (s.matches("\\.[0-9]+F")) {
            return assembleOutput('"' + s + '"');
        }
        if (s.matches("\\.[0-9]+D")) {
            return assembleOutput('"' + s + '"');
        }
        if (s.matches("\\.[0-9]+BD")) {
            return assembleOutput('"' + s + '"');
        }
        return assembleOutput(s);
    }

    private static YAPIONValue<?> tryParse(String s, ParseType... parseOrder) {
        return tryParse(s, 10, parseOrder);
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

    private enum ParseType {

        NUMBER("-?[0-9]+", "-?(0[xX]|#)[0-9A-F]+", ""),
        NUMBER_FLOAT("(-?[0-9]+\\.([0-9]+)?)|(-?\\.[0-9]+)", "", ""),

        BYTE("", "", "B"),
        SHORT("", "", "S"),
        INTEGER("", "", "I"),
        LONG("", "", "L"),
        BIGINTEGER("", "", "BI"),

        FLOAT("", "", "F"),
        DOUBLE("", "", "D"),
        BIGDECIMAL("", "", "BD");

        private String normalRegex;
        private String hexRegex;
        private String suffix;

        ParseType(String normalRegex, String hexRegex, String suffix) {
            this.normalRegex = normalRegex;
            this.hexRegex = hexRegex;
            this.suffix = suffix;
        }

        public String getNormalRegex() {
            return normalRegex;
        }

        public String getHexRegex() {
            return hexRegex;
        }

        public String getSuffix() {
            return suffix;
        }

    }
}