package yapion.hierarchy.types.value;

import java.util.Optional;

import static yapion.hierarchy.types.value.NumberSuffix.*;
import static yapion.utils.ReferenceIDUtils.calc;

public class StringHandler implements ValueHandler<String> {

    @Override
    public String output(String s) {
        s = s.replaceAll("[()]", "\\\\$0");
        if (s.equals("true") || s.equals("false") || s.equals("null")) {
            return '"' + s + '"';
        }

        if (s.matches(NUMBER_HEX)) {
            return '"' + s + '"';
        }
        if (s.matches(NUMBER_NORMAL)) {
            return '"' + s + '"';
        }
        if (s.matches(NUMBER_FLOAT)) {
            return '"' + s + '"';
        }

        if (NumberSuffix.tryValueAssemble(s, BYTE)) {
            return '"' + s + '"';
        }
        if (NumberSuffix.tryValueAssemble(s, SHORT)) {
            return '"' + s + '"';
        }
        if (NumberSuffix.tryValueAssemble(s, INTEGER)) {
            return '"' + s + '"';
        }
        if (NumberSuffix.tryValueAssemble(s, LONG)) {
            return '"' + s + '"';
        }
        if (NumberSuffix.tryValueAssemble(s, BIG_INTEGER)) {
            return '"' + s + '"';
        }

        if (NumberSuffix.tryValueAssemble(s, FLOAT)) {
            return '"' + s + '"';
        }
        if (NumberSuffix.tryValueAssemble(s, DOUBLE)) {
            return '"' + s + '"';
        }
        if (NumberSuffix.tryValueAssemble(s, BIG_DECIMAL)) {
            return '"' + s + '"';
        }

        return s;
    }

    @Override
    public Optional<String> preParse(String s) {
        return Optional.empty();
    }

    @Override
    public Optional<String> parse(String s) {
        if (s.startsWith("'") && s.endsWith("'") && s.length() > 3) {
            return Optional.of(s.substring(1, s.length() - 1));
        }
        if (s.startsWith("\"") && s.endsWith("\"")) {
            return Optional.of(s.substring(1, s.length() - 1));
        }
        return Optional.of(s);
    }

    @Override
    public long referenceValue() {
        return calc("java.lang.String");
    }

}
