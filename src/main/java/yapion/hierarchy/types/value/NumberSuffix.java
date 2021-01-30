// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020,2021 yoyosource

package yapion.hierarchy.types.value;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Optional;
import java.util.function.Function;

final class NumberSuffix<T> {

    static final String NUMBER_NORMAL = "-?[0-9]+";
    static final String NUMBER_HEX = "-?(0[xX]|#)[0-9A-F]+";
    static final String NUMBER_FLOAT = "(-?[0-9]+\\.([0-9]+)?)|(-?\\.[0-9]+)";

    public static final NumberSuffix<Byte> BYTE = new NumberSuffix<>("B", Byte::decode, NUMBER_NORMAL, NUMBER_HEX);
    public static final NumberSuffix<Short> SHORT = new NumberSuffix<>("S", Short::decode, NUMBER_NORMAL, NUMBER_HEX);
    public static final NumberSuffix<Integer> INTEGER = new NumberSuffix<>("I", Integer::decode, NUMBER_NORMAL, NUMBER_HEX);
    public static final NumberSuffix<Long> LONG = new NumberSuffix<>("L", Long::decode, NUMBER_NORMAL, NUMBER_HEX);
    public static final NumberSuffix<BigInteger> BIG_INTEGER = new NumberSuffix<>("BI", BigInteger::new, NUMBER_NORMAL);

    public static final NumberSuffix<Float> FLOAT = new NumberSuffix<>("F", Float::parseFloat, NUMBER_FLOAT);
    public static final NumberSuffix<Double> DOUBLE = new NumberSuffix<>("D", Double::parseDouble, NUMBER_FLOAT);
    public static final NumberSuffix<BigDecimal> BIG_DECIMAL = new NumberSuffix<>("BD", BigDecimal::new, NUMBER_FLOAT);

    private String suffix;
    private Function<String, T> numberMapper;
    private String[] matcher;

    private NumberSuffix(String suffix, Function<String, T> numberMapper, String... matcher) {
        this.suffix = suffix;
        this.numberMapper = numberMapper;
        this.matcher = matcher;
    }

    public String getSuffix() {
        return suffix;
    }

    public static <T> Optional<T> tryValueParse(String input, NumberSuffix<T> numberSuffix) {
        if (input.length() < numberSuffix.suffix.length()) {
            return Optional.empty();
        }
        if (!input.endsWith(numberSuffix.suffix)) {
            return Optional.empty();
        }
        try {
            return Optional.of(numberSuffix.numberMapper.apply(input.substring(0, input.length() - numberSuffix.suffix.length())));
        } catch (NumberFormatException e) {
            // Ignored
        }
        return Optional.empty();
    }

    public static <T> Optional<T> trySuffixLessValueParse(String input, NumberSuffix<T> numberSuffix) {
        try {
            if (!input.endsWith(numberSuffix.suffix)) {
                if (numberSuffix == BYTE || numberSuffix == SHORT) return Optional.empty();
                return Optional.of(numberSuffix.numberMapper.apply(input));
            }
        } catch (NumberFormatException e) {
            // Ignored
        }
        return Optional.empty();
    }

    public static <T> boolean tryValueAssemble(String input, NumberSuffix<T> numberSuffix) {
        if (!input.endsWith(numberSuffix.suffix)) return false;
        String s = input.substring(0, input.length() - numberSuffix.suffix.length());
        for (String match : numberSuffix.matcher) {
            if (s.matches(match)) return true;
        }
        return false;
    }

}